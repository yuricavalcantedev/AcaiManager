package com.heavendevelopment.acaimanager.Activity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.heavendevelopment.acaimanager.Dominio.Pedido;
import com.heavendevelopment.acaimanager.Dominio.Relatorio;
import com.heavendevelopment.acaimanager.Fragment.FragmentPedidosAbertosDia;
import com.heavendevelopment.acaimanager.Fragment.FragmentPedidosConcluidosMain;
import com.heavendevelopment.acaimanager.R;
import com.heavendevelopment.acaimanager.Service.RelatorioService;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final int PERMISSION_CODE = 200;
    AlertDialog alerta;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Tela Principal");
        setSupportActionBar(toolbar);

        context = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // tabLayout tem o seu listener.
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        //enche o banco na primeira vez que o app for executado.
        sharedPreferences = context.getSharedPreferences("banco_populado",MODE_PRIVATE);
        int qtd_vezes_app_executado = sharedPreferences.getInt("qtd_vezes_app_executado",0);

        //na primeira vez que for executado, ele entra no if, depois não entra mais.
        if(qtd_vezes_app_executado <= 3) {

            populateDataBase();
            editor = sharedPreferences.edit();

            editor.putInt("qtd_vezes_app_executado",++qtd_vezes_app_executado);
            editor.commit();

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ConfiguracoesActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nova_venda) {

            abrirNovaVenda();

        } else if (id == R.id.nav_historico) {

            startActivity(new Intent(this, HistoricoVendasActivity.class));

        } else if (id == R.id.nav_relatorio) {

            startActivity(new Intent(this, VerRelatoriosActivity.class));

        }else if (id == R.id.nav_configuracoes) {

            startActivity(new Intent(this, ConfiguracoesActivity.class));

        } else if (id == R.id.nav_ajuda) {

            startActivity(new Intent(this, AjudaActivity.class));

        } else if (id == R.id.nav_fechar_caixa) {

            //verificar se o caixa já foi aberto.
            chooseReportToCreate();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void chooseReportToCreate(){

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, "Caixa fechado, que dia hein! Deseja sair do app?", Snackbar.LENGTH_LONG)
                .setAction("SAIR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        finish();
                    }
                });

        GregorianCalendar calendarDia = new GregorianCalendar();
        int dia = calendarDia.get(GregorianCalendar.DAY_OF_MONTH);
        int semanaDoMes = calendarDia.get(GregorianCalendar.WEEK_OF_MONTH);
        int dia_da_semana = calendarDia.get(GregorianCalendar.DAY_OF_WEEK);
        int mes = calendarDia.get(GregorianCalendar.MONTH) + 1;
        int ano = calendarDia.get(GregorianCalendar.YEAR);

        //só deixa fazer uma nova venda, se ele já tiver configurado o preço de venda e compra do açai.
        SharedPreferences sp_configuracoes = context.getSharedPreferences("sp_configuracoes",MODE_PRIVATE);

        boolean gerarRelatorio = sp_configuracoes.getBoolean("gerar_relatorio",false);

        boolean gerarRelatorioDiario = sp_configuracoes.getBoolean("gerar_relatorio_diario",false);
        boolean gerarRelatorioSemanal = sp_configuracoes.getBoolean("gerar_relatorio_semanal",false);
        boolean gerarRelatorioMensal = sp_configuracoes.getBoolean("gerar_relatorio_mensal",false);

        RelatorioService relatorioService = new RelatorioService(this);

        if(gerarRelatorio){

            if(gerarRelatorioDiario)
                relatorioService.gerarRelatorioDiario(dia,semanaDoMes,mes,ano,snackbar);

            if(gerarRelatorioSemanal){

                //verificar se hoje é o fim da semana ou seja se hoje é domingo
                //o número 1 significa o domingo
                if(dia_da_semana == 1){

                    relatorioService.gerarRelatorioSemanal(semanaDoMes,mes,ano,snackbar);
                }
            }
            if(gerarRelatorioMensal){

                //adiciono um dia na data que peguei
                calendarDia.add(Calendar.DAY_OF_MONTH,1);

                //se depois de eu ter adicionado, o mês que eu peguei lá em cima, for diferente do mês do calendar
                //é porque hoje é o último dia do mês
                if(mes != (calendarDia.get(Calendar.MONTH) + 1))

                    relatorioService.gerarRelatorioMensal(mes,ano,snackbar);
            }
        }



    }

    private void populateDataBase(){

        //Pedido(int dia, int mes, int ano, int hora, int minuto, int pagamento, int semanaDoMes, double total, int diaDaSemana, bool realizado

        Pedido pedido1  = new Pedido(10,7,15,19,20,2,3,35.99,3,true);
        Pedido pedido2  = new Pedido(10,7,15,19,20,1,3,35.99,3,true);
        Pedido pedido3  = new Pedido(10,7,15,19,20,1,3,35.99,3,true);
        Pedido pedido4  = new Pedido(5,3,15,19,20,2,3,35.99,3,true);
        Pedido pedido5  = new Pedido(10,11,16,19,20,2,3,35.99,3,true);
        Pedido pedido6  = new Pedido(10,11,16,19,20,2,3,35.99,3,true);
        Pedido pedido7  = new Pedido(10,11,16,19,20,2,3,35.99,3,true);
        Pedido pedido8  = new Pedido(12,9,16,19,20,1,3,35.99,3,true);
        Pedido pedido9  = new Pedido(12,9,16,19,20,2,3,35.99,3,true);
        Pedido pedido10 = new Pedido(9,9,16,19,20,2,3,35.99,3,true);
        Pedido pedido11 = new Pedido(1,11,16,19,20,1,3,35.99,3,true);
        Pedido pedido12 = new Pedido(15,11,16,19,20,1,3,35.99,3,true);
        Pedido pedido13 = new Pedido(15,11,16,19,20,1,3,35.99,3,true);
        Pedido pedido14 = new Pedido(15,11,16,19,20,1,3,35.99,3,true);
        Pedido pedido15 = new Pedido(15,11,16,19,20,2,3,35.99,3,true);
        Pedido pedido16 = new Pedido(27,11,16,19,20,2,3,35.99,3,true);
        Pedido pedido17 = new Pedido(28,11,16,19,20,2,3,35.99,3,true);
        Pedido pedido18 = new Pedido(30,11,16,19,20,1,3,35.99,3,true);
        Pedido pedido19 = new Pedido(31,10,16,19,20,1,3,35.99,3,true);
        Pedido pedido20 = new Pedido(8,10,16,19,20,2,3,35.99,3,true);
        Pedido pedido21 = new Pedido(8,10,16,19,20,2,3,35.99,3,true);
        Pedido pedido22 = new Pedido(8,10,16,19,20,1,3,35.99,3,true);
        Pedido pedido23 = new Pedido(2,7,16,19,20,1,3,35.99,3,true);
        Pedido pedido24 = new Pedido(3,7,16,19,20,1,3,35.99,3,true);
        Pedido pedido25 = new Pedido(4,7,16,19,20,1,3,35.99,3,true);
        Pedido pedido26 = new Pedido(5,7,16,19,20,1,3,35.99,3,true);
        Pedido pedido27 = new Pedido(5,7,16,19,20,2,3,35.99,3,true);
        Pedido pedido28 = new Pedido(5,7,16,19,20,2,3,35.99,3,true);
        Pedido pedido29 = new Pedido(23,7,16,19,20,1,3,35.99,3,true);
        Pedido pedido30 = new Pedido(12,7,16,19,20,1,3,35.99,3,true);



        List<Pedido> pedidosList = new ArrayList<>();

        pedidosList.add(pedido1);
        pedidosList.add(pedido2);
        pedidosList.add(pedido3);
        pedidosList.add(pedido4);
        pedidosList.add(pedido5);
        pedidosList.add(pedido6);
        pedidosList.add(pedido7);
        pedidosList.add(pedido8);
        pedidosList.add(pedido9);
        pedidosList.add(pedido10);
        pedidosList.add(pedido11);
        pedidosList.add(pedido12);
        pedidosList.add(pedido13);
        pedidosList.add(pedido14);
        pedidosList.add(pedido15);
        pedidosList.add(pedido16);
        pedidosList.add(pedido17);
        pedidosList.add(pedido18);
        pedidosList.add(pedido19);
        pedidosList.add(pedido20);
        pedidosList.add(pedido21);
        pedidosList.add(pedido22);
        pedidosList.add(pedido23);
        pedidosList.add(pedido24);
        pedidosList.add(pedido25);
        pedidosList.add(pedido26);
        pedidosList.add(pedido27);
        pedidosList.add(pedido28);
        pedidosList.add(pedido29);
        pedidosList.add(pedido30);



        // using transaction increase the speed of inserts a lot.
        ActiveAndroid.beginTransaction();
        try {

            for (int i = 0; i < pedidosList.size(); i++)
                pedidosList.get(i).save();

            ActiveAndroid.setTransactionSuccessful();

        }catch (Exception ex){

            Toast.makeText(context, "Um ou mais itens não foram inseridos corretamente. Adicione-os novamente.", Toast.LENGTH_SHORT).show();

        }finally {

            ActiveAndroid.endTransaction();
        }






    }

    private void abrirNovaVenda (){

        //só deixa fazer uma nova venda, se ele já tiver configurado o preço de venda e compra do açai.
        SharedPreferences sp_configuracoes = context.getSharedPreferences("sp_configuracoes",MODE_PRIVATE);

        String valorCompraAcai = sp_configuracoes.getString("valor_compra_acai", "0.0");
        String valorVendaAcai = sp_configuracoes.getString("valor_venda_acai", "0.0");

        if(valorCompraAcai == "00,00" | valorVendaAcai == "00,00"){

            Toast.makeText(context, "Você não pode vender sem antes ter configurado o valor de venda e compra do açai.", Toast.LENGTH_LONG).show();
        }else{

            startActivity(new Intent(this,NovoPedidoActivity.class));
        }


    }


