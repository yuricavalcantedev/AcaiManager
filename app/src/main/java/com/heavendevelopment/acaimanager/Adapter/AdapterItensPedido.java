package com.heavendevelopment.acaimanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heavendevelopment.acaimanager.Dominio.Item;
import com.heavendevelopment.acaimanager.Dominio.Pedido;
import com.heavendevelopment.acaimanager.R;

import java.util.List;

/**
 * Created by Yuri on 02/11/2016.
 */

public class AdapterItensPedido extends BaseAdapter {

    private Context context;
    private List<Item> listaItens;

    public AdapterItensPedido(Context context, List<Item> listaItens) {

        this.context = context;
        this.listaItens = listaItens;
    }

    @Override
    public int getCount() {
        return listaItens.size();
    }

    @Override
    public Object getItem(int i) {
        return listaItens.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listaItens.get(i).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_nova_quantidade_pedido, parent, false);
        }

        // get current item to be displayed
        Item currentItem = (Item) getItem(position);

        TextView peso = (TextView) convertView.findViewById(R.id.tv_peso_item_quantidade_pedido);
        TextView valor = (TextView) convertView.findViewById(R.id.tv_valor_item_quantidade_pedido);
        TextView idItem = (TextView) convertView.findViewById(R.id.tv_id_item_nova_quantidade_pedido);
        TextView idPedido = (TextView) convertView.findViewById(R.id.tv_id_pedido_nova_quantidade_pedido);


        String idTexto = currentItem.getId()+"";

        peso.setText(currentItem.getPesoAcai() + " Kg");
        valor.setText("R$ " + currentItem.getPreco());
        idItem.setText(idTexto);
        idPedido.setText(currentItem.getId_pedido()+"");

        // returns the view for the current row
        return convertView;

    }
}