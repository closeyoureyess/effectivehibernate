package com.effectivemobile.hibernatejpa.effectivehibernate.repositories;

import com.effectivemobile.hibernatejpa.effectivehibernate.dto.TaskDto;
import com.effectivemobile.hibernatejpa.effectivehibernate.entities.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRepositoryImpl implements JdbcRepository<Object, Object> {

    private final SessionFactory sessionFactory;

    public JdbcRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean existsById(Object id, Object entity) throws Exception {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            StringBuilder stringBuilder = new StringBuilder("select count(");
            transaction = session.beginTransaction();
            if (entity instanceof Task) {
                stringBuilder.append("t.id) from Task t where t.id = :id");
            }
            Long countas = commonExistQuery(stringBuilder, id, session);
            transaction.commit();
            if (countas != null) {
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return false;
    }

    private Long commonExistQuery(StringBuilder stringBuilder, Object id, Session session) {
        Long longId = (Long) id;
        return session.createQuery(stringBuilder.toString(), Long.class)
                .setParameter("id", longId)
                .uniqueResult();
    }

    @Override
    public Object save(Object entity) throws Exception {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return entity;
    }

    @Override
    public Object update(Object newEntity) throws Exception {
        Transaction transaction = null;
        Session session = null;
        Object updatedEntity;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            updatedEntity = session.merge(newEntity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return updatedEntity;
    }

    @Override
    public Object findById(Object id, Object entity) throws Exception {
        Transaction transaction = null;
        Session session = null;
        Object objectFromDB = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            if (entity instanceof TaskDto || entity instanceof Task) {
                objectFromDB = session.get(Task.class, id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
        return objectFromDB;
    }

    @Override
    public void deleteById(Object id, Object entity) throws Exception {
        Transaction transaction = null;
        Session session = null;
        Object objectFromDB;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            objectFromDB = session.get(entity.getClass(), id);
            if (objectFromDB != null) {
                session.remove(objectFromDB);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}