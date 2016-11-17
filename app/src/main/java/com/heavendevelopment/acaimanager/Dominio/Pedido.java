package com.heavendevelopment.acaimanager.Dominio;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Yuri on 01/11/2016.
 */

@Table(name = "Pedido")
public class Pedido extends Model {

    //The Item class already has a foreign key to this class.
    @Column(name = "dia")
    private int dia;

    @Column(name = "mes")
    private int mes;

    @Column(name = "ano")
    private int ano;

    @Column(name = "hora")
    private int hora;

    @Column(name = "minuto")
    private int minuto;

    //1 - à vista, 2 - cartão
    @Column(name = "pagamento")
    private int pagamento;

    //1 é a primeira semana, 2 é a segunda, etc.
    @Column(name = "semana_do_mes")
    private int semanaDoMes;

    @Column(name = "total")
    private double total;

    //1 = domingo, 2 = segunda, etc.
    @Column(name = "dia_da_semana")
    private int diaDaSemana;

    @Column(name = "realizado")
    private boolean realizado;

    //requerido construtor padrão
    public Pedido(){}

    public Pedido(int dia, int mes, int ano, int hora, int minuto, int pagamento, int semanaDoMes, double total, int diaDaSemana, boolean realizado) {

        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minuto = minuto;
        this.pagamento = pagamento;
        this.semanaDoMes = semanaDoMes;
        this.total = total;
        this.diaDaSemana = diaDaSemana;
        this.realizado = realizado;
    }

    public boolean addItem(Item item){

        String idTexto = this.getId()+"";
        int idPedido = Integer.parseInt(idTexto);

        item.setId_pedido(idPedido);
        item.save();
        total += item.getPreco();

        return true;
    }

    public int getQtdItens(){

        String idTexto = this.getId()+"";
        int idPedido = Integer.parseInt(idTexto);

        List<Item> listItens = new Select().from(Item.class).where("id_pedido = ?",idPedido).execute();
        return listItens.size();

    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getSemanaDoMes() {
        return semanaDoMes;
    }

    public void setSemanaDoMes(int semanaDoMes) {
        this.semanaDoMes = semanaDoMes;
    }

    public double getTotal() {

        String idTexto = this.getId()+"";
        int idPedido = Integer.parseInt(idTexto);

        List<Item> listItems = new Select().from(Item.class).where("id_pedido = ?",idPedido).execute();
        total = 0.0;

        for(Item item : listItems){

            total+= item.getPreco();
        }

        BigDecimal bd = new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
        total = bd.doubleValue();

        return total;

    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getDiaDaSemana() {
        return diaDaSemana;
    }

    public void setDiaDaSemana(int diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    public double getPesoTotal(){

        String idTexto = this.getId()+"";
        int idPedido = Integer.parseInt(idTexto);

        List<Item> listaItens = new Select().from(Item.class).where("id_pedido = ?",idPedido).execute();

        double pesoTotal = 0;

        for(int i = 0; i < listaItens.size(); i++)
            pesoTotal += listaItens.get(i).getPesoAcai();

        return pesoTotal;
    }

    public int getPagamento() {
        return pagamento;
    }

    public void setPagamento(int pagamento) {
        this.pagamento = pagamento;
    }

    public boolean isRealizado() {
        return realizado;
    }

    public void setRealizado(boolean realizado) {
        this.realizado = realizado;
    }
}
