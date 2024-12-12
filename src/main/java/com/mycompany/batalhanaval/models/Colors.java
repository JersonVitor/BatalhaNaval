package com.mycompany.batalhanaval.models;

import java.awt.*;

import static com.mycompany.batalhanaval.models.Consts.*;


public class Colors {
    public static final Color CORBP = Color.PINK;
    public static final Color CORBM = Color.magenta;
    public static final Color CORBG = Color.orange;
    public static final Color COR_AGUA = Color.blue;
    public static final Color COR_DESTAQUE = Color.green;

    public static Color selectColor(int x){
        Color C = COR_AGUA;
        switch (x){
            case BARCO_PEQUENO: C = CORBP;
            break;
            case BARCO_MEDIO: C = CORBM;
            break;
            case BARCO_GRANDE: C = CORBG;
            break;
        }
        return C;
    }
}
