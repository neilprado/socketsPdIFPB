package br.edu.ifpb.neil.so.sockets;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppFile {
    public static void main(String[] args) throws RemoteException {
        Arquivo arquivo = new ArquivoImpl();
        Registry registry = LocateRegistry.getRegistry(7001);
        registry.rebind("ArquivoImpl", arquivo);
    }
}