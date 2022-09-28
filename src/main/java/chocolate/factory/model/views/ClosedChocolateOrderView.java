package chocolate.factory.model.views;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;
import chocolate.factory.model.OrderStatus;

import java.sql.Types;
import java.time.LocalDate;

@Table(value = "CLOSED_CHOCOLATE_ORDERS_VIEW", isView = true)
public class ClosedChocolateOrderView implements ChocolateOrderView {

    @Column(value = "order_id", sqlType = Types.INTEGER)
    private Integer orderId;
    @Column(value = "client_id", sqlType = Types.INTEGER)
    private Integer clientId;
    @Column(value = "client_name", sqlType = Types.NVARCHAR)
    private String clientName;
    @Column(value = "order_date", sqlType = Types.DATE)
    private LocalDate date;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public OrderStatus getStatus() {
        return OrderStatus.COMPLETED;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
