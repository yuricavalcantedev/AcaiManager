package com.heavendevelopment.acaimanager.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.heavendevelopment.acaimanager.R;

public class VerDetalhesRelatorioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_detalhes_relatorio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ver Detalhes Relatório");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        String data = bundle.getString("data");
        String tipo_relatorio = bundle.getString("tipo_relatorio");
        String qtd_vendas_avista = bundle.getString("qtd_vendas_avista");
        String qtd_vendas_cartao = bundle.getString("qtd_vendas_cartao");
        String qtd_vendas_total = bundle.getString("qtd_vendas_total");
        String total_entrada_avista = bundle.getString("total_entrada_avista");
        String total_entrada_cartao = bundle.getString("total_entrada_cartao");
        String total_entrada = bundle.getString("total_entrada");
        String lucro_avista = bundle.getString("lucro_avista");
        String lucro_cartao = bundle.getString("lucro_cartao");
        String lucro_total = bundle.getString("lucro_total");

        TextView tv_data = (TextView) findViewById(R.id.tv_data_ver_detalhes_relatorio);
        TextView tv_tipo_relatorio = (TextView) findViewById(R.id.tv_tipo_ver_detalhes_relatorio);
        TextView tv_qtd_vendas_avista = (TextView) findViewById(R.id.tv_qtd_vendas_avista_ver_detalhes_relatorio);
        TextView tv_qtd_vendas_cartao = (TextView) findViewById(R.id.tv_qtd_vendas_cartao_ver_detalhes_relatorio);
        TextView tv_qtd_vendas_total = (TextView) findViewById(R.id.tv_qtd_vendas_total_ver_detalhes_relatorio);
        TextView tv_total_entrada_avista = (TextView) findViewById(R.id.tv_total_entrada_avista_ver_detalhes_relatorio);
        TextView tv_total_entrada_cartao = (TextView) findViewById(R.id.tv_total_entrada_cartao_ver_detalhes_relatorio);
        TextView tv_total_entrada = (TextView) findViewById(R.id.tv_total_entrada_ver_detalhes_relatorio);
        TextView tv_lucro_avista = (TextView) findViewById(R.id.tv_lucro_avista_ver_detalhes_relatorio);
        TextView tv_lucro_cartao = (TextView) findViewById(R.id.tv_lucro_cartao_ver_detalhes_relatorio);
        TextView tv_lucro_total = (TextView) findViewById(R.id.tv_lucro_total_ver_detalhes_relatorio);

        tv_data.setText(data);
        tv_tipo_relatorio.setText(tipo_relatorio);
        tv_qtd_vendas_avista.setText(qtd_vendas_avista);
        tv_qtd_vendas_cartao.setText(qtd_vendas_cartao);
        tv_qtd_vendas_total.setText(qtd_vendas_total);
        tv_total_entrada_avista.setText(total_entrada_avista);
        tv_total_entrada_cartao.setText(total_entrada_cartao);
        tv_total_entrada.setText(total_entrada);
        tv_lucro_avista.setText(lucro_avista);
        tv_lucro_cartao.setText(lucro_cartao);
        tv_lucro_total.setText(lucro_total);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

}
