/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.messageboard.sessions;

import enterprise.messageboard.entities.Item;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author caiqu
 */
@Stateless
public class ItemFacade extends AbstractFacade<Item> {

    @PersistenceContext(unitName = "MessageBoardPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ItemFacade() {
        super(Item.class);
    }
   
    public List<Item> findItemByUserId(int user_id){
        return em.createQuery("SELECT i FROM Item i WHERE i.itemPK.idUser = :idUser")
                .setParameter("idUser", user_id)
                .getResultList();
    }
    
    public Item findItemById(int productId, int userId){
        return (Item) em.createQuery("SELECT i FROM Item i WHERE i.itemPK.idUser = :idUser AND i.itemPK.idProduct = :idProduct")
                .setParameter("idUser", userId)
                .setParameter("idProduct", productId)
                .getSingleResult();
    }
    
}
