package modulizer.ui;

import modulizer.algorithms.ModularizationAlgorithm;
import uflow.data.model.immutable.ProcessModel;

/**
 * Created by Brigitte on 28.12.2016.
 * @author August, Brigitte, Emanuel, Stefanie
 */
public class ModulizerUI {
    public static void main(String[] args) {
        // create Window
        // selection of modularization algorithm
        // upload of process model
        ModularizationAlgorithm algorithm;
        ProcessModel model;

        // start of modularization
        // get the modularized process model
        ProcessModel modularized = algorithm.startModularization(model);

        // print the modularized process model
        printProcessModel(modularized);
    }

    public static void printProcessModel(ProcessModel model) {

    }
}
