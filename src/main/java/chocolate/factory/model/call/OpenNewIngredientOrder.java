package chocolate.factory.model.call;

import chocolate.factory.db.call.Parameter;
import chocolate.factory.db.call.StoredProcedure;

import java.sql.Types;
import java.time.LocalDate;

@StoredProcedure("procedures_pkg.open_new_ingredient_order")
public class OpenNewIngredientOrder {

    @Parameter(name = "pingredientid", index = 1)
    private Integer ingredientId;
    @Parameter(name = "porderdate", index = 2)
    private LocalDate date;
    @Parameter(name = "pamount", index = 3)
    private int amount;
    @Parameter(name = "porderno", index = 4, outParam = true, sqlType = Types.INTEGER)
    private Integer orderId;

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
