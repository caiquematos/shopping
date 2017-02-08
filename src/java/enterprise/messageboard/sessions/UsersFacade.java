/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.messageboard.sessions;

import enterprise.messageboard.entities.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author caiqu
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "MessageBoardPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }

    public int isThereUser(String string) {
       List<Users> users = em.createQuery("SELECT u FROM Users u WHERE u.username = :username")
                .setParameter("username", string).getResultList();
       return users.size();
    }
    
    public Users findUserByUsername(String string) {
        try{
            return (Users) em.createQuery("SELECT u FROM Users u WHERE u.username = :username")
                .setParameter("username", string).getSingleResult();
        }catch(Exception e){
            return null;
        }
       
    }
        
}
