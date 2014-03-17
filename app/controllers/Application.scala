package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.templates.Html
import model.ProjectDAO

object Application extends Controller {

  def referer(implicit request: RequestHeader) =request.headers.get(REFERER).getOrElse("/")

  def index = Action {
    Redirect(routes.ProjectController.list)

  }

  def handleError(formWithErrors: Form[_], html: Html)(implicit request: RequestHeader) = render {
    case Accepts.Html() => BadRequest(html)
    //case Accepts.Json() => BadRequest(toJson(formWithErrors))
  }

  def handleSuccess(flashMessage: String)(implicit request: RequestHeader) = render {
    case Accepts.Html() => Redirect(referer).flashing("success" -> flashMessage)
    //case Accepts.Json() => Ok(Json.obj("status" -> "Ok"))
  }


}