package com.example.hmshop.model.EventBus;

import com.example.hmshop.model.NewProduct;

public class RepairRemoveEvent {
    NewProduct newProduct;

    public RepairRemoveEvent(NewProduct newProduct) {
        this.newProduct = newProduct;
    }

    public NewProduct getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(NewProduct newProduct) {
        this.newProduct = newProduct;
    }
}
