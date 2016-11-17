package com.heavendevelopment.acaimanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.heavendevelopment.acaimanager.Dominio.Relatorio;
import com.heavendevelopment.acaimanager.R;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Yuri on 11/11/2016.
 */

public class AdapterRelatorio extends BaseAdapter {

    private Context context;
    private List<Relatorio> relatorioList;

    public AdapterRelatorio(Context context, List<Relatorio>  relatorioList) {

        this.context = context;
        this.relatorioList = relatorioList;

    }

    @Override
    public int getCount() {
        return relatorioList.size();
    }

    @Override
    public Object getItem(int i) {
        return relatorioList.get(i);
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
                    inflate(R.layout.item_ver_relatorios, parent, false);
        }

        Relatorio relatorio = relatorioList.get(position);

        //o total que me refiro, é o quanto de dinheiro entrou no caixa e não o lucro.

        TextView tvDataRelatorio = (TextView) convertView.findViewById(R.id.tv_item_data_ver_relatorios);
        TextView tvTipoRelatorio = (TextView) convertView.findViewById(R.id.tv_item_tipo_relatorio_ver_relatorios);
        TextView tvTotalRelatorio = (TextView) convertView.findViewById(R.id.tv_item_total_ver_relatorios);
        TextView tvidRelatorio = (TextView) convertView.findViewById(R.id.tv_item_id_relatorio_ver_relatorios);

        String dataRelatorio = relatorio.getDia()+"/"+relatorio.getMes()+"/"+relatorio.getAno();
        double total = relatorio.getTotalEntrouAVista() + relatorio.getTotalEntrouCartao();

        BigDecimal bd = new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
        total = bd.doubleValue();

        String tipoRelatorio = "";

        if(relatorio.getTipo_relatorio() == 1)
            tipoRelatorio = "Diário";
        else if(relatorio.getTipo_relatorio() == 2)
            tipoRelatorio = "Semanal";
        else
            tipoRelatorio = "Mensal";


        tvDataRelatorio.setText(dataRelatorio);
        tvTipoRelatorio.setText(tipoRelatorio);
        tvTotalRelatorio.setText("R$ "+ total);
        tvidRelatorio.setText(relatorio.getId()+"");

        // returns the view for the current row
        return convertView;
    }
}

