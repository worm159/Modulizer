package modulizer.algorithms;

import uflow.data.model.immutable.ProcessModel;

import java.util.ArrayList;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class SingleEntrySingleExit implements ModularizationAlgorithm{
    @Override
    public ArrayList<ProcessModel> startModularization(ProcessModel model) {
        ArrayList<ProcessModel> models = new ArrayList<>();
        models.add(model);
        return models;
    }
}
