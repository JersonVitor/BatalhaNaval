/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.batalhanaval;

/**
 *
 * @author usuario
 */
public class BatalhaNaval {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaPrincipal().setVisible(true);
            }
        });
       
    }
}
/*
import javax.swing.*;

import java.awt.*;
import java.util.Collections;

import static javax.swing.GroupLayout.Alignment.LEADING;

public class Tela extends JFrame {

    JPanel mainPanel;
    public Tela(){
        initComponents();
        addMatrizes();
    }

    private void addMatrizes() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1, 10, 10)); // 1 linha, 2 colunas, espaçamento de 10px
        JPanel p1 =addMatrix();
        JPanel p2 = addMatrix();
        mainPanel.add(p1);
        mainPanel.add(p2);
        this.getContentPane().add(mainPanel, BorderLayout.WEST);
    }

    private JPanel addMatrix() {
        JPanel jp = new JPanel(new GridLayout(8,8));
        ButtonGroup bg = new ButtonGroup();
        for (int i = 0; i < 8;i++){
            for (int j = 0; j<8; j++){
                JToggleButton b = new JToggleButton();
                b.setFocusable(false);
                b.addActionListener(e -> {
                    b.setBackground(b.isSelected() ? Color.GREEN: Color.GRAY);
                    for (AbstractButton btn: Collections.list(bg.getElements())){
                        if(btn!= b){
                            btn.setSelected(false);
                            btn.setBackground(Color.gray);
                        }
                    }
                });

                bg.add(b);
                jp.add(b);
            }
        }
        return jp;
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 400); // Define o tamanho da janela
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setLayout(new BorderLayout());
    }

}

*/