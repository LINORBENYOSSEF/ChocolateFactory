package chocolate.factory.model;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;

@Table("ingredients")
public class Ingredient {

    @Column(value = "id", sqlType = Types.INTEGER, primaryKey = true)
    private Integer id;
    @Column(value = "name", sqlType = Types.NVARCHAR)
    private String name;
    @Column(value = "amount", sqlType = Types.INTEGER)
    private Integer amount;
    @Column(value = "price", sqlType = Types.REAL)
    private Double priceInDollars;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Double getPriceInDollars() {
        return priceInDollars;
    }

    public void setPriceInDollars(Double priceInDollars) {
        this.priceInDollars = priceInDollars;
    }
}
