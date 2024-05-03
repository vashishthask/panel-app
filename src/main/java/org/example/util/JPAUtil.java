package org.example.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;

public class JPAUtil {
    private static EntityManagerFactory emf;

    public static void init(String persistenceUnitName, Map<String, String> props) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName, props);
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf != null) {
            emf.close();
        }
    }
}
