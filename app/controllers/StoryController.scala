package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.templates.Html
import model.{StoryType, ProjectDAO, Story, StoryDAO}
import play.api.data.Forms._

object StoryController extends Controller {

  def add(projectId: Long) = Action { implicit request =>
    Ok(views.html.helper2.storyForm(storyForm, projectId))
  }

  def save(projectId: Long) = Action { implicit request =>
    storyForm.bindFromRequest.fold(
      formWithErrors => Application.handleError(formWithErrors, views.html.helper2.storyForm(formWithErrors, projectId)),
      story =>{
        StoryDAO.create(projectId, story)
        Application.handleSuccess(f"Story: $story created")
      }
    )
  }

  def list(projectId: Long) = Action { implicit request =>
    val projects = ProjectDAO.readAll()
    val projectOpt = ProjectDAO.read(projectId)
    if(projectOpt.isDefined){
      Ok(views.html.stories("Your new application is ready.", projects, projectOpt.get))
    }else{
      BadRequest
    }
  }

  def get(projectId: Long, id: Long) = Action { implicit request =>
    val projects = ProjectDAO.readAll()
    val projectOpt = ProjectDAO.read(projectId)
    if(projectOpt.isDefined){
      Ok(views.html.stories("Your new application is ready.", projects, projectOpt.get))
    }else{
      BadRequest
    }
  }

  def changePhase(projectId: Long, storyId: Long, phaseId:Long) = Action { implicit request =>
    StoryDAO.updatePhase(projectId, storyId, phaseId)
    Ok
  }

  val storyForm = Form[Story](
    mapping(
      "projectId" -> longNumber,
      "title" -> nonEmptyText,
      "description" -> nonEmptyText,
      "storyType" -> nonEmptyText

    )
      ((projectId, title, description, storyType) => Story(-1, projectId, title, description, StoryType.usingName(storyType)))
      ((story: Story) => Option(story.projectId, story.title, story.description, story.storyType.toString))
  )
}