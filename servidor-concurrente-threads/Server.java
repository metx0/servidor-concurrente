import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // Contador del número de clientes en tiempo de ejecución
    private static int numeroClientes = 0;
    private static final int puerto = 5000;

    static class HiloManejador implements Runnable {
        private Socket socket;
        private int numeroCliente;

        public HiloManejador(Socket socket, int numeroCliente) {
            this.socket = socket;
            this.numeroCliente = numeroCliente;
        }

        /*
        Obtener la información necesaria del socket para crear el flujo de salida
        y recibir los mensajes del cliente
         */
        @Override
        public void run() {
            try {
                // Lector del flujo de entrada del socket (los mensajes del cliente)
                InputStreamReader in = new InputStreamReader(socket.getInputStream());
                BufferedReader lector = new BufferedReader(in);

                // Objeto para escribir mensajes en el flujo de salida del socket (responder al cliente)
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);

                String mensajeRecibido = "";

                while (!mensajeRecibido.equals("exit")) {
                    mensajeRecibido = lector.readLine();
                    System.out.printf("Cliente %d dice: %s\n", numeroCliente, mensajeRecibido);
                    // Contestarle al cliente
                    escritor.printf("Mensaje recibido del cliente %d: '%s'\n", numeroCliente, mensajeRecibido);
                }

                socket.close();
                System.out.println("Conexión cerrada con el cliente " + numeroCliente);
            } catch (IOException e) {
                System.out.println("Ha ocurrido una excepción de I/O");
            }
        }
    }

    public static void main(String[] args) {
        // Levantar el servidor en el puerto determinado, y esperar por conexiones
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor escuchando en el puerto " + puerto);

            while (true) {
                // Aceptar una conexión con un cliente
                Socket socket = serverSocket.accept();
                numeroClientes++;
                System.out.printf("Cliente número %d conectado\n", numeroClientes);

                Thread hilo = new Thread(new HiloManejador(socket, numeroClientes));
                hilo.start();
            }
        } catch (IOException e) {
            System.out.println("Ha ocurrido una excepción de I/O");
        }
    }
}
