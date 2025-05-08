package DmData.cases;

import DmData.cases.model.Case;
import DmData.cases.repository.CaseRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitCaseData implements CommandLineRunner {


    private final CaseRepository caseRepository;

    public InitCaseData(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        Case no1 = new Case("Python project", "/hej/hej/hej", "https://hej.dk");
        Case no2 = new Case("R project", "/hej/hej/hej", "https://hej.dk");
        Case no3 = new Case("Sterilcentralen project", "/hej/hej/hej", "https://hej.dk");
        Case no4 = new Case("Region Hovedstaden project", "/hej/hej/hej", "https://hej.dk");
        caseRepository.saveAll(List.of(no1, no2, no3, no4));
    }


}
