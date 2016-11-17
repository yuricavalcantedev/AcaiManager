package com.heavendevelopment.acaimanager.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.heavendevelopment.acaimanager.Adapter.AdapterRelatorio;
import com.heavendevelopment.acaimanager.Dominio.Relatorio;
import com.heavendevelopment.acaimanager.R;
import com.heavendevelopment.acaimanager.Service.RelatorioService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerRelatoriosActivity extends AppCompatActivity {

    //EX de busca
    // 1 - diario, 2 semanal, 3 mensal
    // 08/11/16 diario
    // 04-11-16 semanal
    // 04/11 mensal

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_relatorio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ver Relatórios");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        final ListView listViewRelatorios = (ListView) findViewById(R.id.listview_relatorios);

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        int dia = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);
        int mes = gregorianCalendar.get(GregorianCalendar.MONTH) + 1;
        int ano = gregorianCalendar.get(GregorianCalendar.YEAR);

        //só para poder setar o hint com a data de hoje.
        String anotexto = ano+"";
        anotexto = anotexto.substring(2,4);

        EditText etFiltro = (EditText) findViewById(R.id.et_filtro_ver_relatorios);
        etFiltro.setHint(dia+"/"+mes+"/"+anotexto);

        //na verdade, pegar os relatórios do mês atual
        RelatorioService relatorioService = new RelatorioService(context);
        List<Relatorio> listRelatorio = relatorioService.getRelatorioByMonth(mes,ano);

        AdapterRelatorio adapterRelatorio = new AdapterRelatorio(context,listRelatorio);
        listViewRelatorios.setAdapter(adapterRelatorio);

        Button bt_filtro = (Button) findViewById(R.id.bt_filtro_ver_relatorio);
        bt_filtro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Relatorio> listRelatorio = checkarTipoFiltroAndReturnsList();

                //se a lista que veio for nula, é porque deu erro
                if(listRelatorio != null){

                    AdapterRelatorio adapterRelatorio = new AdapterRelatorio(context,listRelatorio);

                    listViewRelatorios.setAdapter(adapterRelatorio);

                }
            }
        });

        listViewRelatorios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tvIdRelatorio = (TextView) view.findViewById(R.id.tv_item_id_relatorio_ver_relatorios);
                int idRelatorio = Integer.parseInt(tvIdRelatorio.getText().toString());

                Relatorio relatorio = Relatorio.load(Relatorio.class, idRelatorio);

                Intent intent = new Intent(context,VerDetalhesRelatorioActivity.class);

                String data = relatorio.getDia()+"/"+relatorio.getMes()+"/"+relatorio.getAno();
                String tipoRelatorio;

                if(relatorio.getTipo_relatorio() == 1)
                    tipoRelatorio = "Diário";
                else if(relatorio.getTipo_relatorio() == 2)
                    tipoRelatorio = "Semanal";
                else
                    tipoRelatorio = "Mensal";

                String qtd_vendas_avista = relatorio.getQtdPedidosAVista()+"";
                String qtd_vendas_cartao = relatorio.getQtdPedidosCartao()+"";
                String qtd_vendas_total = relatorio.getQtdPedidosVendidos()+"";
                double total_entrada_avista = relatorio.getTotalEntrouAVista();
                double total_entrada_cartao = relatorio.getTotalEntrouCartao();
                double total_entrada = total_entrada_avista + total_entrada_cartao;

                BigDecimal bd = new BigDecimal(total_entrada).setScale(2, RoundingMode.HALF_EVEN);
                total_entrada = bd.doubleValue();

                double lucro_avista = relatorio.getLucroAvista();
                double lucro_cartao = relatorio.getLucroCartao();
                double lucro_total = relatorio.getLucroTotal();

                Bundle bundle = new Bundle();
                bundle.putString("data",data);
                bundle.putString("tipo_relatorio",tipoRelatorio);
                bundle.putString("qtd_vendas_avista",qtd_vendas_avista);
                bundle.putString("qtd_vendas_cartao",qtd_vendas_cartao);
                bundle.putString("qtd_vendas_total",qtd_vendas_total);
                bundle.putString("total_entrada_avista",total_entrada_avista+"");
                bundle.putString("total_entrada_cartao",total_entrada_cartao+"");
                bundle.putString("total_entrada",total_entrada+"");
                bundle.putString("lucro_avista",lucro_avista+"");
                bundle.putString("lucro_cartao",lucro_cartao+"");
                bundle.putString("lucro_total",lucro_total+"");

                intent.putExtras(bundle);

                startActivity(intent);


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ajuda_ver_relatorios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_help) {

            showHelpDialog();

        }else if (id == android.R.id.home){

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Relatorio> checkarTipoFiltroAndReturnsList(){

        //EX de busca
        // 1 - diario, 2 semanal, 3 mensal
        // 08/11/16 diario
        // 04-11-16 semanal
        // 04/11 mensal
        // [nada] mostrar todos
        //fazer a mensagem aqui

        //0 - filtro inválido , 1 - diario, 2 - semanal, 3 - mensal, 4 - tudo
        int tipoFiltro;
        String[] arrayFiltroUsuario;

        RelatorioService relatorioService = new RelatorioService(context);
        List<Relatorio> relatorioListFiltered;

        int dia = 0, semana = 0, mes = 0, ano = 0;

        EditText et_filtro = (EditText) findViewById(R.id.et_filtro_ver_relatorios);
        String filtro = et_filtro.getText().toString();

        Pattern regexFiltroDiario = Pattern.compile("\\d{1,2}/\\d{2}/\\d{2}");
        Matcher matcherFiltroDiario = regexFiltroDiario.matcher(filtro);

        Pattern regexFiltroSemanal = Pattern.compile("\\d{1}-\\d{2}-\\d{2}");
        Matcher matcherFiltroSemanal = regexFiltroSemanal.matcher(filtro);

        Pattern regexFiltroMensal = Pattern.compile("\\d{2}/\\d{2}");
        Matcher matcherFiltroMensal= regexFiltroMensal.matcher(filtro);

        if(matcherFiltroDiario.find() ){

            tipoFiltro = 1;
            arrayFiltroUsuario = filtro.split("/");

            dia = Integer.parseInt(arrayFiltroUsuario[0]);
            mes = Integer.parseInt(arrayFiltroUsuario[1]);
            ano = Integer.parseInt(arrayFiltroUsuario[2]);

            //como guardo o ano completo (2016,2017) eu adiciono os primeiros dois números na frente do ano que ele passou.
            String textoAno = "20"+ano;
            ano = Integer.parseInt(textoAno);

        }else if (matcherFiltroSemanal.find()){

            tipoFiltro = 2;

            arrayFiltroUsuario = filtro.split("-");

            semana = Integer.parseInt(arrayFiltroUsuario[0]);
            mes = Integer.parseInt(arrayFiltroUsuario[1]);
            ano = Integer.parseInt(arrayFiltroUsuario[2]);

            //como guardo o ano completo (2016,2017) eu adiciono os primeiros dois números na frente do ano que ele passou.
            String textoAno = "20"+ano;
            ano = Integer.parseInt(textoAno);

        }else if(matcherFiltroMensal.find()){

            tipoFiltro = 3;

            arrayFiltroUsuario = filtro.split("/");

            mes = Integer.parseInt(arrayFiltroUsuario[0]);
            ano = Integer.parseInt(arrayFiltroUsuario[1]);

            //como guardo o ano completo (2016,2017) eu adiciono os primeiros dois números na frente do ano que ele passou.
            String textoAno = "20"+ano;
            ano = Integer.parseInt(textoAno);

        }else if(filtro.length() == 0){

            tipoFiltro = 4;

        }else{

            tipoFiltro = 0;
        }

        switch (tipoFiltro){

            case 1 : relatorioListFiltered = relatorioService.getRelatorioDiario(dia,mes,ano);
                break;

            case 2 : relatorioListFiltered = relatorioService.getRelatorioByWeek(semana,mes,ano);
                break;

            case 3 : relatorioListFiltered = relatorioService.getRelatorioByMonth(mes,ano);
                break;

            case 4: relatorioListFiltered = relatorioService.getTodosRelatorios();
                break;

            //se chegar aqui é porque o filtro foi errado, então mostro uma mensagem de erro.
            default:
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayoutVerRelatorio) ;

                final Snackbar snackbar = Snackbar
                        .make(relativeLayout, "Filtro inválido.", Snackbar.LENGTH_LONG)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });

                snackbar.show();
                return null;
        }

        return relatorioListFiltered;
    }

    private void showHelpDialog(){


        //EX de busca
        // 1 - diario, 2 semanal, 3 mensal
        // 08/11/16 diario
        // 04-11-16 semanal
        // 04/11 mensal
        // [nada] mostrar todos
        //fazer a mensagem aqui


        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        int dia = gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);
        int semana = gregorianCalendar.get(GregorianCalendar.WEEK_OF_MONTH);
        int mes = gregorianCalendar.get(GregorianCalendar.MONTH) + 1;
        int ano = gregorianCalendar.get(GregorianCalendar.YEAR);
        String anoTexto = ano+"";
        anoTexto = anoTexto.substring(2,4);

        String messageFiltroDia = "Ex. filtro diário : "+dia+"/"+mes+"/"+anoTexto +"\n\n";
        String messageFiltroSemanal = "Ex. filtro semanal : "+semana+"-"+mes+"-"+anoTexto +"\n\n";
        String messageFiltroMensal = "Ex. filtro mensal : "+mes+"/"+anoTexto +"\n\n";
        String messageFiltroTodos = "Caso queira mostrar todos, aperte o botão 'FILTRAR' sem nenhum número"+"\n\n";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(messageFiltroDia);
        stringBuilder.append(messageFiltroSemanal);
        stringBuilder.append(messageFiltroMensal);
        stringBuilder.append(messageFiltroTodos);


        //mostrar dialog de ajuda.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ajuda");
        builder.setMessage(stringBuilder.toString());
        builder.setPositiveButton("Ok, entendi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }
}
