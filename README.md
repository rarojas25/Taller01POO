# Taller 01 - Sistema de Control del Grupo POO

Sistema desarrollado en Java mediante programación estructurada y vectores estáticos (sin Programación Orientada a Objetos) para gestionar y filtrar el acceso al grupo de WhatsApp del curso de Programación Orientada a Objetos.

---

## 📋 Información de los Integrantes

* **Nombre:** Rocío Azucena Rojas Robledo | **RUT:** 21.694.049-0 | **Usuario GitHub:** rarojas25 (https://github.com/rarojas25) | **Carrera:** Ingeniería Civil en Computación e Informática (ICCI)

---

## ⚙️ Funcionamiento General

El programa opera a través de un menú interactivo por consola con las siguientes opciones:

1. **Cargar archivos:** lee y almacena la información de `Alumnos.txt` y `Solicitudes.txt` en vectores paralelos.
2. **Filtrado automático:** compara las solicitudes contra la lista oficial (por nombre y apellido) para admitir o rechazar ingresos, detectando duplicados.
3. **Inscripción manual:** permite inscribir rezagados de forma manual, ya sea por nombre completo o por RUT (gestionando los casos anónimos).
4. **Administración del curso:** permite cambiar paralelos (C1↔C2), eliminar alumnos o inscribir nuevos alumnos, manteniendo persistencia automática en el archivo `Alumnos.txt`.
5. **Generación de reportes:** crea archivos de salida independientes y versionados (`ReporteC1-VX.txt`, `ReporteC2-VX.txt`, `Rechazados-VX.txt`) reflejando el estado actual.
6. **Análisis estadístico:** muestra métricas clave como porcentaje de rechazo, tasa de admisión, distribución por paralelo, entre otras.

---

## 🗂️ Estructura del Proyecto

```
Taller01POO/
├── Taller/
│   └── logica/
│       └── Main.java        Clase unica con toda la logica del sistema
├── Alumnos.txt                Lista oficial de alumnos (nombre;apellido;rut;paralelo)
├── Solicitudes.txt             Solicitudes de ingreso al grupo (nombre-apellido)
├── Reportes/                   Reportes generados por la opcion 5 del menu
├── .project / .classpath       Configuracion del proyecto Eclipse
└── README.md
```

El programa **no utiliza Programación Orientada a Objetos**: toda la información se guarda en vectores estáticos paralelos (`String[]`, `boolean[]`) dentro de una única clase, `Main` (paquete `logica`), organizada en métodos estáticos según la opción del menú que implementan (carga de archivos, filtrado, inscripción, administración, reportes y estadísticas).

---

## 💻 Requisitos del Entorno

* JDK 8 o superior.
* Eclipse IDE (o cualquier entorno que permita compilar y ejecutar proyectos Java).

---

## 🛠️ Instrucciones de Ejecución

> **Importante:** los archivos `Alumnos.txt` y `Solicitudes.txt` deben estar en la raíz del proyecto (mismo nivel que la carpeta de código fuente), ya que el programa los busca ahí al ejecutarse.

### Opción 1: Importar en Eclipse (recomendada)

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/rarojas25/Taller01POO
   ```
2. En Eclipse: `File > Import... > General > Existing Projects into Workspace`.
3. Seleccionar la carpeta `Taller01POO` recién clonada.
4. Ejecutar `Main.java` (botón derecho sobre el archivo > `Run As > Java Application`).

### Opción 2: Por terminal

Desde la raíz del proyecto ya clonado:
```bash
mkdir -p bin
javac -d bin Taller/logica/Main.java
java -cp bin logica.Main
```
(ajusta la ruta `Taller/logica/Main.java` si el nombre de tu carpeta de código fuente es distinto)

---

## ✅ Manejo de Casos Borde y Validaciones

* **Archivos inexistentes:** si `Alumnos.txt` o `Solicitudes.txt` no se encuentran, se muestra un aviso y el programa continúa sin caerse.
* **Líneas mal formadas:** las líneas que no cumplen el formato esperado, o que tienen un paralelo distinto de C1/C2, se omiten con un aviso sin detener la carga.
* **Entradas de menú inválidas:** letras, campos vacíos o números fuera de rango vuelven a solicitar la opción en vez de interrumpir el programa.
* **Duplicados:** una persona no puede quedar admitida dos veces en el grupo, ni inscribirse dos veces en la lista del curso (se valida por RUT).
* **Capacidad máxima:** los vectores avisan y dejan de agregar registros al alcanzar el límite, sin lanzar errores.
* **Persistencia:** todo cambio hecho en la administración del curso se guarda de inmediato en `Alumnos.txt`.
* **Reportes versionados:** cada vez que se genera un reporte se crea un archivo nuevo (`-V1`, `-V2`, etc.) sin sobrescribir los anteriores.
