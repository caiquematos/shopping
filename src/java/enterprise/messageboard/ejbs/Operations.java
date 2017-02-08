/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.messageboard.ejbs;

import enterprise.messageboard.entities.Products;
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
public class Operations {

    @PersistenceContext(unitName = "MessageBoardPU")
    private EntityManager em;

    public List<Products> retrieveProducts(){
        return em.createQuery("SELECT p FROM Products p").getResultList();
    }
    
     public List<Users> retrieveUsers(){
        return em.createQuery("SELECT u FROM Users u").getResultList();
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
