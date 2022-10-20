package br.com.mauricioborges.graficos;

import br.com.mauricioborges.graficos.gui.CenaGraficoController;
import br.com.mauricioborges.graficos.math.Funcao;
import static br.com.mauricioborges.graficos.utils.FXUtils.findResource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Representa um gráfico. Exemplo de uso:
 *
 * <pre><code>
 * Funcao f = x -> Math.sin(x);
 * Grafico g = new Grafico();
 * g.plotFuncao(f, 0, 2*Math.PI, "Seno");
 * g.show(stage);
 * </code></pre>
 *
 * @author Mauricio Borges
 * @since 2018
 */
public final class Grafico extends Application {

    private CenaGraficoController controle;
    private String tituloJanela = "Gráficos JavaFX";
    private String tituloGrafico = null;
    private String tituloEixoX = null;
    private String tituloEixoY = null;
    // Funcoes
    private final List<Funcao> funcoes = new ArrayList<>();
    private final List<Double> inicio = new ArrayList<>();
    private final List<Double> fim = new ArrayList<>();
    private final List<String> tituloFuncoes = new ArrayList<>();
    private final List<Estilo> estiloFuncoes = new ArrayList<>();
    // Pontos
    private final List<Double[]> x = new ArrayList<>();
    private final List<Double[]> y = new ArrayList<>();
    private final List<String> tituloPontos = new ArrayList<>();
    private final List<Estilo> estiloPontos = new ArrayList<>();
    private final List<LinhaDeTendencia[]> linhasDeTendencia = new ArrayList<>();

    /**
     * Definir o título da janela
     *
     * @param tituloJanela título da janela
     */
    public void setTituloJanela(String tituloJanela) {
        this.tituloJanela = tituloJanela;
    }

    /**
     * Definir o título do gráfico
     *
     * @param tituloGrafico título do gráfico
     */
    public void setTituloGrafico(String tituloGrafico) {
        this.tituloGrafico = tituloGrafico;
    }

    /**
     * Definir o título do eixo X
     *
     * @param tituloEixoX título do eixo X
     */
    public void setTituloEixoX(String tituloEixoX) {
        this.tituloEixoX = tituloEixoX;
    }

    /**
     * Definir o título do eixo Y
     *
     * @param tituloEixoY título do eixo Y
     */
    public void setTituloEixoY(String tituloEixoY) {
        this.tituloEixoY = tituloEixoY;
    }

    /**
     * Plotar função em determinado intervalo
     *
     * @param funcao função
     * @param inicio início do intervalo
     * @param fim fim do intervalo
     * @param titulo legenda do gráfico
     * @param estilo opções de estilo
     */
    public void plotFuncao(Funcao funcao, double inicio, double fim, String titulo, Estilo estilo) {
        Objects.requireNonNull(funcao, "A função não pode ser nula.");
        Objects.requireNonNull(estilo, "O estilo da função não pode ser nulo.");
        this.funcoes.add(funcao);
        this.inicio.add(inicio);
        this.fim.add(fim);
        this.tituloFuncoes.add(titulo);
        this.estiloFuncoes.add(estilo);

        if (controle != null) {
            controle.plotFuncao(funcao, inicio, fim, titulo, estilo);
        }
    }

    /**
     * Plotar função em determinado intervalo com o estilo padrão
     *
     * @param funcao função
     * @param inicio início do intervalo
     * @param fim fim do intervalo
     * @param titulo legenda do gráfico
     */
    public void plotFuncao(Funcao funcao, double inicio, double fim, String titulo) {
        this.plotFuncao(funcao, inicio, fim, titulo, Estilo.LINHA);
    }

