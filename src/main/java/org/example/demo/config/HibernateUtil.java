package org.example.demo.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory; // Declare a static SessionFactory variable

    static {
        try {
            // Load Hibernate configuration from hibernate.cfg.xml by default
            Configuration configuration = new Configuration();

            // Database connection properties
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/hotel_management");
            configuration.setProperty("hibernate.connection.username", "root");
            configuration.setProperty("hibernate.connection.password", "nsk@123K");
            configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");

            // Specify the dialect for MySQL
            configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");

            // Disable second-level cache
            configuration.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");

            // Echo all executed SQL to stdout
            configuration.setProperty("hibernate.show_sql", "true");

            // Automatically update the schema
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");

            // Add annotated classes (entities) for Hibernate to manage
            configuration.addAnnotatedClass(org.example.demo.models.User.class);
            configuration.addAnnotatedClass(org.example.demo.models.RoomCategory.class);
            configuration.addAnnotatedClass(org.example.demo.models.Room.class);
            configuration.addAnnotatedClass(org.example.demo.models.Customer.class);
            configuration.addAnnotatedClass(org.example.demo.models.Reservation.class);

            // Build the SessionFactory based on the configuration
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            // Wrap and rethrow any exceptions during initialization
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Return the configured SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
