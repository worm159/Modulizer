package modulizer.algorithms;

import modulizer.model.Step;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;
import uflow.data.model.modifier.ProcessModelModifier;
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
                if(step.getNextSteps().isEmpty()) {
                    unitModifier.setProcessStepModel(step.getId(),processStep);
                } else if (step.getNextSteps().size()==1) {
                    unitModifier.setProcessStepModel(step.getId(),processStep);
                    for(Step next : step.getNextSteps().values()) {
                        handleStep(next);
                    }
                } else if (false) {
                    /* if SESE is fulfilled then
                     * finish the current model and put it in models
                     * make a new model and set it as current model
                     * continue with the recurssive method
                     */
                } else {
                    unitModifier.setProcessStepModel(step.getId(),processStep);
                    for(Step next : step.getNextSteps().values()) {
                        handleStep(next);
                    }
                }
            }
        }
    }
}
