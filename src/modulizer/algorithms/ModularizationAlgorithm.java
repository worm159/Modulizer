package modulizer.algorithms;

import uflow.data.model.immutable.ProcessModel;

import java.util.ArrayList;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public interface ModularizationAlgorithm {
    public ArrayList<ProcessModel> startModularization(ProcessModel model);
}
