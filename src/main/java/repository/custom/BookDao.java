package repository.custom;

import entity.BookEntity;
import entity.CategoryEntity;
import repository.CrudDao;

import java.util.List;

public interface BookDao extends CrudDao<BookEntity,String> {

    int setValueToCard();
    List<CategoryEntity> getAllCategories();
    boolean updateStatus(Integer id);
    int setDueDateValueToCard();
}
