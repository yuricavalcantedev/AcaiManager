package com.heavendevelopment.acaimanager.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

public class FragmentHistoricoTodos extends Fragment {

    public FragmentHistoricoTodos() {
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
        View fragmentView = inflater.inflate(R.layout.fragment_todos_historico, container, false);

        List<Pedido> listaPedidos = PedidoService.getAllOrders();

        TextView tvInformaUsuario = (TextView) fragmentView.findViewById(R.id.tv_informa_usuario_todos_historico);

        ListView listViewPedidos = (ListView) fragmentView.findViewById(R.id.listview_fragment_todos_historico);

        AdapterHistorico adapter = new AdapterHistorico(getContext(), listaPedidos);
        listViewPedidos.setAdapter(adapter);

        if(listaPedidos.size() > 0)
            tvInformaUsuario.setVisibility(View.INVISIBLE);
        else
            tvInformaUsuario.setVisibility(View.VISIBLE);

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