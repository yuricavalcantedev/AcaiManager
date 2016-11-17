package com.heavendevelopment.acaimanager.Fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.heavendevelopment.acaimanager.Activity.VerDetalhesPedidoActivity;
import com.heavendevelopment.acaimanager.Adapter.AdapterHistorico;
import com.heavendevelopment.acaimanager.Dominio.Pedido;
import com.heavendevelopment.acaimanager.R;
import com.heavendevelopment.acaimanager.Service.PedidoService;

import org.w3c.dom.Text;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FragmentHistoricoDia extends Fragment {

    ListView listViewPedidos;

    public FragmentHistoricoDia() {
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
        final View fragmentView = inflater.inflate(R.layout.fragment_dia_historico, container, false);
        final Button bt_filtro = (Button) fragmentView.findViewById(R.id.bt_filtrar_fragment_dia_historico);

        try{

        //preencher primeiro o listview com os pedidos de hoje
        GregorianCalendar calendar = new GregorianCalendar();
        int dia = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        int mes = calendar.get(GregorianCalendar.MONTH) + 1;
        int ano = calendar.get(GregorianCalendar.YEAR);


        final List<Pedido> listaPedidos = PedidoService.getOrdersByDay(dia, mes, ano);
        TextView tvInformaUsuario = (TextView) fragmentView.findViewById(R.id.tv_informa_usuario_dia_historico);

        listViewPedidos = (ListView) fragmentView.findViewById(R.id.listview_fragment_dia_historico);

        AdapterHistorico adapter = new AdapterHistorico(getContext(), listaPedidos);
        listViewPedidos.setAdapter(adapter);

        if (listaPedidos.size() > 0)
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
                bundle.putInt("idPedido", idPedido);

                Intent intent = new Intent(getContext(), VerDetalhesPedidoActivity.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        bt_filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_filtro = (EditText) fragmentView.findViewById(R.id.et_filtro_fragment_dia_historico);
                String texto = et_filtro.getText().toString();

                try {

                    String dataArray[] = texto.split("/");

                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    int dia, mes, ano;

                    dia = Integer.parseInt(dataArray[0]);
                    mes = Integer.parseInt(dataArray[1]);

                    //se entrar nesse if, é porque ele não passou o ano como parâmetro
                    if (dataArray.length == 2)
                        ano = gregorianCalendar.get(GregorianCalendar.YEAR);
                    else
                        ano = Integer.parseInt(dataArray[2]);

                    TextView tvInformaUsuario = (TextView) fragmentView.findViewById(R.id.tv_informa_usuario_dia_historico);

                    List<Pedido> listPedidosFiltrados = PedidoService.getOrdersByDay(dia, mes, ano);
                    listViewPedidos = (ListView) fragmentView.findViewById(R.id.listview_fragment_dia_historico);

                    AdapterHistorico adapter = new AdapterHistorico(getContext(), listPedidosFiltrados);
                    listViewPedidos.setAdapter(adapter);

                    if (listPedidosFiltrados.size() > 0)
                        tvInformaUsuario.setVisibility(View.VISIBLE);
                    else
                        tvInformaUsuario.setVisibility(View.INVISIBLE);

                } catch (Exception e) {

                    Toast.makeText(getContext(), "Filtro com formato inválido.", Toast.LENGTH_SHORT).show();
                    et_filtro.setText("");

                }


            }
        });

        }catch (Exception ex){


            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        //retorna a view.
        return fragmentView;

    }
}
