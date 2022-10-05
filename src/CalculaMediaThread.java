import java.io.*;
import java.net.Socket;

public class CalculaMediaThread implements Runnable {
    private Socket cliente = null;
    Double notas[] = new Double[3];
    double media;
    ObjectOutputStream saidaCliente = null;
    ObjectInputStream entradaCliente = null;

    /**
     * Este método cria as conexões de entrada e saida com o cliente
     * que se conecta com o servidor e calcula e envia a média das notas.
     */
    public void run() {
        System.out.println("[PROCESSO] - Iniciando processo (thread) do cliente " +
                cliente.getInetAddress().getHostAddress());

        try {
            saidaCliente = new ObjectOutputStream(cliente.getOutputStream());
            entradaCliente = new ObjectInputStream(cliente.getInputStream());

            int count = 0;
            while (count < 3) {
                notas[count] = Double.parseDouble(entradaCliente.readUTF());
                System.out.println("[PROCESSO] - Mensagem Recebida: " + notas[count]);
                count++;
            }

            Double[] normalizadas = normalizar(notas);
            System.out.println(normalizadas.length);
            for (int i = 0; i < normalizadas.length; i++) {
                System.out.printf("[PROCESSO] - Nota normalizada do aluno(%d) = %f\n", i+1, normalizadas[i]);
                saidaCliente.writeDouble(normalizadas[i]);
                saidaCliente.flush();
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("[PROCESSO] - Finalizando processo (thread) do cliente " +
                        cliente.getInetAddress().getHostAddress());
                entradaCliente.close();
                saidaCliente.close();
                cliente.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Configura um novo cliente para se conectar ao servidor.
     * @param socket cliente
     */
    public void setSocket(Socket socket) {
        this.cliente = socket;
    }

    /**
     * Método que calcula a nota normalizada.
     * @param notas
     * @return double[] normalizadas
     */
    private Double[] normalizar(Double notas[]){
        double maior=0;
        Double normalizadas[] = new Double[3];

        for (int i = 0; i < notas.length; i++) {
            if(notas[i] > maior) maior = notas[i];
        }

        for (int i = 0; i < notas.length; i++) {
            normalizadas[i] = (notas[i] * 10) / maior;
        }

        return normalizadas;
    }
}