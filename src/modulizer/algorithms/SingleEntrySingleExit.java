package modulizer.algorithms;

import modulizer.model.Step;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.function.modifier.ProceedFunctionModifier;
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
 * In this class the Single Entry Single Exit algorithm is implemented.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class SingleEntrySingleExit extends ModularizationAlgorithm{

    private Map<String,ProcessStepModel> seseEndSteps;

    /**
     * initializes all the lists and maps,
     * converts the ProcessStepModels to Steps
     * and creates a new ProcessModelModifier for the first model
     *
     * @param model the model that should be modularized
     */
    public SingleEntrySingleExit(ProcessModel model) {
        super(model);
        seseEndSteps = new HashMap<>();
    }

    /**
     * modularizes the model based on the Singel Entry Single Exit algorithm
     *
     * @return result of the modularization as a list of ProcessModels
     */
    @Override
    public List<ProcessModel> startModularization() {

        // loop over each Step in the list firstSteps
        for (Step step : firstSteps) {
            ProcessUnitModelModifier unitModifier = getUnitModifier(step);
            // set this step as StartProcessStep
            unitModifier.setStartProcessStep(step.getId());
            // call the recursive method handleStep
            handleStep(step);
        }

        // get the ProcessModels of each entry in the list models
        for(ProcessModelModifier m : models.values()) {
            result.add(m.getProcessModel());
        }
        return result;
    }

    private void handleStep(Step step) {
        // check if the Step is already finished -> this would be a loop
        if(!finishedSteps.contains(step)){
            boolean prevFinished = true;
            // check if all the previous Steps are finished
            for(Step prev : step.getPrevSteps().values()){
                if(!finishedSteps.contains(prev)) prevFinished = false;
            }
            if(prevFinished) {
                ProcessUnitModelModifier unitModifier = getUnitModifier(step);
                // get the corresponding ProcessStepModel
                ProcessStepModel processStep = modelToSplit.getProcessUnitModels().get(step.getUnitId()).
                        getProcessStepModels().get(step.getId());
                // check if there is a seseEndStep
                ProcessStepModel endStep = seseEndSteps.get(currentModel.getProcessModel().getId().getKey());
                if (processStep.equals(endStep)) {
                    // if the seseEndStep is reached, put it in the currentModel and go back
                    unitModifier.setProcessStepModel(step.getId(),processStep);
                    finishedSteps.add(step);
                } else if (step.getNextSteps().size()<=1) {
                    // if the Step has maximum one ProceedFunction put it in the currentModel
                    unitModifier.setProcessStepModel(step.getId(),processStep);
                    finishedSteps.add(step);
                    if (step.getNextSteps().size()==1) {
                        // if there is a ProceedFunction then call the recursive method for the next Step
                        step.getNextSteps().values().forEach(this::handleStep);
                    }
                } else {
                    // the Step has more than one ProceedFunctions
                    // check if there is an endStep for this possible SESE model
                    endStep = mn.getSESEExitToEntry(processStep);
                    if (endStep != null) {
                        // if there is an endStep
                        // loop over the Steps directly before the current Step
                        for(Step prev : step.getPrevSteps().values()) {
                            ProcessStepModel prevStep = currentModel.getProcessModel()
                                    .getProcessUnitModels().get(prev.getUnitId())
                                    .getProcessStepModels().get(prev.getId());
                            // loop over the ProcessFunctions of this Step
                            for(ProcessFunction func : prevStep.getProcessFunctions()) {
                                if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")
                                        && ((ProceedFunction) func).getNext().equals(step.getId())) {
                                    // set the next of the ProceedFunctions to a new Step
                                    new ProceedFunctionModifier((ProceedFunction) func).setNext("Model" + modelNumber);
                                }
                            }
                        }
                        // create the new Step that shows the reference to the model
                        ProcessStepModelModifier modelStep = new ProcessStepModelModifier();
                        unitModifier.setProcessStepModel("Model"+ modelNumber,modelStep.getProcessStepModel());
                        // save the previous model -> needed later
                        ProcessModelModifier prevModel = currentModel;
                        // save the endStep of the new model
                        seseEndSteps.put("Model"+ modelNumber,endStep);
                        // create a new ProcessModelModifier and put it in the list models
                        currentModel = new ProcessModelModifier().setId("Model"+ modelNumber);
                        models.put("Model"+ modelNumber,currentModel);
                        // increase the counter of the number of models
                        modelNumber++;
                        // set the StartProcessStep of the new Unit in the new model
                        ProcessUnitModelModifier seseUnitModifier = new ProcessUnitModelModifier().setStartProcessStep(step.getId());
                        currentModel.setProcessUnitModel(step.getUnitId(), seseUnitModifier.getProcessUnitModel());
                        seseUnitModifier.setProcessStepModel(step.getId(),processStep);
                        finishedSteps.add(step);
                        // call the recursive method for all the next Steps
                        step.getNextSteps().values().forEach(this::handleStep);
                        // the SESE Model is finished
                        // go back to the previous model
                        currentModel = prevModel;
                        ArrayList<ProceedFunction> proceedFunctions = new ArrayList<>();
                        // loop over the ProcessFunctions of the seseEndStep
                        for(ProcessFunction func : endStep.getProcessFunctions()) {
                            if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")) {
                                // add the ProceedFunctions to the Step that references the model
                                modelStep.addProcessFunction(func);
                                // put all the ProceedFunctions in a List
                                proceedFunctions.add((ProceedFunction) func);
                            }
                        }
                        // loop over the ProceedFunctions of the seseEndStep
                        for(ProceedFunction func : proceedFunctions) {
                            // remove the ProcessFunctions from the seseEndStep
                            new ProcessStepModelModifier(endStep).removeProcessFunction(func);
                            Step next = steps.get(func.getNext());
                            // call the recursive method for all the following Steps
                            handleStep(next);
                        }
                    } else {
                        // if no seseEndStep was found continue in the current model
                        unitModifier.setProcessStepModel(step.getId(),processStep);
                        finishedSteps.add(step);
                        // call the recursive method for all the following Steps
                        step.getNextSteps().values().forEach(this::handleStep);
                    }
                }
            }
        }
    }
}
