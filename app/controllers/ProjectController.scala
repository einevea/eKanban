package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.templates.Html
import model.{Project, ProjectDAO, Story}
import play.api.data.Forms._

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
        ProjectDAO.create(project)
        Application.handleSuccess(f"Project: $project created")
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

  val projectForm = Form[Project](
    mapping(
      "code" -> nonEmptyText,
      "name" -> nonEmptyText,
      "description" -> nonEmptyText,
      "logoURL" -> text
    )
      ((code, name, description, logoURL) => Project(-1, code, name, description, if(logoURL.isEmpty) None else Some(logoURL)))
      ((project: Project) => Option(project.code, project.name, project.description, project.logoURL.getOrElse("")))
  )
}