package model

/**
 * Created by einevea on 19/03/2014.
 */

object StoryType extends Enumeration{
  class MyValue(override val id: Int, val url:String) extends Val(i=id)
  object MyValue{
    def apply(url: String) = new MyValue(nextId, url)
  }

  final def usingName(s: String): StoryType = {
    withName(s) match {
      case st: StoryType.StoryType => st
      case _ => throw new ClassCastException
    }
  }


  type StoryType = MyValue

  val bug = MyValue("bug.svg")
  val improvement = MyValue("improvement.svg")
  val feature = MyValue("feature.svg")
  val task = MyValue("task.svg")
}




