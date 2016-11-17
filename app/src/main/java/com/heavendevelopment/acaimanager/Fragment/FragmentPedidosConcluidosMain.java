package com.heavendevelopment.acaimanager.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.heavendevelopment.acaimanager.Activity.VerDetalhesPedidoActivity;
import com.heavendevelopment.acaimanager.Adapter.AdapterHistorico;
import com.heavendevelopment.acaimanager.Dominio.Pedido;
import com.heavendevelopment.acaimanager.R;
import com.heavendevelopment.acaimanager.Service.PedidoService;

import java.util.GregorianCalendar;
import java.util.List;

public class FragmentPedidosConcluidosMain extends Fragment {

    public FragmentPedidosConcluidosMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_pedidos_concluidos_main, container, false);

        //preencher o listview com os pedidos de hoje
        GregorianCalendar calendar = new GregorianCalendar();
        int dia = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        int mes = calendar.get(GregorianCalendar.MONTH) + 1;
        int ano = calendar.get(GregorianCalendar.YEAR);

        ListView listViewPedidos = (ListView) fragmentView.findViewById(R.id.listview_pedidos_concluidos_main);
        List<Pedido> listPedidos = PedidoService.getPedidosConcluidosHoje(dia,mes,ano);

        //se não tiver pedidos, deixo uma textview informando que não há pedidos ainda.
        if(listPedidos.size() == 0){

            listViewPedidos.setVisibility(View.INVISIBLE);
            TextView tv_pedido_concluido = (TextView) fragmentView.findViewById(R.id.tv_pedidos_concluidos_dia);
            tv_pedido_concluido.setVisibility(View.VISIBLE);
        }

        AdapterHistorico adapter = new AdapterHistorico(getContext(),listPedidos);
        listViewPedidos.setAdapter(adapter);

        listViewPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tvIdPedido = (TextView) view.findViewById(R.id.tv_id_pedido_item_listview_historico);

                String idPedidoString = tvIdPedido.getText().toString();
                int idPedido = Integer.parseInt(idPedidoString);

                Bundle bundle = new Bundle();
                bundle.putInt("idPedido",idPedido);

                Intent intent = new Intent(getContext(), VerDetalhesPedidoActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        //retorna a view.
        return fragmentView;

    }
}