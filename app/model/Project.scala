package model

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._
import anorm.~
import scala.Some
import anorm.~
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsObject
import scala.Some
import play.api.libs.json.JsNumber

/**
 * Created by einevea on 10/03/2014.
 */
case class Project(val id: Long = -1, val code: String, val name: String, val description: String, val logoURL: Option[String]){

  def stories = StoryDAO.readAll(id)
  def phases = PhaseDAO.readAllFromProject(id)
  def storiesByPhase(phase: Phase) = {
    StoryDAO.readAllFromProjectAndPhase(id, phase)
  }
}

object Project{

  implicit object projectJSON extends Format[Project]{
    def reads(json: JsValue): JsResult[Project] = JsSuccess(
      throw new UnsupportedOperationException()
    )

    def writes(project: Project): JsValue = JsObject(Seq(
      "id" -> JsNumber(project.id),
      "code" -> JsString(project.code),
      "name" -> JsString(project.name),
      "description" -> JsString(project.description),
      "logoURL" -> Json.toJson(project.logoURL),
      "phases" -> Json.toJson(project.phases)
    ))
  }
}

object ProjectDAO{
  val simpleParser = {
    get[Long]("id") ~ get[String]("code") ~ get[String]("name") ~ get[String]("description") ~ get[Option[String]]("logoURL") map {
      case id~code~name~description~logoURL=> Project(id, code, name, description, logoURL)
    }
  }

  def create(project: Project): Option[Project] = {
    create(project.code, project.name, project.description, project.logoURL)
  }

  def create(code: String, name: String, description: String, logoURL: Option[String]): Option[Project] = {
    val id = DB.withConnection{implicit c =>
      SQL("insert into projects (code, name, description, logoURL) values ({code}, {name}, {description}, {logoURL})")
        .on("code" -> code, "name" -> name, "description" -> description, "logoURL" -> logoURL)
        .executeInsert(scalar[Long].single)
    }

    Some(Project(id, code, name, description, logoURL))
  }

  def readAll(): List[Project] = {
    DB.withConnection{implicit c =>
      SQL("select * from projects").as(simpleParser *)
    }
  }

  def read(id: Long): Option[Project] = {
    DB.withConnection{implicit c =>
      SQL("select * from projects where id = {id}").on("id" -> id).singleOpt(simpleParser)
    }
  }
}
