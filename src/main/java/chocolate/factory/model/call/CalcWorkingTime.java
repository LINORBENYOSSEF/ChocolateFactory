package chocolate.factory.model.call;

import chocolate.factory.db.call.Parameter;
import chocolate.factory.db.call.StoredProcedure;

import java.sql.Types;

@StoredProcedure("procedures_pkg.calc_working_time")
public class CalcWorkingTime {

    @Parameter(name = "pemployeeid", index = 1)
    private Integer employeeId;
    @Parameter(name = "pmonth", index = 2)
    private Integer month;
    @Parameter(name = "pyear", index = 3)
    private Integer year;
    @Parameter(name = "empwlhours", index = 4, outParam = true, sqlType = Types.NUMERIC)
    private Number workHours;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Number getWorkHours() {
        return workHours;
    }

    public void setWorkHours(Number workHours) {
        this.workHours = workHours;
    }
}
