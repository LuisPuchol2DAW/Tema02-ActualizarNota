import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

//Las palabras Metodo tienen 2 letras o porque si no "todo" el texto aparece en amarillo.
public class ActualizarNota {

    //Dejamos los paths fuera del main para que sean accesibles desde cualquier métodoo
    private final static Path ROOT = Paths.get("C:\\Program Files");
    private static Path NOTESPATH = null;
    static Path DESTINATION = Paths.get("C:\\NotasDAM2Copia.txt");

    //Usamos el if porque en el caso de que se ejecute sin archivo no salte error
    //Aunque podríamos asumir que siempre existe el archivo eliminar dicho if
    public static void main(String[] args) throws IOException {
        if (recursiveSearch(ROOT.toFile())) {
            modifyNotes(DESTINATION, "LUIS ENRIQUE PUCHOL DELGADO", "5");
            returnModifiedNotes();
            eraseModifiedNotes();
        }
    }

    /**
     * Métodoo para iniciar la busqueda recursiva en una carpeta.
     *
     * @param directory Se inicia la busqueda en una carpeta.
     * @return un Boolean, dependiendo si la busqueda tiene exito o no.
     */
    public static boolean recursiveSearch(File directory) throws IOException {
        File[] files = directory.listFiles();
        //condicion de carrera
        if (files == null) {
            return false;
        }

        for (File file : files) {
            if (isDirectoryAndContainsTarget(file)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Métodoo para inciar la busqueda recursiva en caso de ser una carpeta,
     * o pasar al siguiente metodo si es un archivo.
     *
     * @param file aunque puede ser una carpeta también.
     * @return un Boolean que servira para seguir con la busqueda en caso de ser false.
     */
    public static boolean isDirectoryAndContainsTarget(File file) throws IOException {
        if (file.isDirectory()) {
            return recursiveSearch(file);
        }
        return isTargetFile(file);
    }

    /**
     * Métodoo una vez que lo que tenemos NO es una carpeta,
     * confirmamos que sea el archivo que necesitamos,
     * se hace una copia y guardamos la ruta de donde se ha encontrado.
     * ***IMPORTANTE EJECUTAR COMO ADMINISTRADOR, MOTIVO EXPLICADO EN EL METODO***
     *
     * @param file archivo a analizar.
     * @return El último Boolean de la búsqueda, el cual nos da el true en caso de ser el archivo solicitado.
     */
    public static boolean isTargetFile(File file) throws IOException {
        if (file.getName().equals("NotasDAM2.txt")) {
            Files.copy(file.toPath(), DESTINATION, StandardCopyOption.REPLACE_EXISTING);

            //Si el IDE no se ejecuta como administrador esta linea generara un error al pedir la ruta completa
            NOTESPATH = file.toPath();
            return true;
        }
        return false;
    }

    /**
     * Métodoo encargado de leer las lineas, buscar la coincidencia del nombre en la linea,
     * separarla, localizar la nota, modificarla, volver a juntarla con la linea,
     * añadirla al documento modificado y escribirla.
     *
     * @param notes,studentName,studentNote .
     * @return -.
     */
    public static void modifyNotes(Path notes, String studentName, String studentNote) throws IOException {
        List<String> lines = Files.readAllLines(notes);
        List<String> modifiedLine = new ArrayList<>();
        for (String line : lines) {
            if (line.contains(studentName)) {
                String[] dividedLine = line.split(" ");
                dividedLine[dividedLine.length - 1] = studentNote;
                line = String.join(" ", dividedLine);
            }
            modifiedLine.add(line);
        }
        Files.write(DESTINATION, modifiedLine);
    }

    /**
     * Métodoo que copia el archivo modificado en la ubicación del original con su nombre original.
     *
     * @param -.
     * @return -.
     */
    public static void returnModifiedNotes() throws IOException {
        Files.copy(DESTINATION, NOTESPATH, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Métodoo borra nuestra copia modificada puesto que ya la hemos copiado y no es necesaria.
     *
     * @param -.
     * @return -.
     */
    public static void eraseModifiedNotes() throws IOException {
        Files.delete(DESTINATION);
    }
}