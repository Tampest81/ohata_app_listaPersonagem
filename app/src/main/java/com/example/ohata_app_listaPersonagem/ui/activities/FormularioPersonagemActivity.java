package com.example.ohata_app_listaPersonagem.ui.activities;

import static com.example.ohata_app_listaPersonagem.ui.activities.ConstatesActivities.CHAVE_PERSONAGEM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.ohata_app_listaPersonagem.DAO.PersonagemDAO;
import com.example.ohata_app_listaPersonagem.R;
import com.example.ohata_app_listaPersonagem.model.Personagem;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class FormularioPersonagemActivity extends AppCompatActivity
{
    private static final String TITULO_APPBAR_EDIT_PERSONAGEM = "Editar o Personagem";
    private static final String TITULO_APPBAR_NEW_PERSONAGEM = "Novo Personagem";
    private EditText campoNome;
    private EditText campoNascimento;
    private EditText campoAltura;
    private final PersonagemDAO dao = new PersonagemDAO();
    private Personagem personagem;

    // reescreve o métod, utiliza o getmenuinflater para criar um menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_formulario_personagem_menu_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // reescreve o metodo on options item selected, função que irá ser utilizado no botão finalizar formulario
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        // recebe e atribui o item recebido através do metodo getItemID();
        int itemId = item.getItemId();
        if(itemId == R.id.activity_formulario_personagem_menu_salvar)
        {
            finalizarFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    // reescreve o oncreate, utiliza o método setcontnetview para gerar a ui para o usuario
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_personagem);
        inicializarCampos();
        carregaPersonagem();
    }

    //caso o usuário edite o personagem irá carregar um e se for registrar ele cria um novo personagem
    protected void carregaPersonagem()
    {
        Intent dados = getIntent(); // a utilização dessa variável é o que será utilizado para determinar se há dados ou não dentro dele.
                                    // com isso é possível determinar se há um personagem existente ou não.

        if(dados.hasExtra(CHAVE_PERSONAGEM))
        {
            setTitle(TITULO_APPBAR_EDIT_PERSONAGEM); // edita o que irá aparecerá no cabeçalho
            personagem = (Personagem) dados.getSerializableExtra(CHAVE_PERSONAGEM);
            preencheCampos();
        }
        else
        {
            setTitle(TITULO_APPBAR_NEW_PERSONAGEM);
            personagem = new Personagem();
        }
    }

    // Preenche os valores, nome altura e nascimento que estão presentes no personagem
    private void preencheCampos()
    {
        campoNome.setText(personagem.getNome());
        campoAltura.setText(personagem.getAltura());
        campoNascimento.setText(personagem.getNascimento());
    }

    // se o id for valido -> existir ele edita, se for novo ele salva.
    private void finalizarFormulario()
    {
        preencherPersonagem();
        if(personagem.idValido())
        {
            dao.edita(personagem);
            finish();
        }
        else
        {
            dao.salva(personagem);
        }
        finish();
    }

    // referencia os campos no xml e coloca uma máscara para cada campo inputável pelo usuário.
    private void inicializarCampos()
    {
        // pega a referência do texto inputado e atribui nas variaveis
        campoNome = findViewById(R.id.editText_nome);
        campoNascimento = findViewById(R.id.editText_nascimento);
        campoAltura = findViewById(R.id.editText_altura);
        // mascara para a altura
        SimpleMaskFormatter smfAltura =  new SimpleMaskFormatter("N, NN");
        MaskTextWatcher mtwAltura = new MaskTextWatcher(campoAltura, smfAltura);
        campoAltura.addTextChangedListener(mtwAltura);
        // mascara para a data
        SimpleMaskFormatter smfNascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwNascimento = new MaskTextWatcher(campoNascimento, smfNascimento);
        campoNascimento.addTextChangedListener(mtwNascimento);
    }

    // atribui as variáveis inputadas pelo usuário no personagem.
    private void preencherPersonagem()
    {
        // pega referencia dos campos inputados e preenche nas variaveis
        String nome = campoNome.getText().toString();
        String nascimento = campoNascimento.getText().toString();
        String altura = campoAltura.getText().toString();
        // atribui as variaveis nos personagens, preenchendo eles.
        personagem.setNome(nome);
        personagem.setAltura(altura);
        personagem.setNascimento(nascimento);
    }
}
