import java.util.ArrayList;
import java.util.List;

/**
 * Representa UMA embarcação do tabuleiro.
 * Cada navio é um objeto próprio, então cada um tem a sua "barra de acertos"
 * (dois destroieres nunca compartilham vida).
 */
public class Embarcacao {

    private final String nome;
    private final int tamanho;          // quantidade de casas = vida total
    private int acertos = 0;            // quantas partes já foram atingidas
    private final String arquivoImagem; // imagem exibida quando o navio afunda
    private boolean horizontal = true;   // direção sorteada
    private final List<int[]> posicoes = new ArrayList<>(); // {linha, coluna}

    public Embarcacao(String nome, int tamanho, String arquivoImagem) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.arquivoImagem = arquivoImagem;
    }

    public void adicionarPosicao(int linha, int coluna) {
        posicoes.add(new int[] { linha, coluna });
    }

    /** Registra um acerto nesta embarcação. */
    public void acertar() {
        acertos++;
    }

    public boolean estaAfundada() {
        return acertos >= tamanho;
    }

    public void setHorizontal(boolean h) { this.horizontal = h; }
    public boolean isHorizontal() { return horizontal; }
    public String getNome() { return nome; }
    public int getTamanho() { return tamanho; }
    public int getAcertos() { return acertos; }
    public String getArquivoImagem() { return arquivoImagem; }
    public List<int[]> getPosicoes() { return posicoes; }
}
