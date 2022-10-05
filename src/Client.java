import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        createClient(1234);
    }

    /**
     * Cria novo cliente para interagir com servidor.
     * @param port
     */
    private static void createClient(int port) {
        Socket conexao = null;
        Scanner entradaTeclado = null;
        ObjectOutputStream enviaServidor = null;
        ObjectInputStream respostaServidor = null;
        int n = 3;
        int i;
        String notas[] = new String[n];

        try {
            System.out.println("[CLIENT] - Conectando no servidor");
            conexao = new Socket("localhost", port);
            System.out.println("[CLIENT] - Conexão com sucesso !!!");

            entradaTeclado = new Scanner(System.in);
            enviaServidor = new ObjectOutputStream(conexao.getOutputStream());
            respostaServidor = new ObjectInputStream(conexao.getInputStream());

            for (i = 0; i < n; i++) {
                System.out.printf("[CLIENT] - Informe nota do aluno(%d): ", (i+1));
                notas[i] = entradaTeclado.nextLine();
                enviaServidor.writeUTF(notas[i]);
                enviaServidor.flush();
            }

            for (int j = 0; j < n; j++) {
                System.out.printf("[CLIENT] - Recebendo media normalizada aluno(%d) = %f\n", j+1, respostaServidor.readDouble());
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                entradaTeclado.close();
                enviaServidor.close();
                respostaServidor.close();
                conexao.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}