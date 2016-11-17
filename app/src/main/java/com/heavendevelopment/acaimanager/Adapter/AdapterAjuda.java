package com.heavendevelopment.acaimanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heavendevelopment.acaimanager.R;

/**
 * Created by Yuri on 03/11/2016.
 */

public class AdapterAjuda extends BaseAdapter {

    private Context context;
    private String [] titulosAjuda = new String[]{"Título 1","Título 2", "Título 3"};
    private String [] categoriaAjuda = new String[]{"Categoria 1","Categoria 2", "Categoria 3"};

    public AdapterAjuda(Context context) {

        this.context = context;
    }

    @Override
    public int getCount() {
        return titulosAjuda.length;
    }

    @Override
    public Object getItem(int i) {
        return titulosAjuda[i];
    }

    @Override
    public long getItemId(int i) {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_ajuda_activity, parent, false);
        }

        String titulo = titulosAjuda[position];
        String categoria = categoriaAjuda[position];

        TextView tvTitulo = (TextView) convertView.findViewById(R.id.tv_item_titulo_ajuda);
        TextView tvCategoria = (TextView) convertView.findViewById(R.id.tv_item_categoria_ajuda);

        tvTitulo.setText(titulo);
        tvCategoria.setText(categoria);

        // returns the view for the current row
        return convertView;
    }
}
