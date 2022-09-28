package chocolate.factory.model;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;

@Table("ingredient_usage_in_chocolate")
public class IngredientUsageInChocolate {

    @Column(value = "chocolate_id", sqlType = Types.INTEGER)
    private Integer chocolateId;
    @Column(value = "ingredient_id", sqlType = Types.INTEGER)
    private Integer ingredientId;
    @Column(value = "amount", sqlType = Types.INTEGER)
    private Integer amount;

    public Integer getChocolateId() {
        return chocolateId;
    }

    public void setChocolateId(Integer chocolateId) {
        this.chocolateId = chocolateId;
    }

    public Integer getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Integer ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
