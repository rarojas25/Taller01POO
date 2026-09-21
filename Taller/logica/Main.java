package logica;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
				cargarArchivos();
				break;
			case 2:
				procesarSolicitudes();
				break;
			case 3:
				inscripcionManual();
				break;
			case 4:
				administracionCurso();
				break;
			case 5:
				generarReportes();
				break;
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

	private static void generarReportes() {
		if(!verificarArchivosCargados()) {
			return;
		}
		int opcion;
		do {
			System.out.println();
			System.out.println("---Generar Reportes---");
			System.out.println("1) Reporte paralelo C1");
			System.out.println("2) Reporte paralelo C2");
			System.out.println("3) Reporte de rechazados");
			System.out.println("4) Volver");
			opcion = leerOpcionMenu(1,4);
			
			switch(opcion) {
				case 1:
					generarReporteParalelo("C1");
					break;
				case 2:
					generarReporteParalelo("C2");
					break;
				case 3:
					generarReporteRechazados();
					break;
				case 4:
					System.out.println("Volviendo al menu principal...");
					break;
			}
		}while(opcion!=4);
	}

	private static void generarReporteParalelo(String paralelo) {
		File carpeta = new File("Reportes");
		if(!carpeta.exists()) {
			carpeta.mkdir();
		}
		String prefijo = "Reporte" + paralelo;
		int version = obtenerSiguienteVersion(prefijo);
		String rutaArchivo = "Rportes/" + prefijo + "-V" + version + ".txt";
		
		try(BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))){
			escritor.write("=== Miembros del grupo - Paralelo " + paralelo + " ===") ;
			escritor.newLine();
			for(int i = 0; i < cantMiembros; i++) {
				if(paraleloMiembro[i].equals(paralelo)) {
					escritor.write(nombreMiembro[i] + " " + apellidoMiembro[i] + " - " + rutMiembro[i]);
					escritor.newLine();
				}
			}
			System.out.println("Reporte generado: " + rutaArchivo);
		}catch(IOException e) {
			System.out.println("Error al generar el reporte: " + e.getMessage());
		}
	}
	private static void generarReporteRechazados() {
		File carpeta = new File("Reportes");
		if(!carpeta.exists()) {
			carpeta.mkdir();
		}
		int version = obtenerSiguienteVersion("Rechazados");
		String rutaArchivo = "Reportes/Rechazados-V" + version + ".txt";
		
		try(BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaArchivo))){
			escritor.write("=== Solicitudes rechazadas ===");
			escritor.newLine();
			for(int i = 0; i < cantRechazados; i++) {
				if(rechazadoSoloRut[i]) {
					escritor.write("Sin nombre resgistrado, RUT: " + rechazadoRut[i]);
				}else{
					escritor.write(rechazadoNombre[i] + " " + rechazadoApellido[i] + " - No pertenece a ningun paralelo del curso");
				}
				escritor.newLine();
			}
			System.out.println("Reporte generado: " + rutaArchivo);
		}catch(IOException e) {
			System.out.println("Error al general el reporte: " + e.getMessage());
		}
	}
	
	private static int obtenerSiguienteVersion(String prefijo) {
		int version = 1;
		File archivo = new File("Reportes/" + prefijo + "-V" + version + ".txt");
		while(archivo.exists()){
			version++;
			archivo = new File("Reportes/" + prefijo + "-V" + version + ".txt");
		}
		return version;
	}
	
	private static void administracionCurso() {
		if(!verificarArchivosCargados()) {
			return;
		}
		int opcion;
		do {
			System.out.println();
			System.out.println("--- Administacion del curso ---");
			System.out.println("1) Cambiar paralelo de un alumno");
			System.out.println("2) Eliminar alumno del curso");
			System.out.println("3) Inscribir alumno nuevo");
			System.out.println("4) Volver");
			opcion = leerOpcionMenu(1,4);
			
			switch (opcion) {
				case 1: 
					cambiarParalelo();
					break;
				case 2:
					eliminarAlumno();
					break;
				case 3:
					inscribirAlumnoNuevo();
					break;
				case 4:
					System.out.println("Volviendo al menu principal...");
					break;
				
			}
		}while (opcion != 4);
	}


	private static void cambiarParalelo() {
		System.out.println("Ingresa RUT del alumno: ");
		String rut = teclado.nextLine().trim();
		
		int idx = buscarAlumnoPorRut(rut);
		if(idx == -1) {
			System.out.println("No existe ningun alumno con ese RUT.");
			return;
		}
		System.out.println("Alumno: " + nombreAlumno[idx] + " " + apellidoAlumno[idx] + 
				" (actualmente en " + paraleloAlumno[idx] + ")");
		System.out.println("Nuevo paralelo (C1/C2): ");
		String nuevoParalelo = teclado.nextLine().trim().toUpperCase();
	
		if(!validarParalelo(nuevoParalelo)) {
			System.out.println("Paralelo invalido, solo se acepta C1 o C2. No se realizaron cambios.");
			return;
		}
		paraleloAlumno[idx] = nuevoParalelo;
			
		int idxMiembro = buscarMiembroPorRut(rut);
		if(idxMiembro != -1) {
			paraleloMiembro[idxMiembro] = nuevoParalelo;
			
			guardarAlumnosEnArchivo();
			System.out.println("Paralelo actualizado! Cambios guardados en Alumnos.txt");
		}
	}

	private static void eliminarAlumno() {
		System.out.println("Ingrese  RUT del alumno a eliminar: ");
		String rut = teclado.nextLine().trim();
		
		int idx = buscarAlumnoPorRut(rut);
		if(idx == -1) {
			System.out.println("No existe ningun alumno a ese RUT.");
			return;
		}
		String nombreEliminado = nombreAlumno[idx];
		String apellidoEliminado = apellidoAlumno[idx];
		
		for(int i = idx; i < cantMiembros - 1; i++) {
			nombreMiembro[i] = nombreMiembro[i + 1];
			apellidoMiembro[i] = apellidoMiembro[i + 1];
			paraleloMiembro[i] = paraleloMiembro[i + 1];
		}
		cantAlumnos--;
		
		int idxMiembro = buscarMiembroPorRut(rut);
		if(idxMiembro != -1) {
			for(int i = idxMiembro; i < cantMiembros - 1; i++) {
				nombreMiembro[i] = nombreMiembro[i + 1];
				apellidoMiembro[i] = apellidoMiembro[i + 1];
				rutMiembro[i] = rutMiembro[i + 1];
				paraleloMiembro[i] = paraleloMiembro[i + 1];
			}
			cantMiembros--;
		}
		guardarAlumnosEnArchivo();
		System.out.println(nombreEliminado + " " + apellidoEliminado + " fue eliminado del curso y del grupo (si correspondia).");
	}

	private static void inscribirAlumnoNuevo() {
		if(cantAlumnos >= MAX) {
			System.out.println("No hay espacio disponible para inscribir mas alumnos");
			return;
		}
		System.out.println("Nombre: ");
		String nombre = teclado.nextLine().trim();
		System.out.println("Apellido: ");
		String apellido = teclado.nextLine().trim();
		System.out.println("RUT: ");
		String rut = teclado.nextLine().trim();
		System.out.println("Paralelo (C1/C2): ");
		String paralelo = teclado.nextLine().trim().toUpperCase();
		
		if(nombre.isEmpty() || apellido.isEmpty()) {
			System.out.println("El nombre y el apellido no pueden estar vacios.");
			return;
		}
		if(!validarRutNoVacio(rut)) {
			System.out.println("El RUT no puede estar vacio.");
			return;
		}
		if(!validarParalelo(paralelo)) {
			System.out.println("Paralelo invalido, solo se acepta C1 o C2");
			return;
		}
		if(buscarAlumnoPorRut(rut) != -1) {
			System.out.println("Ya existe un alumno inscrito con ese RUT.");
			return;
		}
		nombreAlumno[cantAlumnos] = nombre;
		apellidoAlumno[cantAlumnos] = apellido;
		rutAlumno[cantAlumnos] = rut;
		paraleloAlumno[cantAlumnos] = paralelo;
		cantAlumnos++;
		
		guardarAlumnosEnArchivo();
		System.out.println(nombre + " " + apellido + " fue inscrito wn la lista del curso (paralelo " + paralelo + ").");
		System.out.println("Recuerda que aun debe procesarse su ingreso al grupo (opcion 2 o 3).");
		
	}
	
	


	private static void guardarAlumnosEnArchivo() {
		try (BufferedWriter escritor = new BufferedWriter(new FileWriter("Alumnos.txt"))){
			for(int i = 0; i < cantAlumnos; i++) {
				escritor.write(nombreAlumno[i] + ";" + apellidoAlumno[i] + ";" + rutAlumno[i] + ";" + paraleloAlumno[i]);
				escritor.newLine();
			}
			
		}catch(IOException e) {
			System.out.println("Error al guardar Alumnos.txt: " + e.getMessage());
		}
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
