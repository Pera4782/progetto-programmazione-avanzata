package it.unipi.server;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            configuration.addAnnotatedClass(Paziente.class);
            configuration.addAnnotatedClass(Medico.class);
            
            return configuration.buildSessionFactory();
        } catch (Throwable e) {
            
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        
        getSessionFactory().close();
    }
}
