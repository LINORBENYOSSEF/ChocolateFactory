package chocolate.factory.model;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;
import java.time.LocalDate;

@Table("ingredient_orders")
public class IngredientOrder {

    @Column(value = "id", sqlType = Types.INTEGER, primaryKey = true)
    private Integer id;
    @Column(value = "date", sqlType = Types.DATE)
    private LocalDate date;
    @Column(value = "amount", sqlType = Types.INTEGER)
    private Integer amount;
    @Column(value = "status", sqlType = Types.INTEGER)
    private OrderStatus status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
