package chocolate.factory.model;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;

@Table("employees")
public class Employee {

    @Column(value = "employee_id", sqlType = Types.INTEGER, primaryKey = true)
    private Integer id;
    @Column(value = "employee_name", sqlType = Types.NVARCHAR)
    private String name;
    @Column(value = "salary", sqlType = Types.REAL)
    private Double salaryPerHourInDollars;

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

    public double getSalaryPerHourInDollars() {
        return salaryPerHourInDollars;
    }

    public void setSalaryPerHourInDollars(double salaryPerHourInDollars) {
        this.salaryPerHourInDollars = salaryPerHourInDollars;
    }
}
