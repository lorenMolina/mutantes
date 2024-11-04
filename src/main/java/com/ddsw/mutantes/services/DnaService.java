package com.ddsw.mutantes.services;

import com.ddsw.mutantes.entities.Dna;
import com.ddsw.mutantes.repositories.DnaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class DnaService {

    private final DnaRepository dnaRepository;
    private static final int SEQUENCE_LENGTH = 4;

    @Autowired
    public DnaService(DnaRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    public static boolean isMutant(String[] dna) {
        int n = dna.length;

        // Buscar si hay mas de 1 secuencia en las respectivas direcciones.
        long sequenceCount = Stream.of(
                        checkDirection(dna, n, 0, 1),   // Fila
                        checkDirection(dna, n, 1, 0),   // Columna
                        checkDirection(dna, n, 1, 1),   // Diagonal hacia la derecha
                        checkDirection(dna, n, 1, -1)   // Diagonal hacia la izquierda
                ).mapToLong(count -> count)  // Contador total de secuencias
                .sum();

        return sequenceCount > 1;  //si hay mas de una secuencia mutante, es mutante
    }

    private static long checkDirection(String[] dna, int n, int dx, int dy) {
        // Uso `flatMap` para recorrer toda la matriz y contar las secuencias en la dirección (dx, dy)
        return IntStream.range(0, n)
                .flatMap(i -> IntStream.range(0, n)
                        .map(j -> countSequencesInDirection(dna, i, j, dx, dy, n)))
                .sum();  // Sumar las secuencias mutantes
    }

    private static int countSequencesInDirection(String[] dna, int x, int y, int dx, int dy, int n) {
        int count = 0;

        // Verifica si podemos analizar una secuencia completa
        while (x + (SEQUENCE_LENGTH - 1) * dx < n && y + (SEQUENCE_LENGTH - 1) * dy >= 0 && y + (SEQUENCE_LENGTH - 1) * dy < n) {
            char first = dna[x].charAt(y);
            boolean isMutantSequence = true;

            for (int i = 1; i < SEQUENCE_LENGTH; i++) {
                if (dna[x + i * dx].charAt(y + i * dy) != first) {
                    isMutantSequence = false;
                    break;
                }
            }

            if (isMutantSequence) {
                count++;
                // desplazo la posición
                x += SEQUENCE_LENGTH * dx;
                y += SEQUENCE_LENGTH * dy;
            } else {
                // Si NO  es mutante. solo se avanza 1 posición
                x += dx;
                y += dy;
            }
        }
        return count;
    }

    public boolean analyzeDna(String[] dna) {
        String dnaSequence = String.join(",", dna);

        // Busco el ADN en la data base
        Optional<Dna> existingDna = dnaRepository.findByDna(dnaSequence);
        if (existingDna.isPresent()) {
            // retomo el resultado si es un ADN analizado
            return existingDna.get().isMutant();
        }

        //Si el ADN es mutante y lo guardo en la data base
        boolean isMutant = isMutant(dna);
        Dna dnaEntity = Dna.builder()
                .dna(dnaSequence)
                .isMutant(isMutant)
                .build();
        dnaRepository.save(dnaEntity);

        return isMutant;
    }
}
