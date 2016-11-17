package com.heavendevelopment.acaimanager.Service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;

import com.activeandroid.query.Select;
import com.heavendevelopment.acaimanager.Dominio.Pedido;
import com.heavendevelopment.acaimanager.Dominio.Relatorio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yuri on 08/11/2016.
 */

public class RelatorioService {

    private Context context;


    public RelatorioService(Context context){

        this.context = context;
    }

    //1 - diario, 2 - semanal, 3 - mensal, 4 - semanal e mensal juntos
    public void gerarRelatorioDiario(final int dia, int semana, int mes, int ano, final Snackbar snackbar){

        //a única coisa que preciso buscar no banco são os campos:
        // pagamento, total, totalPeso e claro o tamanho do select.


        int qtdPedidosVendidos;
        int qtdPedidosAVista = 0, qtdPedidosCartao = 0;
        double valorClientePaga, valorVendedorPaga;
        double totalEntrouAVista = 0, totalEntrouCartao = 0;
        double lucroAvista = 0, lucroCartao = 0, lucroTotal = 0;

        List<Pedido> listaPedidosDiario;

        listaPedidosDiario =  PedidoService.getOrdersByDay(dia,mes,ano);

        //quantidade de vendas
        qtdPedidosVendidos = listaPedidosDiario.size();

        //talvez não pegue os valores por causa do contexto.
        SharedPreferences sp_configuracoes = context.getSharedPreferences("sp_configuracoes",MODE_PRIVATE);

        double preco_compra_acai = Double.parseDouble(sp_configuracoes.getString("valor_compra_acai", ""));


        for(Pedido pedido : listaPedidosDiario){

            valorClientePaga = pedido.getTotal();
            valorVendedorPaga = pedido.getPesoTotal() * preco_compra_acai;

            //o lucro do pedido atual.
            double lucroPedido = (valorClientePaga - valorVendedorPaga);

            //vai incrementar a quantidade de pedidos vendidos e o total que entrou à vista e no cartão.
            if(pedido.getPagamento() == 1) {
                qtdPedidosAVista++;
                totalEntrouAVista += pedido.getTotal();
                lucroAvista += lucroPedido;

            }else {

                qtdPedidosCartao++;
                totalEntrouCartao += pedido.getTotal();
                lucroCartao += lucroPedido;
            }

        }

        BigDecimal bd = new BigDecimal(lucroAvista).setScale(2, RoundingMode.HALF_EVEN);
        lucroAvista = bd.doubleValue();

        bd = new BigDecimal(lucroCartao).setScale(2, RoundingMode.HALF_EVEN);
        lucroCartao = bd.doubleValue();

        lucroTotal = lucroAvista + lucroCartao;

        bd =  new BigDecimal(lucroTotal).setScale(2, RoundingMode.HALF_EVEN);
        lucroTotal = bd.doubleValue();

        bd =  new BigDecimal(totalEntrouCartao).setScale(2, RoundingMode.HALF_EVEN);
        totalEntrouCartao = bd.doubleValue();

        bd =  new BigDecimal(totalEntrouAVista).setScale(2, RoundingMode.HALF_EVEN);
        totalEntrouAVista = bd.doubleValue();

        double totalGeral = totalEntrouAVista+ totalEntrouCartao;


        //fazer a mensagem aqui
        String messageData = "Data : "+dia+"/"+mes+"/"+ano +"\n\n";
        String messageQtdVendas = "Num. de Vendas : " + qtdPedidosVendidos + "\n";
        String messageTipoVendas = "Num. de Vendas à vista : " + qtdPedidosAVista+"\n" + "Num. de Vendas no cartão : " + qtdPedidosCartao+"\n\n";
        String messageTotalVenda = "Total à vista : R$ "+ totalEntrouAVista + "\n" + "Total no cartão : R$ "+ totalEntrouCartao + "\n" + "Total : R$ "+ totalGeral + "\n\n";
        String messageLucro = "Lucro à vista : R$ "+lucroAvista + "\n" + "Lucro no cartão : R$ " + lucroCartao + "\n" + "Lucro total : R$ "+lucroTotal + "\n\n";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageData);
        stringBuilder.append(messageQtdVendas);
        stringBuilder.append(messageTipoVendas);
        stringBuilder.append(messageTotalVenda);
        stringBuilder.append(messageLucro);

