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

    static ArrayList<String> printedSteps = new ArrayList<>();

    /*
        private constructor to hide the implicit public one.
     */
    private Print() {
        throw new IllegalAccessError("Print class");
    }

    public static void printModel(ProcessModel model, String chosenAlgorithm) {
        switch (chosenAlgorithm) {
            case "Single Entry Single Exit":
                printProcessModel(model);
                break;
            case "Data objects":
                printDataObjectProcessModel(model);
                break;
            case "Repetition":
            case "Clustering":
            default:
                break;
        }
    }

    /**
     *
     * @param model
     */
    public static void printProcessModel(ProcessModel model) {
        printedSteps.clear();
        ModulizerGUI.getjTextAreaOutput().append("Model: " + model.getId().getKey() + "\n");
        for (ProcessUnitModel unit : model.getProcessUnitModels().getValues()) {
            String startStep = unit.getStartProcessStep();
            if (startStep != null && !startStep.isEmpty()) {
                ModulizerGUI.getjTextAreaOutput().append("Unit " + unit.getName() + "\n");
                ModulizerGUI.getjTextAreaOutput().append("Unit Start Process Step: " + startStep + "\n");
                ProcessStepModel step = unit.getProcessStepModels().get(startStep);
                if (step != null && !printedSteps.contains(step.getId().getKey())) {
                    printedSteps.add(step.getId().getKey());
                    printProcessSteps(step, unit, model);
                    ModulizerGUI.getjTextAreaOutput().append("\n");
                }
            }
        }
    }

    /**
     *
     * @param model
     */
    public static void printDataObjectProcessModel(ProcessModel model) {
        ModulizerGUI.getjTextAreaOutput().append("Model: " + model.getId().getKey() + "\n");
        for (ProcessUnitModel unit : model.getProcessUnitModels().getValues()) {
            ModulizerGUI.getjTextAreaOutput().append("Unit: " + unit.getName() + "\n");
            for (ProcessStepModel step : unit.getProcessStepModels().getValues()) {
                printProcessStep(step, model.getId().getKey());
                ModulizerGUI.getjTextAreaOutput().append("\n");
            }
        }
    }

    private static void printProcessStep(ProcessStepModel step, String dataObject) {
        ModulizerGUI.getjTextAreaOutput().append("Process Step:   " + step.getId().getKey() + "\n");
        ModulizerGUI.getjTextAreaOutput().append("        Name:   " + step.getName() + "\n");
        for (ProcessFunction func : step.getProcessFunctions()) {
            switch (func.getClass().getName()) {
                case "uflow.data.function.immutable.RequireFunction":
                    RequireFunction reqFunc = (RequireFunction) func;
                    if (reqFunc.getValues().contains(dataObject)) {
                        ModulizerGUI.getjTextAreaOutput().append("    Requires:   " + dataObject + "\n");
                    }
                    break;
                case "uflow.data.function.immutable.ProvideFunction":
                    ProvideFunction provFunc = (ProvideFunction) func;
                    if (provFunc.getKey().equals(dataObject)) {
                        ModulizerGUI.getjTextAreaOutput().append("    Provides:   " + dataObject + "\n");
                    }
                    break;
                case "uflow.data.function.immutable.ProceedFunction":
                    ProceedFunction procFunc = (ProceedFunction) func;
                    if (procFunc.getValues().containsKey(dataObject)) {
                        String nextStep = procFunc.getNext();
                        ModulizerGUI.getjTextAreaOutput().append("   Next Step:   " + procFunc.getNext() + "\n");
                        ModulizerGUI.getjTextAreaOutput().append("       Value:   " + dataObject + "\n");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static void printProcessSteps(ProcessStepModel step, ProcessUnitModel unit, ProcessModel model) {
        ModulizerGUI.getjTextAreaOutput().append("Process Step:   " + step.getId().getKey() + "\n");
        ModulizerGUI.getjTextAreaOutput().append("        Name:   " + step.getName() + "\n");
        List<String> nextSteps = new ArrayList<>();
        for (ProcessFunction func : step.getProcessFunctions()) {
            switch (func.getClass().getName()) {
                case "uflow.data.function.immutable.RequireFunction":
                    RequireFunction reqFunc = (RequireFunction) func;
                    ModulizerGUI.getjTextAreaOutput().append("    Requires:   " + reqFunc.getValues() + "\n");
                    break;
                case "uflow.data.function.immutable.RequestInputFunction":
                    /* not used */
                    break;
                case "uflow.data.function.immutable.ProvideFunction":
                    ProvideFunction provFunc = (ProvideFunction) func;
                    ModulizerGUI.getjTextAreaOutput().append("    Provides:   " + provFunc.getValue() + "\n");
                    break;
                case "uflow.data.function.immutable.CallFunction":
                    /* not used */
                    break;
                case "uflow.data.function.immutable.ProceedFunction":
                    ProceedFunction procFunc = (ProceedFunction) func;
                    String targetProcessUnit = procFunc.getTargetProcessUnit();
                    String nextStep = procFunc.getNext();
                    nextSteps.add(targetProcessUnit + "/" + nextStep);
                    ModulizerGUI.getjTextAreaOutput().append("   Next Step:   " + procFunc.getNext() + "\n");
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

            if (nextStep != null && !printedSteps.contains(nextStep.getId().getKey())) {
                ModulizerGUI.getjTextAreaOutput().append("\n");
                printedSteps.add(nextStep.getId().getKey());
                printProcessSteps(nextStep, nextUnit, model);
            }
        }
    }

}
