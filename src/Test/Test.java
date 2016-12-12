package Test;

import Test.factory.ProcessModelFactory;
import uflow.data.model.immutable.ProcessModel;

/**
 * Created by augus on 12.12.2016.
 */
public class Test {

    public static void main (String [ ] args) {
        ProcessModel test = ProcessModelFactory.createPurchaseProduct();

        System.out.println(test);
    }
}
