package model

import anorm._
import anorm.SqlParser._
import play.api.db.DB
import play.api.Play.current


/**
 * Created by einevea on 08/03/2014.
 */
case class Story( val id: Long = -1,
                  val projectId: Long,
                  val title: String,
                  val description: String,
                  val storyType: StoryType.StoryType,
                  val phaseId: Option[Long] = None){
  def phase: Option[Phase] = {PhaseDAO.read(phaseId.getOrElse(0))}
}

object StoryDAO{
  val simpleParser = {
      get[Long]("id") ~ get[Long]("project_id") ~ get[String]("title") ~ get[Long]("phase_id") ~ get[String]("description")~ get[String]("storyType") map {
      case id~projectId~title~phaseId~description~storyType => Story(id, projectId, title, description, StoryType.usingName(storyType), Some(phaseId))
    }
  }

  def create(projectId: Long, story: Story){
    create(projectId, story.title, story.description, story.storyType.toString())
  }

  def create(projectId: Long, title: String, description:String, storyType: String): Option[Story] = {
    val id = DB.withConnection{implicit c =>
        SQL("insert into stories (project_id, title, description, storyType) values ({project_id}, {title}, {description}, {storyType})")
        .on("project_id" -> projectId,
            "title" -> title,
            "description" -> description,
            "storyType" -> storyType)
        .executeInsert(scalar[Long].single)
    }

    Some(Story(id, projectId, title, description, StoryType.bug))
  }

  def readAll(projectId: Long): List[Story] = {
    DB.withConnection{implicit c =>
      SQL("select * from stories where project_id = {project_id}").on("project_id" -> projectId).as(simpleParser *)
    }
  }

  def readAllFromProjectAndPhase(projectId: Long, phase: Phase): List[Story] = {
    DB.withConnection{implicit c =>
      SQL("select * from stories where project_id = {project_id} and phase_id = {phase_id}")
        .on("project_id" -> projectId, "phase_id" -> phase.id)
        .as(simpleParser *)
    }
  }

  def updatePhase(projectId: Long, storyId:Long, phaseId: Long) = {
    DB.withConnection{implicit c =>
      SQL(
        """
          update stories set phase_id = {phase_id}
          where project_id = {project_id} and id = {id}
        """)
        .on("project_id" -> projectId, "phase_id" -> phaseId, "id" -> storyId)
        .executeUpdate()
    }
  }
}
