package chocolate.factory.model.views;

import chocolate.factory.model.OrderStatus;

import java.time.LocalDate;

public interface ChocolateOrderView {

    Integer getOrderId();
    Integer getClientId();
    String getClientName();
    LocalDate getDate();

    OrderStatus getStatus();
}
