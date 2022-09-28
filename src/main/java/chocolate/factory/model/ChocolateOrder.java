package chocolate.factory.model;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;
import java.time.LocalDate;

@Table("chocolate_orders")
public class ChocolateOrder {

    @Column(value = "id", sqlType = Types.INTEGER, primaryKey = true)
    private Integer id;
    @Column(value = "client_id", sqlType = Types.INTEGER)
    private Integer clientId;
    @Column(value = "date", sqlType = Types.DATE)
    private LocalDate date;
    @Column(value = "status", sqlType = Types.INTEGER)
    private OrderStatus status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
