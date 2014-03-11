package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.templates.Html
import model.{Story, StoryDAO}
import play.api.data.Forms._

object StoryController extends Controller {

  def add = Action { implicit request =>
    Ok(views.html.helper2.storyForm(storyForm))
  }

  def save = Action { implicit request =>
    storyForm.bindFromRequest.fold(
      formWithErrors => Application.handleError(formWithErrors, views.html.helper2.storyForm(formWithErrors)),
      story =>{
        StoryDAO.create(story)
        Application.handleSuccess(f"Story: $story created")
      }
    )
  }

  val storyForm = Form[Story](
    mapping(
      "id" -> longNumber,
      "title" -> nonEmptyText,
      "phase" -> text
    )
      ((id, title, phase) => Story(id, title, phase))
      ((story: Story) => Option(story.id, story.title, story.phase))
  )
}