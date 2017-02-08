/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.messageboard.sessions;

import enterprise.messageboard.entities.Creds;
import java.security.Timestamp;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author caiqu
 */
@Stateless
public class CredsFacade extends AbstractFacade<Creds> {

    @PersistenceContext(unitName = "MessageBoardPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CredsFacade() {
        super(Creds.class);
    }
    
    public void addCred(int id, String username, String password, String name, Timestamp timestamp){
        em.createQuery("INSERT INTO Creds (id, username, password, name, date) VALUES (:id, :username, :password, :name, :timestamp)")
                .setParameter("id", id)
                .setParameter("username", username)
                .setParameter("password",password)
                .setParameter("name", name)
                .setParameter("timestamp", timestamp).executeUpdate();
    }
}
