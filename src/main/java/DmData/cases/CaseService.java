package DmData.cases;

import DmData.cases.dto.CaseSummaryDTO;
import DmData.cases.model.Case;
import DmData.cases.repository.CaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CaseService {

    private final CaseRepository caseRepository;

    public CaseService(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    public List<CaseSummaryDTO> findAllSummaries() {
        return caseRepository.findAll().stream()
                .map(c -> new CaseSummaryDTO(c.getId(), c.getTitle(), c.getImagePath()))
                .toList();
    }

    public List<Case> findAll() {
        return caseRepository.findAll();
    }

    public Optional<Case> findById(int id) {
        return caseRepository.findById(id);
    }

    //for CMS when it is up
    public Case save(Case c) {
        return caseRepository.save(c);
    }
}
