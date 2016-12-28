package modulizer.algorithms;

import uflow.data.model.immutable.ProcessModel;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class SingleEntrySingleExit extends ModularizationAlgorithm{

    @Override
    public List<ProcessModel> startModularization(ProcessModel model) {
        super(model);
        models = new ArrayList<>();

        //models.add(model);
        return models;
    }

    private void makeModel() {

    }
}
