package cn.icloudit.itfree.service;

import java.util.List;

public interface IBaseService<T> {

    List<T> queryAll();

    int save(T entity);

    int delete(Object id);

    T queryById(Object id);

    int update(T entity);

    List<T> queryByTj(T entity);

    List<T> queryByPager(T entity);
}
