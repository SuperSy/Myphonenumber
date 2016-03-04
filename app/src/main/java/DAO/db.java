package DAO;

import java.util.List;

import model.People;

/**
 * Created by Sy on 2016/2/25.
 */
public interface db{
    public List mQuery();
    public void mInsert(People people);
    public void mDel(String name);
    public void mUpdata(String...str);
}
