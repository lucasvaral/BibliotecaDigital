package br.edu.atitus.bibliodigital.views;

import java.util.List;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.services.LivroService;
import br.edu.atitus.bibliodigital.repositories.LivroRepository;

public class BiblioDigitalApplication {

    private final LivroService service = new LivroService(new LivroRepository());
    void main() {
        IO.println("===== Bem vindo ao BiblioDigital =====");

        int opcao = -1;
        do {
            mostrarMenu();
            try {
                opcao = lerInteiro("Escolha uma das opções acima: ");

                switch (opcao) {
                    case 1 -> cadastrarLivro();
                    case 2 -> pesquisarPorTitulo();
                    case 3 -> pesquisarPorAutor();
                    case 4 -> alterarLivro();
                    case 5 -> excluirLivro();
                    case 0 -> IO.println("Até logo!");
                    default -> IO.println("Opção inválida! Tente novamente.");
                }
            } catch (RuntimeException e) {
                IO.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    void mostrarMenu() {
        IO.println();
        IO.println("Menu:");
        IO.println("1 - Cadastrar novo Livro");
        IO.println("2 - Pesquisar Livro por título");
        IO.println("3 - Pesquisar Livro por autor");
        IO.println("4 - Alterar dados do Livro");
        IO.println("5 - Excluir Livro");
        IO.println("0 - Sair");
        IO.println();
    }

    void cadastrarLivro() {
        IO.println("Qual tipo de livro deseja cadastrar?");
        IO.println("1 - Livro físico");
        IO.println("2 - Livro digital");
        IO.println("3 - Audiolivro");
        int tipo = lerInteiro("Escolha uma opção: ");

        if (tipo < 1 || tipo > 3) {
            IO.println("Opção de tipo de livro inválida!");
            return;
        }

        Livro livro = lerDadosLivro(tipo);
        service.inserir(livro);
        IO.println("Livro cadastrado com sucesso!");
    }

    
    Livro lerDadosLivro(int tipo) {
        String titulo = IO.readln("Digite o título: ");
        String autor = IO.readln("Digite o autor: ");
        int ano = lerInteiro("Digite o ano de publicação: ");

        if (tipo == 1) {
            int exemplares = lerInteiro("Digite a quantidade de exemplares: ");
            String localizacao = IO.readln("Digite a localização na biblioteca: ");
            return new LivroFisico(titulo, autor, ano, exemplares, localizacao);
        } else if (tipo == 2) {
            String formato = IO.readln("Digite o formato do arquivo: ");
            double tamanho = lerDecimal("Digite o tamanho do arquivo em MB: ");
            return new LivroDigital(titulo, autor, ano, formato, tamanho);
        } else {
            int duracao = lerInteiro("Digite a duração em minutos: ");
            String narrador = IO.readln("Digite o nome do narrador: ");
            return new AudioLivro(titulo, autor, ano, duracao, narrador);
        }
    }

    void pesquisarPorTitulo() {
        String pesquisa = IO.readln("Pesquisa por título: ");
        List<Livro> encontrados = service.findByTitulo(pesquisa);
        exibirResultadoPesquisa(encontrados);
    }

    void pesquisarPorAutor() {
        String pesquisa = IO.readln("Pesquisa por autor: ");
        List<Livro> encontrados = service.findByAutor(pesquisa);
        exibirResultadoPesquisa(encontrados);
    }

    void exibirResultadoPesquisa(List<Livro> encontrados) {
        if (encontrados.isEmpty()) {
            IO.println("Nenhum livro encontrado para a pesquisa realizada.");
            return;
        }

        IO.println("===== Resultado da pesquisa =====");
        for (Livro livro : encontrados) {
            IO.println(livro);
        }
    }

    void alterarLivro() {
        if (!listarLivrosComIndice()) {
            return;
        }

        int index = lerIndice("Informe o índice do livro que deseja alterar: ");
        Livro atual = service.findByIndex(index);

        IO.println();
        IO.println("Dados atuais:");
        IO.println(atual);
        IO.println();
        IO.println("Informe os novos dados:");

        
        Livro novo = lerDadosLivro(identificarTipo(atual));
        service.alterar(index, novo);
        IO.println("Livro alterado com sucesso!");
    }

    void excluirLivro() {
        if (!listarLivrosComIndice()) {
            return;
        }

        int index = lerIndice("Informe o índice do livro que deseja excluir: ");
        Livro livro = service.findByIndex(index);

        String confirmacao = IO.readln("Deseja realmente excluir \"" + livro.getTitulo() + "\"? (S/N): ").trim();
        if (confirmacao.equalsIgnoreCase("S")) {
            service.excluir(index);
            IO.println("Livro excluído com sucesso!");
        } else {
            IO.println("Exclusão cancelada.");
        }
    }

    
    boolean listarLivrosComIndice() {
        List<Livro> livros = service.findAll();
        if (livros.isEmpty()) {
            IO.println("O acervo está vazio.");
            return false;
        }

        IO.println("===== Livros cadastrados =====");
        for (int i = 0; i < livros.size(); i++) {
            IO.println((i + 1) + " - " + livros.get(i).getTitulo());
        }
        IO.println();
        return true;
    }
    int lerIndice(String mensagem) {
        return lerInteiro(mensagem) - 1;
    }
    int lerInteiro(String mensagem) {
        try {
            return Integer.parseInt(IO.readln(mensagem).trim());
        } catch (RuntimeException e) {
            throw new RuntimeException("Informe um valor numérico válido.");
        }
    }
    double lerDecimal(String mensagem) {
        try {
            return Double.parseDouble(IO.readln(mensagem).trim());
        } catch (RuntimeException e) {
            throw new RuntimeException("Informe um valor numérico válido.");
        }
    }

    int identificarTipo(Livro livro) {
        if (livro instanceof LivroFisico) {
            return 1;
        } else if (livro instanceof LivroDigital) {
            return 2;
        }
        return 3;
    }
}