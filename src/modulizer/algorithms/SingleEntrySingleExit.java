package modulizer.algorithms;

import Test.ModelNavigator;
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
    private boolean dataObjectFlows = false;
    private int minimalSese = 4;

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

        // create the first ProcessModelModifier and increase the modelNumber
        currentModel = new ProcessModelModifier().setId("Model1");
        models.put("Model1",currentModel);
        modelNumber = 2;
    }

    public SingleEntrySingleExit(ProcessModel model, boolean dataObjectFlows, int minimalSese) {
        super(model);
        seseEndSteps = new HashMap<>();
        this.dataObjectFlows = dataObjectFlows;
        if(minimalSese > 4) {
            this.minimalSese = minimalSese;
        }

        // create the first ProcessModelModifier and increase the modelNumber
        currentModel = new ProcessModelModifier().setId("Model1");
        models.put("Model1",currentModel);
        modelNumber = 2;
    }

    /**
     * modularizes the model based on the Single Entry Single Exit algorithm
     *
     * @return result of the modularization as a list of ProcessModels
     */
    @Override
    public List<ProcessModel> startModularization() {

        // loop over each Step in the list firstSteps
        for (ProcessStepModel step : mn.getFirstSteps()) {
        //for(Step step : firstSteps) {
            ProcessUnitModelModifier unitModifier = getUnitModifier(step);
            // call the recursive method handleStep
            handleStep(step);
            // set this step as StartProcessStep
            unitModifier.setStartProcessStep(step.getId().getKey());
            //unitModifier.setStartProcessStep(step.getId());
        }

        // get the ProcessModels of each entry in the list models
        for(ProcessModelModifier m : models.values()) {
            result.add(m.getProcessModel());
        }
        return result;
    }

