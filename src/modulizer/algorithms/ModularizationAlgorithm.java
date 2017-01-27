package modulizer.algorithms;

import modulizer.model.ModelNavigator;
import modulizer.model.Step;
import uflow.data.common.immutable.Id;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;
import uflow.data.model.modifier.ProcessModelModifier;
import uflow.data.model.modifier.ProcessStepModelModifier;
import uflow.data.model.modifier.ProcessUnitModelModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brigitte on 28.12.2016.
 * This abstract class is the superclass of our four algorithms
 * and contains methods that can be used by all four modularization algorithms
 * @author August, Brigitte, Emanuel, Stefanie
 */
public abstract class ModularizationAlgorithm {

    ProcessModel modelToSplit;
    Map<String,ProcessModelModifier> models;
    List<ProcessModel> result;
    ProcessModelModifier currentModel;
    int modelNumber;

    Map<String, Step> steps;
    List<Step> firstSteps;
    List<Step> finishedSteps;
    List<Id> finished;

    ModelNavigator mn;

    /**
     * initializes all the lists and maps,
     * converts the ProcessStepModels to Steps
     *
     * @param model the model that should be modularized
     */
    public ModularizationAlgorithm(ProcessModel model) {
        modelToSplit = model;
        models = new HashMap<>();
        result = new ArrayList<>();

        steps = new HashMap<>();
        firstSteps = new ArrayList<>();
        finishedSteps = new ArrayList<>();
        finished = new ArrayList<>();

        mn = new ModelNavigator(model);

        // convert the ProcessStepModels into Steps
        for (ProcessUnitModel unit : modelToSplit.getProcessUnitModels().getValues()) {
            ProcessStepModel firstStep = unit.getProcessStepModels().get(unit.getStartProcessStep());
            generateStep(null, firstStep, unit);
        }
    }

    /**
     *
     * @return result of the modularization as a list of ProcessModels
     */
    public abstract List<ProcessModel> startModularization();

    private void generateStep(String prevKey, ProcessStepModel processStep, ProcessUnitModel unit) {
        if (processStep != null) {
            Step next;
            String key = processStep.getId().getKey();
            if (steps.containsKey(key)) {
                next = steps.get(key);
            } else {
                next = new Step(processStep,unit.getId().getKey());
                steps.put(key, next);
            }
            if (prevKey == null) {
                firstSteps.add(next);
            } else {
                Step prev = steps.get(prevKey);
                prev.addNextStep(key, next);
                next.addPrevStep(prevKey, prev);
            }
            List<String> nextStepIds = new ArrayList<>();
            for (ProcessFunction func : processStep.getProcessFunctions()) {
                if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")) {
                    ProceedFunction f = (ProceedFunction) func;
                    String targetProcessUnit = f.getTargetProcessUnit();
                    String nextStep = f.getNext();
                    nextStepIds.add(targetProcessUnit + "/" + nextStep);
                }
            }
            for (String id : nextStepIds) {
                String[] splitted = id.split("/");
                String unitName = splitted[0];
                String stepName = splitted[1];
                if (next.getNextSteps().get(stepName) == null) {
                    if (unitName == null || unitName.isEmpty()) {
                        generateStep(key, unit.getProcessStepModels().get(stepName), unit);
                    } else {
                        ProcessUnitModel nextUnit = modelToSplit.getProcessUnitModels().get(unitName);
                        generateStep(key, nextUnit.getProcessStepModels().get(stepName), nextUnit);
                    }
                }
            }
        }
    }

    /**
     * method that searches for the ProcessUnitModel to which the Step belongs
     *
     * @param step the Step for which the Unit should be searched
     * @return the ProcessUnitModelModifier
     */
    ProcessUnitModelModifier getUnitModifier(Step step) {
        ProcessUnitModelModifier unitModifier;
        // try to get the Unit to which the Step belongs
        ProcessUnitModel unitModel = currentModel.getProcessModel()
                .getProcessUnitModels().get(step.getUnitId());
        if(unitModel == null) {
            // if the Unit does not exist it has to be created
            unitModifier = new ProcessUnitModelModifier();
            currentModel.setProcessUnitModel(step.getUnitId(), unitModifier.getProcessUnitModel());
        } else {
            unitModifier = new ProcessUnitModelModifier(unitModel);
        }
        return unitModifier;
    }

    /**
     * method that searches for the ProcessUnitModel to which the ProcessStepModel belongs
     *
     * @param step the Step for which the Unit should be searched
     * @return the ProcessUnitModelModifier
     */
    ProcessUnitModelModifier getUnitModifier(ProcessStepModel step) {
        ProcessUnitModelModifier unitModifier;
        // try to get the Unit to which the Step belongs
        String unitId = step.getId().getContext().split("/")[1];
        ProcessUnitModel unitModel = currentModel.getProcessModel()
                .getProcessUnitModels().get(unitId);
        if(unitModel == null) {
            // if the Unit does not exist it has to be created
            unitModifier = new ProcessUnitModelModifier();
            currentModel.setProcessUnitModel(unitId, unitModifier.getProcessUnitModel());
        } else {
            unitModifier = new ProcessUnitModelModifier(unitModel);
        }
        return unitModifier;
    }

    /**
     * method that creates a copy of the given ProcessStepModel
     *
     * @param step the Step that should be copied
     * @return the copied ProcessStepModel
     */
    ProcessStepModel copyStep(ProcessStepModel step) {
        ProcessStepModelModifier copy = new ProcessStepModelModifier();
        copy.setId(step.getId().getKey());
        copy.setName(step.getName());
        for (ProcessFunction func : step.getProcessFunctions()) {
            copy.addProcessFunction(func);
        }
        return copy.getProcessStepModel();
    }
}
