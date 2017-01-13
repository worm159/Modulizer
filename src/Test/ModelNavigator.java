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

        if (entry == null)                      return false;
        if (entry.equals(step))                 return true;
        if (getNextSteps(entry).size() == 0)    return false;

        boolean ret = true;
        // Überprüfen, ob alle nachfolgenden Pfade zu step führen:
        for (ProcessStepModel next: getNextSteps(entry)) {
           ret = ret && isExitToEntry(next, step);
        }

        // Überprüfen, ob alle Pfade rückwerts (der Vorgänger) zu entry führen:
        for (ProcessStepModel prev: getPrevSteps(step)) {
            ret = ret && isExitToEntry(step, entry);
        }

        return ret;
    }

    private boolean isExitToEntryForward(ProcessModel entry, ProcessStepModel step) {
        return true;
    }

    /**
     * Liefert true zruück, wenn Step1 irgendein Vorgänger von Step2 ist
     * @param step1
     * @param step2
     * @return
     */
    public boolean isStepBeforeStep(ProcessStepModel step1, ProcessStepModel step2) {

        return false;
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

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step_tmp : unit.getProcessStepModels().getValues())
                if (step_tmp.equals(step))
                    steps.add(step);

        return steps;
    }

    /**
     * Liefert alle direkten nachfolger Steps des übergebnen Steps
     * @param step
     * @return
     */
    public ArrayList<ProcessStepModel> getNextSteps(ProcessStepModel step) {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        Id id = null;

        for (ProcessFunction func : step.getProcessFunctions()) {
            if (func.getClass() == ProceedFunction.class) {
                if (((ProceedFunction)func).getTargetProcessUnit().equals(""))
                    id = new Id("ProcessStepModel", ((ProceedFunction)func).getNext(), step.getId().getContext());
                else
                    id = new Id("ProcessStepModel", ((ProceedFunction)func).getNext(), ((ProceedFunction)func).getTargetProcessUnit());
                steps.add( getStep(id) );
                // old: steps.add( getStep(((ProceedFunction)func).getNext()) );
            }
        }

        return steps;
    }
}
