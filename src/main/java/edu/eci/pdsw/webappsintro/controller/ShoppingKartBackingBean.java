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

import com.sun.media.jfxmedia.logging.Logger;
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

@SessionScoped
@ManagedBean(name="ttShoppingKartBackingBean")
public class ShoppingKartBackingBean implements Serializable {
    
    private TreeNode root;
    private Producto selectedProduct;
    private ItemPedido selectedItem;
    private HashMap<Producto, ItemPedido> selectedProducts;
    private List<String> currencies;
    private String currency;
    
    public ShoppingKartBackingBean() {
        Logger.setLevel(Logger.DEBUG);
        
        selectedProducts = new HashMap<>();
        
        currencies = new ArrayList<>();
        currencies.add("USD");
        currencies.add("COP");
        currency = "USD";
        
        Logger.logMsg(Logger.INFO, "Se inicializo el servidor");
        
        Logger.logMsg(Logger.DEBUG, "Construido " + this.getClass().getName());
    }
    
    @ManagedProperty("#{productService}")
    private ProductService service;
    
    @PostConstruct
    public void init() {
        root = service.createProducts(getProductos());
        Logger.logMsg(Logger.DEBUG, "Tabla de productos inicializada");
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
        Logger.logMsg(Logger.DEBUG, "Se obtiene el producto seleccionado " +
                selectedProduct != null ? selectedProduct.getNombre() : "null");
        return selectedProduct;
    }

    /**
     * @param selectedProduct the selectedProduct to set
     */
    public void setSelectedProduct(Producto selectedProduct) {
        Logger.logMsg(Logger.DEBUG, "Se especifica el producto seleccionado "
                + "para anadirlo al carrito " + 
                selectedProduct != null ? selectedProduct.getNombre() : "null");
        this.selectedProduct = selectedProduct;
        if (selectedProducts.containsKey(selectedProduct)) {
            setSelectedItem(selectedProducts.get(selectedProduct));
        } else {
            Logger.logMsg(Logger.DEBUG, "Es la primera vez que se agrega un item");
            setSelectedItem(new ItemPedido(selectedProduct, 1));
        }
    }
    
    public void addToKart() {
        if (selectedItem != null && 
                !selectedProducts.containsKey(selectedItem.getProducto())) {
            selectedProducts.put(selectedItem.getProducto(), selectedItem);
            
            Logger.logMsg(Logger.DEBUG, "Se agrego al carrito: " + 
                    selectedItem.getProducto().getNombre() + " " +
                    selectedItem.getCantidad());
        }
        
        Logger.logMsg(Logger.DEBUG,"Productos en carrito: ");
        for(ItemPedido i : selectedProducts.values()) {
            Logger.logMsg(Logger.DEBUG,"\t" + i.getProducto().getNombre() + " " + i.getCantidad());
        }
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        Logger.logMsg(Logger.DEBUG, "Se obtiene el string de la moneda. ("
                + currency + ")");
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        Logger.logMsg(Logger.DEBUG, "Se especifica la moneda " + currency);
        this.currency = currencies.contains(currency) ? currency : this.currency;
    }

    /**
     * @param service the service to set
     */
    public void setService(ProductService service) {
        Logger.logMsg(Logger.DEBUG, "Se establece el servicio para la tabla");
        this.service = service;
    }

    /**
     * @return the selectedItem
     */
    public ItemPedido getSelectedItem() {
        Logger.logMsg(Logger.DEBUG, "Se obtiene el item actual " + 
                (selectedItem != null ? selectedItem.getProducto().getNombre() : "null"));
        return selectedItem;
    }

    /**
     * @param selectedItem the selectedItem to set
     */
    public void setSelectedItem(ItemPedido selectedItem) {
        Logger.logMsg(Logger.DEBUG, "Se especifica el item " + 
                (selectedItem != null ? selectedItem.getProducto().getNombre() : "null"));
        this.selectedItem = (selectedItem != null ? selectedItem : this.selectedItem);
    }
    
    public int getCantidad() {
        Logger.logMsg(Logger.DEBUG, "Se obtiene la cantidad " + selectedItem.getCantidad());
        return selectedItem != null ? selectedItem.getCantidad() : 0;
    }
    
    public void setCantidad(int cantidad) {
        Logger.logMsg(Logger.DEBUG, "Se intenta cambiar la cantidad a " + cantidad);
        if (selectedItem != null && cantidad > 0) {
            Logger.logMsg(Logger.DEBUG, "Se especifica la cantidad del item " + cantidad);
            selectedItem.setCantidad(cantidad);
        }
    }
}
