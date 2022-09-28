package chocolate.factory.model.views;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;
import chocolate.factory.model.OrderStatus;

import java.sql.Types;
import java.time.LocalDate;

@Table(value = "INGREDIENTS_ORDERS_VIEW", isView = true)
public class IngredientOrderView {

    @Column(value = "order_id", sqlType = Types.INTEGER)
    private Integer orderId;
    @Column(value = "ingredient_id", sqlType = Types.INTEGER)
    private Integer ingredientId;
    @Column(value = "ingredient_name", sqlType = Types.NVARCHAR)
    private String ingredientName;
    @Column(value = "order_date", sqlType = Types.DATE)
    private LocalDate date;
    @Column(value = "amount", sqlType = Types.INTEGER)
    private Integer amount;
    @Column(value = "status", sqlType = Types.INTEGER)
    private OrderStatus status;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
