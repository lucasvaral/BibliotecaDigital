package br.edu.atitus.bibliodigital.services;

import java.util.List;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.repositories.LivroRepository;

public class LivroService {

    private static final int ANO_MINIMO = 1900;

    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public void inserir(Livro livro) {
        validarDados(livro);
        validarTituloUnico(livro.getTitulo());
        repository.inserir(livro);
    }

    public List<Livro> findAll() {
        return repository.findAll();
    }

    public Livro findByIndex(int index) {
        validarIndice(index);
        return repository.findByIndex(index);
    }

    public List<Livro> findByTitulo(String parametro) {
        validarParametroPesquisa(parametro);
        return repository.findByTitulo(parametro.trim());
    }

    public List<Livro> findByAutor(String parametro) {
        validarParametroPesquisa(parametro);
        return repository.findByAutor(parametro.trim());
    }

    public void alterar(int index, Livro livro) {
        validarIndice(index);
        validarDados(livro);

        Livro atual = repository.findByIndex(index);
        boolean mudouTitulo = !atual.getTitulo().equalsIgnoreCase(livro.getTitulo());
        if (mudouTitulo) {
            validarTituloUnico(livro.getTitulo());
        }

        repository.alterar(index, livro);
    }

    public void excluir(int index) {
        validarIndice(index);
        repository.removeByIndex(index);
    }

    private void validarDados(Livro livro) {
        if (livro == null) {
            throw new RuntimeException("Livro inválido.");
        }
        if (livro.getTitulo() == null || livro.getTitulo().isBlank()) {
            throw new RuntimeException("O título não pode ser vazio.");
        }
        if (livro.getAutor() == null || livro.getAutor().isBlank()) {
            throw new RuntimeException("O autor não pode ser vazio.");
        }
        if (livro.getAnoPublicacao() < ANO_MINIMO) {
            throw new RuntimeException("O ano de publicação deve ser igual ou superior a " + ANO_MINIMO + ".");
        }

        limparTextos(livro);
    }

    private void limparTextos(Livro livro) {
        livro.setTitulo(livro.getTitulo().trim());
        livro.setAutor(livro.getAutor().trim());

        if (livro instanceof LivroFisico) {
            LivroFisico fisico = (LivroFisico) livro;
            fisico.setLocalizacao(fisico.getLocalizacao().trim());
        } else if (livro instanceof LivroDigital) {
            LivroDigital digital = (LivroDigital) livro;
            digital.setFormato(digital.getFormato().trim());
        } else if (livro instanceof AudioLivro) {
            AudioLivro audio = (AudioLivro) livro;
            audio.setNarrador(audio.getNarrador().trim());
        }
    }

    private void validarTituloUnico(String titulo) {
        if (repository.findByTituloExato(titulo) != null) {
            throw new RuntimeException("Já existe um livro cadastrado com esse título.");
        }
    }

    private void validarIndice(int index) {
        if (repository.count() == 0) {
            throw new RuntimeException("O acervo está vazio.");
        }
        if (index < 0 || index >= repository.count()) {
            throw new RuntimeException("Índice inválido.");
        }
    }

    private void validarParametroPesquisa(String parametro) {
        if (parametro == null || parametro.isBlank()) {
            throw new RuntimeException("Informe um texto para a pesquisa.");
        }
    }
}