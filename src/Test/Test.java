package Test;

import Test.factory.ProcessModelFactory;
import uflow.data.function.immutable.ProceedFunction;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

/**
 * Created by augus on 12.12.2016.
 */
public class Test {

    public static void main (String [ ] args) {
        ProcessModel test = ProcessModelFactory.createBsp1();

        test.getAuthorizedStartRoles();

        for (ProcessUnitModel unit : test.getProcessUnitModels().getValues() ) {
            System.out.println("Unit Start Process Step: " + unit.getStartProcessStep());
            for (ProcessStepModel step : unit.getProcessStepModels().getValues()) {
                System.out.println("Step: " + step.getName());
                for (ProcessFunction func : step.getProcessFunctions()) {
                    System.out.println("Function: " + func);
                }
            }
        }
    }
}
