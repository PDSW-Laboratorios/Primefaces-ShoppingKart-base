/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import com.sun.media.jfxmedia.logging.Logger;
import edu.eci.pdsw.stubs.servicesfacadestub.Producto;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author skinman95
 */

@ManagedBean(name="productService")
@SessionScoped
public class ProductService implements Serializable {
    
    public TreeNode createProducts(List<Producto> products) {
        TreeNode root = new DefaultTreeNode(new Producto(0, "Nombre", 0.0));
        
        for(Producto p : products) {
            TreeNode t = new DefaultTreeNode(p, root);
        }
        
        Logger.logMsg(Logger.DEBUG, "Paso por productService");
        
        return root;
    }
}
