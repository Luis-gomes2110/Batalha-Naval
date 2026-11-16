import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/** Janela do jogo: título, grade de botões com imagens e contador de tiros. */
public class TelaJogo extends JFrame {

    private static final Color FUNDO = new Color(45, 100, 150);

    private final Tabuleiro tabuleiro;
    private final JButton[][] botoes;
    private final JLabel lblTiros = new JLabel();
    private final JLabel lblNavios = new JLabel();
    private final int celula; // tamanho de cada casa em pixels

    private final ImageIcon iconeOculto;
    private final ImageIcon iconeAgua;
    private final ImageIcon iconeAcerto;

    public TelaJogo(String nivel, Tabuleiro tabuleiro) {
        super("Batalha Naval - Nível " + nivel);
        this.tabuleiro = tabuleiro;
        int n = tabuleiro.getTamanho();
        this.botoes = new JButton[n][n];
        this.celula = 480 / n; // 6x6 -> 80px | 8x8 -> 60px

        iconeOculto = carregar("oculto", false);
        iconeAgua = carregar("agua", true);   // pixel art: sem suavizar
        iconeAcerto = carregar("acerto", false);

        JPanel raiz = new JPanel();
        raiz.setLayout(new BoxLayout(raiz, BoxLayout.Y_AXIS));
        raiz.setBackground(FUNDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JLabel titulo = new JLabel("Batalha Naval");
        titulo.setFont(new Font("Arial", Font.BOLD, 38));
        titulo.setForeground(Color.WHITE);
        titulo.setAlignmentX(LEFT_ALIGNMENT);

        JPanel grade = new JPanel(new GridLayout(n, n));
        grade.setPreferredSize(new Dimension(celula * n, celula * n));
        grade.setMaximumSize(new Dimension(celula * n, celula * n));
        grade.setAlignmentX(LEFT_ALIGNMENT);

        for (int l = 0; l < n; l++) {
            for (int c = 0; c < n; c++) {
                JButton b = new JButton(iconeOculto);
                b.setDisabledIcon(iconeOculto); // não fica cinza ao desabilitar
                b.setMargin(new java.awt.Insets(0, 0, 0, 0));
                b.setBorder(BorderFactory.createLineBorder(new Color(8, 30, 60)));
                b.setFocusPainted(false);
                final int linha = l, coluna = c;
                b.addActionListener(e -> clicou(linha, coluna));
                botoes[l][c] = b;
                grade.add(b);
            }
        }

        lblTiros.setFont(new Font("Arial", Font.BOLD, 34));
        lblTiros.setForeground(Color.WHITE);
        lblTiros.setAlignmentX(LEFT_ALIGNMENT);
        lblNavios.setFont(new Font("Arial", Font.PLAIN, 18));
        lblNavios.setForeground(Color.WHITE);
        lblNavios.setAlignmentX(LEFT_ALIGNMENT);

        raiz.add(titulo);
        raiz.add(javax.swing.Box.createVerticalStrut(20));
        raiz.add(grade);
        raiz.add(javax.swing.Box.createVerticalStrut(15));
        raiz.add(lblTiros);
        raiz.add(lblNavios);

        atualizarPlacar();

        setContentPane(raiz);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** Chamado quando o jogador clica em uma casa. */
    private void clicou(int linha, int coluna) {
        if (tabuleiro.jaAtirou(linha, coluna)) {
            return;
        }

        Embarcacao e = tabuleiro.atirar(linha, coluna);
        JButton b = botoes[linha][coluna];
        b.setEnabled(false);

        String mensagem;
        if (e == null) {
            fixarIcone(b, iconeAgua);
            mensagem = "Tiro na água!";
        } else {
            fixarIcone(b, iconeAcerto);
            if (e.estaAfundada()) {
                mostrarNavioAfundado(e);
                mensagem = "Acertou e AFUNDOU o " + e.getNome() + "!";
            } else {
                mensagem = "Acertou o " + e.getNome() + "! ("
                        + e.getAcertos() + "/" + e.getTamanho() + ")";
            }
        }

        atualizarPlacar();
        ((javax.swing.JComponent) getContentPane()).paintImmediately(0, 0, getWidth(), getHeight());
        JOptionPane.showMessageDialog(this, mensagem, "Resultado do tiro",
                JOptionPane.INFORMATION_MESSAGE);

        if (tabuleiro.ganhou()) {
            fimDeJogo("Parabéns, você VENCEU!\nTodas as embarcações foram afundadas.");
        } else if (tabuleiro.perdeu()) {
            revelarTudo();
            fimDeJogo("Seus tiros acabaram. Você PERDEU!\nAinda restavam "
                    + tabuleiro.getEmbarcacoesRestantes() + " embarcação(ões).");
        }
    }

    /** Troca a explosão pela imagem do navio em todas as casas dele. */
    private void mostrarNavioAfundado(Embarcacao e) {
        mostrarNavio(e, false);
    }

    /** Ao perder, mostra onde estavam as embarcações que sobraram. */
    private void revelarTudo() {
        for (Embarcacao e : tabuleiro.getEmbarcacoes()) {
            if (!e.estaAfundada()) {
                mostrarNavio(e, true);
            }
        }
    }

    /**
     * A imagem do navio (imagens/NOME.png) é uma faixa com uma fatia por casa.
     * Cada fatia vai para a casa correspondente; se o navio está na vertical, a fatia é girada.
     * @param soNaoAtingidas true = só mexe nas casas que ainda não receberam tiro
     */
    private void mostrarNavio(Embarcacao e, boolean soNaoAtingidas) {
        BufferedImage faixa;
        try {
            faixa = ImageIO.read(new File("imagens/" + e.getArquivoImagem() + ".png"));
        } catch (Exception ex) {
            return; // sem imagem: mantém a explosão
        }
        int n = e.getTamanho();
        int largura = faixa.getWidth() / n;
        int altura = faixa.getHeight();

        for (int i = 0; i < n; i++) {
            int[] p = e.getPosicoes().get(i);
            if (soNaoAtingidas && tabuleiro.jaAtirou(p[0], p[1])) {
                continue;
            }
            BufferedImage fatia = faixa.getSubimage(i * largura, 0, largura, altura);

            BufferedImage casa = new BufferedImage(celula, celula, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = casa.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setColor(new Color(160, 215, 245)); // cor da água atrás do navio
            g.fillRect(0, 0, celula, celula);
            if (!e.isHorizontal()) {
                g.translate(celula / 2.0, celula / 2.0);
                g.rotate(Math.PI / 2);
                g.drawImage(fatia, -celula / 2, -celula / 2, celula, celula, null);
            } else {
                g.drawImage(fatia, 0, 0, celula, celula, null);
            }
            g.dispose();
            fixarIcone(botoes[p[0]][p[1]], new ImageIcon(casa));
        }
    }

    private void fimDeJogo(String mensagem) {
        int n = tabuleiro.getTamanho();
        for (int l = 0; l < n; l++) {
            for (int c = 0; c < n; c++) {
                botoes[l][c].setEnabled(false);
            }
        }
        int op = JOptionPane.showConfirmDialog(this, mensagem + "\n\nDeseja jogar novamente?",
                "Fim de jogo", JOptionPane.YES_NO_OPTION);
        dispose();
        if (op == JOptionPane.YES_OPTION) {
            Principal.novoJogo();
        } else {
            System.exit(0);
        }
    }

    private void atualizarPlacar() {
        lblTiros.setText("Tiros: " + tabuleiro.getTirosRestantes());
        lblNavios.setText("Embarcações restantes: " + tabuleiro.getEmbarcacoesRestantes());
    }

    /** Define o ícone mesmo com o botão desabilitado (senão ele fica cinza). */
    private void fixarIcone(JButton b, ImageIcon icone) {
        b.setIcon(icone);
        b.setDisabledIcon(icone);
    }

    /**
     * Carrega imagens/NOME.png e ajusta para o tamanho da casa.
     * @param pixelado true = mantém os pixels nítidos (pixel art); false = suaviza.
     * Se o arquivo não existir, devolve um quadrado azul para o jogo não quebrar.
     */
    private ImageIcon carregar(String nome, boolean pixelado) {
        BufferedImage casa = new BufferedImage(celula, celula, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = casa.createGraphics();
        try {
            BufferedImage original = ImageIO.read(new File("imagens/" + nome + ".png"));
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, pixelado
                    ? RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
                    : RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(original, 0, 0, celula, celula, null);
        } catch (Exception ex) {
            System.err.println("Imagem não encontrada: imagens/" + nome + ".png");
            g.setColor(new Color(40, 110, 180));
            g.fillRect(0, 0, celula, celula);
        }
        g.dispose();
        return new ImageIcon(casa);
    }
}
