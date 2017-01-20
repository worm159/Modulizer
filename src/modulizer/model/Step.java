package modulizer.model;

import uflow.data.base.DataItem;
import uflow.data.function.immutable.ProceedFunction;
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
    private List<String> provided;
    private List<String> required;
    private Map<String,String> proceeded;

    /**
     * constructor of Step, which expects the step that should be transformed and the id of its Unit
     *
     * @param step the ProcessStepModel that should be transformed to a Step
     * @param unitId the id of the Unit the given ProcessStepModel belongs to
     */
    public Step(ProcessStepModel step, String unitId) {
        id = step.getId().getKey();
        this.unitId = unitId;
        prevSteps = new HashMap<>();
        nextSteps = new HashMap<>();
        provided = new ArrayList<>();
        required = new ArrayList<>();
        for (ProcessFunction func : step.getProcessFunctions()) {
            String c = func.getClass().getName();
            switch (c) {
                case "uflow.data.function.immutable.ProvideFunction":
                    provided.add(((ProvideFunction)func).getKey());
                    break;
                case "uflow.data.function.immutable.RequireFunction":
                    for(String s : ((RequireFunction) func).getValues()) {
                        required.add(s);
                    }
                    break;
                case "uflow.data.function.immutable.ProceedFunction":
                    ProceedFunction procFunc = (ProceedFunction) func;
                    String targetProcessUnit = procFunc.getTargetProcessUnit();
                    String nextStep = procFunc.getNext();
                    for (String dataObject : procFunc.getValues().getKeys()) {
                        proceeded.put(targetProcessUnit + "/" + nextStep,dataObject);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * adds the given Step to the  map of previous Steps
     *
     * @param id the id of the given Step
     * @param step the Step that should be added
     */
    public void addPrevStep(String id, Step step) {
        prevSteps.put(id, step);
    }

    /**
     * adds the given Step to the map of next Steps
     *
     * @param id the id of the given Step
     * @param step the Step that should be added
     */
    public void addNextStep(String id, Step step) {
        nextSteps.put(id, step);
    }

    /**
     * method to get the previous Steps of this Step
     *
     * @return the Map of the next Steps
     */
    public Map<String,Step> getNextSteps() {
        return nextSteps;
    }

    /**
     * method to get the previous Steps of this Step
     *
     * @return the Map of the previous Steps
     */
    public Map<String,Step> getPrevSteps() {
        return prevSteps;
    }

    /**
     * method to get the id of this Step
     *
     * @return the id of this Step
     */
    public String getId() { return id; }

    /**
     * method to get the id of the Unit of this Step
     *
     * @return the id of the Unit
     */
    public String getUnitId() { return unitId; }

    /**
     * method to get the Data Objects this Step provides
     *
     * @return the list of provided Data Objects
     */
    public List<String> getProvided() {
        return provided;
    }

    /**
     * method to get the Data Objects this Step requires
     *
     * @return the list of required Data Objects
     */
    public List<String> getRequired() {
        return required;
    }

    /**
     * method to get the nextSteps with the corresponding Data Objects
     *
     * @return the map of the nextSteps that receive Data Objects
     */
    public Map<String, String> getProceeded() {
        return proceeded;
    }
}
