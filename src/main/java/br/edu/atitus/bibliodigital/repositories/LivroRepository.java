package br.edu.atitus.bibliodigital.repositories;

import java.util.ArrayList;
import java.util.List;

import br.edu.atitus.bibliodigital.entities.Livro;

public class LivroRepository {

    private final List<Livro> acervo = new ArrayList<>();

    public void inserir(Livro livro) {
        acervo.add(livro);
    }
    public List<Livro> findAll() {
        return new ArrayList<>(acervo);
    }

    public int count() {
        return acervo.size();
    }

    public Livro findByIndex(int index) {
        return acervo.get(index);
    }
    public Livro findByTituloExato(String titulo) {
        for (Livro livro : acervo) {
            if (livro.getTitulo().equalsIgnoreCase(titulo)) {
                return livro;
            }
        }
        return null;
    }

    public List<Livro> findByTitulo(String parametro) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : acervo) {
            if (livro.getTitulo().toUpperCase().contains(parametro.toUpperCase())) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    public List<Livro> findByAutor(String parametro) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : acervo) {
            if (livro.getAutor().toUpperCase().contains(parametro.toUpperCase())) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    public void alterar(int index, Livro livro) {
        acervo.set(index, livro);
    }

    public void removeByIndex(int index) {
        acervo.remove(index);
    }
}