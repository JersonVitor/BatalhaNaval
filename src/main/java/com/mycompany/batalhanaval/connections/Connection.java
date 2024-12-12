/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package com.mycompany.batalhanaval.connections;


import com.mycompany.batalhanaval.intefaces.ServerListener;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author usuario
 */
public class Connection  {

    //porta fixa para o host do jogo
    private final static int portaServidor = 4567;
    //porta para cliente
    private final static int portaCliente = 6789;
    private final Object monitor = new Object();
    private static Boolean isHost;
    private volatile Boolean noFriends;
    private ServerListener server;
    private MatrizConnection mConnection = MatrizConnection.getInstance();
    private static Connection connection = Connection.getInstance();

    public Object getMonitor() {
        return monitor;
    }
    public void serverConnection(ServerListener server){
        this.server = server;
    }
     public static Connection getInstance() {
        if(connection == null){
            connection = new Connection();
        }
        return connection;
     }

    //Conexão incial --------------------------------------------------
    public Boolean clientConnection(String ipCliente, int porta){
       isHost = false;
        try{
            Socket socket = new Socket(ipCliente, porta);

            //Efetua a primitiva send
            DataOutputStream saida = new DataOutputStream(socket.getOutputStream());
            saida.writeBytes(InetAddress.getLocalHost().toString()+":"+portaCliente+"\n");
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            Integer resp = Integer.parseInt(entrada.readLine());
           
            //Efetua a primitiva close
            socket.close();
            if (resp == 200){
                mConnection.setIpAndPorts(ipCliente,porta,portaCliente);
            }
            return resp == 200;
        }catch(Exception e){
            System.out.println("ERROR: "+ e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public void hostConnection(){
        isHost = true;
      new Thread(() -> {
          try {
              ServerSocket socket = new ServerSocket(portaServidor);
              this.noFriends = true;
              while (noFriends) {
                  System.out.println("Esperando Jogadores: \n");
                  //Efetua as primitivas de listen e accept, respectivamente
                  Socket conexao = socket.accept();

                  //Efetua a primitiva receive
                  System.out.println("Aguardando datagrama do cliente....");
                  BufferedReader entrada =  new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                  String inf = entrada.readLine();
                  System.out.println("Informações do Cliente: "+ inf);
                  if(server != null){

                      server.onClientConnected(inf.substring(0,inf.indexOf(':')));
                  }
                   synchronized (monitor) {
                    monitor.wait(); // Aguarda até que a resposta seja fornecida
                    }
                  //Efetua a primitiva send
                  DataOutputStream saida = new DataOutputStream(conexao.getOutputStream());
                  String resp = (server.isConnectionAccepted())?"200\n":"404\n";
                  if (server.isConnectionAccepted())
                      mConnection.setIpAndPorts(
                              inf.substring(inf.indexOf('/')+1, inf.indexOf(":")),
                              Integer.parseInt(inf.substring(inf.indexOf(":")+1)),portaServidor);
                  saida.writeBytes(resp);
                  conexao.close();
                  noFriends = !server.isConnectionAccepted();
              }
          }catch(Exception e){
              System.out.println("ERROR: "+ e.getMessage());
              e.printStackTrace();
          }
      }).start();
      
    }

    public void stopConnection(){
        if(server != null){
            noFriends = false; // Sinaliza para a thread parar
            synchronized (this) {
                notify(); // Libera qualquer thread esperando em wait()
            }
            System.out.println("Servidor encerrado.");
        }
    }
    public static String getLocalIpServidor(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        }catch(Exception e){
             System.out.println("ERROR: "+ e.getMessage());
             e.printStackTrace();
        }
        return "UnknownHost";
    }
    public static int getPortaServidor(){
        return portaServidor;
    }


}
