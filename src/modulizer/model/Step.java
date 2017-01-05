package modulizer.model;

import uflow.data.base.DataItem;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.function.immutable.ProvideFunction;
import uflow.data.function.immutable.RequireFunction;
import uflow.data.model.immutable.ProcessStepModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class Step {
    private String id;
    private String unitId;
    private Map<String,Step> prevSteps;
    private Map<String,Step> nextSteps;
    private List<DataItem> provided;
    private List<String> required;

    public Step(ProcessStepModel step, String unitId) {
        id = step.getName();
        this.unitId = unitId;
        prevSteps = new HashMap<>();
        nextSteps = new HashMap<>();
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

    public void addPrevStep(String id, Step step) {
        prevSteps.put(id, step);
    }

    public void addNextStep(String id, Step step) {
        nextSteps.put(id, step);
    }

    public Map<String,Step> getNextSteps() {
        return nextSteps;
    }
    public Map<String,Step> getPrevSteps() {
        return prevSteps;
    }
    public String getId() { return id; }
    public String getUnitId() { return unitId; }
}
