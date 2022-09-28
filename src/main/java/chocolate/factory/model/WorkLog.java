package chocolate.factory.model;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Table("work_log")
public class WorkLog {

    @Column(value = "employee_id", sqlType = Types.INTEGER)
    private Integer employeeId;
    @Column(value = "clock_in", sqlType = Types.TIMESTAMP)
    private LocalDateTime clockInTime;
    @Column(value = "clock_out", sqlType = Types.TIMESTAMP)
    private LocalDateTime clockOutTime;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getClockDate() {
        if (clockInTime == null) {
            return null;
        }
        return clockInTime.toLocalDate();
    }

    public LocalTime getClockInTime() {
        if (clockInTime == null) {
            return null;
        }
        return clockInTime.toLocalTime();
    }

    public void setClockInTime(LocalDateTime clockInTime) {
        this.clockInTime = clockInTime;
    }

    public LocalTime getClockOutTime() {
        if (clockOutTime == null) {
            return null;
        }
        return clockOutTime.toLocalTime();
    }

    public void setClockOutTime(LocalDateTime clockOutTime) {
        this.clockOutTime = clockOutTime;
    }
}
