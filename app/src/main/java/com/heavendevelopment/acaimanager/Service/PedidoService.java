package com.heavendevelopment.acaimanager.Service;

import android.content.Context;
import android.content.SharedPreferences;

import com.activeandroid.query.Select;
import com.heavendevelopment.acaimanager.Dominio.Item;
import com.heavendevelopment.acaimanager.Dominio.Pedido;

import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yuri on 01/11/2016.
 */

public class PedidoService {

    private Context context;

    public PedidoService(Context context){

        this.context = context;
    }

    public static int addPedido(GregorianCalendar calendar, double total){

        Pedido pedido = new Pedido();

        pedido.setTotal(total);
        pedido.setAno(calendar.get(GregorianCalendar.YEAR));
        pedido.setMes(calendar.get(GregorianCalendar.MONTH)+1);
        pedido.setDia(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        pedido.setHora(calendar.get(GregorianCalendar.HOUR_OF_DAY));
        pedido.setMinuto(calendar.get(GregorianCalendar.MINUTE));
        pedido.setDiaDaSemana(calendar.get(GregorianCalendar.DAY_OF_WEEK));
        pedido.setSemanaDoMes(calendar.get(GregorianCalendar.WEEK_OF_MONTH));

        pedido.save();

        String idText = ""+pedido.getId();
        int idPedido = Integer.parseInt(idText);

        return idPedido;
    }

    public int addItemToAnOrder(int idOrder, double quantity){

        Pedido pedido = Pedido.load(Pedido.class,idOrder);

        SharedPreferences sp_configuracoes =  context.getSharedPreferences("sp_configuracoes",MODE_PRIVATE);

        double valorVendaKg = Double.parseDouble(sp_configuracoes.getString("valor_venda_acai", "0.0"));
        double totalValorItem = quantity*valorVendaKg;
        Item item = new Item(quantity,totalValorItem);

        pedido.addItem(item);

        return 0;

    }

    public static List<Pedido> getOrdersByWeek(int week,int month, int year){

        return new Select().from(Pedido.class)
                .where("semana_do_mes = ? AND mes = ? AND ano = ?",week,month,year)
                .orderBy("dia ASC")
                .execute();
    }

    public static List<Pedido> getOrdersByDay(int day, int month, int year){

        return new Select().from(Pedido.class)
                .where("dia = ? AND mes = ? AND ano = ?",day,month,year)
                .orderBy("dia ASC")
                .execute();

    }

    public static  List<Pedido> getOrdersByMonth(int month, int year){

        return new Select().from(Pedido.class)
                .where("mes = ? AND ano = ?",month,year)
                .orderBy("dia ASC")
                .execute();

    }

    public static List<Pedido> getAllOrders(){
        return new Select("*").from(Pedido.class)
                .orderBy("ano ASC")
                .execute();

    }

    public static List<Item> getItensFromPedido(int idOrder){

        return new Select().from(Item.class).where("id_pedido = ?",idOrder).execute();

    }

    public static List<Pedido> getPedidosConcluidosHoje(int day, int month, int year){

        return new Select().from(Pedido.class)
                .where("dia = ? AND mes = ? AND ano = ? AND realizado = ?",day,month,year,true)
                .orderBy("dia ASC")
                .execute();

    }

    public static List<Pedido> getPedidosAbertosHoje(int day, int month, int year){

        return new Select().from(Pedido.class)
                .where("dia = ? AND mes = ? AND ano = ? AND realizado = ?",day,month,year,false)
                .orderBy("dia ASC")
                .execute();

    }


}