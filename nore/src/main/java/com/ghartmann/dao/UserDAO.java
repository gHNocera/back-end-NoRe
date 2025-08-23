package com.ghartmann.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;


import com.ghartmann.JPAUtil;
import com.ghartmann.PasswordUtil;
import com.ghartmann.domain.User;

public class UserDAO implements IUserDAO {

    private PasswordUtil passwordUtil = new PasswordUtil();

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
            e.printStackTrace(); // Adicionado para logar a exceção
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
                existingUser.setUsername(user.getUsername());
                existingUser.setEmail(user.getEmail());
                if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                    existingUser.setPasswordHash(user.getPasswordHash());
                }
                existingUser.setEmailVerified(user.isEmailVerified());
                existingUser.setVerificationCode(user.getVerificationCode());
                existingUser.setCodeExpiryDate(user.getCodeExpiryDate());
                
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

    @Override
    public boolean loginUser(String email, String password) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            User user = getUserByEmail(email);
            if (user == null) {
                return false;
            }

            if (!user.isEmailVerified()) {
                throw new RuntimeException("Email não verificado");
            }

            if (passwordUtil.verifyPassword(password, user.getPasswordHash())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer login", e);
        } finally {
            entityManager.close();
        }
    }

     @Override
    public User getUserByVerificationCode(String code) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.verificationCode = :code", User.class);
            query.setParameter("code", code);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário por código", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean verifyUserEmail(String code) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = null;

        try {
            User user = getUserByVerificationCode(code);
            if (user == null) {
                return false;
            }

            transaction = entityManager.getTransaction();
            transaction.begin();

            user.setEmailVerified(true);
            user.setVerificationCode(null);
            user.setCodeExpiryDate(null);
            user.setCodeAttempts(0);
            entityManager.merge(user);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao verificar email", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean incrementVerificationAttempts(String email) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        EntityTransaction transaction = null;

        try {
            User user = getUserByEmail(email);
            if (user == null) {
                return false;
            }

            transaction = entityManager.getTransaction();
            transaction.begin();

            user.incrementCodeAttempts();
            entityManager.merge(user);

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Erro ao incrementar tentativas", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public User getUserByEmail(String email) {
        EntityManager entityManager = JPAUtil.getEntityManager();
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar usuário por email", e);
        } finally {
            entityManager.close();
        }
    }


}