package it.unipi.server.model.utils;

import it.unipi.server.model.Paziente;
import it.unipi.server.model.Visita;
import it.unipi.server.model.Medico;
import it.unipi.server.model.Medico;
import it.unipi.server.model.Paziente;
import it.unipi.server.model.Visita;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * funzione per costruire la sessionFactory
     * @return la sessionFactory o null in caso di errore
     */
    private static SessionFactory buildSessionFactory() {
        try {
            
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            configuration.addAnnotatedClass(Paziente.class);
            configuration.addAnnotatedClass(Medico.class);
            configuration.addAnnotatedClass(Visita.class);
            
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
