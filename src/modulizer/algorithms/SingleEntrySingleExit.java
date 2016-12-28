package modulizer.algorithms;

import modulizer.model.Step;
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
        super.startModularization(model);
        makeModel();
        for (Step step : firstSteps) {

        }
        return models;
    }

    private void makeModel() {

    }
}
