package DmData.cases;

import DmData.cases.dto.CaseSummaryDTO;
import DmData.cases.model.Case;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CaseController.class)
class CaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean //ved godt den er deprecated men ved ikke lige hvad vi skal skifte den til
    private CaseService caseService;

    // --- for at kunne skrive objekternes værdier til string hurtigt ---
    private static final ObjectMapper M = new ObjectMapper();



    @Test
    @DisplayName("liste af summaries -> Http code 200")
    void getAllCases_returnsSummaries() throws Exception {
        List<CaseSummaryDTO> summaries = List.of(
                new CaseSummaryDTO(1, "T1", "/img/img"),
                new CaseSummaryDTO(2, "T2", "/img/img")
        );
        when(caseService.findAllSummaries()).thenReturn(summaries);

        mockMvc.perform(get("/api/cases"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("T1")))
                .andExpect(jsonPath("$[1].id",    is(2)))
                .andExpect(jsonPath("$[1].title", is("T2")));
        verify(caseService).findAllSummaries();
    }

    @Test
    @DisplayName("GET /api/cases/{id} → 404 og fejl når ikke findes")
    void getCaseById_whenNotFound() throws Exception {
        when(caseService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cases/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", containsString("Case med id 99 ikke fundet")));

        verify(caseService).findById(99);
    }

    @Test
    @DisplayName("POST /api/cases → 201 og Location‐header ved succes")
    void addCase_validInput_returnsCreated() throws Exception {
        Case input = new Case("NewCase", "/img/new", "http://new");
        // output fra service: har fået tildelt id = 7
        Case saved = new Case("NewCase", "/img/new", "http://new");
        saved.setId(7);

        when(caseService.save(any(Case.class))).thenReturn(saved);

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(M.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",    is(7)))
                .andExpect(jsonPath("$.title", is("NewCase")));

        verify(caseService).save(any(Case.class));
    }

    @Test
    @DisplayName("POST /api/cases → 400 ved tom titel")
    void addCase_blankTitle_returnsBadRequest() throws Exception {
        Case invalid = new Case("", "/img/x", "http://l");

        mockMvc.perform(post("/api/cases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(M.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Title må ikke være tom")));

        verify(caseService, never()).save(any());
    }
}


