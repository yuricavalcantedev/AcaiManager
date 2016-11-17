package com.heavendevelopment.acaimanager.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.heavendevelopment.acaimanager.Adapter.AdapterItensPedido;
import com.heavendevelopment.acaimanager.Dominio.Item;
import com.heavendevelopment.acaimanager.Dominio.Pedido;
import com.heavendevelopment.acaimanager.R;
import com.heavendevelopment.acaimanager.Service.PedidoService;

import java.util.List;

public class VerDetalhesPedidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalhes_pedido);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ver Detalhes do Produto");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        int idPedidoDetalhado = bundle.getInt("idPedido",0);

        String data, hora, qtdItens, totalAcai, totalPedido;

        Pedido pedido = Pedido.load(Pedido.class, idPedidoDetalhado);

        data = pedido.getDia() + "/" + pedido.getMes() + "/" + pedido.getAno();
        hora = pedido.getHora() + " : " + pedido.getMinuto();
        qtdItens = pedido.getQtdItens() + "";
        totalAcai = pedido.getPesoTotal() + "";
        totalPedido = pedido.getTotal() + "";

        TextView tvData = (TextView) findViewById(R.id.tv_data_ver_pedido);
        TextView tvHora = (TextView) findViewById(R.id.tv_hora_ver_pedido);
        TextView tvQtdItens = (TextView) findViewById(R.id.tv_qtd_itens_ver_pedido);
        TextView tvTotalAcai = (TextView) findViewById(R.id.tv_total_acai_ver_pedido);
        TextView tvTotalPedido = (TextView) findViewById(R.id.tv_total_ver_pedido);

        tvData.setText(data);
        tvHora.setText(hora);
        tvQtdItens.setText(qtdItens);
        tvTotalAcai.setText(totalAcai+"(kg)");
        tvTotalPedido.setText("R$ " + totalPedido);

        List<Item> listaItens = PedidoService.getItensFromPedido(idPedidoDetalhado);
        ListView listView = (ListView) findViewById(R.id.listview_ver_pedido);

        AdapterItensPedido adapter = new AdapterItensPedido(this,listaItens);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
