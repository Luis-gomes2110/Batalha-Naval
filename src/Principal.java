import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/** Classe principal: mostra a escolha de dificuldade e abre o jogo. */
public class Principal {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Principal::novoJogo);
    }

    /** Pergunta a dificuldade e cria a tela do jogo. */
    public static void novoJogo() {
        String[] opcoes = { "Fácil (6x6)", "Difícil (8x8)", "Sair" };
        int escolha = JOptionPane.showOptionDialog(null,
                "Encontre todas as embarcações com o menor número de tiros!\n"
                + "Escolha a dificuldade:",
                "Batalha Naval",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opcoes, opcoes[0]);

        // Quantidades: {Submarino, Destroier, Cruzador, Encouraçado, Porta-aviões}
        switch (escolha) {
            case 0:
                new TelaJogo("Fácil", new Tabuleiro(6, new int[] { 4, 2, 1, 1, 1 }, 30));
                break;
            case 1:
                new TelaJogo("Difícil", new Tabuleiro(8, new int[] { 6, 2, 2, 2, 1 }, 40));
                break;
            default:
                System.exit(0);
        }
    }
}
