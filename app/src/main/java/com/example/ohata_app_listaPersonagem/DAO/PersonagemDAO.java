package com.example.ohata_app_listaPersonagem.DAO;

import com.example.ohata_app_listaPersonagem.model.Personagem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// Salva personagens criados pelo usuário
public class PersonagemDAO
{
    private final static List<Personagem> personagens = new ArrayList<>();
    private static int contadorDeIds = 1;

    // Salva os personagens na lista e atualiza
    public void salva(Personagem personagemSalvo)
    {
        personagemSalvo.setId(contadorDeIds);
        personagens.add(personagemSalvo);
        atualizaId();
    }

    private void atualizaId()
    {
        contadorDeIds++;
    }

    // se sofrer alguma alteração o metodo edita reescreve e da um set nos novos valores
    public void edita(Personagem personagem)
    {
        Personagem personagemEncontrado = buscaPersonagemId(personagem);
        if(personagemEncontrado != null)
        {
            int posicaoDoPersonagem = personagens.indexOf(personagemEncontrado);
            personagens.set(posicaoDoPersonagem, personagem);
        }
    }

    // retorna um personagem da lista
    private Personagem buscaPersonagemId(Personagem personagem)
    {
        for(Personagem p : personagens)
        {
            if(p.getId() == personagem.getId())
            {
                return p;
            }
        }
        return null;
    }
    // retorna o novo array criado a partir da lista personagem
    public List<Personagem> todos()
    {
        return new ArrayList<>(personagens);
    }
    // remove os personagens da lista
    public void remove(Personagem personagem)
    {
        Personagem personagemDevolvido = buscaPersonagemId(personagem);
        if(personagemDevolvido != null)
        {
            personagens.remove(personagemDevolvido);
        }
    }
}
