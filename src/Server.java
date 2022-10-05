import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private ServerSocket servidor;
    private Socket cliente;
    private CalculaMediaThread mediaThread;

    /**
     * Este método cria um novo servidor e aguarda conexão do cliente.
     * @param port
     * @throws IOException
     */
    public void newServer(int port) throws IOException {
        try {
            System.out.println("[SERVER] - Iniciando Servidor na porta 3000...");
            servidor = new ServerSocket(port);
            System.out.println("[SERVER] - Servidor Iniciado");

            while (true) {
                System.out.println("[SERVER] - Aguardando conexão...");
                cliente = servidor.accept();

                System.out.println("[SERVER] - Conexão Recebida de " +
                        cliente.getInetAddress().getHostAddress());

                System.out.println("[SERVER] - Criando thread...");
                mediaThread = new CalculaMediaThread();
                mediaThread.setSocket(cliente);
                Thread newThread = new Thread(mediaThread);
                newThread.start();
                System.out.println("[SERVER] - Thread de processamento em execução.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                servidor.close();
                cliente.close();
            } catch (IOException e) {}
        }
    }
}
