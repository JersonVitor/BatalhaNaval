package com.mycompany.batalhanaval.views;

import com.mycompany.batalhanaval.connections.GameConnection;
import com.mycompany.batalhanaval.intefaces.GameListener;
import com.mycompany.batalhanaval.models.Colors;
import com.mycompany.batalhanaval.models.Consts;

import javax.swing.*;
import java.awt.*;

public class ViewGame extends JFrame implements GameListener {
    private int[][] matrizLocal;
    private int[][] matrizCliente;
    private JToggleButton[][] buttonsLocal;
    private JToggleButton[][] buttonsOponente;
    private ButtonGroup buttonGroup;
    private boolean isLocalTurn = true; 
    private GameConnection connection = GameConnection.getInstance();

    public ViewGame(int[][] matrizLocal, int[][] matrizCliente) {
        this.matrizLocal = matrizLocal;
        this.matrizCliente = matrizCliente;
        initComponents();
    }

    private void initComponents() {
        setTitle("Batalha Naval");
        setLayout(new BorderLayout());
        setResizable(false);
        setSize(800, 450);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // Painel para matrizes
        JPanel panelMatrizes = new JPanel(new GridLayout(1, 2, 10, 0)); // 2 colunas, espaçamento de 10

        // Matriz Local
        JPanel panelLocal = createMatrizPanel(matrizLocal, "Sua Matriz", false);
        panelMatrizes.add(panelLocal);

        // Matriz Oponente
        JPanel panelOponente = createMatrizPanel(matrizCliente, "Matriz Oponente", true);
        panelMatrizes.add(panelOponente);

        add(panelMatrizes, BorderLayout.CENTER);

        // Painel de botões
        JPanel panelBotoes = new JPanel();
        JButton btnAtirar = new JButton("Atirar");
        JButton btnEncerrar = new JButton("Encerrar Jogo");

        // Ação do botão Atirar
        btnAtirar.addActionListener(e -> {
            if (!isLocalTurn) {
                JOptionPane.showMessageDialog(this, "Não é a sua vez!");
                return;
            }

            // Obter o botão selecionado no ButtonGroup
            ButtonModel selectedButton = buttonGroup.getSelection();
            if (selectedButton == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma posição para atirar!");
                return;
            }

            // Identificar as coordenadas selecionadas
            String actionCommand = selectedButton.getActionCommand(); // Formato: "x,y"
            String[] coordinates = actionCommand.split(",");
            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);

            System.out.println("Atirando na posição: " + x + "," + y);
            buttonsOponente[x][y].setBackground(getAcertoOuErro(matrizCliente[x][y]));
            connection.clienteConnection(actionCommand);
            connection.hostConnection();
            buttonsOponente[x][y].setEnabled(false); // Desativa o botão atacado
            disableMatriz();
            isLocalTurn = false;
        });

        // Ação do botão Encerrar
        btnEncerrar.addActionListener(e -> {
            dispose(); // Fecha a janela
        });

        panelBotoes.add(btnAtirar);
        panelBotoes.add(btnEncerrar);
        add(panelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createMatrizPanel(int[][] matriz, String title, boolean isOponente) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH);

        // Criação da matriz visual
        JPanel matrizPanel = new JPanel(new GridLayout(matriz.length, matriz[0].length,2,2));
        JToggleButton[][] buttons = new JToggleButton[Consts.GRID_MATRIZ][Consts.GRID_MATRIZ];

        // Apenas a matriz do oponente usa um ButtonGroup
        if (isOponente) {
            buttonGroup = new ButtonGroup();
        }

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                JToggleButton btn = new JToggleButton();
                btn.setBackground(getColorForCell(matriz[i][j]));
                btn.setBorderPainted(false);
                btn.setOpaque(true);
                btn.setEnabled(isOponente); 

                if (isOponente) {
                    // Adiciona o botão ao ButtonGroup e define a ação
                    btn.setActionCommand(i + "," + j); // Formato "x,y"
                    buttonGroup.add(btn);
                }

                buttons[i][j] = btn;
                matrizPanel.add(btn);
            }
        }

        if (isOponente) {
            buttonsOponente = buttons;
        } else {
            buttonsLocal = buttons;
        }

        panel.add(matrizPanel, BorderLayout.CENTER);
        return panel;
    }

    private Color getColorForCell(int value) {
        Color cor;
        cor = switch (value) {
            case Consts.AGUA -> Colors.COR_AGUA;
            case Consts.BARCO_PEQUENO -> Colors.CORBP;
            case Consts.BARCO_MEDIO -> Colors.CORBM;
            case Consts.BARCO_GRANDE -> Colors.CORBG;
            default -> Color.GRAY;
        };
        return cor;
    }
    private Color getAcertoOuErro(int value) {
            Color cor;
            cor = switch (value) {
                case Consts.BARCO_PEQUENO -> Colors.CORBP;
                case Consts.BARCO_MEDIO -> Colors.CORBM;
                case Consts.BARCO_GRANDE -> Colors.CORBG;
                default -> Colors.COR_ERRO;
            };
            return cor;
    }
    private void disableMatriz() {
        for (JToggleButton[] row : buttonsLocal) {
            for (JToggleButton btn : row) {
                btn.setEnabled(false); // Desativa todos os botões
            }
        }
    }
   

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new ViewGame(createMatriz(), createMatriz()));
    }

    private static int[][] createMatriz() {
        int[][] m1 = new int[Consts.GRID_MATRIZ][Consts.GRID_MATRIZ];
        for (int i = 0; i < Consts.GRID_MATRIZ; i++) {
            for (int j = 0; j < Consts.GRID_MATRIZ; j++) {
                m1[i][j] = Consts.AGUA;
            }
        }
        return m1;
    }

    @Override
    public void enableMatriz() {
        for (JToggleButton[] row : buttonsLocal) {
            for (JToggleButton btn : row) {
                btn.setEnabled(true); // Desativa todos os botões
            }
        }
    }
}
