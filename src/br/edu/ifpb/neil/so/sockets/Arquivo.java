package br.edu.ifpb.neil.so.sockets;

import java.io.IOException;
import java.rmi.Remote;

public interface Arquivo extends Remote {
    String readdir(String pasta) throws IOException;
    boolean rename(String nome, String novoNome) throws IOException;
    boolean create(String nome) throws IOException;
    boolean remove(String nome) throws IOException;
}
