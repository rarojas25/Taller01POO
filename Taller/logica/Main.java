package logica;

import java.util.Scanner;

public class Main {
	
	static Scanner teclado = new Scanner(System.in);
	
	public static void main(String[]args) {
		int opcion;
		do {
			mostrarMenuPrincipal();
			opcion = leerOpcionMenu(1,7);
			switch (opcion) {
			case 1:
				System.out.println("(pendiente) Cargar Archivos");
				break;
			case 2:
				System.out.println("(pendiente) Procesar solicitudes");
				break;
			case 3:
				System.out.println("(pendiente) Incripcion manual");
				break;
			case 4:
				System.out.println("(pendiente) Administracion del curso");
				break;
			case 5:
				System.out.println("(pendiente) Generar reportes");
			case 6:
				System.out.println("(pendiente) Analisis estadistico");
				break;
			case 7:
				System.out.println("Saliendo del programa. Hasta luego!");
				break;
			}
		}while(opcion != 7);
		
		teclado.close();
	}


	private static void mostrarMenuPrincipal() {
		System.out.println();
		System.out.println("===== Sistema de Control del Grupo POO =====");
		System.out.println("1) Cargar archivos (Alumnos y Solicitudes");
		System.out.println("2) Procesar solicitudes (Filtrando automatico)");
		System.out.println("3) Inscripcion manual al grupo");
		System.out.println("4) Administracion del curso");
		System.out.println("5) Generar reportes");
		System.out.println("6) Analisis estadistico");
		System.out.println("7) Salir");	
	}
	
	private static int leerOpcionMenu(int min, int max) {
		while(true) {
			System.out.println("Ingrese opcion: ");
			String entrada = teclado.nextLine().trim();
			try {
				int opcion = Integer.parseInt(entrada);
				if(opcion >= min && opcion <= max) {
					return opcion;
				}
				System.out.println("Opcion fuera de rango, Intente nuevamente.");
			}catch(NumberFormatException e) {
				System.out.println("Entrada invalida, debe ingresar un numero.");
			}
		}
	}
}
