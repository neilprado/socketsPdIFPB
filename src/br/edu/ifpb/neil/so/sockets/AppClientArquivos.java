package br.edu.ifpb.neil.so.sockets;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class AppClientArquivos {

    public static void main(String[] args) throws IOException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 7001);
        ArquivoImpl cliente = (ArquivoImpl) registry.lookup("ArquivoImpl");
        menu();

        Scanner entrada = new Scanner(System.in);
        int opt = entrada.nextInt();

        while (opt != 9) {
            switch (opt) {
                case 1: {
                    System.out.println("Insira o nome do diretório");
                    String file = entrada.next();
                    if (cliente.readdir(file).isEmpty()) {
                        System.out.println("Erro, Diretório vazio");
                    }
                    System.out.println("Wunderbar!! " + cliente.readdir(file));
                    break;
                }

                case 2: {
                    System.out.println("Insira o arquivo e o seu novo nome");
                    if (entrada.hasNext()) {
                        entrada.next();
                    }
                    String arquivo = entrada.nextLine();
                    String[] a = arquivo.split(" ");
                    boolean check = cliente.rename(a[0], a[1]);
                    if (check) {
                        System.out.println("Renomeado com sucesso");
                    }
                    System.out.println("Erro desconhecido");
                    break;
                }

                case 3: {
                    System.out.println("Insira o arquivo");
                    String nome = entrada.next();
                    boolean check = cliente.remove(nome);
                    if (check) {
                        System.out.println("Removido com sucesso");
                    }
                    System.out.println("Erro desconhecido");
                    break;
                }

                case 4: {
                    System.out.println("Insira o arquivo");
                    String nome = entrada.next();
                    boolean check = cliente.create(nome);
                    if (check) {
                        System.out.println("Criado com sucesso");
                    }
                    System.out.println("Erro desconhecido");
                    break;
                }
            }
            menu();
            opt = entrada.nextInt();
        }

    }


    public static void menu() {
        System.out.println("\n=== Vamos guardar arquivos? ===");
        System.out.println("1 - Listar arquivos");
        System.out.println("2 - Renomear arquivo");
        System.out.println("3 - Remover arquivo");
        System.out.println("4 - Criar arquivo");
        System.out.println("9 - Sair");
    }
}