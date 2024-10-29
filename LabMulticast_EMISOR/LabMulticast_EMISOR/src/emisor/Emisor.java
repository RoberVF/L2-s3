package src.emisor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class Emisor {
    private MulticastSocket socket;
    private static final int TTL = 1;

    private void iniciar() throws IOException {
        // TODO Inicie el socket multicast y establezca el valor de TTL a 1
        socket = new MulticastSocket();
        socket.setTimeToLive(TTL);
    }

    private void enviar(int puertoMulticast1, InetAddress direccionMulticast1,
            int puertoMulticast2, InetAddress direccionMulticast2)
            throws IOException {
        if (socket != null) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                // TODO Realizar repetidamente las siguientes tareas
                // 1. Solicitar al usuario el mensaje de publicidad que se desea enviar
                System.out.println("Ingrese el mensaje de publicidad o 'no' para terminar");

                // 2. Si el mensaje es ?No? la aplicacion terminar su ejecucion
                String mensaje = scanner.nextLine();
                if (mensaje.equalsIgnoreCase("No")) {
                    System.out.println("Terminando aplicacion");
                    break;
                }

                // 3. Pedir al usuario el grupo de clientes al que se desea enviar la
                // publicidad: 1 (grupo 1), 2 (grupo 2) o 3 (ambos grupos). Si el grupo
                // introducido no corresponde con los indicados, se elige el grupo 3
                System.out.println("Seleccion el grupo de usuarios (1, 2, 3):");
                int grupo;
                try {
                    grupo = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    grupo = 3;
                }

                // 4. Crear el datagrama con el mensaje de publicidad al grupo o
                // grupos de clientes escogidos
                DatagramPacket packet = null;
                byte[] buffer = mensaje.getBytes();

                // 5. Enviar el datagrama por el socket multicast
                if (grupo == 1) {
                    packet = new DatagramPacket(buffer, buffer.length, direccionMulticast1, puertoMulticast1);
                } else if (grupo == 2) {
                    packet = new DatagramPacket(buffer, buffer.length, direccionMulticast2, puertoMulticast2);
                } else {
                    packet = new DatagramPacket(buffer, buffer.length, direccionMulticast1, puertoMulticast1);
                    socket.send(packet);
                    packet = new DatagramPacket(buffer, buffer.length, direccionMulticast2, puertoMulticast2);
                }
                socket.send(packet);
                System.out.println("Mensaje enviado al grupo: " + grupo);
                // 6. Volver a 1.
            }

        }
    }

    private void cerrar() {
        // TODO Cierre el socket
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    private void run(int puertoMulticast1, InetAddress direccionMulticast1,
            int puertoMulticast2, InetAddress direccionMulticast2) {
        try {
            iniciar();
            enviar(puertoMulticast1, direccionMulticast1, puertoMulticast2, direccionMulticast2);
            cerrar();
        } catch (IOException e) {
            System.out.println("Error al enviar el mensaje: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        System.out.println("Emisor de publicidad...");

        InetAddress direccionMulticast1 = null, direccionMulticast2 = null;
        int puertoMulticast1 = 0, puertoMulticast2 = 0;

        // TODO Extraiga los valores introducidos al main mediante el array args[].
        if (args.length < 4) {
            System.err.println("Se requiren cuatro argumentos: Dir1; Dir2; Puerto1; Puerto2");
            return;
        }
        // Recuerde que:
        // args[0] se corresponde con la direccion multicast del grupo de receptores 1
        // args[1] se corresponde con el numero de puerto del grupo de receptores 1
        // args[2] se corresponde con la direccion multicast del grupo de receptores 2
        // args[3] se corresponde con el numero de puerto del grupo de receptores 2
        // El tipo de estos valores es String y debe acomodarse, segun el caso, al
        // tipo int o InetAddress.
        // Debe comprobarse que las direcciones multicast recibidas son validas,
        // y en caso de error mostrar un mensaje de error en consola

        try {
            direccionMulticast1 = InetAddress.getByName(args[0]);
            puertoMulticast1 = Integer.parseInt(args[1]);
            direccionMulticast2 = InetAddress.getByName(args[2]);
            puertoMulticast2 = Integer.parseInt(args[3]);
            // Comprobar que las direcciones recibidas son validas:
            if (!direccionMulticast1.isMulticastAddress() && !direccionMulticast2.isMulticastAddress()) {
                System.err.println("Las direcciones enviadas no son multicast :(");
                return;
            }

        } catch (IOException e) {
            System.err.println("Ha ocurrido un error: " + e.getMessage());
        }

        // Si todo lo anterior es correcto, cree un objeto Emisor e inicie su
        // ejecucion llamando a su metodo run
        Emisor emisor = new Emisor();
        emisor.run(puertoMulticast1, direccionMulticast1, puertoMulticast2, direccionMulticast2);

    }
}
