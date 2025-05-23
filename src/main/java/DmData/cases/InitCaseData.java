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

    }


}
