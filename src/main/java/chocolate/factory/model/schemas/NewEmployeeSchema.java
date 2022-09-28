package chocolate.factory.model.schemas;

import chocolate.factory.model.EmployeePosition;

public class NewEmployeeSchema {

    private String name;
    private EmployeePosition position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }
}
