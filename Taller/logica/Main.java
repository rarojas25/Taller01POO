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
	
	static String[] nombreMiembro = new String[MAX];
	static String[] apellidoMiembro = new String[MAX];
	static String[] rutMiembro = new String[MAX];
	static String[] paraleloMiembro = new String[MAX];
	static int cantMiembros = 0;
	
	static String[] rechazadoNombre = new String[MAX];
	static String[] rechazadoApellido = new String[MAX];
	static String[] rechazadoRut = new String[MAX];
	static boolean[] rechazadoSoloRut = new boolean[MAX];
	static int cantRechazados = 0;
	
	static boolean archivosCargados = false;
	
	static int totalIntentosIngresos = 0;
	
	static int solicitudesDuplicadas = 0;
	
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
				procesarSolicitudes();
				break;
			case 4:
				System.out.println("(pendiente) Administracion del curso");
				inscripcionManual();
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


	private static void inscripcionManual() {
		if(!verificarArchivosCargados()) {
			return;
		}
		
		System.out.println("Como desea inscribir a la persona?");
		System.out.println("1) Por nombre completo");
		System.out.println("2) Por RUT");
		int opcion = leerOpcionMenu(1,2);
		
		if(opcion == 1) {
			inscribirPorNombre();
		}else {
			inscribirPorRut();
		}
	}


	private static void inscribirPorNombre() {
		System.out.println("Ingrese nombre: ");
		String nombre = teclado.nextLine().trim();
		System.out.println("Ingrese apellido: ");
		String apellido = teclado.nextLine().trim();
		
		if(nombre.isEmpty() || apellido.isEmpty()) {
			System.out.println("El nombre y el apellido no pueden estar vacios");
			return;
		}
		
		totalIntentosIngresos++;
		int idxAlumno = buscarAlumnoPorNombre(nombre, apellido);
		
		if(idxAlumno == -1) {
			agregarRechazado(nombre, apellido, "", false);
			System.out.println(nombre + " " + apellido + " ya es miembro del grupo.");
			return;
		}
		String rut = rutAlumno[idxAlumno];
		if(buscarMiembroPorRut(rut) != -1) {
			System.out.println(nombre + " " + apellido + " ya es el miembro del grupo.");
			return;
			
		}
		if(cantMiembros >= MAX) {
			System.out.println("No hay espacio disponible para mas miembros en el grupo.");
			return;
		}
		agregarMiembro(nombreAlumno[idxAlumno], apellidoAlumno[idxAlumno], rut, paraleloAlumno[idxAlumno], true);
		System.out.println(nombre + " " + apellido + " fue inscrito correctamente en el grupo (" + paraleloAlumno[idxAlumno] + ").");
		
	}

	private static void inscribirPorRut() {
		System.out.println("Ingrese RUT: ");
		String rut = teclado.nextLine().trim();
		
		if(!validarRutNoVacio(rut)) {
			System.out.println("El RUT no puede estar vacio.");
			return;
		}
		totalIntentosIngresos++;
		int idxAlumno = buscarAlumnoPorRut(rut);
		
		if(idxAlumno == -1) {
			agregarRechazado("","", rut, true);
			System.out.println("El RUT " + rut + " no pertenece a ningun paralelo del curso.");
			System.out.println("No tenemos su nombre, por lo que se registrara solo el RUT en los rechazados.");
			return;
		}
		if(buscarMiembroPorRut(rut) != -1) {
			System.out.println("Esa persona ya es miembro del grupo.");
			return;
		}
		if(cantMiembros >= MAX) {
			System.out.println("No hay espacio disponible para mas miembros en el grupo.");
			return;
		}
		agregarMiembro(nombreAlumno[idxAlumno], apellidoAlumno[idxAlumno], rut, paraleloAlumno[idxAlumno], true);
		System.out.println(nombreAlumno[idxAlumno] + " " + apellidoAlumno[idxAlumno] + " fue inscrito correstamente en el grupo (" + paraleloAlumno[idxAlumno] + ").");
		
	}

	private static boolean validarRutNoVacio(String rut) {
		return rut != null && !rut.trim().isEmpty();
	}

	
	private static int buscarAlumnoPorRut(String rut) {
		for(int i = 0; i < cantAlumnos; i++) {
			if(rutAlumno[i].equalsIgnoreCase(rut)) {
				return i;
			}
		}
		return -1;
	}

	
	private static void procesarSolicitudes() {
		if(!verificarArchivosCargados()) {
			return;
		}
		System.out.println("Procesando solicitudes...");
		System.out.println();
		
		int admitidosNuevos = 0;
		int rechazadosNuevos = 0;
		
		for(int i = 0; i < cantSolicitudes; i++) {
			String nombre = nombreSolicitud[i];
			String apellido = apellidoSolicitud[i];
			totalIntentosIngresos++;
			
			int idxAlumno = buscarAlumnoPorNombre(nombre, apellido);
			
			if(idxAlumno == -1) {
				agregarRechazado(nombre, apellido, "", false);
				rechazadosNuevos++;
				System.out.println("[RECHAZO]  " + nombre + " " + apellido + "-> no pertenece a ningun paralelo");
				continue;
			}
			
			String rut = rutAlumno[idxAlumno];
			if(buscarMiembroPorRut(rut) != -1) {
				solicitudesDuplicadas++;
				System.out.println("[DUPLICADO]  " + nombre + " " + apellido + "-> ya esta admitido, se ignora.");
				continue;
			}
			if(cantMiembros >= MAX) {
				System.out.println("Aviso: no hay espacio disponible para mas miembros en el grupo ");
				break;
			}
			agregarMiembro(nombreAlumno[idxAlumno], apellidoAlumno[idxAlumno], rut, paraleloAlumno[idxAlumno], false);
			admitidosNuevos++;
			System.out.println("[OK]   " + nombre + " " + apellido + " -> admitido en " + paraleloAlumno[idxAlumno]);
		}
		System.out.println();
		System.out.println("Resumen: " + admitidosNuevos + " admitidos / " + rechazadosNuevos + " rechazados.");
	}


	private static void agregarRechazado(String nombre, String apellido, String rut, boolean soloRut) {
		if(cantRechazados >= MAX) {
			System.out.println("Aviso: no hay espacio disponible para registrar mas rechazados");
			return;
		}
		rechazadoNombre[cantRechazados] = nombre;
		rechazadoApellido[cantRechazados] = apellido;
		rechazadoRut[cantRechazados] = rut;
		rechazadoSoloRut[cantRechazados] = soloRut;
		cantRechazados++;
	}

	private static void agregarMiembro(String nombre, String apellido, String rut, String paralelo, boolean manual) {
		nombreMiembro[cantMiembros] = nombre;
		apellidoMiembro[cantMiembros] = apellido;
		rutMiembro[cantMiembros] = rut;
		paraleloMiembro[cantMiembros] = paralelo;
		cantMiembros++;
	}


	private static int buscarAlumnoPorNombre(String nombre, String apellido) {
		for(int i = 0; i < cantAlumnos; i++) {
			if(nombreAlumno[i].equalsIgnoreCase(nombre) && apellidoAlumno[i].equalsIgnoreCase(apellido)) {
				return i;
			}
		}
		return 1;
	}

	private static int buscarMiembroPorRut(String rut) {
		for(int i = 0; i < cantMiembros; i++) {
			if(rutMiembro[i].equalsIgnoreCase(rut)) {
				return i;
			}
		}
		return 1;
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
	
	
	private static boolean verificarArchivosCargados() {
		if(!archivosCargados) {
			System.out.println("Primero debe cargar los archivos (opcion 1).");
			return false;
		}
		return true;
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
