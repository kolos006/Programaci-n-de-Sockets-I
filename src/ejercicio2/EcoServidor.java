package ejercicio2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class ServidorTCP {
	private Socket socketCliente;
	private ServerSocket socketServidor;
	private BufferedReader entrada;
	private PrintWriter salida;

	public ServidorTCP(int puerto) {
		this.socketCliente = null;
		this.socketServidor = null;
		this.entrada = null;
		this.salida = null;
		try {
			socketServidor = new ServerSocket(puerto);
			System.out.println("Esperando conexión...");
			socketCliente = socketServidor.accept();
			System.out.println("Conexión acceptada: " + socketCliente);
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
		} catch (IOException e) {
			System.out.println("No puede escuchar en el puerto: " + puerto);
			System.exit(-1);
		}
	}

	public void closeServidorTCP() {
		try {
			salida.close();
			entrada.close();
			socketCliente.close();
			socketServidor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Servidor Terminado");
	}

	public int recibirMsg() {
		int linea = 0;
		try {
			linea = entrada.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linea;
	}

	public void enviarMsg(int linea)
	{
		salida.println(linea);
	}
	
	public void imprimirTuMapa(int[][] mapa, int row, int column)
	{
		for (int i = 0; i < mapa.length; i++) 
		{
			for (int j = 0; j < mapa.length; j++) 
			{
				if(i == row && j == column)
				{
					System.out.print("0");
				}
				else
				{
					System.out.print("X");
				}
				
			}
			System.out.println();
		}
	}
	
	public void imprimirIntentos(int[][] mapa, int aciertoFila, int aciertoColumna)
	{
		for (int i = 0; i < mapa.length; i++) 
		{
			for (int j = 0; j < mapa.length; j++) 
			{
				if(i == aciertoFila && j == aciertoColumna)
				{
					mapa[i][j] = 0;
					System.out.print("0");
				}
				else
				{
					System.out.print("X");
				}
				
			}
			System.out.println();
		}
	}

}

public class EcoServidor 
{

	public static void main(String[] args) throws IOException 
	{
		Scanner sc = new Scanner(System.in);
		ServidorTCP canal = new ServidorTCP(5555);
		
		boolean bMyTurn = false;
		int aciertoFila = -1;
		int aciertoColumna = -1;
		int [][] mapa = new int [10][10];
		
		int row = (int) Math.random() * 10;
		
		int column = (int) Math.random() * 10;

		canal.imprimirTuMapa(mapa, row, column);

		do 
		{
			if(bMyTurn)
			{
				System.out.print("Adivina su columna: ");
				canal.enviarMsg(sc.nextInt());
				System.out.print("Adivina su fila: ");
				canal.enviarMsg(sc.nextInt());
				bMyTurn = false;
			}
			else
			{
				aciertoFila = canal.recibirMsg();
				aciertoColumna = canal.recibirMsg();
				bMyTurn = true;
			}
			
		
		
		} while ((aciertoFila != row) && (aciertoColumna != column));
		
		System.out.println("ACIERTO!!!");
		
		canal.closeServidorTCP();
		sc.close();
	}
}