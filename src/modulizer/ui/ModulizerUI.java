package modulizer.ui;

import Test.factory.ProcessModelFactory;
import modulizer.algorithms.ModularizationAlgorithm;
import modulizer.algorithms.SingleEntrySingleExit;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class ModulizerUI {

    private JPanel panel;
    private JComboBox algorithm;
    private JTextField processModel;
    private JTextPane textPane1;

    public static void main(String[] args) {
        // create Window
        // selection of modularization algorithm
        // upload of process model

        ModularizationAlgorithm algorithm = new SingleEntrySingleExit();
        ProcessModel model = ProcessModelFactory.createFunctionalTest();

        // get the modularized process model
        ArrayList<ProcessModel> modularized = algorithm.startModularization(model);

        // print the modularized process model
        for(ProcessModel m : modularized)
            printProcessModel(m);
    }

    public static void printProcessModel(ProcessModel model) {

    }
}
