package Test;

import Test.factory.ProcessModelFactory;
import uflow.data.function.immutable.ProcessFunction;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.immutable.ProcessStepModel;
import uflow.data.model.immutable.ProcessUnitModel;

/**
 * Created by augus on 12.12.2016.
 */
public class Test {

    public static void main (String [ ] args) {
        ProcessModel test = ProcessModelFactory.createPurchaseProduct();

        test.getAuthorizedStartRoles();

        for (ProcessUnitModel unit : test.getProcessUnitModels().getValues() ) {
            for (ProcessStepModel step : unit.getProcessStepModels().getValues()) {
                for (ProcessFunction func : step.getProcessFunctions()) {
                    System.out.println(func);
                }
            }
        }

        //System.out.println(test);
    }
}
