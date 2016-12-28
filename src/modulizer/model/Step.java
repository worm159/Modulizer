package modulizer.model;

import uflow.data.base.DataItem;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.function.immutable.ProvideFunction;
import uflow.data.function.immutable.RequireFunction;
import uflow.data.model.immutable.ProcessStepModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class Step {
    String id;
    List<Step> prevSteps;
    List<Step> nextSteps;
    List<DataItem> provided;
    List<String> required;

    public Step(ProcessStepModel step) {
        id = step.getName();
        prevSteps = new ArrayList<>();
        nextSteps = new ArrayList<>();
        provided = new ArrayList<>();
        required = new ArrayList<>();
        for (ProcessFunction func : step.getProcessFunctions()) {
            String c = func.getClass().getName();
            switch (c) {
                case "uflow.data.function.immutable.ProvideFunction":
                    provided.add(((ProvideFunction)func).getValue());
                    break;
                case "uflow.data.function.immutable.RequireFunction":
                    for(String s : ((RequireFunction) func).getValues()) {
                        required.add(s);
                    }
            }
        }
    }

    public void addPrevStep(Step step) {
        prevSteps.add(step);
    }

    public void addNextStep(Step step) {
        nextSteps.add(step);
    }
}
