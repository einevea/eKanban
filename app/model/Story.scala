package model

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current


/**
 * Created by einevea on 08/03/2014.
 */
case class Story(val id: Long, val title: String, val phase: String)

object StoryDAO{
  val simpleParser = {
      get[Long]("id") ~ get[String]("title") ~ get[String]("phase") map {
      case id~title~phase=> Story(id, title, phase)
    }
  }

  def create(story: Story){
    create(story.title, story.phase)
  }

  def create(title: String, phase: String): Option[Story] = {
    val id = DB.withConnection{implicit c =>
        SQL("insert into stories (title, phase) values ({title}, {phase})")
        .on("title" -> title, "phase" -> phase)
        .executeInsert(scalar[Long].single)
    }

    Some(Story(id, title, phase))
  }

}
