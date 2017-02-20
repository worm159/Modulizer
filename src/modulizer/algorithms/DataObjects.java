package modulizer.algorithms;

import modulizer.model.Step;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.modifier.ProcessModelModifier;
import uflow.data.model.modifier.ProcessUnitModelModifier;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brigitte on 28.12.2016.
 * In this class the Modularization Algorithm that uses the Data Objects is implemented.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class DataObjects extends ModularizationAlgorithm {
    /**
     * initializes all the lists and maps,
     * converts the ProcessStepModels to Steps
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

    /**
     * method that finds all dataObjects that are provided, required or proceeded by this step
     *
     * @param step the step that should be searched for dataObjects
     */
    private void handleStep(Step step) {
        // get the corresponding ProcessStepModel and create a copy of it
        ProcessStepModel processStep = modelToSplit.getProcessUnitModels().get(step.getUnitId())
                .getProcessStepModels().get(step.getId());
        ProcessStepModel copiedStep = copyStep(processStep);
        // put all the dataObjects of this Step in the Provide or Require Functions in a new List
        ArrayList<String> dataObjects = new ArrayList<>();
        dataObjects.addAll(step.getProvided());
        dataObjects.addAll(step.getRequired());
        // loop over all the dataObjects
        for(String dataObject : dataObjects) {
            // get the model for this dataObject
            getCurrentModel(dataObject);
            // get the ProcessUnitModelModifier for this step
            ProcessUnitModelModifier unitModifier = getUnitModifier(step);
            // add the copied ProcessStepModel to the current model
            unitModifier.setProcessStepModel(step.getId(),copiedStep);
        }
        // loop over all the ProcessFunctions of this step
        for (ProcessFunction func : processStep.getProcessFunctions()) {
            if(("uflow.data.function.immutable.ProceedFunction").equals(func.getClass().getName())) {
                // if it is a ProceedFunction save the function, the target Unit and the next Step
                ProceedFunction f = (ProceedFunction) func;
                String targetProcessUnit = f.getTargetProcessUnit();
                String next = f.getNext();
                // loop over all the dataObjects in this ProceedFunction
                for(String dataObject : f.getValues().getKeys()) {
                    // get the model for this dataObject
                    getCurrentModel(dataObject);
                    // get the ProcessUnitModelModifier for this step
                    ProcessUnitModelModifier unitModifier = getUnitModifier(step);
                    // add the copied ProcessStepModel to the current model
                    unitModifier.setProcessStepModel(step.getId(),copiedStep);
                    ProcessStepModel nextStep,copiedNextStep;
                    if (targetProcessUnit == null || targetProcessUnit.isEmpty()) {
                        // if the unit is the same for the next step
                        // get the next step from the current unit
                        nextStep = modelToSplit.getProcessUnitModels().get(step.getUnitId()).getProcessStepModels().get(next);
                        if(nextStep != null) {
                            // copy the step and add it to the current model
                            copiedNextStep = copyStep(nextStep);
                            unitModifier.setProcessStepModel(next,copiedNextStep);
                        }
                    } else {
                        // if the unit is different for the next step
                        // get the ProcessUnitModelModifier for the next step
                        ProcessUnitModelModifier nextUnitModifier = getUnitModifier(steps.get(next));
                        if(nextUnitModifier != null) {
                            // get the next step from the target unit
                            nextStep = modelToSplit.getProcessUnitModels().get(targetProcessUnit)
                                    .getProcessStepModels().get(next);
                            if(nextStep != null) {
                                // copy the step and add it to the current model
                                copiedNextStep = copyStep(nextStep);
                                nextUnitModifier.setProcessStepModel(next,copiedNextStep);
                            }
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
        if(models.containsKey(modelId))
            currentModel = models.get(modelId);
        else {
            currentModel = new ProcessModelModifier().setId(modelId);
            models.put(modelId,currentModel);
        }
    }
}
