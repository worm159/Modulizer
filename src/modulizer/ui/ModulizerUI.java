package modulizer.ui;

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

        ModularizationAlgorithm algorithm = new SingleEntrySingleExit();
        ProcessModel model = ProcessModelFactory.createPurchaseProduct();

        // get the modularized process model
        List<ProcessModel> modularized = algorithm.startModularization(model);

        // print the modularized process model
        for(ProcessModel m : modularized)
            printProcessModel(m);
    }

    public static void printProcessModel(ProcessModel model) {
        for (ProcessUnitModel unit : model.getProcessUnitModels().getValues() ) {
            String startStep = unit.getStartProcessStep();
            System.out.println("Unit Start Process Step: " + startStep);
            ProcessStepModel step = unit.getProcessStepModels().get(startStep);
            if(step != null)
                printProcessStep(step, unit, model);
        }
    }

    private static void printProcessStep(ProcessStepModel step, ProcessUnitModel unit, ProcessModel model) {
        System.out.println("Process Step: " + step.getName());
        System.out.println(step);
        List<String> nextSteps = new ArrayList<>();
        for (ProcessFunction func : step.getProcessFunctions()) {
            switch (func.getClass().getName()) {
                // other cases
                case "uflow.data.function.immutable.ProceedFunction":
                    ProceedFunction f = (ProceedFunction) func;
                    String targetProcessUnit = f.getTargetProcessUnit();
                    String nextStep = f.getNext();
                    nextSteps.add(targetProcessUnit+"/"+nextStep);
                    break;
            }
        }
        for(String x : nextSteps) {
            String[] splitted = x.split("/");
            String unitName = splitted[0];
            String stepName = splitted[1];
            if(unitName==null || unitName.isEmpty()) {
                printProcessStep(unit.getProcessStepModels().get(stepName),unit,model);
            } else {
                ProcessUnitModel nextUnit = model.getProcessUnitModels().get(unitName);
                printProcessStep(nextUnit.getProcessStepModels().get(splitted[1]),nextUnit,model);
            }
        }
    }
}
