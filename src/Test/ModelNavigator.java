package Test;

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

    public ProcessStepModel getSESEExitToEntry(ProcessStepModel entry) {
        ProcessStepModel ret = null;

        for (ProcessStepModel next : getNextSteps(entry)) {
            ret = getSESEExitToEntry(entry, next);
            if (ret != null) return ret;
        }
        return ret;
    }

    private ProcessStepModel getSESEExitToEntry (ProcessStepModel entry, ProcessStepModel step) {
        ProcessStepModel ret = null;
        if (isExitToEntry(entry, step)) return step;
        for (ProcessStepModel next: getNextSteps(step)) {
            ret = getSESEExitToEntry(entry, next);
            if (ret != null) return ret;
        }

        return ret;
    }

    public boolean isExitToEntry(ProcessStepModel entry, ProcessStepModel step) {

        if (entry == null)                          return false;
        if (entry.getName().equals(step.getName())) return true;
        if (getNextSteps(entry).size() == 0)        return false;

        boolean ret = true;
        for (ProcessStepModel next: getNextSteps(entry)) {
           ret = ret && isExitToEntry(next, step);
        }
        return ret;
    }

    public String printModel() {

        for (ProcessStepModel step: getFirstSteps()) {
            printStep(step);
        }

        return null;
    }

    private void printStep(ProcessStepModel step) {
        System.out.println(step);
        for (ProcessStepModel next: getNextSteps(step)) {
            if (next != null)
                printStep(next);
        }
    }

    public ArrayList<ProcessStepModel> getFirstSteps() { /***********************************************/
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            if (unit.getStartProcessStep() != null)
                steps.add(getStep(unit.getStartProcessStep()));
        return steps;
    }

    public ProcessStepModel getStep(String step_id) { /************************************************/
        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step : unit.getProcessStepModels().getValues())
                if (step.getName().equals(step_id))
                    return step;
        return null;
    }

    public ArrayList<ProcessStepModel> getPrevSteps(ProcessStepModel step) { /*************************/
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step_tmp : unit.getProcessStepModels().getValues())
                if (step_tmp.getName().equals(step.getName()))
                    steps.add(step);

        return steps;
    }

    public ArrayList<ProcessStepModel> getNextSteps(ProcessStepModel step) {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessFunction func : step.getProcessFunctions()) {
            if (func.getClass() == ProceedFunction.class) {
                steps.add( getStep(((ProceedFunction)func).getNext()) );
            }
        }

        return steps;
    }
}
