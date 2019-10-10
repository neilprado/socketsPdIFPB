package br.edu.ifpb.neil.so.sockets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArquivoImpl extends UnicastRemoteObject implements Arquivo {
    private static String HOME = "/home/neil";

    protected ArquivoImpl() throws RemoteException{}

    @Override
    public String readdir(String pasta) throws IOException {
        Path path = Paths.get(HOME + pasta);
        if(Files.exists(path)){
            Stream<Path> stream = Files.list(path);
            return stream.map(Path::getFileName).map(Objects::toString).collect(Collectors.joining(", "));
        }
        return null;
    }

    @Override
    public boolean rename(String nome, String novoNome) throws IOException {
        Path path = Paths.get(HOME + nome);
        Path novoPath = Paths.get(HOME + novoNome);
        if(Files.exists(path)){
            Files.move(path, novoPath);
            return true;
        }
        return false;
    }

    @Override
    public boolean create(String nome) throws IOException {
        Path path = Paths.get(HOME + nome);
        if(!Files.exists(path)){
            Files.createFile(path);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(String nome) throws IOException {
        Path path = Paths.get(HOME + nome);
        if(Files.exists(path)){
            Files.delete(path);
            return true;
        }
        return false;    }
}
