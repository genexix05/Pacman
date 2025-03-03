package org.example.pacman;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreManager {

    private static final String SCORE_FILE = "scores.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Guarda un registro en el archivo, manteniendo solo los 10 mejores.
     */
    public static void saveScore(ScoreRecord record) {
        // Cargar todos los registros
        List<ScoreRecord> records = loadScores();

        // Añadir el nuevo
        records.add(record);

        // Ordenar descendente
        Collections.sort(records);

        // Recortar a 10 si excede
        if (records.size() > 10) {
            records = records.subList(0, 10);
        }

        // Reescribir el archivo
        writeScores(records);
    }

    /**
     * Devuelve todos los registros (máximo 10 guardados), ordenados de mayor a menor.
     */
    public static List<ScoreRecord> loadScores() {
        List<ScoreRecord> records = new ArrayList<>();
        File file = new File(SCORE_FILE);
        if (!file.exists()) {
            return records;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Formato: NAME;SCORE;YYYY-MM-DD HH:mm
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    LocalDateTime dateTime = LocalDateTime.parse(parts[2], FORMATTER);

                    ScoreRecord record = new ScoreRecord(name, score, dateTime);
                    records.add(record);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        // Ordenar y devolver
        Collections.sort(records);
        return records;
    }

    /**
     * Escribe la lista de records al archivo.
     */
    private static void writeScores(List<ScoreRecord> records) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCORE_FILE))) {
            for (ScoreRecord r : records) {
                String line = String.format("%s;%d;%s",
                        r.getName(),
                        r.getScore(),
                        r.getDateTime().format(FORMATTER));
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
