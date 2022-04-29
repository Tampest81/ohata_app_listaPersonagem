package com.example.ohata_app_listaPersonagem.ui.activities;

import static com.example.ohata_app_listaPersonagem.ui.activities.ConstatesActivities.CHAVE_PERSONAGEM;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ohata_app_listaPersonagem.DAO.PersonagemDAO;
import com.example.ohata_app_listaPersonagem.R;
import com.example.ohata_app_listaPersonagem.model.Personagem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListaPersonagemActivity extends AppCompatActivity
{
    public static final String TITULO_APPBAR = "Lista de Personagens";
    private final PersonagemDAO dao = new PersonagemDAO();
    private ArrayAdapter<Personagem> adapter;

    // cria a activity
    @Override
    protected void onCreate(@Nullable Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        // gera a ui para o usuário
        setContentView(R.layout.activity_lista_personagem);

        // define o título
        setTitle(TITULO_APPBAR);

        // define a função do botão floatingactionbutton - será utilizado para criar um novo personagem
        configuraFabNovoPersonagem();

        // configura a lista
        configuraLista();
    }

    // Define a função do botão floating action button e roda o método abre formulario
    private void configuraFabNovoPersonagem()
    {
        // Pega a referencia do botão
        FloatingActionButton botaoNovoPersonagem = findViewById(R.id.fab_add);

        // escuta o click e atribui a nova função
        botaoNovoPersonagem.setOnClickListener
        (
            new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    // roda o método abre formulario
                    abreFormulario();
                }
            }
        );
    }

    // abre o formulario para registrar um novo personagem
    private void abreFormulario()
    {
        startActivity(new Intent(this, FormularioPersonagemActivity.class));
    }

    // atualiza os personagens ao entrar na página
    @Override
    protected void onResume()
    {
        super.onResume();
        atualizaPersonagem();
    }

    // atualiza os personagens da lista
    private void atualizaPersonagem()
    {
        adapter.clear(); // limpando o adapter pois ele fica com "sujeira" na memoria
        adapter.addAll(dao.todos()); // adiciona os personagens na lista
    }

    // remove todos os personagens criados que estão na lista
    private void remove(Personagem personagem)
    {
        dao.remove(personagem); // limpa o personagem da lista
        adapter.remove(personagem);
    }

    // Cria um novo contexto
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, view, menuInfo);
        getMenuInflater().inflate(R.menu.activity_lista_personagem_menu, menu);
    }

    // caso um contexto do menu seja selecionado ele irá fazer algo
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item)
    {
        int itemId = item.getItemId();
        // caso o item selecionado seja o botão de remover
        if(itemId == R.id.activity_lista_personagem_menu_remover)
        {
            // irá criar um modal de alerta, perguntando se o usuário quer ou nao deletar o seu personagem.
            // juntamente irá criar os botões
            new AlertDialog.Builder(this)
                    .setTitle("Removendo Personagem")
                    .setMessage("Tem certeza que quer remover?")
                    .setPositiveButton
                    ( // se o botão sim for selecionado, irá gerar um novo "dialog"
                    "Sim", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                // pega a referencia do personagem -> personagem escolhido e deleta
                                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                Personagem personagemEscolhido = adapter.getItem(menuInfo.position);
                                remove(personagemEscolhido);
                            }
                        }
                    )
                    .setNegativeButton("Não", null) // caso seja selecionado o botão não, irá retornar null
                    .show();
        }
        return super.onContextItemSelected(item);
    }

    // configura a lista
    private void configuraLista()
    {
        // faz referencia a lista no xml
        ListView listaDePersonagens = findViewById(R.id.activity_main_lista_personagem);

        configuraAdapter(listaDePersonagens);

        // configura o item ao clicar nele
        configuraItemPorClique(listaDePersonagens);

        // habilita o contextmenu ao clicar no item
        registerForContextMenu(listaDePersonagens);
    }

    // configura a interação ao clicar no item.
    private void configuraItemPorClique(ListView listaDePersonagens)
    {
        listaDePersonagens.setOnItemClickListener
        (
            new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id)
                {
                    Personagem personagemEscolhido = (Personagem) adapterView.getItemAtPosition(posicao);
                    abreFormularioEditar(personagemEscolhido);
                }
            }
        );
    }

    // ao clicar abre o formulario para o usuário editar o personagem selecionado.
    private void abreFormularioEditar(Personagem personagemEscolhido)
    {
        Intent vaiParaFormulario = new Intent(ListaPersonagemActivity.this, FormularioPersonagemActivity.class);
        vaiParaFormulario.putExtra(CHAVE_PERSONAGEM, personagemEscolhido);
        startActivity(vaiParaFormulario);
    }

    // atribui os valores do array no layout
    private void configuraAdapter(ListView listaDePersonagens)
    {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listaDePersonagens.setAdapter(adapter);
    }
}