//    private void gerarPdfDoRelatorio(Relatorio relatorio){
//
//        //recebe o relatório e cria o pdf aqui.
//        Document document = new Document(PageSize.A4);
//
//        try {
//
//            String path = Environment.getExternalStorageDirectory().toString() + "/AcaiManager/Relatorios";
//            File file = new File(Environment.getExternalStorageDirectory().toString() + "/AcaiManager/Relatorios");
//
//            int tipoRelatorio = relatorio.getTipo_relatorio();
//            String filename;
//            String tituloPdf;
//            String subtitituloPdf;
//
//            if(tipoRelatorio == 1){
//                filename = "Relatório Diário - " + relatorio.getDia() + "/" + relatorio.getMes() + "/" + relatorio.getAno();
//                tituloPdf = "Relatório Diário";
//                subtitituloPdf = "Dia : " + relatorio.getDia() + "/" + relatorio.getMes() + "/" + relatorio.getAno();
//
//            }else if(tipoRelatorio == 2) {
//                filename = "Relatório Semanal - " + relatorio.getSemanaDoMes() + "/" + relatorio.getMes() + "/" + relatorio.getAno();
//                tituloPdf = "Relatório Semanal";
//                subtitituloPdf = "Semana :" + relatorio.getSemanaDoMes() + "/" + relatorio.getMes() + "/" + relatorio.getAno();
//
//            }else {
//                filename = "Relatório Mensal - " + relatorio.getMes() + "/" + relatorio.getAno();
//                tituloPdf = "Relatório Mensal";
//                subtitituloPdf = "Mês : " + relatorio.getMes() + "/" + relatorio.getAno();
//
//            }
//
//
//            File dir = new File(path, filename);
//            if (!dir.exists()) {
//                dir.getParentFile().mkdirs();
//            }
//
//            FileOutputStream fOut = new FileOutputStream(dir);
//            fOut.flush();
//
//            //Fontes
//            Font fontTitulo = new Font(Font.FontFamily.COURIER, 25, Font.BOLD);
//            Font fontSubTitulo = new Font(Font.FontFamily.COURIER, 20, Font.NORMAL);
//            Font fontLabel = new Font(Font.FontFamily.TIMES_ROMAN, 16);
//            Font fontValor = new Font(Font.FontFamily.HELVETICA, 14);
//            Font fontRodape = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLDITALIC);
//
//            Paragraph titulo = new Paragraph(tituloPdf, fontTitulo);
//            titulo.setAlignment(Element.ALIGN_CENTER);
//
//            Paragraph subTitutlo = new Paragraph(subtitituloPdf + "\n\n", fontSubTitulo);
//            titulo.setAlignment(Element.ALIGN_LEFT);
//
//            PdfWriter.getInstance(document, fOut);
//            document.open();
//
//            document.add(titulo);
//            document.add(subTitutlo);
//
//            Paragraph paragraphData = new Paragraph(subtitituloPdf , fontLabel);
//            paragraphData.setAlignment(Element.CHAPTER);
//            paragraphData.setSpacingAfter(20);
//            document.add(paragraphData);
//
//            Paragraph paragraphNumVendasAVista = new Paragraph("Número de vendas à vista : " + relatorio.getQtdPedidosAVista() , fontLabel);
//            paragraphNumVendasAVista.setAlignment(Element.CHAPTER);
//            paragraphNumVendasAVista.setSpacingAfter(20);
//            document.add(paragraphNumVendasAVista);
//
//            Paragraph paragraphNumVendasCartao = new Paragraph("Número de vendas no cartão : " + relatorio.getQtdPedidosCartao() , fontLabel);
//            paragraphNumVendasCartao.setAlignment(Element.CHAPTER);
//            paragraphNumVendasCartao.setSpacingAfter(20);
//            document.add(paragraphNumVendasCartao);
//
//            Paragraph paragraphNumVendasTotal = new Paragraph("Número total de vendas : " + relatorio.getQtdPedidosVendidos() , fontLabel);
//            paragraphNumVendasTotal.setAlignment(Element.CHAPTER);
//            paragraphNumVendasTotal.setSpacingAfter(20);
//            document.add(paragraphNumVendasTotal);
//
//            Paragraph paragraphTotalEntradaAVista = new Paragraph("Valor que entrou à vista : R$ " + relatorio.getTotalEntrouAVista() , fontLabel);
//            paragraphTotalEntradaAVista.setAlignment(Element.CHAPTER);
//            paragraphTotalEntradaAVista.setSpacingAfter(20);
//            document.add(paragraphTotalEntradaAVista);
//
//            Paragraph paragraphTotalEntradaCartao  = new Paragraph("Valor que entrou no cartão : R$ " + relatorio.getTotalEntrouCartao() , fontLabel);
//            paragraphTotalEntradaCartao.setAlignment(Element.CHAPTER);
//            paragraphTotalEntradaCartao.setSpacingAfter(20);
//            document.add(paragraphTotalEntradaCartao);
//
//            double totalEntrada = relatorio.getTotalEntrouAVista() + relatorio.getTotalEntrouCartao();
//            BigDecimal bd = new BigDecimal(totalEntrada).setScale(2, RoundingMode.HALF_EVEN);
//            totalEntrada = bd.doubleValue();
//
//            Paragraph paragraphTotalEntrada = new Paragraph("Valor que entrou no total : R$" + totalEntrada , fontLabel);
//            paragraphTotalEntrada.setAlignment(Element.CHAPTER);
//            paragraphTotalEntrada.setSpacingAfter(20);
//            document.add(paragraphTotalEntrada);
//
//            Paragraph paragraphLucroTotalAVista = new Paragraph("Lucro real à vista : " + relatorio.getLucroAvista() , fontLabel);
//            paragraphLucroTotalAVista.setAlignment(Element.CHAPTER);
//            paragraphLucroTotalAVista.setSpacingAfter(20);
//            document.add(paragraphLucroTotalAVista);
//
//            Paragraph paragraphLucroTotalCartao = new Paragraph("Lucro real no cartão: " + relatorio.getLucroCartao() , fontLabel);
//            paragraphLucroTotalCartao.setAlignment(Element.CHAPTER);
//            paragraphLucroTotalCartao.setSpacingAfter(20);
//            document.add(paragraphLucroTotalCartao);
//
//            Paragraph paragraphLucroTotal = new Paragraph("Lucro real no total : " + relatorio.getLucroTotal() , fontLabel);
//            paragraphLucroTotal.setAlignment(Element.CHAPTER);
//            paragraphLucroTotal.setSpacingAfter(20);
//            document.add(paragraphLucroTotal);
//
//            Paragraph rodape = new Paragraph("Heaven Development\n heavendevelopment@gmail.com", fontRodape);
//            rodape.setAlignment(Element.ALIGN_BOTTOM);
//            document.add(rodape);
//
//            Toast.makeText(this,"Relatório criado com sucesso na pasta do app.",Toast.LENGTH_SHORT).show();
//
//
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            document.close();
//        }
//
//    }

    private void setupViewPager(ViewPager viewPager) {



        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentPedidosAbertosDia(), "Pedidos Abertos");
        adapter.addFragment(new FragmentPedidosConcluidosMain(), "Pedidos Concluídos");

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


}
