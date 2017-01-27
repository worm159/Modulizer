package Test;

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

    public ProcessModel m;

    public ModelNavigator(ProcessModel m) {
        this.m = m;
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
    public ProcessStepModel getSESEExitToEntry(ProcessStepModel entry) {
        ProcessStepModel ret = null;

        for (ProcessStepModel next : getNextSteps(entry)) {
            ret = getSESEExitToEntry(entry, next);
            if (ret != null) return ret;
        }
        return ret;
    }

    /**
     * Rekursive Methode zum finden des SESE Exits
     * @param entry
     * @param step
     * @return
     */
    private ProcessStepModel getSESEExitToEntry (ProcessStepModel entry, ProcessStepModel step) {
        ProcessStepModel ret = null;

        if (isExitToEntry(entry, step)) return step;
        for (ProcessStepModel next: getNextSteps(step)) {
            ret = getSESEExitToEntry(entry, next);
            if (ret != null) return ret;
        }

        return ret;
    }

    /**
     * Überprüft, ob der Übergebene SESE Anfang zum Übergebnen SESE Ende passt.
     * TODO: überprüfen, ob alle Vorgänger vom step auch wieder zurück zum entry führen. Damit wird ausgeschlossen,
     * dass ein zweiter Start, oder ein Vorgänger vor dem Step in den SESE Block verweist.
     * @param entry
     * @param step
     * @return
     */
    public boolean isExitToEntry(ProcessStepModel entry, ProcessStepModel step) {

        boolean ret = true;

        ret = ret && isExitToEntryForward(entry, step);
        if (!ret) return false;
        //System.out.println("Start Backward ***************************************************************************");
        ret = ret && isExitToEntryBackward(entry, step);

        return ret;
    }

    /**
     * Überprüfen, ob alle Pfade rückwerts (der Vorgänger) zu entry führen
     * @param entry
     * @param exit
     * @return
     */
    private boolean isExitToEntryBackward(ProcessStepModel entry, ProcessStepModel exit) {
        if (exit == null)                      return false;
        if (entry.equals(exit))                return true;
        if (getPrevSteps(exit).size() == 0)    return false;

        boolean ret = true;
        for (ProcessStepModel prev: getPrevSteps(exit)) {
            ret = ret && isExitToEntryBackward(entry, prev);
        }

        return ret;
    }

    /**
     * Überprüfen, ob alle nachfolgenden Pfade zu step führen
     * @param entry
     * @param exit
     * @return
     */
    private boolean isExitToEntryForward(ProcessStepModel entry, ProcessStepModel exit) {
        if (entry == null)                      return false;
        if (entry.equals(exit))                 return true;
        if (getNextSteps(entry).size() == 0)    return false;

        boolean ret = true;
        for (ProcessStepModel next: getNextSteps(entry)) {
            ret = ret && isExitToEntryForward(next, exit);
        }

        return ret;
    }

    /**
     * Liefert true zruück, wenn step1 im Modell vor step2 ist, auch wenn es kein direkter Vorgänger ist
     * @param step1
     * @param step2
     * @return
     */
    public boolean isStepBeforeStep(ProcessStepModel step1, ProcessStepModel step2) {
        if (step2 == null)                      return false;
        if (step1.equals(step2))                return true;
        if (getPrevSteps(step2).size() == 0)    return false;

        boolean ret = false;
        for (ProcessStepModel prev: getPrevSteps(step2))
            ret = ret || isStepBeforeStep(step1, prev);

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

        for (ProcessStepModel step: getFirstSteps()) {
            printStep(step);
        }

        return null;
    }

    /**
     * Rekursive Methode zur ausgabe des Modelles
     * @param step
     */
    private void printStep(ProcessStepModel step) {
        System.out.println(step);
        for (ProcessStepModel next: getNextSteps(step)) {
            if (next != null)
                printStep(next);
        }
    }

    /**
     * Liefert alle Start Steps (aller Units) in einer ArrayList zurück
     * @return
     */
    public ArrayList<ProcessStepModel> getFirstSteps() { /***********************************************/
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            if (unit.getStartProcessStep() != null) {
                steps.add(getStep(new Id("ProcessStepModel", unit.getStartProcessStep(), unit.getId().getContext() + "/" + unit.getId().getKey())));
                //steps.add(getStep(unit.getStartProcessStep()));
            }
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
    public ArrayList<ProcessStepModel> getPrevSteps(ProcessStepModel step) { /*************************/
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        Id id = null;

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step_tmp : unit.getProcessStepModels().getValues())
                /* new */
                for (ProcessFunction func : step_tmp.getProcessFunctions()) {
                    if (func.getClass() == ProceedFunction.class) {
                        if (((ProceedFunction) func).getTargetProcessUnit().equals(""))
                            id = new Id("ProcessStepModel", ((ProceedFunction) func).getNext(), step.getId().getContext());
                        else
                            id = new Id("ProcessStepModel", ((ProceedFunction) func).getNext(), unit.getId().getContext() + "/" + ((ProceedFunction) func).getTargetProcessUnit());

                        if (getStep(id) != null && getStep(id).equals(step)) {
                            //System.out.println(id);
                            //System.out.println(getStep(id));
                            //System.out.println("Vorgänger: " + step_tmp);
                            steps.add(step_tmp);
                        }
                    }
                }
                /* old:
                if (step_tmp.equals(step))
                    steps.add(step);
                    */

        //System.out.println("Anzahl Vorgänger: " + steps.size());
        //System.out.println("");
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
                    steps.add( getStep(id) );
                // old: steps.add( getStep(((ProceedFunction)func).getNext()) );
            }
        }

        return steps;
    }
}
