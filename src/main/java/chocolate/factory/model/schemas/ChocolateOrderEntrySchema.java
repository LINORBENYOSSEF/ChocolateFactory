package chocolate.factory.model.schemas;

import chocolate.factory.model.Chocolate;

public class ChocolateOrderEntrySchema {

    private Chocolate chocolate;
    private int amount;

    public ChocolateOrderEntrySchema(Chocolate chocolate, int amount) {
        this.chocolate = chocolate;
        this.amount = amount;
    }

    public ChocolateOrderEntrySchema(Chocolate chocolate) {
        this(chocolate, 1);
    }

    public Chocolate getChocolate() {
        return chocolate;
    }

    public void setChocolate(Chocolate chocolate) {
        this.chocolate = chocolate;
    }

    public String getChocolateName() {
        return chocolate.getName();
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
