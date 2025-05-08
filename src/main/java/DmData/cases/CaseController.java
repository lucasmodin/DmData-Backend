package DmData.cases;

import DmData.cases.dto.CaseSummaryDTO;
import DmData.cases.model.Case;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    //sender dto objektet, hvor det hele ikke behøves at sendes med. Kommer først på den individuelle
    @GetMapping
    public ResponseEntity<List<CaseSummaryDTO>> getAllCases() {
        List<CaseSummaryDTO> list = caseService.findAllSummaries();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCaseById(@PathVariable int id) {
        return caseService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> {
                    Map<String, String> error = new HashMap<>();
                    error.put("error", "Case med id " + id + " ikke fundet");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    @PostMapping
    public ResponseEntity<?> addCase(@RequestBody Case newCase) {
        //først validerer vi casen
        if(newCase.getTitle() == null || newCase.getTitle().isBlank()) {
            Map<String, String> error = Map.of("error", "Title må ikke være tom");
            return ResponseEntity.badRequest().body(error);
        }

        Case saved = caseService.save(newCase);

        //giver besked til admin i frontend om, hvor casen er gemt (url)
        //f.eks. /api/cases/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);
    }
}
