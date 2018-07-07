package controllers

import javax.inject._
import play.api.mvc._
import play.api.i18n._
import scala.concurrent.ExecutionContext
import play.api.libs.json._
import scala.collection.immutable.HashMap

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def test = Action {
    Ok(Json.toJson("data-" + System.currentTimeMillis()))
  }

}