    /**
     * Plotar um conjunto de pontos
     *
     * @param x array com os valores de X
     * @param y array com os valores de Y
     * @param titulo legenda do gráfico
     * @param estilo opções de estilo
     * @param linhasDeTendencia linhas de tendência
     */
    public void plotPontos(Double[] x, Double[] y, String titulo, Estilo estilo, LinhaDeTendencia... linhasDeTendencia) {
        if (x.length != y.length) {
            throw new UnsupportedOperationException("Os arrays de X e Y devem ter o mesmo tamanho.");
        }
        Objects.requireNonNull(estilo, "O estilo não pode ser nulo.");
        this.x.add(x);
        this.y.add(y);
        this.tituloPontos.add(titulo);
        this.estiloPontos.add(estilo);
        this.linhasDeTendencia.add(linhasDeTendencia);

        if (controle != null) {
            controle.plotPontos(x, y, titulo, estilo, linhasDeTendencia);
        }
    }

    /**
     * Plotar um conjunto de pontos com o estilo padrão (com linha e marcador) e
     * sem linha de tendência
     *
     * @param x array com os valores de X
     * @param y array com os valores de Y
     * @param titulo legenda do gráfico
     */
    public void plotPontos(Double[] x, Double[] y, String titulo) {
        this.plotPontos(x, y, titulo, Estilo.LINHA_E_MARCADOR);
    }

    /**
     * Plotar um conjunto de pontos sem linha de tendência
     *
     * @param x array com os valores de X
     * @param y array com os valores de Y
     * @param titulo legenda do gráfico
     * @param estilo opções de estilo
     */
    public void plotPontos(Double[] x, Double[] y, String titulo, Estilo estilo) {
        this.plotPontos(x, y, titulo, estilo, (LinhaDeTendencia) null);
    }

    /**
     * Plotar um conjunto de pontos com o estilo padrão (com linha e marcador)
     *
     * @param x array com os valores de X
     * @param y array com os valores de Y
     * @param titulo legenda do gráfico
     * @param linhasDeTendencia linhas de tendência
     */
    public void plotPontos(Double[] x, Double[] y, String titulo, LinhaDeTendencia... linhasDeTendencia) {
        this.plotPontos(x, y, titulo, Estilo.LINHA_E_MARCADOR, linhasDeTendencia);
    }

    /**
     * Exibir o gráfico.<br>
     * Mesmo que chamar o método <code>start(Stage janela)</code>
     *
     * @param janela janela onde o gráfico será exibido
     */
    public final void show(Stage janela) {
        if (controle == null) {
            this.start(janela);
            return;
        }
        // plotando os gráficos
        for (int i = 0; i < funcoes.size(); i++) {
            controle.plotFuncao(funcoes.get(i), inicio.get(i), fim.get(i), tituloFuncoes.get(i), estiloFuncoes.get(i));
        }
        for (int i = 0; i < x.size(); i++) {
            controle.plotPontos(x.get(i), y.get(i), tituloPontos.get(i), estiloPontos.get(i), linhasDeTendencia.get(i));
        }
    }

    /**
     * Exibir o gráfico.<br>
     * Mesmo que chamar o método <code>show(Stage janela)</code>
     *
     * @param janela janela onde o gráfico será exibido
     */
    @Override
    public final void start(Stage janela) {
        Objects.requireNonNull(janela, "A janela não pode ser nula.");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(findResource("gui/CenaGrafico.fxml"));
        Parent root;
        try {
            root = loader.load();
            controle = loader.getController();
            Scene scene = new Scene(root);
            janela.setTitle(tituloJanela);
            janela.setScene(scene);
        } catch (IOException ex) {
            throw new RuntimeException("Erro ao tentar iniciar a janela do gráfico.", ex);
        }

        // adicionando o icone da janela
        Image icone = new Image(findResource("gui/img/iconeJava.png").toString());
        janela.getIcons().add(icone);

        // titulo do gráfico no controller
        controle.setTituloGrafico(tituloGrafico);
        // titulo dos eixos no controller
        controle.setTituloEixos(tituloEixoX, tituloEixoY);

        // configurando e exibindo a janela
        janela.setOnCloseRequest(event -> System.exit(0));
        janela.show();

        // plotando os gráficos
        this.show(janela);
    }

}
