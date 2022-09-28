package chocolate.factory.model;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;
import javafx.scene.layout.VBox;

import java.sql.Types;

@Table("chocolate_order_entry")
public class ChocolateOrderEntry {

    @Column(value = "chocolate_id", sqlType = Types.INTEGER)
    private Integer chocolateId;
    @Column(value = "order_id", sqlType = Types.INTEGER)
    private Integer orderId;
    @Column(value = "amount", sqlType = Types.INTEGER)
    private Integer amount;

    public Integer getChocolateId() {
        return chocolateId;
    }

    public void setChocolateId(Integer chocolateId) {
        this.chocolateId = chocolateId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
