package ft.training.by.dao.interfaces;

import ft.training.by.bean.Entity;
import ft.training.by.dao.exception.DAOException;

import java.util.List;
import java.util.Optional;

public interface Dao<K, T extends Entity> {
    List<T> findAll() throws DAOException;

    Optional<T> findEntityById(K id) throws DAOException; // read

    boolean delete(K id) throws DAOException;

    boolean delete(T entity) throws DAOException;

    boolean create(T entity) throws DAOException;

    T update(T entity);
}
