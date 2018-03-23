package cn.yzlee.hibernate;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:lyz
 * @Date: 2018/3/23 13:44
 * @Desc:
 **/
public interface HibernateBaseDao<T>
{
    void save(T entity);

    void update(T entity);

    void saveOrUpdate(T entity);

    void merge(T entity);

    void delete(Serializable id);

    T findById(Serializable id);

    List<T> queryPage(String hql, int currentPage, int pageSize);
}
