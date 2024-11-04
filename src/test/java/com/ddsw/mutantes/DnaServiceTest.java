package com.ddsw.mutantes;

import com.ddsw.mutantes.dto.StatsResponse;
import com.ddsw.mutantes.entities.Dna;
import com.ddsw.mutantes.repositories.DnaRepository;
import com.ddsw.mutantes.services.DnaService;
import com.ddsw.mutantes.services.StatsService;
import com.ddsw.mutantes.validators.DnaValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DnaServiceTest {

    @InjectMocks
    // Inyección de mocks en DnaService
    private DnaService dnaService;

    @Mock
    private DnaRepository dnaRepository;

    @Mock
    private StatsService statsService;

    private final DnaValidator validator = new DnaValidator();

    @BeforeEach
    public void setUp() {
        // Inicializar los mocks y crear instancias de prueba
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testAnalyzeDna_AlreadyExists_ReturnsIsMutant() {

        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAGGA", "CCCCTA", "TCACTG"};
        String dnaSequence = String.join(",", dna);

        Dna existingDna = new Dna();
        existingDna.setMutant(true);
        when(dnaRepository.findByDna(dnaSequence)).thenReturn(Optional.of(existingDna));

        boolean result = dnaService.analyzeDna(dna);

        // Verificar resultados
        assertTrue(result);
        verify(dnaRepository, never()).save(any());
    }
    @Test
    public void testAnalyzeDna_IsMutant_SavesInRepository() {

        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAGGA", "CCCCTA", "TCACTG"};

        when(dnaRepository.findByDna(any())).thenReturn(Optional.empty());
        when(dnaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = dnaService.analyzeDna(dna);

        // Verificar resultados
        assertTrue(result);
        verify(dnaRepository).save(any());
    }
    @Test
    public void testGetStats_ReturnsCorrectStats() {

        StatsResponse mockStats = new StatsResponse(5L, 3L, 1.6666666666666667);

        when(statsService.getStats()).thenReturn(mockStats);

        StatsResponse stats = statsService.getStats();

        assertEquals(5L, stats.getCountMutantDna());
        assertEquals(3L, stats.getCountHumanDna());
        assertEquals(1.6666666666666667, stats.getRatio());
    }

    @Test
    public void testRows() {
        String[] dna = {
                "AAAATG",
                "TGCAGT",
                "GCTTCC",
                "CCCCTG",
                "GTAGTC",
                "AGTCAC"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testColumns() {
        String[] dna = {
                "AGAATG",
                "TGCAGT",
                "GCTTCC",
                "GTCCTC",
                "GTAGTC",
                "GGTCAC"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testMainDiagonals() {
        String[] dna = {
                "AGAATG",
                "TACAGT",
                "GCAGCC",
                "TTGATG",
                "GTAGTC",
                "AGTCAA"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testSecondaryLeftDiagonals() {
        String[] dna = {
                "ATAATG",
                "GTTAGT",
                "GGCTCG",
                "TTGATG",
                "GTAGTC",
                "AGTCAA"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testSecondaryRightDiagonals() {
        String[] dna = {
                "ATAATG",
                "GTATGA",
                "GCTTAG",
                "TTTAGG",
                "GTAGTC",
                "AGTCAA"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testTertiaryLeftDiagonals() {
        String[] dna = {
                "ATGATG",
                "GTAGTA",
                "CCTTGG",
                "TCTAGG",
                "GGCGTC",
                "AGTCAA"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testTertiaryRightDiagonals() {
        String[] dna = {
                "ATGATG",
                "GTATTA",
                "AATTGG",
                "ACTAGT",
                "GGAGTC",
                "AGGCAA"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testNonMutant() {
        String[] dna = {
                "ATGATG",
                "GTCTTA",
                "AATTGG",
                "ACTAGT",
                "GGATTC",
                "AGGCAA"
        };
        assertFalse(DnaService.isMutant(dna));
    }

    /* ========= TESTS DEL PROFE ========== */
    @Test
    public void testMutant1() {
        String[] dna = {
                "AAAA",
                "CCCC",
                "TCAG",
                "GGTC"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testNonMutant1() {
        String[] dna = {
                "AAAT",
                "AACC",
                "AAAC",
                "CGGG"
        };
        assertFalse(DnaService.isMutant(dna));
    }

    @Test
    public void testMutant2() {
        String[] dna = {
                "TGAC",
                "AGCC",
                "TGAC",
                "GGTC"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testMutant3() {
        String[] dna = {
                "AAAA",
                "AAAA",
                "AAAA",
                "AAAA"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testNonMutant2() {
        String[] dna = {
                "TGAC",
                "ATCC",
                "TAAG",
                "GGTC"
        };
        assertFalse(DnaService.isMutant(dna));
    }

    @Test
    public void testMutant4() {
        String[] dna = {
                "TCGGGTGAT",
                "TGATCCTTT",
                "TACGAGTGA",
                "AAATGTACG",
                "ACGAGTGCT",
                "AGACACATG",
                "GAATTCCAA",
                "ACTACGACC",
                "TGAGTATCC"
        };
        assertTrue(DnaService.isMutant(dna));
    }

    @Test
    public void testMutant5() {
        String[] dna = {
                "TTTTTTTTT",
                "TTTTTTTTT",
                "TTTTTTTTT",
                "TTTTTTTTT",
                "CCGACCAGT",
                "GGCACTCCA",
                "AGGACACTA",
                "CAAAGGCAT",
                "GCAGTCCCC"
        };
        assertTrue(DnaService.isMutant(dna));
    }
    @Test
    public void testIsValid_NullDna() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    public void testIsValid_EmptyArray() {
        assertFalse(validator.isValid(new String[]{}, null));
    }

    @Test
    public void testIsValid_DifferentLengthSequences() {
        String[] dna = {"ATGC", "AGT", "TTATG", "AGAGGA"};
        assertFalse(validator.isValid(dna, null));
    }

    @Test
    public void testIsValid_ContainsInvalidCharacters() {
        String[] dna = {"ATGZ", "CAGT", "TTATG", "AGAGGA"};
        assertFalse(validator.isValid(dna, null));
    }

    @Test
    public void testIsValid_ValidDna() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAGGA", "CCCCTA", "TCACTG"};
        assertTrue(validator.isValid(dna, null));
    }

    @Test
    public void testIsValid_ValidDnaWithNullSequence() {
        String[] dna = {"ATGCGA", null, "TTATGT", "AGAGGA", "CCCCTA", "TCACTG"};
        assertFalse(validator.isValid(dna, null));
    }

}