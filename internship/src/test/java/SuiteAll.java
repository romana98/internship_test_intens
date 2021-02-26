import com.project.internship.api.CandidateControllerTest;
import com.project.internship.api.SkillControllerTest;
import com.project.internship.service.CandidateServiceIntegrationTest;
import com.project.internship.service.CandidateServiceUnitTest;
import com.project.internship.service.SkillServiceIntegrationTest;
import com.project.internship.service.SkillServiceUnitTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@Suite.SuiteClasses({
        //Unit tests
        CandidateControllerTest.class,
        SkillControllerTest.class,
        CandidateServiceUnitTest.class,
        SkillServiceUnitTest.class,

        //Integration tests
        CandidateServiceIntegrationTest.class,
        SkillServiceIntegrationTest.class
})
@RunWith(Suite.class)
public class SuiteAll {
}
