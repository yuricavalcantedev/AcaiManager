package com.heavendevelopment.acaimanager.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.heavendevelopment.acaimanager.Activity.NovoPedidoActivity;
import com.heavendevelopment.acaimanager.Adapter.AdapterHistorico;
import com.heavendevelopment.acaimanager.Dominio.Pedido;
import com.heavendevelopment.acaimanager.R;
import com.heavendevelopment.acaimanager.Service.PedidoService;

import java.util.GregorianCalendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FragmentPedidosAbertosDia extends Fragment {

    ListView listViewPedidos;
    AdapterHistorico adapter;
    View fragmentView;

    public FragmentPedidosAbertosDia() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_pedidos_abertos_dia, container, false);

        FloatingActionButton fab = (FloatingActionButton) fragmentView.findViewById(R.id.fab_novo_pedido);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //só deixa fazer uma nova venda, se ele já tiver configurado o preço de venda e compra do açai.
                SharedPreferences sp_configuracoes = getContext().getSharedPreferences("sp_configuracoes",MODE_PRIVATE);

                String valorCompraAcai = sp_configuracoes.getString("valor_compra_acai", "0.0");
                String valorVendaAcai = sp_configuracoes.getString("valor_venda_acai", "0.0");

                if(valorCompraAcai == "00,00" | valorVendaAcai == "00,00"){

                    Toast.makeText(getContext(), "Você não pode vender sem antes ter configurado o valor de venda e compra do açai.", Toast.LENGTH_LONG).show();
                }else{

                    startActivity(new Intent(getContext(), NovoPedidoActivity.class));
                }

            }
        });

        listViewPedidos = (ListView) fragmentView.findViewById(R.id.listview_pedidos_abertos_main);

        //preencher o listview com os pedidos de hoje
        fillListView();

        listViewPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();

                TextView tvIdPedido = (TextView) view.findViewById(R.id.tv_id_pedido_item_listview_historico);
                int idPedidoClicado = Integer.parseInt(tvIdPedido.getText().toString());

                bundle.putInt("idPedido",idPedidoClicado);

                Intent intent = new Intent(getContext(), NovoPedidoActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        //retorna a view.
        return fragmentView;

    }

    @Override
    public void onResume() {

        super.onResume();
        fillListView();

    }

    private void fillListView(){

        //preencher o listview com os pedidos de hoje
        GregorianCalendar calendar = new GregorianCalendar();
        int dia = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        int mes = calendar.get(GregorianCalendar.MONTH) + 1;
        int ano = calendar.get(GregorianCalendar.YEAR);

        List<Pedido> listPedidos = PedidoService.getPedidosAbertosHoje(dia,mes,ano);
        TextView tv_pedido_aberto = (TextView) fragmentView.findViewById(R.id.tv_pedidos_abertos_dia);
        //se não tiver pedidos, deixo uma textview informando que não há pedidos ainda.
        if(listPedidos.size() == 0){
            listViewPedidos.setVisibility(View.INVISIBLE);
            tv_pedido_aberto.setVisibility(View.VISIBLE);
        }else{
            listViewPedidos.setVisibility(View.VISIBLE);
            tv_pedido_aberto.setVisibility(View.INVISIBLE);

        }

        adapter = new AdapterHistorico(getContext(),listPedidos);
        listViewPedidos.setAdapter(adapter);

    }
}
