import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Guarda a matriz do jogo, sorteia a posição das embarcações
 * e controla os tiros, as partes restantes e os tiros restantes.
 */
public class Tabuleiro {

    // Ordem dos tipos usada nos vetores de quantidade:
    // 0=Submarino, 1=Destroier, 2=Cruzador, 3=Encouraçado, 4=Porta-aviões
    private static final String[] NOMES = { "Submarino", "Destroier", "Cruzador", "Encouraçado", "Porta-aviões" };
    private static final int[] TAMANHOS = { 1, 2, 3, 4, 5 };
    private static final String[] IMAGENS = { "submarino", "destroier", "cruzador", "encouracado", "porta_avioes" };

    private final int tamanho;
    private final Embarcacao[][] grade;   // null = água
    private final boolean[][] atirado;    // casas que já receberam tiro
    private final List<Embarcacao> embarcacoes = new ArrayList<>();
    private final Random sorteio = new Random();

    private int tirosRestantes;
    private int partesRestantes;          // quando chega a 0, o jogador ganhou
    private int embarcacoesRestantes;

    /**
     * @param tamanho  lado da matriz (6 ou 8)
     * @param quantidades  quantos navios de cada tipo (mesma ordem de NOMES)
     * @param tiros    total de tiros disponíveis
     */
    public Tabuleiro(int tamanho, int[] quantidades, int tiros) {
        this.tamanho = tamanho;
        this.tirosRestantes = tiros;
        this.grade = new Embarcacao[tamanho][tamanho];
        this.atirado = new boolean[tamanho][tamanho];

        // Cria os objetos (do maior para o menor, fica mais fácil de encaixar)
        for (int tipo = NOMES.length - 1; tipo >= 0; tipo--) {
            for (int i = 0; i < quantidades[tipo]; i++) {
                embarcacoes.add(new Embarcacao(NOMES[tipo], TAMANHOS[tipo], IMAGENS[tipo]));
                partesRestantes += TAMANHOS[tipo];
            }
        }
        embarcacoesRestantes = embarcacoes.size();

        posicionarTodas();
    }

    /** Sorteia linha, coluna e direção de cada navio até encontrar um lugar livre. */
    private void posicionarTodas() {
        for (Embarcacao e : embarcacoes) {
            boolean colocou = false;
            while (!colocou) {
                int linha = sorteio.nextInt(tamanho);
                int coluna = sorteio.nextInt(tamanho);
                boolean horizontal = sorteio.nextBoolean();
                if (cabe(e.getTamanho(), linha, coluna, horizontal)) {
                    e.setHorizontal(horizontal);
                    for (int i = 0; i < e.getTamanho(); i++) {
                        int l = horizontal ? linha : linha + i;
                        int c = horizontal ? coluna + i : coluna;
                        grade[l][c] = e;
                        e.adicionarPosicao(l, c);
                    }
                    colocou = true;
                }
            }
        }
    }

    /** Verifica se o navio cabe dentro do tabuleiro e se as casas estão livres. */
    private boolean cabe(int tam, int linha, int coluna, boolean horizontal) {
        for (int i = 0; i < tam; i++) {
            int l = horizontal ? linha : linha + i;
            int c = horizontal ? coluna + i : coluna;
            if (l >= tamanho || c >= tamanho || grade[l][c] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Efetua o tiro. Gasta um tiro e devolve a embarcação atingida,
     * ou null se o tiro caiu na água.
     */
    public Embarcacao atirar(int linha, int coluna) {
        atirado[linha][coluna] = true;
        tirosRestantes--;

        Embarcacao e = grade[linha][coluna];
        if (e != null) {
            e.acertar();
            partesRestantes--;
            if (e.estaAfundada()) {
                embarcacoesRestantes--;
            }
        }
        return e;
    }

    public boolean jaAtirou(int linha, int coluna) { return atirado[linha][coluna]; }
    public boolean ganhou() { return partesRestantes == 0; }
    public boolean perdeu() { return tirosRestantes <= 0 && partesRestantes > 0; }

    public List<Embarcacao> getEmbarcacoes() { return embarcacoes; }
    public int getTamanho() { return tamanho; }
    public int getTirosRestantes() { return tirosRestantes; }
    public int getPartesRestantes() { return partesRestantes; }
    public int getEmbarcacoesRestantes() { return embarcacoesRestantes; }
}
