function FiltersCtrl($scope, $cookies, $cookieStore) {
	var url = $.url();

	$("#addition-filter-fs input[type=checkbox]").each(function(i, elem) {
		$(elem).on("change", function() {
			var checked = $(this).is(":checked");
			var name = $(this).attr("data-name");

			if (checked) {
				url.data.param.query[name] = true;
			} else {
				delete url.data.param.query[name]
			}
		});
	});
	$("#addition-filter-fs button").on("click", function() {
		var path = url.attr('path')
		var str = jQuery.param(url.data.param.query);
		window.location = path + "?" + str;
	});

	$("#addition-filter-fs-bt").showFilterPanel($("#addition-filter-fs"));
}

$.fn.extend({
	showFilterPanel : function(content, options, callback) {
		var that = this;
		var lock = true;
		var elem = $('<div class="filter-panel"></div>').hide();
		elem.addClass("popup-info ui-corner-all ui-widget-content ui-dialog");
		elem.mouseover(function() {
			lock = true;
		}).mouseout(function() {
			lock = false;
		});

		var cancelButton = $("<button>Отменить</button>");
		var okButton = $("<button>Ок</button>");
		var buttons = $("<div class='filter-bottom-bts'></div>");
		buttons.append(cancelButton).append(okButton);

		cancelButton.click(hide);
		okButton.click(function() {
			hide();
			callback();
		});

		$(this).click(function(e, el) {
			lock = null;

			elem.append(content.show());
			$("body").append(elem);

			var windowHeight = $(window).height();
			var scrollTop = $(window).scrollTop();
			var posX = $(this).offset().left;
			var posY = $(this).offset().top;
			var elWidth = elem.width();
			var elHeight = elem.height();
			var top = posY - 16;
			if (windowHeight < (top - scrollTop) + elHeight)
				top = windowHeight + scrollTop - elHeight - 10;
			var left = (posX - 0) - (elWidth - 0) - 20;

			elem.css("left", left);
			elem.css("top", top);
			elem.show();
		});

		$(document).click(function(e) {
			if (lock == null) {
				lock = false;
				return;
			}

			if (lock !== true) {
				hide();
				lock = true;
			}
		});

		function hide() {
			elem.detach();
		}
		return elem;
	}
});