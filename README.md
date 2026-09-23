# Taller 01 - Sistema de Control del Grupo POO

Sistema desarrollado en Java mediante programación estructurada y vectores estáticos para gestionar y filtrar el acceso al grupo de WhatsApp del curso.

## 📋 Información de los Integrantes
* **Nombre:** Rocio Rojas R
* **Carrera:** ICCI

---

## ⚙️ Funcionamiento General
El programa opera a través de un menú interactivo por consola con las siguientes opciones:
1. **Cargar archivos:** Lee y almacena la información de `Alumnos.txt` y `Solicitudes.txt` en vectores paralelos.
2. **Filtrado automático:** Compara las solicitudes contra la lista oficial (por nombre y apellido) para admitir o rechazar ingresos, detectando duplicados.
3. **Inscripción manual:** Permite inscribir rezagados de forma manual, ya sea por nombre completo o por RUT (gestionando los casos anónimos).
4. **Administración del curso:** Permite cambiar paralelos ($C1 \leftrightarrow C2$), eliminar alumnos o inscribir nuevos alumnos, manteniendo **persistencia automática** en el archivo `Alumnos.txt`.
5. **Generación de reportes:** Crea archivos de salida independientes y versionados (`ReporteC1-VX.txt`, `ReporteC2-VX.txt`, `Rechazados-VX.txt`) reflejando el estado actual.
6. **Análisis estadístico:** Muestra métricas clave como porcentaje de rechazo, tasa de admisión, distribución por paralelo, entre otros.

---

## 🛠️ Instrucciones de Ejecución y Testeo

1. **Clonar el repositorio:**
   ```bash
   git clone rarojas25/https://github.com/rarojas25/Taller01POO
