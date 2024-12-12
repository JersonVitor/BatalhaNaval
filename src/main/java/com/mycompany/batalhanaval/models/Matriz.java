package com.mycompany.batalhanaval.models;

import java.awt.*;

public class Matriz {

    public int [][]matriz = new int[Consts.GRID_MATRIZ][Consts.GRID_MATRIZ];

    public Matriz(){
        for (int i = 0; i < Consts.GRID_MATRIZ; i++) {
            for (int j = 0; j < Consts.GRID_MATRIZ; j++) {
                matriz[i][j] = Consts.AGUA;
            }

        }
    }


}
