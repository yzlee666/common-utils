package cn.yzlee.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @Author:lyz
 * @Date: 2018/3/23 13:47
 * @Desc:
 **/
public class HibernateBaseDaoImpl<T> implements  HibernateBaseDao<T>
{
    private Class<T> clazz;

    protected SessionFactory sessionFactory;

    public HibernateBaseDaoImpl(){
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz = (Class<T>) type.getActualTypeArguments()[0];
    }

    @Resource
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    @Override
    public void save(T entity) {
        Session session = null;
        Transaction tx = null;
        try{
            session = this.getSession();
            tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        }catch(Exception ex){
            System.out.println("保存对象出现错误！");
            ex.printStackTrace();
            if(tx != null){
                tx.rollback();
            }
        }finally{
            if(session != null){
                session.close();
            }
        }
    }

    @Override
    public void update(T entity) {
        Session session = null;
        Transaction tx = null;
        try{
            session = this.getSession();
            tx = session.beginTransaction();
            session.update(entity);
            tx.commit();
        }catch(Exception ex){
            System.out.println("更新对象出现错误！");
            ex.printStackTrace();
            if(tx != null){
                tx.rollback();
            }
        }finally{
            if(session != null){
                session.close();
            }
        }
    }

    @Override
    public void saveOrUpdate(T entity) {
        Session session = null;
        Transaction tx = null;
        try{
            session = this.getSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(entity);
            tx.commit();
        }catch(Exception ex){
            System.out.println("更新对象出现错误！");
            ex.printStackTrace();
            if(tx != null){
                tx.rollback();
            }
        }finally{
            if(session != null){
                session.close();
            }
        }
    }

    @Override
    public void merge(T entity) {
        Session session = null;
        Transaction tx = null;
        try{
            session = this.getSession();
            tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        }catch(Exception ex){
            System.out.println("更新对象出现错误！");
            ex.printStackTrace();
            if(tx != null){
                tx.rollback();
            }
        }finally{
            if(session != null){
                session.close();
            }
        }
    }

    @Override
    public void delete(Serializable id) {
        T obj = findById(id);

        Session session = null;
        Transaction tx = null;
        try{
            session = this.getSession();
            tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }catch(Exception ex){
            System.out.println("删除对象出现错误！");
            ex.printStackTrace();
            if(tx != null){
                tx.rollback();
            }
        }finally{
            if(session != null){
                session.close();
            }
        }
    }

    @Override
    public T findById(Serializable id) {
        Session session = null;
        Transaction tx = null;
        T obj = null;
        try{
            session = this.getSession();
            tx = session.beginTransaction();
            obj = (T) session.get(clazz, id);
            tx.commit();
        }catch(Exception ex){
            System.out.println("查找对象出现错误！");
            ex.printStackTrace();
            if(tx != null){
                tx.rollback();
            }
        }finally{
            if(session != null){
                session.close();
            }
        }
        return obj;
    }

    @Override
    public List<T> queryPage(String hql, int currentPage, int pageSize) {
        List<T> list = null;
        Session session = null;
        Transaction tx = null;
        try{
            session = this.getSession();
            tx = session.beginTransaction();

            Query query = session.createQuery(hql);
            list = query.setFirstResult((currentPage - 1) * pageSize)
                    .setMaxResults(pageSize).list();

            tx.commit();
        }catch(Exception ex){
            System.out.println("分页查询出现错误出现错误！");
            ex.printStackTrace();
            if(tx != null){
                tx.rollback();
            }
        }finally{
            if(session != null){
                session.close();
            }
        }
        return list;
    }

}
