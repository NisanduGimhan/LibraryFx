package repository.custom;

import entity.MemberEntity;
import repository.CrudDao;

public interface MemberDao extends CrudDao<MemberEntity,Integer> {
    int setValueToCard();
}
