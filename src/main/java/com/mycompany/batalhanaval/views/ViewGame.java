package com.mycompany.batalhanaval.views;

import com.mycompany.batalhanaval.models.Colors;
import com.mycompany.batalhanaval.models.Consts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViewGame extends JFrame {
    private int[][] matrizLocal;
    private int[][] matrizCliente;
    private JButton[][] buttonsLocal;
    private JButton[][] buttonsOponente;
    private boolean isLocalTurn = true; // Controla de quem é a vez


    public ViewGame(int[][] matrizLocal, int[][] matrizCliente) {
        this.matrizLocal = matrizLocal;
        this.matrizCliente = matrizCliente;
        initComponents();
    }

    private void initComponents() {
        setTitle("Nova Tela");
        setLayout(new BorderLayout());
        setSize(800, 450);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Painel para matrizes
        JPanel panelMatrizes = new JPanel(new GridLayout(1, 3)); // 3 colunas para a separação

        // Matriz Local
        JPanel panelLocal = createMatrizPanel(matrizLocal, "Sua Matriz");
        panelMatrizes.add(panelLocal);

        // Painel de separação
        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(10, 0)); // Largura do separador
        panelMatrizes.add(separator);

        // Matriz Oponente
        JPanel panelOponente = createMatrizPanel(matrizCliente, "Matriz Oponente");
        panelMatrizes.add(panelOponente);

        add(panelMatrizes, BorderLayout.CENTER);

        // Painel de botões
        JPanel panelBotoes = new JPanel();
        JButton btnAtirar = new JButton("Atirar");
        JButton btnEncerrar = new JButton("Encerrar");

        btnAtirar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isLocalTurn) {
                    // Lógica para atirar
                    disableMatriz(buttonsLocal);
                    // Chamar função para atualizar matriz
                    // updateMatriz(coordenada);
                    isLocalTurn = false; // Passa a vez
                } else {
                    JOptionPane.showMessageDialog(ViewGame.this, "É a vez do oponente!");
                }
            }
        });

        btnEncerrar.addActionListener(e -> {
            // Lógica para encerrar o jogo
            dispose();
        });

        panelBotoes.add(btnAtirar);
        panelBotoes.add(btnEncerrar);
        add(panelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createMatrizPanel(int[][] matriz, String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        // Criação da matriz visual
        JPanel matrizPanel = new JPanel(new GridLayout(matriz.length, matriz[0].length));
        JButton[][] buttons = new JButton[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                JButton btn = new JButton();
                btn.setBackground(getColorForCell(matriz[i][j]));
                btn.setBorderPainted(false);
                btn.setOpaque(true);
                final int x = i, y = j;

                // Adiciona ação ao botão
                btn.addActionListener(e -> {
                    if (!isLocalTurn) return; // Não permite clicar se não for a vez
                    // Aqui você pode chamar a função para atirar
                    // updateMatriz(x, y);
                });

                buttons[i][j] = btn;
                matrizPanel.add(btn);
            }
        }
        buttonsLocal = buttons; // Salva a referência
        panel.add(matrizPanel, BorderLayout.CENTER);
        return panel;
    }

    private Color getColorForCell(int value) {
        switch (value) {
            case Consts.AGUA:
                return Colors.COR_AGUA;
            case Consts.BARCO_PEQUENO:
            case Consts.BARCO_MEDIO:
            case Consts.BARCO_GRANDE:
                return Colors.selectColor(value);
            default:
                return Color.GRAY; // Cor padrão para células não definidas
        }
    }

    private void disableMatriz(JButton[][] buttons) {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                btn.setEnabled(false); // Desativa todos os botões
            }
        }
    }

    // Método para atualizar a matriz visual
    public void updateMatriz(int x, int y) {
        // Atualiza a matriz visual com base na coordenada
        // Exemplo de mudança de cor:
        buttonsOponente[x][y].setBackground(Color.RED); // Muda a cor para indicar um ataque
        // Você pode adicionar mais lógica aqui para atualizar a matriz
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(()-> new ViewGame(createMatriz(),createMatriz()));
    }
    private static int[][] createMatriz(){
        int [][]m1 = new int[Consts.GRID_MATRIZ][Consts.GRID_MATRIZ];
        for (int i = 0; i < Consts.GRID_MATRIZ; i++) {
            for (int j = 0; j < Consts.GRID_MATRIZ; j++) {
                m1[i][j] = Consts.AGUA;
            }

        }
        return m1;
    }
}