        //cria o objeto relatório
        Relatorio relatorio = new Relatorio();
        relatorio.setTipo_relatorio(1);
        relatorio.setDia(dia);
        relatorio.setSemanaDoMes(semana);
        relatorio.setMes(mes);
        relatorio.setAno(ano);
        relatorio.setLucroAvista(lucroAvista);
        relatorio.setLucroCartao(lucroCartao);
        relatorio.setLucroTotal(lucroTotal);
        relatorio.setQtdPedidosAVista(qtdPedidosAVista);
        relatorio.setQtdPedidosCartao(qtdPedidosCartao);
        relatorio.setQtdPedidosVendidos(qtdPedidosVendidos);
        relatorio.setTotalEntrouAVista(totalEntrouAVista);
        relatorio.setTotalEntrouCartao(totalEntrouCartao);
        relatorio.setDiaComMaisVendas("");
        relatorio.setHorarioCoMaisVendas("");

        //salva o relatório criado
        relatorio.save();

        //gerar o AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
        .setTitle("Relatório Diário")
        .setMessage(stringBuilder.toString())
        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                snackbar.show();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();



    }

    public void gerarRelatorioSemanal(int semana, int mes, int ano, final Snackbar snackbar){

        //a única coisa que preciso buscar no banco são os campos:
        // pagamento, total, totalPeso e claro o tamanho do select.


        int qtdPedidosVendidos;
        int qtdPedidosAVista = 0, qtdPedidosCartao = 0;
        double valorClientePaga, valorVendedorPaga;
        double totalEntrouAVista = 0, totalEntrouCartao = 0;
        double lucroAvista = 0, lucroCartao = 0, lucroTotal = 0;

        List<Pedido> listaPedidosDiario;

        listaPedidosDiario =  PedidoService.getOrdersByWeek(semana,mes,ano);


        //quantidade de vendas
        qtdPedidosVendidos = listaPedidosDiario.size();

        SharedPreferences sp_configuracoes = context.getSharedPreferences("sp_configuracoes",MODE_PRIVATE);

        double preco_compra_acai = Double.parseDouble(sp_configuracoes.getString("valor_compra_acai", ""));


        for(Pedido pedido : listaPedidosDiario){

            valorClientePaga = pedido.getTotal();
            valorVendedorPaga = pedido.getPesoTotal() * preco_compra_acai;

            //o lucro do pedido atual.
            double lucroPedido = (valorClientePaga - valorVendedorPaga);

            //vai incrementar a quantidade de pedidos vendidos e o total que entrou à vista e no cartão.
            if(pedido.getPagamento() == 1) {
                qtdPedidosAVista++;
                totalEntrouAVista += pedido.getTotal();
                lucroAvista += lucroPedido;

            }else {

                qtdPedidosCartao++;
                totalEntrouCartao += pedido.getTotal();
                lucroCartao += lucroPedido;
            }

        }

        BigDecimal bd = new BigDecimal(lucroAvista).setScale(2, RoundingMode.HALF_EVEN);
        lucroAvista = bd.doubleValue();

        bd = new BigDecimal(lucroCartao).setScale(2, RoundingMode.HALF_EVEN);
        lucroCartao = bd.doubleValue();

        lucroTotal = lucroAvista + lucroCartao;

        bd =  new BigDecimal(lucroTotal).setScale(2, RoundingMode.HALF_EVEN);
        lucroTotal = bd.doubleValue();

        bd =  new BigDecimal(totalEntrouCartao).setScale(2, RoundingMode.HALF_EVEN);
        totalEntrouCartao = bd.doubleValue();

        bd =  new BigDecimal(totalEntrouAVista).setScale(2, RoundingMode.HALF_EVEN);
        totalEntrouAVista = bd.doubleValue();

        double totalGeral = totalEntrouAVista+ totalEntrouCartao;


        //fazer a mensagem aqui
        String messageData = "Semana : "+semana+"/"+mes+"/"+ano +"\n\n";
        String messageQtdVendas = "Num. de Vendas : " + qtdPedidosVendidos + "\n";
        String messageTipoVendas = "Num. de Vendas à vista : " + qtdPedidosAVista+"\n" + "Num. de Vendas no cartão : " + qtdPedidosCartao+"\n\n";
        String messageTotalVenda = "Total à vista : R$ "+ totalEntrouAVista + "\n" + "Total no cartão : R$ "+ totalEntrouCartao + "\n" + "Total : R$ "+ totalGeral + "\n\n";
        String messageLucro = "Lucro à vista : R$ "+lucroAvista + "\n" + "Lucro no cartão : R$ " + lucroCartao + "\n" + "Lucro total : R$ "+lucroTotal + "\n\n";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageData);
        stringBuilder.append(messageQtdVendas);
        stringBuilder.append(messageTipoVendas);
        stringBuilder.append(messageTotalVenda);
        stringBuilder.append(messageLucro);

        //cria o objeto relatório
        Relatorio relatorio = new Relatorio();
        relatorio.setTipo_relatorio(2);
        relatorio.setSemanaDoMes(semana);
        relatorio.setMes(mes);
        relatorio.setAno(ano);
        relatorio.setLucroAvista(lucroAvista);
        relatorio.setLucroCartao(lucroCartao);
        relatorio.setLucroTotal(lucroTotal);
        relatorio.setQtdPedidosAVista(qtdPedidosAVista);
        relatorio.setQtdPedidosCartao(qtdPedidosCartao);
        relatorio.setQtdPedidosVendidos(qtdPedidosVendidos);
        relatorio.setTotalEntrouAVista(totalEntrouAVista);
        relatorio.setTotalEntrouCartao(totalEntrouCartao);
        relatorio.setDiaComMaisVendas("");
        relatorio.setHorarioCoMaisVendas("");

        //salva o relatório criado
        relatorio.save();

        //gerar o AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Relatório Semanal")
                .setMessage(stringBuilder.toString())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        snackbar.show();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void gerarRelatorioMensal(int mes, int ano, final Snackbar snackbar){

        //a única coisa que preciso buscar no banco são os campos:
        // pagamento, total, totalPeso e claro o tamanho do select.


        int qtdPedidosVendidos;
        int qtdPedidosAVista = 0, qtdPedidosCartao = 0;
        double valorClientePaga, valorVendedorPaga;
        double totalEntrouAVista = 0, totalEntrouCartao = 0;
        double lucroAvista = 0, lucroCartao = 0, lucroTotal = 0;

        List<Pedido> listaPedidosDiario;

        listaPedidosDiario =  PedidoService.getOrdersByMonth(mes,ano);

        //quantidade de vendas
        qtdPedidosVendidos = listaPedidosDiario.size();

        SharedPreferences sp_configuracoes = context.getSharedPreferences("sp_configuracoes",MODE_PRIVATE);

        double preco_compra_acai = Double.parseDouble(sp_configuracoes.getString("valor_compra_acai", ""));


        for(Pedido pedido : listaPedidosDiario){

            valorClientePaga = pedido.getTotal();
            valorVendedorPaga = pedido.getPesoTotal() * preco_compra_acai;

            //o lucro do pedido atual.
            double lucroPedido = (valorClientePaga - valorVendedorPaga);

            //vai incrementar a quantidade de pedidos vendidos e o total que entrou à vista e no cartão.
            if(pedido.getPagamento() == 1) {
                qtdPedidosAVista++;
                totalEntrouAVista += pedido.getTotal();
                lucroAvista += lucroPedido;

            }else {

                qtdPedidosCartao++;
                totalEntrouCartao += pedido.getTotal();
                lucroCartao += lucroPedido;
            }

        }

        BigDecimal bd = new BigDecimal(lucroAvista).setScale(2, RoundingMode.HALF_EVEN);
        lucroAvista = bd.doubleValue();

        bd = new BigDecimal(lucroCartao).setScale(2, RoundingMode.HALF_EVEN);
        lucroCartao = bd.doubleValue();

        lucroTotal = lucroAvista + lucroCartao;

        bd =  new BigDecimal(lucroTotal).setScale(2, RoundingMode.HALF_EVEN);
        lucroTotal = bd.doubleValue();

        bd =  new BigDecimal(totalEntrouCartao).setScale(2, RoundingMode.HALF_EVEN);
        totalEntrouCartao = bd.doubleValue();

        bd =  new BigDecimal(totalEntrouAVista).setScale(2, RoundingMode.HALF_EVEN);
        totalEntrouAVista = bd.doubleValue();

        double totalGeral = totalEntrouAVista+ totalEntrouCartao;


        //fazer a mensagem aqui
        String messageData = "Mês : "+mes+"/"+ano +"\n\n";
        String messageQtdVendas = "Num. de Vendas : " + qtdPedidosVendidos + "\n";
        String messageTipoVendas = "Num. de Vendas à vista : " + qtdPedidosAVista+"\n" + "Num. de Vendas no cartão : " + qtdPedidosCartao+"\n\n";
        String messageTotalVenda = "Total à vista : R$ "+ totalEntrouAVista + "\n" + "Total no cartão : R$ "+ totalEntrouCartao + "\n" + "Total : R$ "+ totalGeral + "\n\n";
        String messageLucro = "Lucro à vista : R$ "+lucroAvista + "\n" + "Lucro no cartão : R$ " + lucroCartao + "\n" + "Lucro total : R$ "+lucroTotal + "\n\n";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageData);
        stringBuilder.append(messageQtdVendas);
        stringBuilder.append(messageTipoVendas);
        stringBuilder.append(messageTotalVenda);
        stringBuilder.append(messageLucro);

        //cria o objeto relatório
        Relatorio relatorio = new Relatorio();
        relatorio.setTipo_relatorio(2);
        relatorio.setMes(mes);
        relatorio.setAno(ano);
        relatorio.setLucroAvista(lucroAvista);
        relatorio.setLucroCartao(lucroCartao);
        relatorio.setLucroTotal(lucroTotal);
        relatorio.setQtdPedidosAVista(qtdPedidosAVista);
        relatorio.setQtdPedidosCartao(qtdPedidosCartao);
        relatorio.setQtdPedidosVendidos(qtdPedidosVendidos);
        relatorio.setTotalEntrouAVista(totalEntrouAVista);
        relatorio.setTotalEntrouCartao(totalEntrouCartao);
        relatorio.setDiaComMaisVendas("");
        relatorio.setHorarioCoMaisVendas("");

        //salva o relatório criado
        relatorio.save();

        //gerar o AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Relatório Mensal")
                .setMessage(stringBuilder.toString())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        snackbar.show();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public List<Relatorio> getRelatorioDiario(int dia, int mes, int ano){

        return new Select().from(Relatorio.class).where("dia = ? AND mes = ? AND ano = ?", dia ,mes, ano).execute();

    }

    public List<Relatorio> getRelatorioByMonth(int mes, int ano){

        return new Select().from(Relatorio.class).where("mes = ? AND ano = ?", mes, ano).execute();

    }

    public List<Relatorio> getRelatorioByWeek(int semana, int mes, int ano){

        return new Select().from(Relatorio.class).where("semana_do_mes = ? AND mes = ? AND ano = ?", semana ,mes, ano).execute();

    }

    public List<Relatorio> getTodosRelatorios(){

        return new Select("*").from(Relatorio.class).execute();

    }

}