//    private void handleStep(Step step) {
//        // check if the Step is already finished -> this would be a cycle
//        if(!finishedSteps.contains(step)){
//            boolean prevFinished = true;
//            // check if all the previous Steps are finished
//            for(Step prev : step.getPrevSteps().values()){
//                if(!finishedSteps.contains(prev)) prevFinished = false;
//            }
//            // get the corresponding ProcessStepModel
//            ProcessStepModel processStep = modelToSplit.getProcessUnitModels().get(step.getUnitId())
//                    .getProcessStepModels().get(step.getId());
//            boolean prevWithCycle = false;
//            boolean prevWithoutCycle = false;
//            if(!prevFinished) {
//                /**
//                 * loop over the previous Steps of the current Step
//                 * check if the current Step is a previous Step of the previous Step -> cycle
//                 */
//                for(Step prev : step.getPrevSteps().values()){
//                    ProcessStepModel prevStep = modelToSplit.getProcessUnitModels().get(prev.getUnitId())
//                            .getProcessStepModels().get(prev.getId());
//                    boolean isPrev = mn.isStepBeforeStep(processStep,prevStep);
//                    if(isPrev) prevWithCycle=true;
//                    else if (!finishedSteps.contains(prev)) prevWithoutCycle=true;
//                }
//            }
//            /**
//             * continue if all the previous Steps are finished
//             * or if all the previous Steps except those with a cycle are finished
//             */
//            if(prevFinished || (prevWithCycle && !prevWithoutCycle)) {
//                ProcessUnitModelModifier unitModifier = getUnitModifier(step);
//                // check if there is a seseEndStep
//                ProcessStepModel endStep = seseEndSteps.get(currentModel.getProcessModel().getId().getKey());
//                if (processStep.equals(endStep)) {
//                    // if the seseEndStep is reached, put it in the currentModel and go back
//                    unitModifier.setProcessStepModel(step.getId(),processStep);
//                    finishedSteps.add(step);
//                } else if (step.getNextSteps().size()<=1) {
//                    // if the Step has maximum one ProceedFunction put it in the currentModel
//                    unitModifier.setProcessStepModel(step.getId(),processStep);
//                    finishedSteps.add(step);
//                    if (step.getNextSteps().size()==1) {
//                        // if there is a ProceedFunction then call the recursive method for the next Step
//                        step.getNextSteps().values().forEach(this::handleStep);
//                    }
//                } else {
//                    // the Step has more than one ProceedFunctions
//                    // get the endStep for this possible SESE model
//                    endStep = mn.getSESEExitToEntry(processStep);
//                    // if there is an endStep this model is a SESE
//                    if (endStep != null) {
//                        // loop over the Steps directly before the current Step
//                        for(Step prev : step.getPrevSteps().values()) {
//                            ProcessStepModel prevStep = currentModel.getProcessModel()
//                                    .getProcessUnitModels().get(prev.getUnitId())
//                                    .getProcessStepModels().get(prev.getId());
//                            /**
//                             * loop over the ProcessFunctions of this Step
//                             * set the next of the ProceedFunctions to a new Step
//                             */
//                            for(ProcessFunction func : prevStep.getProcessFunctions()) {
//                                if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")
//                                        && ((ProceedFunction) func).getNext().equals(step.getId())) {
//                                    new ProceedFunctionModifier((ProceedFunction) func).setNext("Model" + modelNumber);
//                                }
//                            }
//                        }
//                        // create the new Step that shows the reference to the model
//                        ProcessStepModelModifier modelStep = new ProcessStepModelModifier();
//                        unitModifier.setProcessStepModel("Model"+ modelNumber,modelStep.getProcessStepModel());
//                        /**
//                         * save the previous model -> needed later
//                         * save the endStep of the new Model
//                         * create a new ProcessModelModifier and put it in the list models
//                         * increase the counter of the number of models
//                         * add the Step to the new Model and set it as the StartProcessStep of the new Unit
//                         * and call the recursive method for all the next Steps
//                         */
//                        ProcessModelModifier prevModel = currentModel;
//                        seseEndSteps.put("Model"+ modelNumber,endStep);
//                        currentModel = new ProcessModelModifier().setId("Model"+ modelNumber);
//                        models.put("Model"+ modelNumber,currentModel);
//                        modelNumber++;
//                        ProcessUnitModelModifier seseUnitModifier = new ProcessUnitModelModifier().setStartProcessStep(step.getId());
//                        currentModel.setProcessUnitModel(step.getUnitId(), seseUnitModifier.getProcessUnitModel());
//                        seseUnitModifier.setProcessStepModel(step.getId(),processStep);
//                        finishedSteps.add(step);
//                        step.getNextSteps().values().forEach(this::handleStep);
//                        /**
//                         * the SESE Model is finished
//                         * go back to the previous model
//                         */
//                        currentModel = prevModel;
//                        /**
//                         * loop over the ProcessFunctions of the seseEndStep
//                         * the ProceedFunctions are added to the Step that references the model
//                         * and the ProceedFunctions are added to the list proceedFunctions
//                         */
//                        ArrayList<ProceedFunction> proceedFunctions = new ArrayList<>();
//                        for(ProcessFunction func : endStep.getProcessFunctions()) {
//                            if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")) {
//                                modelStep.addProcessFunction(func);
//                                proceedFunctions.add((ProceedFunction) func);
//                            }
//                        }
//                        /**
//                         * loop over the ProceedFunctions of the seseEndStep
//                         * the Function is remove from the seseEndStep
//                         * and the recursive method is called for all the following Steps
//                         */
//                        for(ProceedFunction func : proceedFunctions) {
//                            new ProcessStepModelModifier(endStep).removeProcessFunction(func);
//                            Step next = steps.get(func.getNext());
//                            handleStep(next);
//                        }
//                    } else {
//                        /**
//                         * if no seseEndStep was found continue in the current model
//                         * and call the recursive method for all the following Steps
//                         */
//                        unitModifier.setProcessStepModel(step.getId(),processStep);
//                        finishedSteps.add(step);
//                        step.getNextSteps().values().forEach(this::handleStep);
//                    }
//                }
//            }
//        }
//    }

    private void handleStep(ProcessStepModel step) {
        // check if the Step is already finished -> this would be a cycle
        if(!finished.contains(step.getId())){
            ProcessStepModel copiedStep = copyStep(step);
            boolean prevFinished = true;
            // check if all the previous Steps are finished
            for(ProcessStepModel prev : mn.getPrevSteps(step)){
                if(!finished.contains(prev.getId())) prevFinished = false;
            }
            // get the corresponding ProcessStepModel
            boolean prevWithCycle = false;
            boolean prevWithoutCycle = false;
            if(!prevFinished) {
                /**
                 * loop over the previous Steps of the current Step
                 * check if the current Step is a previous Step of the previous Step -> cycle
                */
                for(ProcessStepModel prev : mn.getPrevSteps(step)){
                    boolean isPrev = mn.isStepBeforeStep(step,prev);
                    if(isPrev) prevWithCycle=true;
                    else if (!finished.contains(prev.getId())) prevWithoutCycle=true;
                }
            }
            /**
             * continue if all the previous Steps are finished
             * or if all the previous Steps except those with a cycle are finished
             */
            if(prevFinished || (prevWithCycle && !prevWithoutCycle)) {
                ProcessUnitModelModifier unitModifier = getUnitModifier(step);
                // check if there is a seseEndStep
                ProcessStepModel endStep = seseEndSteps.get(currentModel.getProcessModel().getId().getKey());
                if (step.equals(endStep)) {
                    // if the seseEndStep is reached, put it in the currentModel and go back
                    finished.add(step.getId());
                    /**
                     * loop over the ProceedFunctions of the copiedStep
                     * the ProceedFunctions are added to the list proceedFunctions
                     */
                    ArrayList<ProceedFunction> proceedFunctions = new ArrayList<>();
                    for(ProcessFunction func : copiedStep.getProcessFunctions()) {
                        if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")) {
                            proceedFunctions.add((ProceedFunction) func);
                        }
                    }
                    /**
                     * loop over the list of proceedFunctions
                     * and remove the function from the copied step
                     */
                    for(ProceedFunction func : proceedFunctions) {
                        new ProcessStepModelModifier(copiedStep).removeProcessFunction(func);
                    }
                    unitModifier.setProcessStepModel(copiedStep.getId().getKey(),copiedStep);
                } else if (mn.getNextSteps(step).size()==1) {
                    // if the Step has maximum one ProceedFunction put it in the currentModel
                    finished.add(step.getId());
                    if (mn.getNextSteps(step).size()==1) {
                        // if there is a ProceedFunction then call the recursive method for the next Step
                        mn.getNextSteps(step).forEach(this::handleStep);
                    }
                    unitModifier.setProcessStepModel(copiedStep.getId().getKey(),copiedStep);
                } else {
                    // the Step has more than one ProceedFunctions
                    // get the endStep for this possible SESE model
                    endStep = mn.getSESEExitToEntry(step);
                    // if there is an endStep this model is a SESE
                    if (endStep != null) {
                        // loop over the Steps directly before the current Step
                        for(ProcessStepModel prev : mn.getPrevSteps(step)) {
                            /**
                             * loop over the ProcessFunctions of this Step
                             * set the next of the ProceedFunctions to a new Step
                             */
                            for(ProcessFunction func : prev.getProcessFunctions()) {
                                if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")
                                        && ((ProceedFunction) func).getNext().equals(step.getId().getKey())) {
                                    new ProceedFunctionModifier((ProceedFunction) func).setNext("Model" + modelNumber);
                                }
                            }
                        }
                        // create the new Step that shows the reference to the model
                        ProcessStepModelModifier modelStep = new ProcessStepModelModifier();
                        unitModifier.setProcessStepModel("Model"+ modelNumber,modelStep.getProcessStepModel());
                        /**
                         * save the previous model -> needed later
                         * save the endStep of the new Model
                         * create a new ProcessModelModifier and put it in the list models
                         * increase the counter of the number of models
                         * add the Step to the new Model and set it as the StartProcessStep of the new Unit
                         * and call the recursive method for all the next Steps
                         */
                        ProcessModelModifier prevModel = currentModel;
                        seseEndSteps.put("Model"+ modelNumber,endStep);
                        currentModel = new ProcessModelModifier().setId("Model"+ modelNumber);
                        models.put("Model"+ modelNumber,currentModel);
                        modelNumber++;
                        ProcessUnitModelModifier seseUnitModifier = new ProcessUnitModelModifier().setStartProcessStep(step.getId().getKey());
                        currentModel.setProcessUnitModel(step.getId().getContext(), seseUnitModifier.getProcessUnitModel());
                        finished.add(step.getId());
                        mn.getNextSteps(step).forEach(this::handleStep);
                        seseUnitModifier.setProcessStepModel(copiedStep.getId().getKey(),copiedStep);
                        /**
                         * the SESE Model is finished
                         * go back to the previous model
                         */
                        currentModel = prevModel;
                        /**
                         * loop over the ProcessFunctions of the seseEndStep
                         * the ProceedFunctions are added to the Step that references the model
                         * and the recursive method is called for all the following Steps
                         */
                        for(ProcessFunction func : endStep.getProcessFunctions()) {
                            if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")) {
                                modelStep.addProcessFunction(func);
                                ProcessStepModel next = mn.getStep(((ProceedFunction)func).getNext());
                                handleStep(next);
                            }
                        }
                    } else {
                        /**
                         * if no seseEndStep was found continue in the current model
                         * and call the recursive method for all the following Steps
                         */
                        finished.add(step.getId());
                        mn.getNextSteps(step).forEach(this::handleStep);
                        unitModifier.setProcessStepModel(copiedStep.getId().getKey(),copiedStep);
                    }
                }
            }
        }
    }
}
