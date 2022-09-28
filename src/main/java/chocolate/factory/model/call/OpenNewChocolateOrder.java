package chocolate.factory.model.call;

import chocolate.factory.db.call.Parameter;
import chocolate.factory.db.call.StoredProcedure;

import java.sql.Types;
import java.time.LocalDate;

@StoredProcedure("procedures_pkg.open_new_chocolate_order")
public class OpenNewChocolateOrder {

    @Parameter(name = "porderdate", index = 1)
    private LocalDate date;
    @Parameter(name = "pclientid", index = 2)
    private Integer clientId;
    @Parameter(name = "porderno", index = 3, outParam = true, sqlType = Types.INTEGER)
    private Integer orderId;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }
}
