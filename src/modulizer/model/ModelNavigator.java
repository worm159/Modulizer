package modulizer.model;

import uflow.data.common.immutable.Id;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

import java.util.ArrayList;

/**
 * Created by augus on 05.01.2017.
 */
public class ModelNavigator {

    private ProcessModel m;
    private boolean dataObjectFlows;
    private int minimalSteps;

    private int stepcounter;

    public ModelNavigator(ProcessModel m, boolean dataObjectFlows, int minimalSteps) {
        this.m               = m;
        this.dataObjectFlows = dataObjectFlows;
        this.minimalSteps    = minimalSteps;
    }

    public ModelNavigator(ProcessModel m, boolean dataObjectFlows) {
        this.m               = m;
        this.dataObjectFlows = dataObjectFlows;
        this.minimalSteps    = 0;
    }

    public ProcessStepModel getSESEEntry() {
        //System.out.println(getFirstSteps(m));

        return null;
    }

    /**
     * Liefert das SESE Ende zum übergebenen Step. Falls kein SESE Ende vorhanden ist, wird null zurückgegeben.
     * @param entry
     * @return
     */
    public ProcessStepModel getSESEExitToEntry (ProcessStepModel entry) {
        if (getPrevSteps(entry).size() >= 2)
            return null; // Start ist kein Start. Darf nur einen Vorgänger haben.

        ProcessStepModel ret = null;

        ArrayList<ProcessStepModel> visited = new ArrayList<ProcessStepModel>();

        for (ProcessStepModel next : getNextSteps(entry)) {
            ret = getSESEExitToEntry(entry, next, visited);
            if (ret != null && getStepsBetweenSteps(entry, ret) >= minimalSteps)
                return ret;
            else if (ret != null && getStepsBetweenSteps(entry, ret) < minimalSteps)
                return null;
        }
        return ret;
    }

    public int getStepsBetweenSteps(ProcessStepModel step1, ProcessStepModel step2) {
        ArrayList<ProcessStepModel> visited = new ArrayList<ProcessStepModel>();

        isExitToEntryForward(step1, step2, visited);

        return visited.size();
    }

    /**
     * Rekursive Methode zum finden des SESE Exits
     * @param entry
     * @param step
     * @return
     */
    private ProcessStepModel getSESEExitToEntry (ProcessStepModel entry, ProcessStepModel step, ArrayList<ProcessStepModel> visited) {
        ProcessStepModel ret = null;

        visited.add(step);
        //System.out.println("Beretis besucht: " + visited.size());

        // Ist nur ein Exit, wenn nur 1, oder kein Nachfolger ist
        if (getNextSteps(step).size() <= 1)
            if (isExitToEntry(entry, step)) return step;

        for (ProcessStepModel next: getNextSteps(step)) {
            if (isStepInArrayList(next, visited))
                ; //System.out.println("!!Forward: Schleife entdeckt! ("+next.getName()+")");
            else
                ret = getSESEExitToEntry(entry, next, visited);
            if (ret != null) return ret;
        }

        return ret;
    }

    /**
     * Überprüft, ob der Übergebene SESE Anfang zum Übergebnen SESE Ende passt.
     * @param entry
     * @param exit
     * @return
     */
    public boolean isExitToEntry(ProcessStepModel entry, ProcessStepModel exit) {

        boolean ret = true;
        ArrayList<ProcessStepModel> visited = new ArrayList<ProcessStepModel>();

        //System.out.println("Start Forward ***************************************************************************");
        ret = ret && isExitToEntryForward(entry, exit, visited);
        if (!ret) return false;

        visited = new ArrayList<ProcessStepModel>();
        //System.out.println("Start Backward **************************************************************************");
        ret = ret && isExitToEntryBackward(entry, exit, visited);

        return ret;
    }

    /**
     * Überprüfen, ob alle nachfolgenden Pfade zu step führen
     * @param entry
     * @param exit
     * @return
     */
    private boolean isExitToEntryForward(ProcessStepModel entry, ProcessStepModel exit, ArrayList<ProcessStepModel> visited) {
        visited.add(entry);

        if (entry == null)                      return false;
        if (entry.equals(exit))                 return true;
        if (getNextSteps(entry).size() == 0)    return false;

        boolean ret = true;
        for (ProcessStepModel next: getNextSteps(entry)) {
            if (isStepInArrayList(next, visited))
                ; //System.out.println("!!Forward: Schleife entdeckt! ("+next.getName()+")");
            else
                ret = ret && isExitToEntryForward(next, exit, visited);
        }

        return ret;
    }

    /**
     * Überprüfen, ob alle Pfade rückwerts (der Vorgänger) zu entry führen
     * @param entry
     * @param exit
     * @return
     */
    private boolean isExitToEntryBackward(ProcessStepModel entry, ProcessStepModel exit, ArrayList<ProcessStepModel> visited) {
        //System.out.println("- Bereits besuchte Steps: " + visited.size());
        //System.out.println(" isExitToEntryBackward("+entry.getName()+", " +exit.getName()+")");

        visited.add(exit);

        if (exit == null)                      return false;
        if (entry.equals(exit))                return true;
        if (getPrevSteps(exit).size() == 0)    return false;

        boolean ret = true;
        for (ProcessStepModel prev: getPrevSteps(exit)) {
            if (isStepInArrayList(prev, visited))
                ; //System.out.println("!!Backward: Schleife entdeckt! ("+prev.getName()+")");

            else
                ret = ret && isExitToEntryBackward(entry, prev, visited);
        }

        return ret;
    }

