package com.mycompany.batalhanaval.connections;

import com.mycompany.batalhanaval.Uteis.Uteis;
import com.mycompany.batalhanaval.intefaces.MatrizListener;
import com.mycompany.batalhanaval.intefaces.ServerListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MatrizConnection {


    // Ip e porta do outro Jogador
    private static String clientIp;
    private static int clientPort;
    private static int portaLocal;
    private MatrizListener matrizListener;
    private static MatrizConnection instance = new MatrizConnection();

    public String getClientIp() {
        return clientIp;
    }

    public int getClientPort() {
        return clientPort;
    }

    public int getPortaLocal() {
        return portaLocal;
    }


    public static MatrizConnection getInstance(){
            return instance;
    }


    public void matrizConnection(MatrizListener matriz){
        this.matrizListener = matriz;
    }

        public Boolean sendMatriz(int[][] matriz) {
        try{
            Socket socket = new Socket(clientIp,clientPort);
            System.out.println("Enviando para: "+ clientIp+":"+clientPort);
            //Efetua a primitiva send
            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
            saida.writeBytes(Uteis.intMatrizToString(matriz));

            socket.close();


        }catch(Exception e){
            System.out.println("ERROR: "+ e.getMessage());
            e.printStackTrace();
            return false;
        }
            return true;
    }
    public void receiveMatriz(){
        new Thread(() -> {
            try {
                System.out.println("Servidor Inicializado: "+clientIp+":"+portaLocal);
                ServerSocket socket = new ServerSocket(portaLocal);
                    System.out.println("Esperando Matriz: \n");
                    Socket conexao = socket.accept();

                    BufferedReader entrada =  new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                    String inf = entrada.readLine();
                    System.out.println("Matriz: "+ inf);
                    if (matrizListener != null)
                        matrizListener.onMatrizReceive(Uteis.stringToIntMatriz(inf));
                    conexao.close();
                }
                catch(Exception e){
                    System.out.println("ERROR: "+ e.getMessage());
                    e.printStackTrace();

                }
        }).start();

    }
/*
    public void receiveMatriz() {
        new Thread(() -> {
            boolean enderecoEPortaEncontrados = false;
            String[] enderecosParaTentar = { "127.0.0.1"}; // Lista de endereços possíveis
            int[] portasParaTentar = {6060, 6061}; // Lista de portas possíveis
            ServerSocket serverSocket = null;

            // Tenta encontrar um endereço e porta disponíveis
            for (String endereco : enderecosParaTentar) {
                for (int porta : portasParaTentar) {
                    try {
                        InetAddress ip = InetAddress.getByName(endereco);
                        serverSocket = new ServerSocket(porta, 50, ip);
                        System.out.println("Servidor iniciado no endereço " + endereco + " e porta " + porta);
                        enderecoEPortaEncontrados = true;
                        break;
                    } catch (Exception e) {
                        System.out.println("Endereço " + endereco + " e porta " + porta + " já estão em uso. Tentando outro...");
                    }
                }
                if (enderecoEPortaEncontrados) break;
            }

            if (!enderecoEPortaEncontrados) {
                System.out.println("Nenhum endereço e porta disponíveis. Abortando.");
                return; // Encerra se nenhuma combinação estiver disponível
            }

            try {
                System.out.println("Esperando matriz...");
                while (true) {
                    Socket conexao = serverSocket.accept();
                    BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                    String inf = entrada.readLine();
                    System.out.println("Matriz recebida: " + inf);

                    if (matrizListener != null) {
                        matrizListener.onMatrizReceive(Uteis.stringToIntMatriz(inf));
                    }

                    conexao.close(); // Fecha a conexão
                }
            } catch (Exception e) {
                System.out.println("Erro ao aceitar conexões: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (serverSocket != null) {
                        serverSocket.close(); // Fecha o socket ao sair
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao fechar o servidor: " + e.getMessage());
                }
            }
        }).start();
    }
*/

    public void stopConnection(){
        if(matrizListener != null){
            synchronized (this) {
                notify();
            }
            System.out.println("Servidor encerrado.");
        }
    }

    public void setIpAndPorts(String ip, int portaC, int portaL){
        clientIp = ip;
        clientPort = portaC;
        portaLocal = portaL;
        
    }

}
