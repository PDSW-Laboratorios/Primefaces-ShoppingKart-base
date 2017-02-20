/*
 * Copyright (C) 2015 hcadavid
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.pdsw.webappsintro.controller;

import edu.eci.pdsw.stubs.servicesfacadestub.CurrencyServices;
import edu.eci.pdsw.stubs.servicesfacadestub.ItemPedido;
import edu.eci.pdsw.stubs.servicesfacadestub.Producto;
import edu.eci.pdsw.stubs.servicesfacadestub.ProductsServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.TreeNode;

/**
 * Productos:
 *  id, name, quantity, add(method)
 *  [8:53:37 AM] Fabian Ardila Rodriguez: currency
 *  [8:53:48 AM] Fabian Ardila Rodriguez: prize
 *  [8:53:53 AM] Fabian Ardila Rodriguez: total
 *  [8:56:27 AM] Alejandro Anzola Avila: productService () - productos
 * @author hcadavid
 */

@ManagedBean(name="ttShoppingKartBackingBean")
@SessionScoped
public class ShoppingKartBackingBean implements Serializable {
    
    private TreeNode root;
    private Producto selectedProduct;
    private ItemPedido selectedItem;
    private HashMap<Producto, ItemPedido> selectedProducts;
    private List<String> currencies;
    private String currency;
    
    public ShoppingKartBackingBean() {
        selectedProducts = new HashMap<>();
        
        currencies = new ArrayList<>();
        currencies.add("USD");
        currencies.add("COP");
        currency = "USD";
        
        System.out.println("Construido");
    }
    
    @ManagedProperty("#{productService}")
    private ProductService service;
    
    @PostConstruct
    public void init() {
        root = service.createProducts(getProductos());
        System.out.println("Entro");
    }
    
    public TreeNode getRoot() {
        return root;
    }
    
    public List<Producto> getProductos(){
        return ProductsServices.getInstance().getProductos();
    }
    
    public double getTasaCambioDolar(){
        return CurrencyServices.getInstance().getUSDExchangeRateInCOP();
    }
    
    public double getTotal() {
        return ProductsServices.getInstance().
                calcularCostoCompraEnUSD((List<ItemPedido>) selectedProducts.values())
                    * (currency.equals("COP") ? getTasaCambioDolar() : 1.0);
    }

    /**
     * @return the selectedProduct
     */
    public Producto getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct the selectedProduct to set
     */
    public void setSelectedProduct(Producto selectedProduct) {
        this.selectedProduct = selectedProduct;
        if (selectedProducts.containsKey(selectedProduct)) {
            selectedItem = selectedProducts.get(selectedProduct);
        } else {
            selectedItem = new ItemPedido(selectedProduct, 1);
        }
    }
    
    public void add() {
        if (selectedItem != null && 
                !selectedProducts.containsValue(selectedItem) &&
                selectedItem.getCantidad() > 0) {
            selectedProducts.put(selectedItem.getProducto(), selectedItem);
        }
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currencies.contains(currency) ? currency : this.currency;
    }

    /**
     * @param service the service to set
     */
    public void setService(ProductService service) {
        this.service = service;
    }

    /**
     * @return the selectedItem
     */
    public ItemPedido getSelectedItem() {
        return selectedItem;
    }
}
