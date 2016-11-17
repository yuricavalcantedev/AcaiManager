package com.heavendevelopment.acaimanager.Dominio;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Yuri on 09/11/2016.
 */

@Table(name = "Relatorio")

public class Relatorio extends Model {

    //se ele é diário, semanal, mensal
    //1 - diario, 2 semanal, 3 mensal
    @Column(name = "tipo")
    private int tipo_relatorio;

    @Column(name = "dia")
    private int dia;

    @Column(name = "mes")
    private int mes;

    @Column(name = "ano")
    private int ano;

    @Column(name = "semana_do_mes")
    private int semanaDoMes;

    @Column(name = "qtd_pedidos_vendidos")
    private int qtdPedidosVendidos;

    @Column(name = "qtd_pedidos_avista")
    private int qtdPedidosAVista;

    @Column(name = "qtd_pedidos_cartao")
    private int qtdPedidosCartao;

    @Column(name = "total_entrou_avista")
    private double totalEntrouAVista;

    @Column(name = "total_entrou_cartao")
    private double totalEntrouCartao;

    @Column(name = "lucro_avista")
    private double lucroAvista;

    @Column(name = "lucro_cartao")
    private double lucroCartao;

    @Column(name = "lucro_total")
    private double lucroTotal;

    @Column(name = "dia_com_mais_vendas")
    private String diaComMaisVendas;

    @Column(name = "horario_com_mais_vendas")
    private String horarioCoMaisVendas;


    public Relatorio(){}

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

    public int getSemanaDoMes() {
        return semanaDoMes;
    }

    public void setSemanaDoMes(int semanaDoMes) {
        this.semanaDoMes = semanaDoMes;
    }

    public int getQtdPedidosVendidos() {
        return qtdPedidosVendidos;
    }

    public void setQtdPedidosVendidos(int qtdPedidosVendidos) {
        this.qtdPedidosVendidos = qtdPedidosVendidos;
    }

    public int getQtdPedidosAVista() {
        return qtdPedidosAVista;
    }

    public void setQtdPedidosAVista(int qtdPedidosAVista) {
        this.qtdPedidosAVista = qtdPedidosAVista;
    }

    public int getQtdPedidosCartao() {
        return qtdPedidosCartao;
    }

    public void setQtdPedidosCartao(int qtdPedidosCartao) {
        this.qtdPedidosCartao = qtdPedidosCartao;
    }

    public double getTotalEntrouAVista() {
        return totalEntrouAVista;
    }

    public void setTotalEntrouAVista(double totalEntrouAVista) {
        this.totalEntrouAVista = totalEntrouAVista;
    }

    public double getTotalEntrouCartao() {
        return totalEntrouCartao;
    }

    public void setTotalEntrouCartao(double totalEntrouCartao) {
        this.totalEntrouCartao = totalEntrouCartao;
    }

    public double getLucroAvista() {
        return lucroAvista;
    }

    public void setLucroAvista(double lucroAvista) {
        this.lucroAvista = lucroAvista;
    }

    public double getLucroCartao() {
        return lucroCartao;
    }

    public void setLucroCartao(double lucroCartao) {
        this.lucroCartao = lucroCartao;
    }

    public double getLucroTotal() {
        return lucroTotal;
    }

    public void setLucroTotal(double lucroTotal) {
        this.lucroTotal = lucroTotal;
    }

    public String getDiaComMaisVendas() {
        return diaComMaisVendas;
    }

    public void setDiaComMaisVendas(String diaComMaisVendas) {
        this.diaComMaisVendas = diaComMaisVendas;
    }

    public String getHorarioCoMaisVendas() {
        return horarioCoMaisVendas;
    }

    public void setHorarioCoMaisVendas(String horarioCoMaisVendas) {
        this.horarioCoMaisVendas = horarioCoMaisVendas;
    }

    public int getTipo_relatorio() {
        return tipo_relatorio;
    }

    public void setTipo_relatorio(int tipo_relatorio) {
        this.tipo_relatorio = tipo_relatorio;
    }
}
