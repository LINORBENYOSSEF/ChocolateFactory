package chocolate.factory.model.views;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;

@Table(value = "CHOCOLATE_ORDERS_ENTRY_VIEW", isView = true)
public class ChocolateOrderEntryView {

    @Column(value = "order_id", sqlType = Types.INTEGER)
    private Integer orderId;
    @Column(value = "chocolate_id", sqlType = Types.INTEGER)
    private Integer chocolateId;
    @Column(value = "chocolate_name", sqlType = Types.NVARCHAR)
    private String chocolateName;
    @Column(value = "amount_wanted", sqlType = Types.INTEGER)
    private Integer amount;
    @Column(value = "amount_available", sqlType = Types.INTEGER)
    private Integer amountAvailable;

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

    public String getChocolateName() {
        return chocolateName;
    }

    public void setChocolateName(String chocolateName) {
        this.chocolateName = chocolateName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Integer getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(Integer amountAvailable) {
        this.amountAvailable = amountAvailable;
    }
}
