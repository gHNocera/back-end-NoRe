package com.ghartmann.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import com.ghartmann.JPAUtil;
import com.ghartmann.domain.User;

public class UserDAO implements IUserDAO {


    @Override
    public boolean registerUser(User user) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            entityManager.persist(user);

            transaction.commit();
            
            return true;
        } catch (Exception e) {
            if(transaction != null && transaction.isActive()){
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao adicionar usuário", e);
        } finally{
            entityManager.close();
        }
    }

    @Override
    public User getUserById(int userId) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        User user = null;

        try {
            user = entityManager.find(User.class, userId);
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean updateUser(User user) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            User existingUser = entityManager.find(User.class, user.getId());
            if (existingUser != null) {
                existingUser.setUserName(user.getUserName());
                existingUser.setEmail(user.getEmail());
                existingUser.setPassword(user.getPassword());
                entityManager.merge(existingUser);
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao atualizar usuário", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            User user = entityManager.find(User.class, userId);
            if (user != null) {
                entityManager.remove(user);
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao deletar usuário", e);
        } finally {
            entityManager.close();
        }
    }
}