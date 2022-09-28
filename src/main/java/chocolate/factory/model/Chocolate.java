package chocolate.factory.model;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;

@Table("chocolates")
public class Chocolate {

    @Column(value = "id", sqlType = Types.INTEGER, primaryKey = true)
    private Integer id;
    @Column(value = "name", sqlType = Types.NVARCHAR)
    private String name;
    @Column(value = "sell_price_dollars", sqlType = Types.REAL)
    private Double sellPriceInDollars;
    @Column(value = "amount", sqlType = Types.INTEGER)
    private Integer amountPresentInInventory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSellPriceInDollars() {
        return sellPriceInDollars;
    }

    public void setSellPriceInDollars(double sellPriceInDollars) {
        this.sellPriceInDollars = sellPriceInDollars;
    }

    public int getAmountPresentInInventory() {
        return amountPresentInInventory;
    }

    public void setAmountPresentInInventory(int amountPresentInInventory) {
        this.amountPresentInInventory = amountPresentInInventory;
    }
}
