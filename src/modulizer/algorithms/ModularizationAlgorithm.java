package modulizer.algorithms;

import modulizer.model.Step;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brigitte on 28.12.2016.
 *
 * @author August, Brigitte, Emanuel, Stefanie
 */
public abstract class ModularizationAlgorithm {

    protected Map<String,ProcessModel> models;
    protected ProcessModel modelToSplit;
    protected Map<String, Step> steps;
    protected List<Step> firstSteps;

    protected List<Step> finishedSteps;

    protected ProcessModel currentModel;
    protected Map<String,String> outerModels;

    public Map<String,ProcessModel> startModularization(ProcessModel model) {
        modelToSplit = model;
        models = new HashMap<>();
        steps = new HashMap<>();
        firstSteps = new ArrayList<>();
        finishedSteps = new ArrayList<>();
        for (ProcessUnitModel unit : modelToSplit.getProcessUnitModels().getValues()) {
            ProcessStepModel firstStep = unit.getProcessStepModels().get(unit.getStartProcessStep());
            generateStep(null, firstStep, unit);
        }
        return null;
    }

    private void generateStep(String prevKey, ProcessStepModel processStep, ProcessUnitModel unit) {
        if (processStep != null) {
            Step next = null;
            String key = processStep.getId().getKey();
            if (steps.containsKey(key)) {
                next = steps.get(key);
            } else {
                next = new Step(processStep);
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

    protected ProcessModel createNewModel() {
        ProcessModel model = null;
        return model;
    }
}
