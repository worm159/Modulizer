package modulizer.algorithms;

import uflow.data.model.immutable.ProcessModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Brigitte on 28.12.2016.
 */
public class Repetition extends ModularizationAlgorithm {
    /**
     * initializes all the lists and maps,
     * converts the ProcessStepModels to Steps
     * and creates a new ProcessModelModifier for the first model
     *
     * @param model the model that should be modularized
     */
    public Repetition(ProcessModel model) {
        super(model);
    }

    /**
     *
     * @return result of the modularization as a list of ProcessModels
     */
    @Override
    public List<ProcessModel> startModularization() {
        return null;
    }
}
