package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.templates.Html
import model.{PhaseDAO, Project, ProjectDAO, Story}
import play.api.data.Forms._
import org.apache.commons.codec.digest.DigestUtils
import play.api.libs.json.Json

object PhasesController extends Controller {

  def listJson = Action { implicit request =>
    val phases = PhaseDAO.readAll()
    Ok(Json.toJson(phases))
  }

  def list(projectId: Long) = Action { implicit request =>
    val projectOpt = ProjectDAO.read(projectId);
    if(projectOpt.isDefined){
      if(projectOpt.get.phases.isEmpty){
        Ok(Json.toJson(projectOpt))
      }else{
        Ok(Json.toJson(projectOpt))
      }
    }else{
      BadRequest
    }
  }
}