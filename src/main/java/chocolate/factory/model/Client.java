package chocolate.factory.model;

import chocolate.factory.db.dao.Column;
import chocolate.factory.db.dao.Table;

import java.sql.Types;

@Table("clients")
public class Client {

    @Column(value = "id", sqlType = Types.INTEGER, primaryKey = true)
    private Integer id;
    @Column(value = "name", sqlType = Types.NVARCHAR)
    private String name;

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
}
