package models

import scala.xml.Node
import scala.xml.NodeSeq
import play.api.Logger

object Pagination {
  private val LOG = Logger(Pagination.getClass)

  def getPagination(currentPage: Int, totalPages: Int, visibleButtons: Int)(implicit ap: PageQueryGenerator): NodeSeq = {

    var links = Seq[Node]()

    // button 'prev'
    if (currentPage != 1) {
      links ++= (if (currentPage != 1)
        <li><a href={ ap.toQueryString(currentPage - 1) }>«</a></li>
      else
        <li class="disabled"><a href="#">«</a></li>)
    }

    // middle buttons
    var diff = currentPage - visibleButtons - 1
    var firstPageIndex = if (diff < 1) 1 else diff
    for (a <- firstPageIndex to totalPages) {
      links ++= (if (Math.abs(currentPage - a) > visibleButtons) {
        <li><a href={ ap.toQueryString(a) }>...</a></li>
      } else {
        if (currentPage == a) {
          <li class="active"><a href="#">{ a }</a></li>
        } else {
          <li><a href={ ap.toQueryString(a) }>{ a }</a></li>
        }
      })
    }

    // button 'next'
    if (currentPage != totalPages) {
      links ++= <li><a href={ ap.toQueryString(currentPage + 1) }>»</a></li>
    }

    links
  }
}

trait PageQueryGenerator {
  val page: Int
  def toQueryString(): String
  def toQueryString(page: Int): String
  def toQueryString(page: Int, list: List[(String, AnyVal)]): String
}