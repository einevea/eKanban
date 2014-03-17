package model

import anorm.SqlParser._
import anorm._
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._
import anorm.~
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import scala.Some

/**
 * Created by einevea on 14/03/2014.
 */
case class Phase(id: Long, name: String, size: Int = 2, order: Int = 0)

object Phase{

  implicit object phaseJSON extends Format[Phase]{
    def reads(json: JsValue): JsResult[Phase] = JsSuccess(
      throw new UnsupportedOperationException()
    )

    def writes(phase: Phase): JsValue = JsObject(Seq(
      "id" -> JsNumber(phase.id),
      "name" -> JsString(phase.name),
      "size" -> JsNumber(phase.size),
      "order" -> JsNumber(phase.order)
    ))
  }
}

object PhaseDAO{

  val simpleParser = {
    get[Long]("id") ~ get[String]("name")  map {
      case id~name=> Phase(id, name)
    }
  }

  val fromProjectParser = {
    get[Long]("id") ~ get[String]("name") ~ get[Int]("capacity") ~ get[Int]("position")  map {
      case id~name~capacity~position=> Phase(id, name, capacity, position)
    }
  }

  def create(phase: Phase){
    create(phase.name)
  }

  def create(name: String): Option[Phase] = {
    val id = DB.withConnection{implicit c =>
      SQL("insert into phases (name) values ({name})")
        .on("name" -> name)
        .executeInsert(scalar[Long].single)
    }

    Some(Phase(id, name))
  }

  def readAll(): List[Phase] = {
    DB.withConnection{implicit c =>
      SQL("select * from phases").as(simpleParser *)
    }
  }

  def read(id: Long): Option[Phase] = {
    DB.withConnection{implicit c =>
      SQL("select * from phases where id = {id}").on("id" -> id).singleOpt(simpleParser)
    }
  }

  def readAllFromProject(projectId: Long): List[Phase] = {
    DB.withConnection{implicit c =>
      SQL(
        """
          select p.id, p.name, pp.capacity, pp.position
          from phases p join project_phases pp on p.id = pp.phase_id
          where pp.project_id = {projectId}
        """).on("projectId" -> projectId).as(fromProjectParser *)
    }
  }

  def readFromProject(id: Long, projectId: Long): Option[Phase] = {
    DB.withConnection{implicit c =>
      SQL("select * from phases where id = {id}").on("id" -> id).singleOpt(fromProjectParser)
    }
  }

  def createInProjectAtIndex(projectId: Long, phase: Phase, position: Int){
    val id = DB.withConnection{implicit c =>
      SQL("insert into project_phases (project_id, phase_id, position) values ({projectId}, {phaseId}, {position})")
        .on("projectId" -> projectId, "phaseId" -> phase.id, "position" -> position)
        .executeInsert()
    }
  }
}


