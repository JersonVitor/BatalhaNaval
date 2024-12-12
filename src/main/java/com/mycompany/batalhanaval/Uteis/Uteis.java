
package com.mycompany.batalhanaval.Uteis;

import com.mycompany.batalhanaval.models.Consts;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author usuario
 */
public class Uteis {
    public static Boolean emptyFields(String s1, int s2){
        return(s1.isEmpty()&& s2 >1000);
    }

    public static String intMatrizToString(int [][] matriz){
       StringBuilder str = new StringBuilder();
       try{
           for (int i = 0; i < Consts.GRID_MATRIZ; i++) {
               for (int j = 0; j < Consts.GRID_MATRIZ; j++) {
                   str.append(matriz[i][j]);
               }
           }


       }catch (Exception e){
           System.out.println("ERROR: "+ e.getMessage());
           e.printStackTrace();
       }
       str.append("\n");
        // Retornar o array de bytes
        return str.toString();
    }

    public static int[][] stringToIntMatriz(String response){


        // Reconstruindo a matriz
        int[][] matriz = new int[Consts.GRID_MATRIZ][Consts.GRID_MATRIZ];
        for (int i = 0; i < Consts.GRID_MATRIZ; i++) {
            for (int j = 0; j < Consts.GRID_MATRIZ; j++) {
                matriz[i][j] = response.charAt(i*Consts.GRID_MATRIZ+j)-'0';
            }
        }

        return matriz;
    }
}
