package Test;

import Test.factory.ProcessModelFactory;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.function.modifier.ProceedFunctionModifier;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

/**
 * Created by augus on 12.12.2016.
 */
public class Test {

    public static void main (String [ ] args) {
        ProcessModel test = ProcessModelFactory.createBspSeseVerschachtelt2End();
        ModelNavigator mn = new ModelNavigator(test);
        mn.printModel();
        System.out.println(mn.isExitToEntry(mn.getStep("SESE 1 Start"), mn.getStep("SESE 1 left")));
        System.out.println(mn.getSESEExitToEntry(mn.getStep("SESE 1 right, SESE 2 Start")));

        System.out.println("");

        ProcessModel test2 = ProcessModelFactory.createBspSeseEinfach();
        ModelNavigator mn2 = new ModelNavigator(test2);
        System.out.println(mn2.getSESEExitToEntry(mn2.getStep("SESE Start")));
/*
        test.getAuthorizedStartRoles();

        for (ProcessUnitModel unit : test.getProcessUnitModels().getValues() ) {
            System.out.println("Unit Start Process Step: " + unit.getStartProcessStep());
            for (ProcessStepModel step : unit.getProcessStepModels().getValues()) {
                System.out.println("Step: " + step.getName());
                for (ProcessFunction func : step.getProcessFunctions()) {
                    System.out.println("Function: " + func);
                    if (func.getClass() == ProceedFunction.class)
                        System.out.println("ProceedFunction Klasse: " + func.getClass());
                }
            }
        }
        */

    }
}
