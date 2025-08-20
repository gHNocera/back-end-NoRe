package com.ghartmann;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {

    private static final EntityManagerFactory entityManagerFactory;

    static{
        entityManagerFactory = Persistence.createEntityManagerFactory("gHartmann");

    }

    public static EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

    public static void close(){
        if(entityManagerFactory != null){
            entityManagerFactory.close();
        }
    }

}
