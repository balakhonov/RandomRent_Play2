var API = new Api();

function Api($scope) {
	var POST_SAVE_APARTMENT = "/apartment/"

	function postRequest(link, data, callback, ars) {
		$http({
			method : 'POST',
			url : link,
			data : data,
			headers : {
				'Content-Type' : 'application/json'
			}
		}).success(function(data, status, headers, config) {
			console.log(data, status, headers, config);
		}).error(function(data, status, headers, config) {
			console.log(data, status, headers, config);
		});
	}

	function postCallback(data, callback, args) {
		switch (data.RESULT_CODE) {
		case SESSION_CLOSED:
			location.reload();
			break;
		default:
			if (callback) {
				callback(data.RESULT_CODE, data.aaData, data.MESSAGE, args);
			}
		}
	}

	function postCallbackError(functionName, textStatus, errorThrown, callback, args) {
		if (callback) {
			switch (errorThrown) {
			case "Not Found":
				callback(PAGE_NOT_FOUND, textStatus, errorThrown, args);
				if (errorOverlay) {
					errorOverlay.showPageNotFound(functionName);
				}
				break;
			case "session closed":
				if (errorOverlay) {
					errorOverlay.showSessionClosed();
				}
				break;

			default:
				// callback(SERVER_NOT_AVAILABLE, textStatus, errorThrown,
				// args);
				if (errorOverlay) {
					errorOverlay.showServerNotAvailable();
				}
				break;
			}
		}
	}

	this.postSaveApartment = function(apartment, callback) {

		postRequest(POST_SAVE_APARTMENT, apartment, callback);
	};
}