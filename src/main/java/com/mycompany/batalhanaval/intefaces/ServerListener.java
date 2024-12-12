/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.batalhanaval.intefaces;

/**
 *
 * @author usuario
 */
public interface ServerListener {
    void onClientConnected(String inf);
    void onConnectionAccepted();
    void onConnectionRefused();
    Boolean isConnectionAccepted();
}
