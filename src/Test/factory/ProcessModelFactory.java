package Test.factory;

import uflow.data.function.modifier.*;
import uflow.data.model.immutable.ProcessModel;
import uflow.data.model.modifier.ProcessModelModifier;
import uflow.data.model.modifier.ProcessStepModelModifier;
import uflow.data.model.modifier.ProcessUnitModelModifier;

/**
 * Created by Signum+ Software GmbH
 * Author: Dr. Markus Jahn
 * Email: jahn@signum-plus.at
 * Web: www.signum-plus.at
 * Date: 06/2015
 */

public class ProcessModelFactory {

  public static ProcessModel createPurchaseProduct() {
    return new ProcessModelModifier()

        //model meta data
        .setId("PurchaseProduct")
        .setName("Purchase Product")
        .setCreator("Markus Jahn")
        .setDescription("This is a demo process for presenting uFlow.")
        .addAuthorizedStartRole("Requester").setProcessUnitModel("Requester", new ProcessUnitModelModifier()

            //set subject meta data
            .setStartProcessStep("GetProductDescription")
            .addAuthorizedRole("Requester")

                //add subject
            .setProcessStepModel("GetProductDescription", new ProcessStepModelModifier()
                .setId("GetProductDescription")
                .setName("GetProductDescription")

                .addProcessFunction(new AssignUserFunctionModifier()
                    .getAssignUserFunction())
                .addProcessFunction(new RequestInputFunctionModifier()
                    .setModel("RequestProductDescription")
                    .getRequestInputFunction())
                .addProcessFunction(new RequireFunctionModifier()
                    .addValue("ProductDescription")
                    .getRequireFunction())


                .addProcessFunction(new ProceedFunctionModifier()
                    .setNext("<: return \"SendApprovalRequest\"; :>")
//                    .addValue("ProductDescription")
                    .addValue("ProductDescription", "<ProductDescription>")
                    .getProceedFunction())
                .getProcessStepModel())

            .setProcessStepModel("SendApprovalRequest", new ProcessStepModelModifier()
                .addProcessFunction(new ProvideFunctionModifier()
                    .setTo("Approver")
//                    .addValue("ProductDescription")
                    .getProvideFunction())
                .addProcessFunction(new ProceedFunctionModifier()
                    .setNext("ReceiveApprovalResponse")
                    .getProceedFunction())
                .getProcessStepModel())

            .setProcessStepModel("ReceiveApprovalResponse", new ProcessStepModelModifier()
                .addProcessFunction(new RequireFunctionModifier()
                    .addValue("IsApproved")
                    .getRequireFunction())
                .addProcessFunction(new ProceedFunctionModifier()
                    .setNext("<: locals -> (boolean) locals.get(\"IsApproved\") ? \"BuyProduct\" : null :>")
                    .getProceedFunction())
                .getProcessStepModel())

            .setProcessStepModel("BuyProduct", new ProcessStepModelModifier()
                .addProcessFunction(new CallFunctionModifier()
                    .setCode("System.out.println(\"  >> Buy Product <<\");")
                    .getCallFunction())
                .getProcessStepModel())

            .getProcessUnitModel())

            //add subject
        .setProcessUnitModel("Approver", new ProcessUnitModelModifier()
            .setStartProcessStep("ReceiveApprovalRequest")
            .addAuthorizedRole("Approver")

            .setProcessStepModel("ReceiveApprovalRequest", new ProcessStepModelModifier()
                .addProcessFunction(new RequireFunctionModifier()
                    .addValue("ProductDescription")
                    .getRequireFunction())
                .addProcessFunction(new ProceedFunctionModifier()
                    .setNext("ProcessApprovalRequest")
//                    .addValue("ProductDescription")
                    .addValue("ProductDescription", "<ProductDescription>")
                    .getProceedFunction())
                .getProcessStepModel())

            .setProcessStepModel("ProcessApprovalRequest", new ProcessStepModelModifier()
//                .addProcessFunction(new RequestUserFunctionModifier()
//                    .setRoleId("Approver")
//                    .setModel("RequestPurchaseApprover")
//                    .getRequestUserFunction())
                .addProcessFunction(new RequestInputFunctionModifier()
                    .setModel("RequestPurchaseApprover")
                    .getRequestInputFunction())
                .addProcessFunction(new RequireFunctionModifier()
                    .addValue("IsApproved")
                    .getRequireFunction())
                .addProcessFunction(new ProceedFunctionModifier()
                    .setNext("SendApprovalResponse")
//                    .addValue("IsApproved")
                    .addValue("IsApproved", "<IsApproved>")
                    .getProceedFunction())
                .getProcessStepModel())

            .setProcessStepModel("SendApprovalResponse", new ProcessStepModelModifier()
                .addProcessFunction(new ProvideFunctionModifier()
                    .setTo("Requester")
//                    .addValue("IsApproved")
                    .getProvideFunction())
                .getProcessStepModel())

            .getProcessUnitModel())

        .getProcessModel();
  }
}
