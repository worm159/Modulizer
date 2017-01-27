package modulizer.print;

import Test.ModelNavigator;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextArea;
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
    static ModelNavigator modelNavigator;
    static JTextArea outputArea;

    /*
        private constructor to hide the implicit public one.
     */
    private Print() {
        throw new IllegalAccessError("Print class");
    }

    public static void printModel(ProcessModel model, String chosenAlgorithm, JTextArea jTextOutputArea) {
        modelNavigator = new ModelNavigator(model);
        outputArea = jTextOutputArea;
        switch (chosenAlgorithm) {
            case "Single Entry Single Exit":
                printProcessModel(model);
                break;
            case "Data objects":
                printDataObjectProcessModel(model);
                break;
            case "Repetition":
            case "Clustering":
            case "Original":
                printProcessModel(model);
                break;
            default:
                break;
        }
    }

    /**
     *
     * @param model
     */
    private static void printProcessModel(ProcessModel model) {
        printedSteps.clear();
        outputArea.append("Model: " + model.getId().getKey() + "\n");
        for (ProcessUnitModel unit : model.getProcessUnitModels().getValues()) {
            String startStep = unit.getStartProcessStep();
            if (startStep != null && !startStep.isEmpty()) {
                outputArea.append("Unit Start Process Step: " + unit.getName() + " - " + startStep + "\n");
                ProcessStepModel step = unit.getProcessStepModels().get(startStep);
                if (step != null && !printedSteps.contains(step.getId().getKey())) {
                    printProcessSteps(step, model);
                }
            }
        }
    }

    /**
     *
     * @param model
     */
    private static void printDataObjectProcessModel(ProcessModel model) {
        outputArea.append("Model: " + model.getId().getKey() + "\n");
        for (ProcessUnitModel unit : model.getProcessUnitModels().getValues()) {
            for (ProcessStepModel step : unit.getProcessStepModels().getValues()) {
                printProcessStep(step, model.getId().getKey());
                outputArea.append("\n");
            }
        }
    }

    private static void printProcessStep(ProcessStepModel step, String dataObject) {
        outputArea.append("        Unit:   " + step.getId().getContext().split("/")[1] + "\n");
        outputArea.append("Process Step:   " + step.getId().getKey() + "\n");
        outputArea.append("        Name:   " + step.getName() + "\n");
        for (ProcessFunction func : step.getProcessFunctions()) {
            switch (func.getClass().getName()) {
                case "uflow.data.function.immutable.RequireFunction":
                    RequireFunction reqFunc = (RequireFunction) func;
                    if (reqFunc.getValues().contains(dataObject)) {
                        outputArea.append("    Requires:   " + dataObject + "\n");
                    }
                    break;
                case "uflow.data.function.immutable.ProvideFunction":
                    ProvideFunction provFunc = (ProvideFunction) func;
                    if (provFunc.getKey().equals(dataObject)) {
                        outputArea.append("    Provides:   " + dataObject + "\n");
                    }
                    break;
                case "uflow.data.function.immutable.ProceedFunction":
                    ProceedFunction procFunc = (ProceedFunction) func;
                    if (procFunc.getValues().containsKey(dataObject)) {
                        outputArea.append("   Next Step:   " + procFunc.getNext() + "\n");
                        outputArea.append("       Value:   " + dataObject + "\n");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static void printProcessSteps(ProcessStepModel step, ProcessModel model) {
        boolean prevFinished = true;
        // check if all the previous Steps are finished
        for (ProcessStepModel prev : modelNavigator.getPrevSteps(step)) {
            if (!printedSteps.contains(prev.getId().getKey())) {
                prevFinished = false;
            }
        }
        if (prevFinished) {

            outputArea.append("        Unit:   " + step.getId().getContext().split("/")[1] + "\n");
            outputArea.append("Process Step:   " + step.getId().getKey() + "\n");
            outputArea.append("        Name:   " + step.getName() + "\n");

            for (ProcessFunction func : step.getProcessFunctions()) {
                switch (func.getClass().getName()) {
                    case "uflow.data.function.immutable.RequireFunction":
                        RequireFunction reqFunc = (RequireFunction) func;
                        outputArea.append("    Requires:   " + reqFunc.getValues() + "\n");
                        break;
                    case "uflow.data.function.immutable.RequestInputFunction":
                        /* not used */
                        break;
                    case "uflow.data.function.immutable.ProvideFunction":
                        ProvideFunction provFunc = (ProvideFunction) func;
                        outputArea.append("    Provides:   " + provFunc.getValue() + "\n");
                        break;
                    case "uflow.data.function.immutable.CallFunction":
                        /* not used */
                        break;
                    case "uflow.data.function.immutable.ProceedFunction":
                        ProceedFunction procFunc = (ProceedFunction) func;
                        outputArea.append("   Next Step:   " + procFunc.getNext() + "\n");
                        break;
                    default:
                        break;
                }
            }
            printedSteps.add(step.getId().getKey());
            outputArea.append("\n");
            List<ProcessStepModel> nextSteps = modelNavigator.getNextSteps(step);
            for (ProcessStepModel nextStep : nextSteps) {
                if (nextStep != null && !printedSteps.contains(nextStep.getId().getKey())) {
                    printProcessSteps(nextStep, model);
                }
            }
        }
    }
}