    private boolean isStepInArrayList(ProcessStepModel step, ArrayList<ProcessStepModel> visited) {
        for (ProcessStepModel x : visited)
            if (x.equals(step))
                return true;

        return false;
    }

    public boolean isStepBeforeStep(ProcessStepModel step1, ProcessStepModel step2) {
        ArrayList<ProcessStepModel> visited = new ArrayList<>();

        return isStepBeforeStep_2(step1, step2, visited);
    }

    /**
     * Liefert true zruück, wenn step1 im Modell vor step2 ist, auch wenn es kein direkter Vorgänger ist
     * @param step1
     * @param step2
     * @return
     */
    private boolean isStepBeforeStep_2(ProcessStepModel step1, ProcessStepModel step2, ArrayList<ProcessStepModel> visited) {
        if (step2 == null)
            return false;
        if (step1.equals(step2))
            return true;

        for (ProcessStepModel visitedstep : visited)
            if (visitedstep.equals(step2))
                return false;

        visited.add(step2);

        boolean ret = false;
        for (ProcessStepModel prev: getPrevSteps(step2)) {
            if (prev.equals(step1)) { System.out.println("ist Vorgänger!");  return true; }
            ret = ret || isStepBeforeStep_2(step1, prev, visited);
        }

        return ret;
    }

    /**
     * Liefert true zruück, wenn step1 im Modell nach step2 ist, auch wenn es kein direkter Nachfolger ist
     * @param step1
     * @param step2
     * @return
     */
    public boolean isStepAfterStep(ProcessStepModel step1, ProcessStepModel step2) {
        if (step2 == null)                      return false;
        if (step1.equals(step2))                return true;
        if (getPrevSteps(step2).size() == 0)    return false;

        boolean ret = false;
        for (ProcessStepModel prev: getNextSteps(step2))
            ret = ret || isStepAfterStep(step1, prev);

        return ret;
    }

    /**
     * Gibt textuell das gesamte Modell aus
     * @return
     */
    public String printModel() {

        for (ProcessStepModel step: getFirstSteps())
            printStep(step);

        return null;
    }

    /**
     * Rekursive Methode zur ausgabe des Modelles
     * @param step
     */
    private void printStep(ProcessStepModel step) {
        System.out.println(step);
        for (ProcessStepModel next: getNextSteps(step))
            if (next != null)
                printStep(next);
    }

    /**
     * Liefert alle Start Steps (aller Units) in einer ArrayList zurück
     * @return
     */
    public ArrayList<ProcessStepModel> getFirstSteps() { /***********************************************/
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            if (unit.getStartProcessStep() != null)
                steps.add(getStep(new Id("ProcessStepModel", unit.getStartProcessStep(), unit.getId().getContext() + "/" + unit.getId().getKey())));

        return steps;
    }

    /**
     * Liefert das Step Objekt des übergebenen Strings (=Name) zurück
     * @param step_id
     * @return
     * @deprecated
     */
    public ProcessStepModel getStep(String step_id) { /************************************************/
        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step : unit.getProcessStepModels().getValues())
                if (step.getName().equals(step_id))
                    return step;
        return null;
    }

    /**
     * Liefert das Step Objekt mit der übergenen id (=Id Objekt) zurück
     * @param id
     * @return
     */
    public ProcessStepModel getStep (Id id) {
        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step : unit.getProcessStepModels().getValues())
                if (step.getId().equals(id))
                    return step;
        return null;
    }

    /**
     * Liefert alle direkten vorgänger Steps des übergenen Steps
     * @param step
     * @return
     */
    public ArrayList<ProcessStepModel> getPrevSteps(ProcessStepModel step) {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();
        Id id = null;

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step_tmp : unit.getProcessStepModels().getValues())
                for (ProcessFunction func : step_tmp.getProcessFunctions()) {
                    if (func.getClass() == ProceedFunction.class) {
                        if (((ProceedFunction) func).getTargetProcessUnit().equals(""))
                            id = new Id("ProcessStepModel", ((ProceedFunction) func).getNext(), step.getId().getContext());
                        else
                            id = new Id("ProcessStepModel", ((ProceedFunction) func).getNext(), unit.getId().getContext() + "/" + ((ProceedFunction) func).getTargetProcessUnit());

                        if (getStep(id) != null && getStep(id).equals(step))
                            steps.add(step_tmp);
                    }
                }
        return steps;
    }

    /**
     * Liefert alle direkten nachfolger Steps des übergebnen Steps
     * @param step
     * @return
     */
    public ArrayList<ProcessStepModel> getNextSteps(ProcessStepModel step) {
        if (step == null) return null;

        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        Id id = null;

        for (ProcessFunction func : step.getProcessFunctions()) {
            if (func.getClass() == ProceedFunction.class) {
                String qualifier = step.getId().getQualifier();
                String context = step.getId().getContext();
                String modelId = context.substring(0,context.indexOf('/')+1);
                if (((ProceedFunction)func).getTargetProcessUnit().equals(""))
                    id = new Id(qualifier, ((ProceedFunction)func).getNext(), context);
                else
                    id = new Id(qualifier, ((ProceedFunction)func).getNext(), modelId + ((ProceedFunction)func).getTargetProcessUnit());

                if (getStep(id) != null)
                    steps.add(getStep(id));
            }
        }

        return steps;
    }
}
