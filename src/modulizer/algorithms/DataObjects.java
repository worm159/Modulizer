package modulizer.algorithms;

import modulizer.model.Step;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.modifier.ProcessModelModifier;
import uflow.data.model.modifier.ProcessUnitModelModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Brigitte on 28.12.2016.
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
            ArrayList<String> dataObjects = new ArrayList<>();
            dataObjects.addAll(step.getProvided());
            dataObjects.addAll(step.getRequired());
            ProcessStepModel processStep = modelToSplit.getProcessUnitModels().get(step.getUnitId())
                    .getProcessStepModels().get(step.getId());
            for(String dataObject : dataObjects) {
                /**
                 * check if there is already a model for this dataObject
                 * if not create a new model for this dataObject and put it in the List models
                 */
                if(models.containsKey(dataObject)) {
                    currentModel = models.get(dataObject);
                } else {
                    currentModel = new ProcessModelModifier().setId(dataObject);
                    models.put(dataObject,currentModel);
                }
                ProcessUnitModelModifier unitModifier = getUnitModifier(step);
                unitModifier.setProcessStepModel(step.getId(),processStep);
            }
        }
        for(ProcessModelModifier m : models.values()) {
            result.add(m.getProcessModel());
        }
        return result;
    }
}
