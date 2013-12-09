package controllers.mapping

import scala.xml.Elem

class AbstractFilters(reqParMam: ApartmentsP) {

  protected def genFilter(list: List[(String, Any)], parameterName: String, enableAll: Boolean): Elem = {
    var mutableMap = scala.collection.mutable.Map(reqParMam.paramMap.toList: _*)
    var selected: String = (reqParMam.paramMap.get(parameterName) getOrElse ("")).toString
    <ul>
      {
        if (enableAll) {
          var clazz = if ("".==(selected)) "active first" else "non-active first"
          mutableMap.remove(parameterName)
          var href = reqParMam.toQueryString(reqParMam.page, mutableMap.toList)
          <li><a class={ clazz } href={ href }>Все</a></li>
        }
      }
      {
        for (i <- list) yield {
          var clazz = if (i._1.==(selected)) "active " else "non-active "
          mutableMap(parameterName) = i._1
          var href = reqParMam.toQueryString(reqParMam.page, mutableMap.toList)
          <li><a class={ clazz } href={ href }>{ i._2 }</a></li>
        }
      }
    </ul>
  }

  def rangeSlider(start: Int, end: Int, min: Int, max: Int, step: Int,
    parameterName: String,
    action: String,
    additionParameters: Map[String, AnyVal]): Elem = {
    var script = "$('." + parameterName + "-slider').slider().on('slide', function(ev) {" +
      "$('#" + parameterName + "-amount').html('От ' + ev.value[0] + ' до ' + ev.value[1]);" +
      "$('#" + parameterName + "Gte').val(ev.value[0]);" +
      "$('#" + parameterName + "Lte').val(ev.value[1]);" +
      "});"
    var clazz = parameterName + "-slider"
    var idAmount = parameterName + "-amount"
    var amount = "От %s до %s".format(start, end)
    var idFrom = parameterName + "Gte"
    var idTo = parameterName + "Lte"
    var values = "[%s,%s]".format(start, end)

    <div>
      <form id="" action={ action } method="get">
        <div>
          <div id={ idAmount }>{ amount }</div>
          <input type="text" class={ clazz } value="" data-slider-min={ min.toString } data-slider-max={ max.toString } data-slider-step={ step.toString } data-slider-value={ values } id=""/>
          <input id={ idFrom } name={ idFrom } type="hidden" value={ start.toString }/>
          <input id={ idTo } name={ idTo } type="hidden" value={ end.toString }/>
          {
            for (entry <- additionParameters) yield {
              if (!(entry._1.equals(idFrom) || entry._1.equals(idTo)))
                <input name={ entry._1 } type="hidden" value={ entry._2.toString }/>
            }
          }
          <button type="submit" class="btn btn-primary btn-xs">OK</button>
        </div>
      </form>
      <script>
        { script }
      </script>
    </div>
  }
}