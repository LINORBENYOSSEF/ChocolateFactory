package chocolate.factory.model.call;

import chocolate.factory.db.call.Parameter;
import chocolate.factory.db.call.StoredProcedure;

@StoredProcedure("procedures_pkg.add_new_chocolate_order_entry")
public class AddNewChocolateOrderEntry {

    @Parameter(name = "porderid", index = 1)
    private Integer orderId;
    @Parameter(name = "pchocolateId", index = 2)
    private Integer chocolateId;
    @Parameter(name = "pamount", index = 3)
    private Integer amount;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getChocolateId() {
        return chocolateId;
    }

    public void setChocolateId(Integer chocolateId) {
        this.chocolateId = chocolateId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
