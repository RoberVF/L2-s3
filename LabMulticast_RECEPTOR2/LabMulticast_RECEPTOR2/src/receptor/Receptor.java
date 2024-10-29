package receptor;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;

public class Receptor {

    private MulticastSocket socket;
    private static final int LONGITUD_MAXIMA = 1024;

    private void iniciar(int puertoMulticast, InetAddress direccionMulticast) throws IOException {
        // TODO Inicie el socket multicast y unase al grupo multicast
        // correspondiente al grupo 2
        socket = new MulticastSocket(puertoMulticast);
        socket.joinGroup(direccionMulticast);
        System.out.println("Conectado al grupo " + direccionMulticast + " por el puerto " + puertoMulticast);

    }

    private void recibir() throws IOException {
        if (socket != null) {
            byte[] buffer = new byte[LONGITUD_MAXIMA];

            // TODO De forma repetitiva realizar las siguientes tareas:
            while (true) {
                // 1. Recibir datagrama por el socket multicast
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                // Paso el mensaje a String :D
                String mensaje = new String(packet.getData(), 0, packet.getLength());

                // 2. Comprobar si el mensaje recibido es "No", en cuyo caso finaliza
                // la ejecucion
                if (mensaje.equalsIgnoreCase("No")) {
                    System.out.println("Terminando la ejecucion");
                    return;
                }

                // 3. Mostrar mensaje recibido en consola
                System.out.println("Mensaje recibido: " + mensaje);

                // 4. Volver a 1.
            }

        }
    }

    private void cerrar(InetAddress direccionMulticast) throws IOException {
        if (socket != null) {
            // TODO Abandonde el grupo multicast y cierre el socket
            socket.leaveGroup(direccionMulticast);
            socket.close();
            System.out.println("Socket cerrado, abandonando el grupo multicast");
        }
    }

    private void run(int puertoMulticast, InetAddress direccionMulticast) {
        try {
            iniciar(puertoMulticast, direccionMulticast);
            recibir();
            cerrar(direccionMulticast);
        } catch (IOException e) {
            System.err.println("Ha ocurrido un error: " + e.getMessage());
        }
    }

    public static void main(String args[]) {
        System.out.println("Receptor 2 de publicidad...");
        InetAddress direccionMulticast;
        int puertoMulticast;

        // TODO Extraiga los valores introducidos al main mediante el array args[].
        if (args.length < 2) {
            System.err.println("Se necesitan la direccion multicast y el puerto");
            return;
        }

        // Recuerde que:
        // args[0] se corresponde con la direcci�n multicast del grupo
        // args[1] se corresponde con el numero de puerto del grupo
        // El tipo de estos valores es String y debe acomodarse, seg�n el caso, al
        // tipo int o InetAddress.
        // Debe comprobarse que las direcciones multicast recibidas son v�lidas,
        // y en caso de error mostrar un mensaje de error en consola
        try {
            direccionMulticast = InetAddress.getByName(args[0]);
            puertoMulticast = Integer.parseInt(args[1]);

            if (!direccionMulticast.isMulticastAddress()) {
                System.err.println("La direccion establecida no es multicast");
                return;
            }

            // Si todo lo anterior es correcto, cree un objeto Receptor e inicie su
            // ejecucion llamando a su metodo run
            Receptor receptor = new Receptor();
            receptor.run(puertoMulticast, direccionMulticast);

        } catch (IOException e) {
            System.err.println("Ha ocurrido un error: " + e.getMessage());
        }

    }
}
