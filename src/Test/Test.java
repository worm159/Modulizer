package Test;

import modulizer.factory.ProcessModelFactory;
import modulizer.model.ModelNavigator;
import uflow.data.common.immutable.Id;
import uflow.data.model.immutable.ProcessModel;

/**
 * Created by augus on 12.12.2016.
 */
public class Test {

    public static void main (String [ ] args) {
        Id id = null;

        ProcessModel test = ProcessModelFactory.createSeseVerschachtelt2End();
        ModelNavigator mn = new ModelNavigator(test, true);
        /*old: System.out.println(mn.isExitToEntry(mn.getStep("SESE 1 Start"), mn.getStep("SESE 1 left")));
        System.out.println(mn.getSESEExitToEntry(mn.getStep("SESE 1 right, SESE 2 Start")));
        */
/*

        System.out.println("");

        ProcessModel test2 = ProcessModelFactory.createSeseEinfach();
        ModelNavigator mn2 = new ModelNavigator(test2);
        id = new Id("ProcessStepModel", "SESE Start", "SeseEinfach/Unit-1");
        System.out.println(mn2.getStep(id));
        System.out.println("Exit: " + mn2.getSESEExitToEntry(mn2.getStep(id)));

        System.out.println("");
        System.out.println(mn2.getPrevSteps(mn2.getStep(id)));

        System.out.println("");
        System.out.println("");

        ProcessModel test3 = ProcessModelFactory.createSese2UnitKeineZerteilungV2();
        ModelNavigator mn3 = new ModelNavigator(test3);
        System.out.println(mn3.getFirstSteps());
        id = new Id("ProcessStepModel", "SESE Start ?", "Sese2UnitKeineZerteilungV2/Unit-1");
        System.out.println(id);
        System.out.println(mn3.getStep(id));
        System.out.println(mn3.getNextSteps(mn3.getStep(id)));

        System.out.println("=========================================================================================");
        System.out.println("Exit: " + mn3.getSESEExitToEntry(mn3.getStep(id)));

        System.out.println("Ist step1 ein Vorgänger von step 2 TEST =========================================================================================");
        ProcessModel test4 = ProcessModelFactory.createSese2UnitKeineZerteilungV2();
        ModelNavigator mn4 = new ModelNavigator(test4);

        System.out.println("=========================================================================================");
        Id id2 = new Id("ProcessStepModel", "SESE Start ?", "Sese2UnitKeineZerteilungV2/Unit-1");
        Id id1 = new Id("ProcessStepModel", "SESE End ?", "Sese2UnitKeineZerteilungV2/Unit-1");
        System.out.println(mn4.isStepBeforeStep(mn4.getStep(id1), mn4.getStep(id2)));

        System.out.println(mn4.isStepAfterStep(mn4.getStep(id1), mn4.getStep(id2)));

*/

        System.out.println("=========================================================================================");
        ProcessModel test5 = ProcessModelFactory.createSeseGesamteZerteilung();
        ModelNavigator mn5 = new ModelNavigator(test5, true);

        //Id id1 = new Id("ProcessStepModel", "SESE Left ?", "SeseKeineZerteilungV2/Unit-1");
        //Id id2 = new Id("ProcessStepModel", "SESE Start ?", "SeseKeineZerteilungV2/Unit-1");
        //System.out.println(mn5.isStepBeforeStep(mn5.getStep(id1), mn5.getStep(id2)));

        id = new Id("ProcessStepModel", "SESE Start ?", "SeseKeineZerteilungV1/Unit-1");
        Id id2 = new Id("ProcessStepModel", "End Step", "SeseKeineZerteilungV1/Unit-1");

        System.out.println(mn5.isExitToEntry(mn5.getStep(id), mn5.getStep(id2)));

        System.out.println("\n\nExit: " + mn5.getSESEExitToEntry(mn5.getStep(id)));





    }
}
