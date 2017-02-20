/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.stubs.servicesfacadestub.Producto;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author skinman95
 */

@ManagedBean(name="productService")
@ApplicationScoped
public class ProductService {
    
    public TreeNode createProducts(List<Producto> products) {
        TreeNode root = new DefaultTreeNode(new Producto(0, "Nombre", 0.0));
        
        for(Producto p : products) {
            TreeNode t = new DefaultTreeNode(p, root);
        }
        
        return root;
    }
}
