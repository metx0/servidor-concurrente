import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 5000;

        try {
            // Dirección IP y puerto del servidor al que nos conectaremos
            Socket socket = new Socket(host, puerto);
            System.out.println("Conexión establecida con el servidor. \nIngrese los mensajes a enviar: ");
            
            // Objeto que nos servirá para mandar mensajes al servidor
            PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in);

            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            // Objeto que nos servirá para recibir las respuestas del servidor 
            BufferedReader lector = new BufferedReader(in);
            
            String mensaje = "";
            
            while (!mensaje.equals("exit")) {
                mensaje = sc.nextLine();
                // Mandamos el mensaje a través del flujo de salida del socket
                escritor.println(mensaje);
                System.out.println("Mensaje enviado");
                // Leemos la respuesta del servidor 
                System.out.println("Respuesta del servidor: " + lector.readLine());
            }

            socket.close();
            sc.close();
            System.out.println("Conexión cerrada con el servidor");
        } catch (UnknownHostException e) {
            System.out.println("No se pudo conectar al servidor en: " + host);
        } catch (IOException e) {
            System.out.println("Ha ocurrido una excepción de I/O, o el servidor no está corriendo");
        }
        
    }
}
