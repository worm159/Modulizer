package modulizer.algorithms;

import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;

import java.util.List;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public abstract class ModularizationAlgorithm {

    protected List<ProcessModel> models;
    protected ProcessModel modelToSplit;

    public List<ProcessModel> startModularization(ProcessModel model) {
        modelToSplit = model;
    }

    protected ProcessModel createNewModel() {
        ProcessModel model = null;
        return model;
    }
}
