package repository;

import java.util.List;

public interface CrudDao <T,ID> extends SuperDao {

    boolean add( T entity);
    boolean Delete(ID id);
    boolean update( T entity);
    T Search(ID id);
    List<T> getAll();

}
