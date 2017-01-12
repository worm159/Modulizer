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

import java.util.List;
import java.util.Map;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class SingleEntrySingleExit extends ModularizationAlgorithm{

    @Override
    public List<ProcessModel> startModularization(ProcessModel model) {
        super.startModularization(model);
        for (Step step : firstSteps) {
            ProcessUnitModelModifier unitModifier;
            ProcessUnitModel unitModel = currentModel.getProcessModel()
                    .getProcessUnitModels().get(step.getUnitId());
            if(unitModel == null) {
                unitModifier = new ProcessUnitModelModifier();
                currentModel.setProcessUnitModel(step.getUnitId(), unitModifier.getProcessUnitModel());
            } else {
                unitModifier = new ProcessUnitModelModifier(unitModel);
            }
            unitModifier.setStartProcessStep(step.getId());
            handleStep(step);
        }
        for(ProcessModelModifier m : models.values()) {
            result.add(m.getProcessModel());
        }
        //result.add(model);
        return result;
    }

    private void handleStep(Step step) {
        if(!finishedSteps.contains(step)){
            System.out.println(step.getId());
            // check if all the previous Steps are finished
            boolean prevFinished = true;
            for(Step prev : step.getPrevSteps().values()){
                if(!finishedSteps.contains(prev)) prevFinished = false;
            }
            if(prevFinished) {
                ProcessUnitModelModifier unitModifier;
                ProcessUnitModel unitModel = currentModel.getProcessModel()
                        .getProcessUnitModels().get(step.getUnitId());
                if(unitModel == null) {
                    unitModifier = new ProcessUnitModelModifier();
                    currentModel.setProcessUnitModel(step.getUnitId(), unitModifier.getProcessUnitModel());
                } else {
                    unitModifier = new ProcessUnitModelModifier(unitModel);
                }
                ProcessStepModel processStep = modelToSplit.getProcessUnitModels().get(step.getUnitId()).
                        getProcessStepModels().get(step.getId());
                ProcessStepModel endStep = seseEndSteps.get(currentModel.getProcessModel().getId().getKey());
                if (processStep.equals(endStep)) {
                    unitModifier.setProcessStepModel(step.getId(),processStep);
                    finishedSteps.add(step);
                } else if (step.getNextSteps().size()<=1) {
                    unitModifier.setProcessStepModel(step.getId(),processStep);
                    finishedSteps.add(step);
                    if (step.getNextSteps().size()==1)
                        step.getNextSteps().values().forEach(this::handleStep);
                } else {
                    endStep = mn.getSESEExitToEntry(processStep);
                    if (endStep != null) {
                        for(Step prev : step.getPrevSteps().values()) {
                            ProcessStepModel prevStep = currentModel.getProcessModel()
                                    .getProcessUnitModels().get(prev.getUnitId())
                                    .getProcessStepModels().get(prev.getId());
                            for(ProcessFunction func : prevStep.getProcessFunctions()) {
                                if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")
                                        && ((ProceedFunction) func).getNext().equals(step.getId())) {
                                    new ProceedFunctionModifier((ProceedFunction) func).setNext("Model" + number);
                                }
                            }
                        }
                        ProcessStepModelModifier modelStep = new ProcessStepModelModifier();
                        unitModifier.setProcessStepModel("Model"+number,modelStep.getProcessStepModel());
                        ProcessModelModifier prevModel = currentModel;
                        seseEndSteps.put("Model"+number,endStep);
                        currentModel = new ProcessModelModifier().setId("Model"+number);
                        models.put("Model"+number,currentModel);
                        number++;
                        ProcessUnitModelModifier seseUnitModifier = new ProcessUnitModelModifier().setStartProcessStep(step.getId());
                        currentModel.setProcessUnitModel(step.getUnitId(), seseUnitModifier.getProcessUnitModel());
                        seseUnitModifier.setProcessStepModel(step.getId(),processStep);
                        finishedSteps.add(step);
                        for(Step next : step.getNextSteps().values()) {
                            handleStep(next);
                        }
                        /* the SESE is finished
                         * make the connection to the next step of the endstep
                         * delete it from the endStep
                         * wie kann de bestehende function gelöscht werden?
                         */
                        currentModel = prevModel;
                        for(ProcessFunction func : endStep.getProcessFunctions()) {
                            if (func.getClass().getName().equals("uflow.data.function.immutable.ProceedFunction")) {
                                modelStep.addProcessFunction(func);
                                //new ProcessStepModelModifier(endStep).removeProcessFunction(func);
                                Step next = steps.get(((ProceedFunction) func).getNext());
                                handleStep(next);
                            }
                        }
                    } else {
                        unitModifier.setProcessStepModel(step.getId(),processStep);
                        finishedSteps.add(step);
                        for(Step next : step.getNextSteps().values()) {
                            handleStep(next);
                        }
                    }
                }
            }
        }
    }
}
