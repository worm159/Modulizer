package modulizer.algorithms;

import uflow.data.model.immutable.ProcessModel;

import java.util.List;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public abstract class ModularizationAlgorithm {
    public abstract List<ProcessModel> startModularization(ProcessModel model);
}
