package chocolate.factory.model.call;

import chocolate.factory.db.call.Parameter;
import chocolate.factory.db.call.StoredProcedure;

@StoredProcedure("add_employee")
public class AddEmployee {

    @Parameter(name = "pemployeename", index = 1)
    private String name;
    @Parameter(name = "pjobposition", index = 2)
    private String position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
