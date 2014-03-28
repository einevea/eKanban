import model.StoryType
import org.specs2.mutable.Specification

/**
 * Created by einevea on 27/03/2014.
 */
class StoryTypeSpec extends Specification {
  "The StoryType" should {
    "have 4 types" in {
      StoryType.values must have size(4)
    }
    "parse 'bug'" in {
      StoryType.usingName("bug") mustEqual StoryType.bug
    }
    "parse 'improvement'" in {
      StoryType.usingName("improvement") mustEqual StoryType.improvement
    }
    "parse 'feature'" in {
      StoryType.usingName("feature") mustEqual StoryType.feature
    }
    "parse 'task'" in {
      StoryType.usingName("task") mustEqual StoryType.task
    }

  }
}