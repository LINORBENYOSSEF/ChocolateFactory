package chocolate.factory.model.call;

import chocolate.factory.db.call.Parameter;
import chocolate.factory.db.call.StoredProcedure;

@StoredProcedure("procedures_pkg.delete_ingredient_from_chocolate")
public class DeleteIngredientFromChocolate {

    @Parameter(name = "pchocolateid", index = 1)
    private Integer chocolateId;
    @Parameter(name = "pingredientid", index = 2)
    private Integer ingredientId;

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
}
