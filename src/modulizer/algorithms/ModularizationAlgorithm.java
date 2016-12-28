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

    protected List<ProcessModel> models;
    protected ProcessModel modelToSplit;
    protected Map<String, Step> steps;

    public List<ProcessModel> startModularization(ProcessModel model) {
        modelToSplit = model;
        models = new ArrayList<>();
        steps = new HashMap<>();
        for (ProcessUnitModel unit : modelToSplit.getProcessUnitModels().getValues()) {
            ProcessStepModel firstStep = unit.getProcessStepModels().get(unit.getStartProcessStep());
            generateStep(null, firstStep, unit);
        }
        return null;
    }

    private void generateStep(Step prev, ProcessStepModel processStep, ProcessUnitModel unit) {
        if (processStep != null) {
            Step next = null;
            if (steps.containsKey(processStep.getName())) {
                next = steps.get(processStep.getName());
            } else {
                next = new Step(processStep);
                steps.put(processStep.getName(), next);
            }
            if (prev != null) {
                prev.addNextStep(next);
                next.addPrevStep(prev);
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
            for (String x : nextStepIds) {
                String[] splitted = x.split("/");
                String unitName = splitted[0];
                String stepName = splitted[1];
                if (unitName == null || unitName.isEmpty()) {
                    generateStep(next, unit.getProcessStepModels().get(stepName), unit);
                } else {
                    ProcessUnitModel nextUnit = modelToSplit.getProcessUnitModels().get(unitName);
                    generateStep(next, nextUnit.getProcessStepModels().get(stepName), nextUnit);
                }
            }
        }
    }

    protected ProcessModel createNewModel() {
        ProcessModel model = null;
        return model;
    }
}
