package ejercicio1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Fantasma {
	private DatagramSocket socketUDP;
	private boolean turno;
	private String Nombre;

	public Fantasma(boolean turno, String Nombre) {
		this.Nombre = Nombre;
		this.turno = turno;

		try {
			socketUDP = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
// metodo para enviar mensajes
	public void enviarMsg(String msg, String ip, int puerto) {
		InetAddress hostServidor;
		try {
			hostServidor = InetAddress.getByName(ip);
			byte[] mensaje = msg.getBytes();
			DatagramPacket peticion = new DatagramPacket(mensaje, mensaje.length, hostServidor, puerto);
			socketUDP.send(peticion);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
//metodo para recibir mensajes 
	public String recibirMsg() {
		DatagramPacket respuesta = null;
		try {
			byte[] bufer = new byte[1000];
			respuesta = new DatagramPacket(bufer, bufer.length);
			socketUDP.receive(respuesta);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(respuesta.getData()).trim();
	}
//y cuando el cliente se desconecta sale el velo entre mundos se cierra
	public void closeClienteUDP() {
		socketUDP.close();
		System.out.println("-> El VELO ENTRE MUNDOS SE CIERRA");
	}

	public static void main(String args[]) {
		Fantasma Fantasma1 = new Fantasma(true,"CASPER");
		Fantasma Fantasma2 = new Fantasma(false,"JASON");

		Scanner sc = new Scanner(System.in);
		String linea;
		Fantasma canal = new Fantasma(true,"");
		
		System.out.println("Comience a escribir ('FIN DEL HECHIZO') para terminar");
		Fantasma1.enviarMsg(Fantasma1.Nombre, "localhost", 5555);
		//Intento hacer que vayan por turnos creando una variable turno y asi que se comuniquen por turnos 
		do {
			linea = sc.nextLine();
			if(Fantasma1.turno ==true) {
			Fantasma1.enviarMsg(linea, "localhost", 5555);
			System.out.println("Servidor: " + Fantasma1.recibirMsg());
			Fantasma1.turno= false;
			}else {
			Fantasma2.enviarMsg(linea, "localhost", 5555);
			System.out.println("Servidor: " + Fantasma2.recibirMsg());
			Fantasma1.turno=true;
			}
			//lo hace hasta que ponen como mensage fin del hechizo
		} while (!linea.equals("FIN DEL HECHIZO"));
		canal.closeClienteUDP();

	}
}
