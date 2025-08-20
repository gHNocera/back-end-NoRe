package com.ghartmann.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    @Override
    public boolean updateUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public boolean deleteUser(int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }


}
