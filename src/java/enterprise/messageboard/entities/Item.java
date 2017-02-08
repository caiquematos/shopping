/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enterprise.messageboard.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author caiqu
 */
@Entity
@Table(name = "item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i")
    , @NamedQuery(name = "Item.findByIdUser", query = "SELECT i FROM Item i WHERE i.itemPK.idUser = :idUser")
    , @NamedQuery(name = "Item.findByIdProduct", query = "SELECT i FROM Item i WHERE i.itemPK.idProduct = :idProduct")
    , @NamedQuery(name = "Item.findByQuantity", query = "SELECT i FROM Item i WHERE i.quantity = :quantity")})
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ItemPK itemPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;

    public Item() {
    }

    public Item(ItemPK itemPK) {
        this.itemPK = itemPK;
    }

    public Item(ItemPK itemPK, int quantity) {
        this.itemPK = itemPK;
        this.quantity = quantity;
    }

    public Item(int idUser, int idProduct) {
        this.itemPK = new ItemPK(idUser, idProduct);
    }

    public ItemPK getItemPK() {
        return itemPK;
    }

    public void setItemPK(ItemPK itemPK) {
        this.itemPK = itemPK;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemPK != null ? itemPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Item)) {
            return false;
        }
        Item other = (Item) object;
        if ((this.itemPK == null && other.itemPK != null) || (this.itemPK != null && !this.itemPK.equals(other.itemPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "enterprise.messageboard.entities.Item[ itemPK=" + itemPK + " ]";
    }
    
}
