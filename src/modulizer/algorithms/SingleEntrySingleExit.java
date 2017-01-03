package modulizer.algorithms;

import modulizer.model.Step;
import uflow.data.function.immutable.ProcessFunction;
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
        // create a model and set it as current model
        for (Step step : firstSteps) {
            handleStep(step);
        }
        models.add(model);
        return models;
    }

    private void handleStep(Step step) {
        if(!finishedSteps.contains(step)){
            // check if all the previous Steps are finished
            boolean prevFinished = true;
            for(Step prev : step.getPrevSteps().values()){
                if(!finishedSteps.contains(prev)) prevFinished = false;
            }
            if(prevFinished) {
                if(step.getNextSteps().isEmpty()) {
                    // put step in current Model
                } else if (step.getNextSteps().size()==1) {
                    // put step in current Model

                    // call this function for the next step
                    for(Step next : step.getNextSteps().values()) {
                        handleStep(next);
                    }
                } else {
                    /* check if SESE is fulfilled
                     * if SESE is fulfilled then
                     * finish the current model and put it in models
                     * make a new model and set it as current model
                     * continue with the recursive method
                     *
                     * else if SESE is not fulfilled then
                     * continue in the current model
                     */
                }
            }
        }
    }
}
