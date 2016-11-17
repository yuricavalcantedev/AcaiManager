package com.heavendevelopment.acaimanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heavendevelopment.acaimanager.Dominio.Pedido;
import com.heavendevelopment.acaimanager.R;

import java.util.List;

/**
 * Created by Yuri on 02/11/2016.
 */

public class AdapterHistorico extends BaseAdapter {

    private Context context;
    private List<Pedido> listaPedidos;
    private LayoutInflater inflater;

    public AdapterHistorico(Context context, List<Pedido> listaPedidos) {

        this.context = context;
        this.listaPedidos = listaPedidos;
    }

    @Override
    public int getCount() {
        return listaPedidos.size();
    }

    @Override
    public Object getItem(int i) {
        return listaPedidos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listaPedidos.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //se ainda não estiver sido carregado essa linha
        // inflate the layout for each list row
        if(view == null){

            view = LayoutInflater.from(context).
                    inflate(R.layout.item_fragments_historico,viewGroup,false);

            TextView peso = (TextView) view.findViewById(R.id.tv_peso_item_listview_historico);
            TextView valor = (TextView) view.findViewById(R.id.tv_valor_item_listview_historico);
            TextView diaMes = (TextView) view.findViewById(R.id.tv_dia_mes_item_listview_historico);
            TextView pagamento = (TextView) view.findViewById(R.id.tv_pagamento_item_listview_historico);
            TextView idPedido = (TextView) view.findViewById(R.id.tv_id_pedido_item_listview_historico);

            int dia = listaPedidos.get(position).getDia();
            int mes = listaPedidos.get(position).getMes();

            //economiza processamento, se eu pegar logo o pedido, do que ficar dando get sempre que for
            //usar ele em baixo.

            Pedido pedido = listaPedidos.get(position);

            peso.setText(pedido.getPesoTotal()+" Kg");
            valor.setText("R$ " + pedido.getTotal());
            diaMes.setText(dia+"/"+mes);
            idPedido.setText(pedido.getId()+"");

            int forma_pagamento = pedido.getPagamento();
            String textoPagamento = "";

            if(forma_pagamento == 1)
                textoPagamento = "À vista";
            else if(forma_pagamento == 2)
                textoPagamento = "Cartão";

            pagamento.setText(textoPagamento);

        }

        return view;
    }
}