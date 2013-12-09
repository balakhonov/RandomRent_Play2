package controllers.mapping

import scala.xml.Elem
import ApartmentsFilters._
import models.db.dao.DistrictDao
import play.api.Logger
import util.control.Breaks._
import scala.xml.Text

class ApartmentFilters(val reqParMam: ApartmentsP) extends AbstractFilters(reqParMam) {
  private val LOG = Logger(getClass)
  private var distrits = DistrictDao.getByCity(reqParMam.cityId).sortBy(_.name)

  private val adTypes = List(("0", "Сниму"), ("1", "Сдам"))
  private val periods = List(("0", "Длительно"), ("1", "Посуточно"))

  /**
   * 
   */
  def swhoAdTypes(): Elem = genFilter(adTypes, AD_TYPE_P, true)

  /**
   * 
   */
  def swhoPeriods(): Elem = genFilter(periods, PERIOD_P, true)

  /**
   * 
   */
  def showPriceSlider(min: Int, max: Int, step: Int): Elem = {
    var action = reqParMam.toQueryString(reqParMam.page, reqParMam.paramMap.toList)
    var start = (reqParMam.paramMap.get(PRICE + SUF_GTE).getOrElse(min)).toString.toInt
    var end = reqParMam.paramMap.get(PRICE + SUF_LTE).getOrElse(max).toString.toInt

    rangeSlider(start, end, min, max, step, PRICE, action, reqParMam.paramMap)
  }

  /**
   * 
   */
  def showRoomsSlider(min: Int, max: Int, step: Int): Elem = {
    var action = reqParMam.toQueryString(reqParMam.page, reqParMam.paramMap.toList)
    var start = (reqParMam.paramMap.get(ROOMS + SUF_GTE).getOrElse(min)).toString.toInt
    var end = reqParMam.paramMap.get(ROOMS + SUF_LTE).getOrElse(max).toString.toInt

    rangeSlider(start, end, min, max, step, ROOMS, action, reqParMam.paramMap)
  }

  /**
   * 
   */
  def showDistricts(): Elem = {
    var list = distrits.map { el => (el.id.toString, el.name) }
    genFilter(list, DISTRICT_P, true)
  }

  /**
   * 
   */
  def showAdditionFilters(): Elem = {
    var list = ApartmentsFilters.additionFilterList
    var blocks = Math.ceil(list.size.toFloat / 6f).toInt

    var content = for (i <- 0 to (blocks - 1)) yield {
      <ul style="float: left;margin: 0;" class="checkbox">
        {
          var start = i * 6
          var end = start + 6
          if (list.size <= end) { end = list.size - 1 }
          for (index <- start to end) yield {
            var parameter = list(index)
            var value = (reqParMam.paramMap.get(parameter.name).getOrElse("false")).toString.toBoolean
            var checked = if (value) Some(Text("checked")) else None

            <li><label><input checked={ checked } data-name={ parameter.name } type="checkbox"/>{ parameter.label }</label></li>
          }
        }
      </ul>
    }

    <div>
      <button id="addition-filter-fs-bt" class="btn btn-primary btn-sm" style="width: 98%;">
        Удобства
      </button>
      <div id="addition-filter-fs">
        <div style="overflow: auto;">
          { content }
        </div>
        <div class="button-set">
          <button class="btn btn-primary btn-xs">Ок</button>
        </div>
      </div>
    </div>
  }
}