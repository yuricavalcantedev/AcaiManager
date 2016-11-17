package com.heavendevelopment.acaimanager.Dominio;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Yuri on 01/11/2016.
 */

@Table(name = "Item")
public class Item extends Model {


    @Column(name = "peso_acai")
    private double pesoAcai;

    @Column(name = "preco")
    private double preco;

    @Column(name = "id_pedido")
    private int id_pedido;

    public Item(){}

    public Item(double pesoAcai, double preco){
        this.pesoAcai = pesoAcai;
        this.preco = preco;
    }

    public double getPesoAcai() {

        BigDecimal bd = new BigDecimal(pesoAcai).setScale(3, RoundingMode.HALF_EVEN);
        pesoAcai = bd.doubleValue();
        return pesoAcai;
    }

    public void setPesoAcai(double pesoAcai) {
        this.pesoAcai = pesoAcai;
    }

    public double getPreco() {

        BigDecimal bd = new BigDecimal(preco).setScale(2, RoundingMode.HALF_EVEN);
        preco = bd.doubleValue();

        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }

}
