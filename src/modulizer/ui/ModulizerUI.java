package modulizer.ui;

import Test.Test;
import Test.factory.ProcessModelFactory;
import com.sun.org.apache.xpath.internal.SourceTree;
import modulizer.algorithms.ModularizationAlgorithm;
import modulizer.algorithms.SingleEntrySingleExit;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uflow.data.function.immutable.ProvideFunction;
import uflow.data.function.immutable.RequestInputFunction;
import uflow.data.function.immutable.RequireFunction;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class ModulizerUI {

    private List<String> printedSteps;

    private JPanel panel;
    private JComboBox algorithm;
    private JTextField processModel;
    private JTextPane textPane1;

    public static void main(String[] args) {
        // create Window
        // selection of modularization algorithm
        // upload of process model
        JFrame frame = new JFrame("Modulizer");
        frame.setContentPane(new ModulizerUI().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        ModularizationAlgorithm algorithm = new SingleEntrySingleExit();
        ProcessModel model = ProcessModelFactory.createPurchaseProductExtended();

        // get the modularized process model
        Map<String, ProcessModel> modularized = algorithm.startModularization(model);

        // print the modularized process model
        for(ProcessModel m : modularized.values())
            printProcessModel(m);
    }

    public static void printProcessModel(ProcessModel model) {
        for (ProcessUnitModel unit : model.getProcessUnitModels().getValues()) {
            String startStep = unit.getStartProcessStep();
            System.out.println("Unit Start Process Step: " + startStep);
            ProcessStepModel step = unit.getProcessStepModels().get(startStep);
            if (step != null) {
                printProcessSteps(step, unit, model);
                System.out.println("");
            }
        }
    }

    private static void printProcessSteps(ProcessStepModel step, ProcessUnitModel unit, ProcessModel model) {
        System.out.println("Process Step:   " + step.getId().getKey());
        System.out.println("        Name:   " + step.getName());
//        System.out.println(step);
        List<String> nextSteps = new ArrayList<>();
        for (ProcessFunction func : step.getProcessFunctions()) {
            switch (func.getClass().getName()) {
                case "uflow.data.function.immutable.RequireFunction":
                    RequireFunction reqFunc = (RequireFunction) func;
                    System.out.println("    Requires:   " + reqFunc.getValues());
                    break;
                case "uflow.data.function.immutable.RequestInputFunction":
                    /* not used */
                    break;
                case "uflow.data.function.immutable.ProvideFunction":
                    ProvideFunction provFunc = (ProvideFunction) func;
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

            if (nextStep != null && nextUnit != null) {
                System.out.println("");
                printProcessSteps(nextStep, nextUnit, model);
            }
        }
    }
}
