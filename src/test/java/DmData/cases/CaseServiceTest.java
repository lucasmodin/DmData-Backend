package DmData.cases;

import DmData.cases.dto.CaseSummaryDTO;
import DmData.cases.model.Case;
import DmData.cases.repository.CaseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {
    @Mock
    CaseRepository caseRepository;

    @InjectMocks
    CaseService svc;

    @Test
    void findAllSummaries_mapsCorrectly() {
        //dummy cases
        Case c1 = new Case("realGoodTitle", "path/path", "link1");
        c1.setId(1);
        Case c2 = new Case("realBadTitle", "path/path", "link2");
        c2.setId(2);

        //mock repository and to find them
        when(caseRepository.findAll()).thenReturn(List.of(c1, c2));

        //map to dto's
        List<CaseSummaryDTO> summaries = svc.findAllSummaries();

        //check if the list has 2
        assertEquals(2, summaries.size());
        CaseSummaryDTO dto1 = new CaseSummaryDTO(1, "realGoodTitle", "path/path");
        CaseSummaryDTO dto2 = new CaseSummaryDTO(2, "realBadTitle", "path/path");

        //check if the mapped dto's are equal to the created expected
        assertEquals(dto1, summaries.get(0));
        assertEquals(dto2, summaries.get(1));
    }

    @Test
    void findAll_returnsAllEntities() {
        //dummy case
        Case c1 = new Case("T1", "p1", "l1");
        Case c2 = new Case("T2", "p2", "l2");

        //return dummy list
        when(caseRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Case> all = svc.findAll();

        //check if the db has the same
        assertEquals(List.of(c1, c2), all);
        verify(caseRepository).findAll();
    }

    @Test
    void findById_returnsOptionalCase_whenExists() {
        Case c = new Case("Title", "img", "link");
        c.setId(42);
        when(caseRepository.findById(42)).thenReturn(Optional.of(c));

        Optional<Case> result = svc.findById(42);

        assertTrue(result.isPresent());
        assertEquals(c, result.get());
        verify(caseRepository).findById(42);
    }

    @Test
    void findById_returnsEmptyOptional_whenNotExists() {
        when(caseRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Case> result = svc.findById(99);

        assertTrue(result.isEmpty());
        verify(caseRepository).findById(99);
    }

    @Test
    void save_delegatesToRepository_andReturnsSaved() {
        Case input = new Case("New", "img", "link");
        Case saved = new Case("New", "img", "link");
        saved.setId(7);

        when(caseRepository.save(input)).thenReturn(saved);

        Case result = svc.save(input);

        assertEquals(7, result.getId());
        assertEquals("New", result.getTitle());
        verify(caseRepository).save(input);
    }




}