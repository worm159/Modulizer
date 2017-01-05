package modulizer.print;

import java.util.ArrayList;
import java.util.List;
import modulizer.ui.ModulizerGUI;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.function.immutable.ProvideFunction;
import uflow.data.function.immutable.RequireFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

/**
 *
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class Print {

    //TODO: Schleifen und Verzweigen → nichts darf doppelt ausgegeben werden.
    static ArrayList<String> printedSteps = new ArrayList<>();

    public static void printProcessModel(ProcessModel model) {
        for (ProcessUnitModel unit : model.getProcessUnitModels().getValues()) {
            String startStep = unit.getStartProcessStep();
            ModulizerGUI.jTextAreaOutput.append("Unit Start Process Step: " + startStep + "\n");
            System.out.println("Unit Start Process Step: " + startStep);
            ProcessStepModel step = unit.getProcessStepModels().get(startStep);
            System.out.println(step.getId().getKey());
            if (step != null && !printedSteps.contains(step.getId().getKey())) {
                printedSteps.add(step.getId().getKey());
                printProcessSteps(step, unit, model);
                ModulizerGUI.jTextAreaOutput.append("\n");
                System.out.println("");
            }
        }
    }

    private static void printProcessSteps(ProcessStepModel step, ProcessUnitModel unit, ProcessModel model) {
        ModulizerGUI.jTextAreaOutput.append("Process Step:   " + step.getId().getKey() + "\n");
        System.out.println("Process Step:   " + step.getId().getKey());
        ModulizerGUI.jTextAreaOutput.append("        Name:   " + step.getName() + "\n");
        System.out.println("        Name:   " + step.getName());
//        System.out.println(step);
        List<String> nextSteps = new ArrayList<>();
        for (ProcessFunction func : step.getProcessFunctions()) {
            switch (func.getClass().getName()) {
                case "uflow.data.function.immutable.RequireFunction":
                    RequireFunction reqFunc = (RequireFunction) func;
                    ModulizerGUI.jTextAreaOutput.append("    Requires:   " + reqFunc.getValues() + "\n");
                    System.out.println("    Requires:   " + reqFunc.getValues());
                    break;
                case "uflow.data.function.immutable.RequestInputFunction":
                    /* not used */
                    break;
                case "uflow.data.function.immutable.ProvideFunction":
                    ProvideFunction provFunc = (ProvideFunction) func;
                    ModulizerGUI.jTextAreaOutput.append("    Provides:   " + provFunc.getValue() + "\n");
                    System.out.println("    Provides:   " + provFunc.getValue());
                    break;
                case "uflow.data.function.immutable.CallFunction":
                    /* not used */
                    break;
                case "uflow.data.function.immutable.ProceedFunction":
                    ProceedFunction procFunc = (ProceedFunction) func;
                    String targetProcessUnit = procFunc.getTargetProcessUnit();
                    String nextStep = procFunc.getNext();
                    nextSteps.add(targetProcessUnit + "/" + nextStep);
                    ModulizerGUI.jTextAreaOutput.append("   Next Step:   " + procFunc.getNext() + "\n");
                    System.out.println("   Next Step:   " + procFunc.getNext());
                    break;
                default:
                    break;
            }
        }
        for (String x : nextSteps) {
            String[] splitted = x.split("/");
            String unitName = splitted[0];
            String stepName = splitted[1];
            ProcessStepModel nextStep;
            ProcessUnitModel nextUnit;
            if (unitName == null || unitName.isEmpty()) {
                nextStep = unit.getProcessStepModels().get(stepName);
                nextUnit = unit;
            } else {
                nextUnit = model.getProcessUnitModels().get(unitName);
                nextStep = nextUnit.getProcessStepModels().get(splitted[1]);
            }

            if (nextStep != null && nextUnit != null && !printedSteps.contains(nextStep.getId().getKey())) {
                ModulizerGUI.jTextAreaOutput.append("\n");
                printedSteps.add(nextStep.getId().getKey());
                printProcessSteps(nextStep, nextUnit, model);
            }
        }
    }

}
