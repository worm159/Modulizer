package modulizer.algorithms;

import modulizer.model.Step;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.modifier.ProcessModelModifier;
import uflow.data.model.modifier.ProcessUnitModelModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class DataObjects extends ModularizationAlgorithm {
    /**
     * initializes all the lists and maps,
     * converts the ProcessStepModels to Steps
     * and creates a new ProcessModelModifier for the first model
     *
     * @param model the model that should be modularized
     */
    public DataObjects(ProcessModel model) {
        super(model);
    }

    /**
     * modularizes the model based on the Data Objects
     * for each Data Object a model is created, which contains all steps that work with this Data Object
     *
     * @return result of the modularization as a list of ProcessModels
     */
    @Override
    public List<ProcessModel> startModularization() {
        // loop over all the steps
        for (Step step : steps.values()) {
            handleStep(step);
        }
        for(ProcessModelModifier m : models.values()) {
            result.add(m.getProcessModel());
        }
        return result;
    }

    private void handleStep(Step step) {
        ArrayList<String> dataObjects = new ArrayList<>();
        dataObjects.addAll(step.getProvided());
        dataObjects.addAll(step.getRequired());
        ProcessStepModel processStep = modelToSplit.getProcessUnitModels().get(step.getUnitId())
                .getProcessStepModels().get(step.getId());
        for(String dataObject : dataObjects) {
            getCurrentModel(dataObject);
            ProcessUnitModelModifier unitModifier = getUnitModifier(step);
            unitModifier.setProcessStepModel(step.getId(),processStep);
        }
        for (ProcessFunction func : processStep.getProcessFunctions()) {
            if(func.getClass().getName().equals("uflow.data.immutable.ProceedFunction")) {
                ProceedFunction f = (ProceedFunction) func;
                String targetProcessUnit = f.getTargetProcessUnit();
                String next = f.getNext();
                for(String dataObject : f.getValues().getKeys()) {
                    getCurrentModel(dataObject);
                    ProcessUnitModelModifier unitModifier = getUnitModifier(step);
                    unitModifier.setProcessStepModel(step.getId(),processStep);
                    if (targetProcessUnit == null || targetProcessUnit.isEmpty()) {
                        ProcessStepModel nextStep = unitModifier.getProcessUnitModel().getProcessStepModels().get(next);
                        if(nextStep != null) {
                            unitModifier.setProcessStepModel(next,nextStep);
                        }
                    }
                }
            }
        }
    }

    /**
     * check if there is already a model for this id
     * if not create a new model and put it in the List models
     *
     * @param modelId the id of the model we want to get
     */
    private void getCurrentModel(String modelId) {
        if(models.containsKey(modelId)) {
            currentModel = models.get(modelId);
        } else {
            currentModel = new ProcessModelModifier().setId(modelId);
            models.put(modelId,currentModel);
        }
    }
}
