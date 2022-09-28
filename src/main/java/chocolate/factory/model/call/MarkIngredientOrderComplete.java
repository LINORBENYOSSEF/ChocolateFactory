package chocolate.factory.model.call;

import chocolate.factory.db.call.Parameter;
import chocolate.factory.db.call.StoredProcedure;

@StoredProcedure("procedures_pkg.mark_ingredient_order_complete")
public class MarkIngredientOrderComplete {

    @Parameter(name = "porderno", index = 1)
    private Integer orderId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
