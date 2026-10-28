package ejercicio2;

import java.net.*;
import java.util.Scanner;
import java.io.*;

class ClienteTCP {
	private Socket socketCliente = null;
	private BufferedReader entrada = null;
	private PrintWriter salida = null;

	public ClienteTCP(String ip, int puerto) {
		try {
			socketCliente = new Socket(ip, puerto);
			System.out.println("Conexión establecida: " + socketCliente);
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
		} catch (IOException e) {
			System.err.printf("Imposible conectar con ip:%s / puerto:%d",ip,puerto);
			System.exit(-1);
		}
	}

	public void closeClienteTCP() {
		try {
			salida.close();
			entrada.close();
			socketCliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Cliente Terminado");
	}

	public void enviarMsg(int linea) {
		salida.println(linea);
	}

	public int recibirMsg() {
		int msg = 0;
		try {
			msg = entrada.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
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

public class EcoCliente 
{
	public static void main(String[] args) throws IOException
	{
		Scanner sc = new Scanner(System.in);
		ClienteTCP canal = new ClienteTCP("localhost", 5555);
		System.out.println("Comience a escribir ('Adiós') para terminar");
		
		boolean bMyTurn = true;
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
		
		canal.closeClienteTCP();
		sc.close();
	}
}