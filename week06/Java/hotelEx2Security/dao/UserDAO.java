package hotelEx2Security.dao;

import hotelEx2Security.persistence.HibernateConfig;
import hotelEx2Security.model.Role;
import hotelEx2Security.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;

public class UserDAO implements ISecurityDAO{
    private static EntityManagerFactory emf;
    private static UserDAO instance;
    public UserDAO(EntityManagerFactory _emf){
        this.emf = _emf;
    }

    public static UserDAO getInstance(EntityManagerFactory _emf){
        if(instance == null){
            emf = _emf;
            instance = new UserDAO(emf);
        }
        return instance;
    }

    @Override
    public User createUser(String username, String password) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        User user = new User(username, password);
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return user;
    }

    public User verifyUser(String username, String password){
        EntityManager em = emf.createEntityManager();
            User user = em.find(User.class, username);
            if(user == null) throw new EntityNotFoundException("No user found");
            if(!user.verifyPassword(password)) throw new EntityNotFoundException("wrong password");
            return user;
    }

    public static void main(String[] args){
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryConfig(false);

        UserDAO dao = new UserDAO(emf);
//        dao.createUser("youssef", "youssef");

        try{
            User user = dao.verifyUser("youssef", "youssef1");
            System.out.println(user.getUsername());
        }catch (EntityNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public Role createRole(String role) {
        return null;
    }

    @Override
    public User addRoleToUser(String username, String role) {
        return null;
    }
}