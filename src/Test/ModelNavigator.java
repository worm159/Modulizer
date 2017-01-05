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

    public ModelNavigator() {
        ;
    }

    public ProcessStepModel getSESEEntry(ProcessModel m) {
        //System.out.println(getFirstSteps(m));

        return null;
    }

    public ProcessStepModel getSESEExit(ProcessModel m, ProcessStepModel entry) {

        return null;
    }

    public String printModel(ProcessModel m) {

        for (ProcessStepModel step: getFirstSteps(m)) {
            printStep(m, step);
        }

        return null;
    }

    private void printStep(ProcessModel m, ProcessStepModel step) {
        System.out.println(step);
        for (ProcessStepModel next: getNextSteps(m, step)) {
            if (next != null)
                printStep(m, next);
        }
    }

    public ArrayList<ProcessStepModel> getFirstSteps(ProcessModel m) { /***********************************************/
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            if (unit.getStartProcessStep() != null)
                steps.add(getStep(m, unit.getStartProcessStep()));
        return steps;
    }

    public ProcessStepModel getStep(ProcessModel m, String step_id) { /************************************************/
        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step : unit.getProcessStepModels().getValues())
                if (step.getName().equals(step_id))
                    return step;
        return null;
    }

    public ArrayList<ProcessStepModel> getPrevSteps(ProcessModel m, ProcessStepModel step) { /*************************/
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessUnitModel unit : m.getProcessUnitModels().getValues() )
            for (ProcessStepModel step_tmp : unit.getProcessStepModels().getValues())
                if (step_tmp.getName().equals(step.getName()))
                    steps.add(step);

        return steps;
    }

    public ArrayList<ProcessStepModel> getNextSteps(ProcessModel m, ProcessStepModel step) {
        ArrayList<ProcessStepModel> steps = new ArrayList<>();

        for (ProcessFunction func : step.getProcessFunctions()) {
            if (func.getClass() == ProceedFunction.class) {
                steps.add( getStep(m, ((ProceedFunction)func).getNext()) );
            }
        }

        return steps;
    }
}
