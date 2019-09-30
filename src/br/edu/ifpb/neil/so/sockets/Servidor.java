package br.edu.ifpb.neil.so.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Servidor {

    private static String HOME = "/home/neil/ifpb/progDistribuida/nfs-sockets";
    private static String currentDirectory = System.getProperty("user.dir");


    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor ==");

        Servidor server = new Servidor();

        // Configurando o socket
        ServerSocket serverSocket = new ServerSocket(7001);
        Socket socket = serverSocket.accept();

        // pegando uma referência do canal de saída do socket. Ao escrever nesse canal, está se enviando dados para o
        // servidor
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        // pegando uma referência do canal de entrada do socket. Ao ler deste canal, está se recebendo os dados
        // enviados pelo servidor
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        // laço infinito do servidor
        while (true) {
            System.out.println("Cliente: " + socket.getInetAddress());

            String mensagem = dis.readUTF();
            System.out.println(mensagem);
            String[] splitted = mensagem.split(" ");

            if(splitted.length<2) {
                dos.writeUTF("Tente Novamente, sintaxe incorreta");
            }else {
                switch (splitted[0]) {
                    case "readdir":
                        String msg = server.readdir(splitted[1]);
                        if (msg != null) {
                            dos.writeUTF(msg);
                        }else {
                            dos.writeUTF("Diretorio inexistente");
                        }
                        break;

                    case "rename":
                        if (server.rename(splitted[1], splitted[2])) {
                            dos.writeUTF("");
                        }else {
                            dos.writeUTF("Diret�rio ou arquivo inexistente");
                        }
                        break;

                    case "create":
                        if (server.create(splitted[1])) {
                            dos.writeUTF("");
                        }else {
                            dos.writeUTF("Arquivo j� existe");
                        }
                        break;

                    case "remove":
                        if (server.remove(splitted[1])) {
                            dos.writeUTF("");
                        }else {
                            dos.writeUTF("Arquivo inexistente");
                        }
                        break;
                    default:
                        dos.writeUTF(splitted[0] + " comando n�o reconhecido");
                        break;
                }
            }

        }
        /*
         * Observe o while acima. Perceba que primeiro se lê a mensagem vinda do cliente (linha 29, depois se escreve
         * (linha 32) no canal de saída do socket. Isso ocorre da forma inversa do que ocorre no while do Cliente2,
         * pois, de outra forma, daria deadlock (se ambos quiserem ler da entrada ao mesmo tempo, por exemplo,
         * ninguém evoluiria, já que todos estariam aguardando.
         */
    }

    public String readdir (String diretorio) throws IOException {
        Path p = Paths.get(HOME+diretorio);
        if(Files.exists(p)) {
            Stream<Path> list = Files.list(p);
            return list.map(Path::getFileName)
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
        }else {
            return null;
        }
    }

    public boolean rename (String arquivo, String novoNome) throws IOException {
        Path p = Paths.get(HOME+arquivo);
        Path p2 = Paths.get(HOME+novoNome);
        if(Files.exists(p)) {
            Files.move(p,p2);
            return true;
        }else {
            return false;
        }
    }

    public boolean create (String nomeArquivo) throws IOException {
        if (nomeArquivo.contains("/")) {
            Path p = Paths.get(HOME+nomeArquivo);
            if(!Files.exists(p)) {
                Files.createFile(p);
                return true;
            }else {
                return false;
            }
        }else {
            Path p = Paths.get(currentDirectory+"/"+nomeArquivo);
            if(!Files.exists(p)) {
                Files.createFile(p);
                return true;
            }else {
                return false;
            }
        }

    }

    public boolean remove (String nomeArquivo) throws IOException {
        Path p = Paths.get(HOME+nomeArquivo);
        if(Files.exists(p)) {
            Files.delete(p);
            return true;
        }else {
            return false;
        }
    }
}