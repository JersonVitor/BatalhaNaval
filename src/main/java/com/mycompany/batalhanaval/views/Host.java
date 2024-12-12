/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.batalhanaval.views;

import com.mycompany.batalhanaval.intefaces.ServerListener;
import com.mycompany.batalhanaval.connections.Connection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 *
 * @author usuario
 */
public class Host extends JFrame implements ServerListener {
        private JPanel panel;
        protected JLabel informations;
        private JLabel waitConnection;
        private Boolean connectionAccepted = false;
        private Connection connection;
      
    public Host() {
        // Configuração do JFrame
        setTitle("Host");
        setSize(400, 320);
        setLayout(null); // Layout manual para posicionar componentes
         setResizable(false);
         String format = String.format(
            "<html>Status do Servidor:       IP: <b>%s</b>       Porta: <b>%s</b></html>",
            Connection.getLocalIpServidor(), Connection.getPortaServidor()
        );
        JLabel topLabel1 = new JLabel(format);
        topLabel1.setBounds(10, 10, 150, 25); // Posição e tamanho
        topLabel1.setSize(topLabel1.getPreferredSize());
        add(topLabel1);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        waitConnection = new JLabel("Esperando Conexão...");
        waitConnection.setBounds(10, 35, 250, 25); // Posição e tamanho
        add(waitConnection);
        // Criação do JPanel
        panel = new JPanel();
        panel.setLayout(null); // Layout manual no JPanel
        panel.setBounds(50, 70, 300, 150);
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Borda para destaque
        panel.setVisible(false); // Inicialmente invisível
        add(panel);

        // Label "Confirmar conexão de:"
        JLabel labelTitulo = new JLabel("Confirmar conexão de:");
        labelTitulo.setBounds(10, 10, 200, 25);
        panel.add(labelTitulo);

        // Label com os dados da conexão
        informations = new JLabel("");
        informations.setBounds(20, 40, 250, 25);
        panel.add(informations);

        // Botão "Aceitar"
        JButton aceitarButton = new JButton("Aceitar");
        aceitarButton.setBounds(50, 100, 100, 30);
        panel.add(aceitarButton);

        aceitarButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(Host.this, "Conexão aceita!");
            onConnectionAccepted();
        });

        // Botão "Recusar"
        JButton recusarButton = new JButton("Recusar");
        recusarButton.setBounds(160, 100, 100, 30);
        panel.add(recusarButton);

        recusarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Host.this, "Conexão recusada!");
                 onConnectionRefused();
                 panel.setVisible(false);
            }
        });

        // Botão "Fechar" fora do JPanel
        JButton fecharButton = new JButton("Fechar");
        fecharButton.setBounds(280, 240, 100, 30);
        add(fecharButton);

        fecharButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connection.stopConnection();
                dispose();
            }
        });
        serverStart();

        // Tornar o JFrame visível
        setVisible(true);
    }


    @Override
    public void onClientConnected(String inf) {
            SwingUtilities.invokeLater(new Runnable(){
                public void run(){
                informations.setText(inf);
                waitConnection.setVisible(false);
                panel.setVisible(true);

            }
        });
    }

    @Override
    public void onConnectionAccepted() {
        connectionAccepted = true;
        connection.stopConnection();
        synchronized(connection.getMonitor()){
            connection.getMonitor().notify();
        }
        new CreateMatriz().setVisible(true);
        dispose();

    }

    @Override
    public void onConnectionRefused() {
        connectionAccepted = false;
        waitConnection.setVisible(true);
        synchronized(connection.getMonitor()){
            connection.getMonitor().notify();
        }
    }

    @Override
    public Boolean isConnectionAccepted() {
        return connectionAccepted;
    }
    
    private void serverStart(){
        try{
          connection = new Connection();
          connection.serverConnection(this);
          connection.hostConnection();
        }catch(Exception e){
            System.out.println("Erro ao iniciar o servidor: "+ e.getMessage());
            e.printStackTrace();
        }
    }


}
