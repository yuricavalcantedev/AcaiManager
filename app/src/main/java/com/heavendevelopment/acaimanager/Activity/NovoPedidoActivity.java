package com.heavendevelopment.acaimanager.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.heavendevelopment.acaimanager.Dominio.Item;
import com.heavendevelopment.acaimanager.Dominio.Pedido;
import com.heavendevelopment.acaimanager.R;
import com.heavendevelopment.acaimanager.Adapter.AdapterItensPedido;
import com.heavendevelopment.acaimanager.Service.PedidoService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.GregorianCalendar;
import java.util.List;

public class NovoPedidoActivity extends AppCompatActivity {


    //essa activity pode ser aberta em dois contextos:
    //1 - um novo pedido
    //2 - abrir um pedido para ver,adicionar mais coisas ou  finalizar.

    private boolean pedidoCriado = false;
    int idPedido = 0;
    ListView listViewItens;
    List<Item> listItens;
    AdapterItensPedido adapterItensPedido;
    Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_pedido);

        context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Pedido");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.inflateMenu(R.menu.menu_salvar);

        listViewItens = (ListView)findViewById(R.id.listview_novo_pedido);

        //se vinher com bundle, é porque o pedido já havia sido aberto antes.
        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            idPedido = bundle.getInt("idPedido");
            listItens = PedidoService.getItensFromPedido(idPedido);

            adapterItensPedido = new AdapterItensPedido(this,listItens);
            listViewItens.setAdapter(adapterItensPedido);

            Pedido pedido = Pedido.load(Pedido.class, idPedido);
            toolbar.setSubtitle("R$ "+pedido.getTotal());
        }

        listViewItens.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final TextView tvIdItem = (TextView) view.findViewById(R.id.tv_id_item_nova_quantidade_pedido);
                final TextView tvIdPedido = (TextView) view.findViewById(R.id.tv_id_pedido_nova_quantidade_pedido);
                TextView tvPeso = (TextView) view.findViewById(R.id.tv_peso_item_quantidade_pedido);
                TextView tvValor = (TextView) view.findViewById(R.id.tv_valor_item_quantidade_pedido);

                String idItemTexto = tvIdItem.getText().toString();
                String peso = tvPeso.getText().toString() + " (Kg)";
                String valor = "R$ " + tvValor.getText().toString();
                String idPedidoTexto = tvIdPedido.getText().toString();

                final int idItem = Integer.parseInt(idItemTexto);

                //esse id abaixo eu carrego do item do listview que foi selecionado.
                final int idPedidoItemExcluido = Integer.parseInt(idPedidoTexto);

                final Item itemParaExcluir = Item.load(Item.class, idItem);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                String message = "Você deseja excluir o item : \n " + peso + "\n " + valor;

                builder.setTitle("Excluir Item");
                builder.setMessage(message);

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        itemParaExcluir.delete();
                        Toast.makeText(NovoPedidoActivity.this, "Item excluido", Toast.LENGTH_SHORT).show();

                        //recarrega o listview
                        listItens = PedidoService.getItensFromPedido(idPedidoItemExcluido);

                        adapterItensPedido = new AdapterItensPedido(context,listItens);
                        listViewItens.setAdapter(adapterItensPedido);

                        Pedido pedido = Pedido.load(Pedido.class, idPedidoItemExcluido);
                        toolbar.setSubtitle("R$ "+pedido.getTotal());

                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

        Button btAdicionar = (Button) findViewById(R.id.bt_adicionar_novo_pedido);
        btAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //se é a primeira vez que a pessoa vai apertar o botão, eu crio o novo pedido
                //o total é 00.00 porque estou criando o pedido agora, depois vai ser incrementado

                EditText et_peso = (EditText)findViewById(R.id.et_peso_acai_novo_pedido);
                TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout_novo_pedido);

                if(et_peso.getText().toString().length() == 0){

                    textInputLayout.setError("Digite uma quantidade de açai");
                    textInputLayout.setErrorEnabled(true);
                }else{

                    textInputLayout.setErrorEnabled(false);

                    PedidoService pedidoService = new PedidoService(context);

                    if(!pedidoCriado) {

                        GregorianCalendar calendar = new GregorianCalendar();
                        int idPedidoCriado = PedidoService.addPedido(calendar, 00.00);

                        double peso = Double.parseDouble(et_peso.getText().toString());

                        pedidoService.addItemToAnOrder(idPedidoCriado,peso);
                        //caso tenha sido criado agora, o 'idPedido' vai se tornar esse idPedidoriado
                        idPedido = idPedidoCriado;

                        listItens = PedidoService.getItensFromPedido(idPedido);

                        adapterItensPedido = new AdapterItensPedido(getBaseContext(),listItens);
                        listViewItens.setAdapter(adapterItensPedido);

                        pedidoCriado = true;

                    }else{

                        EditText et_peso_acai = (EditText)findViewById(R.id.et_peso_acai_novo_pedido);
                        double peso = Double.parseDouble(et_peso_acai.getText().toString());

                        pedidoService.addItemToAnOrder(idPedido,peso);

                        //TODO: Otimizar essa parte do código, usando o 'notifyChange' ou algo do tipo.
                        listItens = PedidoService.getItensFromPedido(idPedido);
                        adapterItensPedido = new AdapterItensPedido(getBaseContext(),listItens);
                        listViewItens.setAdapter(adapterItensPedido);
                    }

                    //atualiza o total do pedido sempre que adicionar um item no pedido.
                    Pedido pedido = Pedido.load(Pedido.class, idPedido);

                    double totalParcialPedido = pedido.getTotal();

                    toolbar.setSubtitle("R$ "+totalParcialPedido);
                    et_peso.setText("");

                }






            }
        });

        Button bt_finalizar_pedido = (Button) findViewById(R.id.bt_finalizar_pedido);
        bt_finalizar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                LayoutInflater layoutInflater = LayoutInflater.from(context);

                final View dialog_customer_layout = layoutInflater.inflate(R.layout.dialog_finalizar_pedido, null);

                final RadioGroup radioGroup = (RadioGroup) dialog_customer_layout.findViewById(R.id.radio_group);
                final RadioButton radioButtonAVista = (RadioButton) radioGroup.findViewById(R.id.rb_avista_finalizar_pedido);

                final TextInputLayout textInputLayoutDinheiroCliente = (TextInputLayout) dialog_customer_layout.findViewById(R.id.textInputLayout_dinheiro_cliente);
                final TextView tvTrocoCliente = (TextView) dialog_customer_layout.findViewById(R.id.tv_troco_cliente_finalizar_pedido);
                final TextView tvTotalPedido = (TextView) dialog_customer_layout.findViewById(R.id.tv_total_pedido_finalizar_pedido);
                final EditText etTrocoCliente = (EditText) dialog_customer_layout.findViewById(R.id.et_troco_cliente_finalizar_pedido);
                final EditText etDinheiroCliente = (EditText) dialog_customer_layout.findViewById(R.id.editText_dinheiro_cliente);

                radioButtonAVista.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                            textInputLayoutDinheiroCliente.setVisibility(View.VISIBLE);
                            textInputLayoutDinheiroCliente.setEnabled(true);
                            tvTrocoCliente.setVisibility(View.VISIBLE);
                            etTrocoCliente.setVisibility(View.VISIBLE);
                        }else{
                            textInputLayoutDinheiroCliente.setVisibility(View.INVISIBLE);
                            textInputLayoutDinheiroCliente.setEnabled(false);
                            tvTrocoCliente.setVisibility(View.INVISIBLE);
                            etTrocoCliente.setVisibility(View.INVISIBLE);

                        }
                    }
                });

                etDinheiroCliente.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        String dinheiroTexto = etDinheiroCliente.getText().toString();
                        if (dinheiroTexto.length() > 0) {

                            try{

                                double dinheiroCliente = Double.parseDouble(dinheiroTexto);
                                double totalPedido = Double.parseDouble(tvTotalPedido.getText().toString());

                                if (dinheiroCliente >= totalPedido) {

                                    double trocoCliente = dinheiroCliente - totalPedido;
                                    BigDecimal bd = new BigDecimal(trocoCliente).setScale(2, RoundingMode.HALF_EVEN);
                                    trocoCliente = bd.doubleValue();

                                    etTrocoCliente.setText(trocoCliente + "");
                                }else{

                                    etTrocoCliente.setText("");

                                }

                            }catch (Exception ex){

                                Toast.makeText(context, "Valor inválido", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });

                final Pedido pedido = Pedido.load(Pedido.class,idPedido);
                tvTotalPedido.setText(pedido.getTotal()+"");

                alertDialogBuilder.setView(dialog_customer_layout);

                alertDialogBuilder.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int formaPagamentoPedido;

                        //1 - À vista , 2 - Cartão
                        if(radioButtonAVista.isChecked())
                            formaPagamentoPedido = 1;
                        else
                            formaPagamentoPedido = 2;

                        pedido.setPagamento(formaPagamentoPedido);
                        pedido.setRealizado(true);

                        pedido.save();

                        Toast.makeText(context, "Pedido concluído.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(context, MainActivity.class));

                    }
                });

                alertDialogBuilder.setNeutralButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });



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
