import com.project.internship.api.CandidateControllerIntegrationTest;
import com.project.internship.api.CandidateControllerUnitTest;
import com.project.internship.api.SkillControllerIntegrationTest;
import com.project.internship.api.SkillControllerUnitTest;
import com.project.internship.service.CandidateServiceIntegrationTest;
import com.project.internship.service.CandidateServiceUnitTest;
import com.project.internship.service.SkillServiceIntegrationTest;
import com.project.internship.service.SkillServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
        //Unit tests
        CandidateControllerUnitTest.class,
        SkillControllerUnitTest.class,
        CandidateServiceUnitTest.class,
        SkillServiceUnitTest.class,

        //Integration tests
        CandidateControllerIntegrationTest.class,
        SkillControllerIntegrationTest.class,
        CandidateServiceIntegrationTest.class,
        SkillServiceIntegrationTest.class
})
@RunWith(Suite.class)
public class SuiteAll {
}
