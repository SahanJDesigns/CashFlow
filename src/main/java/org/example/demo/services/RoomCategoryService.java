package org.example.demo.services;

import org.example.demo.config.HibernateUtil;
import org.example.demo.models.RoomCategory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RoomCategoryService {
    public void addRoomCategory(RoomCategory roomCategory) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(roomCategory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public RoomCategory getRoomCategory(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(RoomCategory.class, id);
        }
    }

    public List<RoomCategory> getAllRoomCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from RoomCategory", RoomCategory.class).list();
        }
    }

    public void updateRoomCategory(RoomCategory roomCategory) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(roomCategory);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void deleteRoomCategory(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            RoomCategory roomCategory = session.get(RoomCategory.class, id);
            if (roomCategory != null) {
                session.delete(roomCategory);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}
