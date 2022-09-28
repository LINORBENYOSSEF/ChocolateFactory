package chocolate.factory.db.dao;

import java.util.List;

public interface Dao<T> {

    List<T> getAll() throws DaoException;
    List<T> findByColumnValue(String columnName, Object value) throws DaoException;
    void insert(T t) throws DaoException;
    void update(T t) throws DaoException;
    void delete(T t) throws DaoException;
    void deleteByColumnValue(String columnName, Object value) throws DaoException;
}
