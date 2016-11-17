package com.heavendevelopment.acaimanager.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.heavendevelopment.acaimanager.R;
import com.itextpdf.awt.geom.CubicCurve2D;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ConfiguracoesActivity extends AppCompatActivity {

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        ctx = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp_configuracoes = ctx.getSharedPreferences("sp_configuracoes",MODE_PRIVATE);

        boolean gerarRelatorio = sp_configuracoes.getBoolean("gerar_relatorio",false);

        boolean gerarRelatorioDiario = sp_configuracoes.getBoolean("gerar_relatorio_diario",false);
        boolean gerarRelatorioSemanal = sp_configuracoes.getBoolean("gerar_relatorio_semanal",false);
        boolean gerarRelatorioMensal = sp_configuracoes.getBoolean("gerar_relatorio_mensal",false);

        String valorCompraAcai = sp_configuracoes.getString("valor_compra_acai", "0.0");
        String valorVendaAcai = sp_configuracoes.getString("valor_venda_acai", "0.0");

        Switch switchGerarRelatorio = (Switch) findViewById(R.id.switch_gerar_relatorio_configuracoes);


        final CheckBox cbRelatorioDiario = (CheckBox) findViewById(R.id.cb_diariamente_configuracoes);
        final CheckBox cbRelatorioSemanal = (CheckBox) findViewById(R.id.cb_semanalmente_configuracoes);
        final CheckBox cbRelatorioMensal = (CheckBox) findViewById(R.id.cb_mensalmente_configuracoes);

        switchGerarRelatorio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    cbRelatorioDiario.setEnabled(true);
                    cbRelatorioSemanal.setEnabled(true);
                    cbRelatorioMensal.setEnabled(true);

                }else{

                    cbRelatorioDiario.setEnabled(false);
                    cbRelatorioSemanal.setEnabled(false);
                    cbRelatorioMensal.setEnabled(false);

                }
            }
        });



        EditText et_valor_compra = (EditText) findViewById(R.id.et_valor_compra_configuracoes);
        EditText et_valor_venda = (EditText) findViewById(R.id.et_valor_venda_configuracoes);

        //setar os valores

        et_valor_compra.setText(valorCompraAcai+"");
        et_valor_venda.setText(valorVendaAcai+"");

        switchGerarRelatorio.setChecked(gerarRelatorio);

        if(gerarRelatorio){

            cbRelatorioDiario.setEnabled(true);
            cbRelatorioSemanal.setEnabled(true);
            cbRelatorioMensal.setEnabled(true);
        }


        cbRelatorioDiario.setChecked(gerarRelatorioDiario);
        cbRelatorioSemanal.setChecked(gerarRelatorioSemanal);
        cbRelatorioMensal.setChecked(gerarRelatorioMensal);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ic_menu_save) {
            salvarConfiguracoes();
        }else if (id == android.R.id.home){

            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return true;
    }

    private void salvarConfiguracoes(){

        SharedPreferences sp_configuracoes = ctx.getSharedPreferences("sp_configuracoes",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_configuracoes.edit();

        Switch switchGerarRelatorio = (Switch) findViewById(R.id.switch_gerar_relatorio_configuracoes);

        CheckBox cbRelatorioDiario = (CheckBox) findViewById(R.id.cb_diariamente_configuracoes);
        CheckBox cbRelatorioSemanal = (CheckBox) findViewById(R.id.cb_semanalmente_configuracoes);
        CheckBox cbRelatorioMensal = (CheckBox) findViewById(R.id.cb_mensalmente_configuracoes);

        EditText et_valor_compra = (EditText) findViewById(R.id.et_valor_compra_configuracoes);
        EditText et_valor_venda = (EditText) findViewById(R.id.et_valor_venda_configuracoes);

        boolean gerarRelatorio = false;
        boolean gerarRelatorioDiario = false ,gerarRelatorioSemanal = false ,gerarRelatorioMensal = false;

        String valorCompraTexto = et_valor_compra.getText().toString();
        String valorVendaTexto = et_valor_venda.getText().toString();


        if(switchGerarRelatorio.isChecked())
            gerarRelatorio = true;

        if(cbRelatorioDiario.isChecked())
            gerarRelatorioDiario = true;

        if(cbRelatorioSemanal.isChecked())
            gerarRelatorioSemanal = true;

        if(cbRelatorioMensal.isChecked())
            gerarRelatorioMensal = true;

        editor.putBoolean("gerar_relatorio",gerarRelatorio);

        editor.putBoolean("gerar_relatorio_diario", gerarRelatorioDiario);
        editor.putBoolean("gerar_relatorio_semanal", gerarRelatorioSemanal);
        editor.putBoolean("gerar_relatorio_mensal", gerarRelatorioMensal);

        //não preciso fazer cálculo com eles aqui, então guardo como string.
        editor.putString("valor_compra_acai", valorCompraTexto);
        editor.putString("valor_venda_acai", valorVendaTexto);

        editor.apply();

        Toast.makeText(ctx, "As configurações foram atualizadas", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(ctx, MainActivity.class));

    }

}
