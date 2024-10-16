import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ActualizarNotaTest {
    private Path testFile;
    private Path destinationFile;

    /**
     * Antes de cada prueba, se genera el archivo de la prueba correspondiente.
     *
     * @param -.
     * @return -.
     */
    @BeforeEach
    public void setUp() throws IOException {

        Path tempDir = Files.createTempDirectory("tempDir");
        testFile = tempDir.resolve("NotasDAM2.txt");
        destinationFile = tempDir.resolve("NotasDAM2Copia.txt");

        List<String> lines = new ArrayList<>();
        lines.add("1 SEGURA DURAN BARTOMEU 9");
        lines.add("2 LÓPEZ RAMIREZ FRANCISCO JOSÉ 4");
        lines.add("3 FERNANDEZ CASAMAYOR JUAN 4");
        lines.add("4 DIAZ ESPAÑA JOSE MIGUEL 4");
        lines.add("5 LUIS ENRIQUE PUCHOL DELGADO 4");
        lines.add("6 ALEJANDRO TORRES FERNANDEZ 4");
        Files.write(testFile, lines);
    }

    /**
     * Devuelve true si detecta nuestro archivo temporal correctamente.
     *
     * @param -.
     * @return -.
     */
    @Test
    public void testIsTargetFile() throws IOException {
        assertTrue(ActualizarNota.isTargetFile(testFile.toFile()));
    }

    /**
     * Le pasamos los parametros que tiene que modificar, y comprobamos si han sido modificados correctamente.
     *
     * @param -.
     * @return -.
     */
    @Test
    public void testModifyNotes() throws IOException {
        ActualizarNota.DESTINATION = destinationFile;
        ActualizarNota.modifyNotes(testFile, "LUIS ENRIQUE PUCHOL DELGADO", "5");

        List<String> modifiedLines = Files.readAllLines(destinationFile);

        assertTrue(modifiedLines.stream().anyMatch(line -> line.contains("5 LUIS ENRIQUE PUCHOL DELGADO 5")));
    }

    /**
     * Hacemos 2 comprobaciones, la de un nuevo alumno y que se ha modificado correctamente con una nota diferente,
     * y que no se ha modificado el valor de otro alumno, que se mantiene igual.
     *
     * @param -.
     * @return -.
     */
    @Test
    public void testModifyNotesForDifferentStudent() throws IOException {
        ActualizarNota.DESTINATION = destinationFile;
        ActualizarNota.modifyNotes(testFile, "ALEJANDRO TORRES FERNANDEZ", "6");

        List<String> modifiedLines = Files.readAllLines(destinationFile);

        assertTrue(modifiedLines.stream().anyMatch(line -> line.contains("6 ALEJANDRO TORRES FERNANDEZ 6")));
        assertTrue(modifiedLines.stream().anyMatch(line -> line.contains("5 LUIS ENRIQUE PUCHOL DELGADO 4")));
    }
}