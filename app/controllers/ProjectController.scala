package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.templates.Html
import model.{PhaseDAO, Project, ProjectDAO, Story}
import play.api.data.Forms._
import org.apache.commons.codec.digest.DigestUtils

object ProjectController extends Controller {

  def add = Action { implicit request =>
    Ok(views.html.helper2.projectForm(projectForm))
  }

  def save = Action { implicit request =>
    projectForm.bindFromRequest.fold(
      formWithErrors => {
        Application.handleError(formWithErrors, views.html.helper2.projectForm(formWithErrors))
      },
      project =>{
        val newProject = ProjectDAO.create(project)
        val phases = PhaseDAO.readAll()
        var index = 1
        for(phase <- phases){
          PhaseDAO.createInProjectAtIndex(newProject.get.id, phase, index)
          index += 1
        }
        Application.handleSuccess(f"Project: $newProject created")
      }
    )
  }

  def list = Action { implicit request =>
    val projects = ProjectDAO.readAll()
    Ok(views.html.index("Your new application is ready.", projects))
  }

  def get(id: Long) = Action { implicit request =>
    val projects = ProjectDAO.readAll()
    val project = ProjectDAO.read(id)
    if(project.isDefined){
      Ok(views.html.stories("Your new application is ready.", projects, project.get))
    }else{
      BadRequest
    }
  }

  def getStoryboard(id: Long) = Action { implicit request =>
    val projects = ProjectDAO.readAll()
    val project = ProjectDAO.read(id)
    if(project.isDefined){
      Ok(views.html.storyboard("Your new application is ready.", projects, project.get))
    }else{
      BadRequest
    }
  }

  private def createProjectIdenticon(name: String) = {
    val hash = DigestUtils.md5Hex(name);
    Some(f"http://www.gravatar.com/avatar/$hash?r=PG&s=256&default=identicon");
  }

  val projectForm = Form[Project](
    mapping(
      "code" -> nonEmptyText,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "logoURL" -> text
    )
      ((code, name, description, logoURL) => Project(-1, code, name, description, if(logoURL.isEmpty) createProjectIdenticon(code) else Some(logoURL)))
      ((project: Project) => Option(project.code, project.name, project.description, project.logoURL.getOrElse("")))
  )
}