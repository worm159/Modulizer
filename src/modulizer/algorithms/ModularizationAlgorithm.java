package modulizer.algorithms;

import modulizer.model.Step;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public abstract class ModularizationAlgorithm {

    protected List<ProcessModel> models;
    protected ProcessModel modelToSplit;
    protected Map<String,Step> steps;

    public List<ProcessModel> startModularization(ProcessModel model) {
        modelToSplit = model;
        models = new ArrayList<>();
        steps = new HashMap<>();
        for(ProcessUnitModel unit : modelToSplit.getProcessUnitModels().getValues()) {

        }
        return null;
    }

    protected ProcessModel createNewModel() {
        ProcessModel model = null;
        return model;
    }
}
