package com.mycompany.batalhanaval.views;

import com.mycompany.batalhanaval.connections.Connection;
import com.mycompany.batalhanaval.connections.MatrizConnection;
import com.mycompany.batalhanaval.intefaces.MatrizListener;
import com.mycompany.batalhanaval.models.Colors;
import com.mycompany.batalhanaval.models.Consts;
import com.mycompany.batalhanaval.models.Matriz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class CreateMatriz extends JFrame implements MatrizListener {
    private  int[][] matrizLocal;
    private int[][] matrizCliente;
    private boolean matrizClienteRecebida = false;
    private boolean matrizLocalPronta = false;

    private static int barcoAtual = Consts.BARCO_PEQUENO;

    private MatrizConnection connection = MatrizConnection.getInstance();

    public CreateMatriz() {
        matrizLocal = new int[Consts.GRID_MATRIZ][Consts.GRID_MATRIZ];
        for (int i = 0; i < Consts.GRID_MATRIZ; i++) {
            for (int j = 0; j < Consts.GRID_MATRIZ; j++) {
                matrizLocal[i][j] = Consts.AGUA;
            }

        }

        initComponents();
        serverStart();
    }
    private void serverStart(){
        try{
            this.connection = new MatrizConnection();
            connection.matrizConnection(this);
            connection.receiveMatriz();
        }catch(Exception e){
            System.out.println("Erro ao iniciar o servidor: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    private void initComponents() {
        setTitle("Colocar Barcos");
        setLayout(new BorderLayout());
        setSize(600, 450);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Painel da matrizLocal
        JPanel jPanel = new JPanel(
                new GridLayout(Consts.GRID_MATRIZ, Consts.GRID_MATRIZ, 1, 1)
        );
        jPanel.setPreferredSize(new Dimension(300, 300)); // Define tamanho fixo para a matrizLocal
        JButton[][] jButton = new JButton[Consts.GRID_MATRIZ][Consts.GRID_MATRIZ];

        for (int i = 0; i < Consts.GRID_MATRIZ; i++) {
            for (int j = 0; j < Consts.GRID_MATRIZ; j++) {
                JButton btn = new JButton();
                btn.setBackground(Colors.COR_AGUA);
                btn.setBorderPainted(false);
                btn.setOpaque(true);

                int x = i, y = j;
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (podeColocarAqui(x, y)) {
                            colocarBarco(jButton, x, y);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (podeColocarAqui(x, y)) {
                            destacarBarco(jButton, x, y, true);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (podeColocarAqui(x, y)) {
                            destacarBarco(jButton, x, y, false);
                        }
                    }
                });

                jButton[i][j] = btn;
                jPanel.add(btn);
            }
        }

        // Painel da matriz centralizado
        JPanel panelCentral = new JPanel(new FlowLayout());
        panelCentral.add(jPanel);

        // Painel de seleção de barcos
        JPanel panelSelecao = new JPanel();
        JLabel jLabel = new JLabel("Selecione os barcos");
        JButton jbuttonBP = new JButton("Barco Pequeno");
        JButton jbuttonBM = new JButton("Barco Médio");
        JButton jbuttonBG = new JButton("Barco Grande");
        jbuttonBP.addActionListener((e) -> barcoAtual = Consts.BARCO_PEQUENO);
        jbuttonBM.addActionListener((e) -> barcoAtual = Consts.BARCO_MEDIO);
        jbuttonBG.addActionListener((e) -> barcoAtual = Consts.BARCO_GRANDE);
        panelSelecao.add(jLabel);
        panelSelecao.add(jbuttonBP);
        panelSelecao.add(jbuttonBM);
        panelSelecao.add(jbuttonBG);

        // Painel inferior com botões Salvar e Fechar
        JPanel panelBotoes = new JPanel(new BorderLayout());
        panelBotoes.setBorder(new EmptyBorder(20, 20, 20, 20));
        JButton btnSalvar = new JButton("Salvar");

        btnSalvar.addActionListener(e -> onClickSalvar(connection.sendMatriz(matrizLocal)));

        JButton btnFechar = new JButton("Fechar");
        panelBotoes.add(btnSalvar, BorderLayout.EAST);
        panelBotoes.add(btnFechar, BorderLayout.WEST);

        // Adiciona os painéis no JFrame
        add(panelSelecao, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void destacarBarco(JButton[][] jButton, int x, int y, boolean destaque) {
        int tamanho = barcoAtual;
        Color cor = (destaque)? Colors.COR_DESTAQUE: Colors.COR_AGUA;
        for (int i = y; i < tamanho+y ; i++) {
            jButton[x][i].setBackground(cor);
        }

    }

    private void colocarBarco(JButton[][] buttons,int x,int y) {
        int tamanho = barcoAtual;
        Color cor = Colors.selectColor(barcoAtual);
        for (int i = y; i < tamanho+ y; i++) {
            buttons[x][i].setBackground(cor);
            this.matrizLocal[x][i] = barcoAtual;
        }
    }

    private boolean podeColocarAqui(int x, int y){
        int tamanho = barcoAtual;
        int posicao = tamanho+y;
        if(posicao > Consts.GRID_MATRIZ) {
            return false;
        }
        for (int i = y; i < posicao; i++) {
            if (this.matrizLocal[x][i]!= Consts.AGUA){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new CreateMatriz().setVisible(true));
    }

    @Override
    public void onMatrizReceive(int[][]matriz) {
        this.matrizCliente = matriz;
        this.matrizClienteRecebida = true;
        checkMatrizesProntas();
    }
    public void onClickSalvar(boolean success){
       if(success){
           this.matrizLocalPronta = true;
           setFocusableWindowState(false);

           checkMatrizesProntas();
       }else{
           JOptionPane.showMessageDialog(CreateMatriz.this, "Erro de Conexão!");
           new TelaPrincipal().setVisible(true);
           dispose();
       }
    }

    private synchronized void checkMatrizesProntas(){
        if (this.matrizLocalPronta && this.matrizClienteRecebida){
                new ViewGame(matrizLocal,matrizCliente).setVisible(true);
                connection.setIpAndPorts(
                        connection.getClientIp(),
                        connection.getClientPort(),
                        connection.getPortaLocal());
                dispose();
        }
    }

}
