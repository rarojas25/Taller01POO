package logica;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	static final int MAX = 200;
	
	static String[] nombreAlumno = new String[MAX];
	static String[] apellidoAlumno = new String[MAX];
	static String[] rutAlumno = new String[MAX];
	static String[] paraleloAlumno = new String[MAX];
	static int cantAlumnos = 0;
	
	static String[] nombreSolicitud = new String[MAX];
	static String[] apellidoSolicitud = new String[MAX];
	static int cantSolicitudes = 0;
	
	static boolean archivosCargados = false;
	
	static Scanner teclado = new Scanner(System.in);
	
	public static void main(String[]args) {
		int opcion;
		do {
			mostrarMenuPrincipal();
			opcion = leerOpcionMenu(1,7);
			switch (opcion) {
			case 1:
				System.out.println("(pendiente) Cargar Archivos");
				cargarArchivos();
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


	private static void cargarArchivos() {
		cantAlumnos = cargarAlumnos("Alumnos.txt");
		cantSolicitudes = cargarSolicitudes("Solicitudes.txt");
		archivosCargados = true;
		
		System.out.println("- " + cantAlumnos + " alumnos en la lista.");
		System.out.println("- " + cantSolicitudes + " solicitudes de ingreso.");
			
	}
	
	private static int cargarAlumnos(String rutaArchivo) {
		File archivo = new File(rutaArchivo);
		int cont = 0;
		
		if(!archivo.exists()) {
			System.out.println("Aviso: no se encuentro el archivo " + rutaArchivo + ".");
			return 0;
		}
		try (Scanner lector = new Scanner(archivo)){
			int numLinea = 0;
			while(lector.hasNextLine()) {
				numLinea++;	
				String linea = lector.nextLine().trim();
				if(linea.isEmpty()) {
					continue;
			}
				
			String[] partes = linea.split(";", -1);
			if(partes.length != 4) {
				System.out.println("Aviso: linea " + numLinea + " de " + rutaArchivo + " mal formada, se omite.");
				continue;
			}
			
			String paralelo = partes[3].trim().toUpperCase();
			if(!validarParalelo(paralelo)) {
				System.out.println("Aviso: linea " + numLinea + " tiene un paralelo invalido, se omite.");
				continue;
			}
			
			if(cont >= MAX) {
				System.out.println("Aviso: se alcanzo la capacidad maxima de  " + MAX + " alumnos, se omite el resto.");
				break;
				
			}
			
			nombreAlumno[cont] = partes[0].trim();
			apellidoAlumno[cont] = partes[1].trim();
			rutAlumno[cont] = partes[2].trim();
			paraleloAlumno[cont] = paralelo;
			cont++;
			
			}
		
		}catch(IOException e) {
			System.out.println("Error al leer " + rutaArchivo + ": " + e.getMessage());
		}
		
		return cont;
	}



	private static int cargarSolicitudes(String rutaArchivo) {
		File archivo = new File(rutaArchivo);
		int cont = 0; 
		
		if(!archivo.exists()) {
			System.out.println("Aviso: no se encontro el archivo " + rutaArchivo + ".");
			return 0;
		}
			
		try (Scanner lector = new Scanner(archivo)){
			int numLinea = 0;
			while(lector.hasNextLine()) {
				numLinea++;
				String linea = lector.nextLine().trim();
				if(linea.isEmpty()) {
					continue;
				}
				
				String[] partes = linea.split("-", 2);
				if(partes.length != 2 || partes[0].trim().isEmpty() || partes[1].trim().isEmpty()) {
					System.out.println("Aviso: linea " + numLinea + " de " + rutaArchivo + " mal formada, se omite");
					continue;
				}
				
				if(cont >= MAX) {
					System.out.println("Aviso: se alcanzo la capacidad maxima de " + MAX + " solicitudes, se omite el resto.");
					break;
				}
				
				nombreSolicitud[cont] = partes[0].trim();
				apellidoSolicitud[cont] = partes[1].trim();
				cont++;
			}
		}catch(IOException e) {
			System.out.println("Error al leer " + rutaArchivo + ": " + e.getMessage());
		}
		
		return cont;
		
	}
	
	private static boolean validarParalelo(String paralelo) {
		
		return paralelo != null && (paralelo.equalsIgnoreCase("C1") || paralelo.equalsIgnoreCase("C2"));
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
