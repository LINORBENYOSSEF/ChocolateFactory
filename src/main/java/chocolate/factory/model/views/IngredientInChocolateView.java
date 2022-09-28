package chocolate.factory.model.views;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;

@Table(value = "INGREDIENT_IN_CHOCOLATE_VIEW", isView = true)
public class IngredientInChocolateView {

    @Column(value = "chocolate_id", sqlType = Types.INTEGER)
    private Integer chocolateId;
    @Column(value = "ingredient_id", sqlType = Types.INTEGER)
    private Integer ingredientId;
    @Column(value = "ingredient_name", sqlType = Types.NVARCHAR)
    private String ingredientName;
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

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
