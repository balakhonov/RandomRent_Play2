function FormController($scope, $resource, $timeout) {
	// Map
	var UKRAINE_LOCATION = new google.maps.LatLng(48.379433, 31.16558);
	var geocoder;
	var map;
	var currentMarker;
	var pTm;

	// REST
	var City = $resource('/json/province_cities/:provinceId');
	var District = $resource('/json/city/:cityId/districts');
	var Apartment = $resource('/apartment/:id', {
		id : '@UserID'
	}, {
		update : {
			method : 'PUT'
		}
	});

	$scope.provinces = PROVINCES;
	$scope.cities = [];

	$scope.cityWrapper = true;
	$scope.districtWrapper = true;
	$scope.streetWrapper = true;

	google.maps.event.addDomListener(window, 'load', initialize);

	$('.selectpicker').selectpicker();

	$scope.loadCities = function() {
		var provinceId = $scope.province.id;
		$scope.city = null;

		var cities = City.query({
			provinceId : provinceId
		}, function(a, b) {
			$scope.cities = cities;
			$scope.cityWrapper = false;
			$scope.districtWrapper = true;
			$scope.streetWrapper = true;
		});
	}

	$scope.loadDistricts = function() {
		var cityId = $scope.city.id;

		var districts = District.query({
			cityId : cityId
		}, function() {
			$scope.districts = districts;

			if (districts.length > 0) {
				$scope.districtWrapper = false;
				$scope.streetWrapper = true;
			} else {
				$scope.districtWrapper = true;
				$scope.streetWrapper = false;
			}
		});

		$scope.showPosition();
	}

	$scope.onDistrictChange = function() {
		$scope.streetWrapper = false;

		$scope.showPosition();
	}

	$scope.$watch('cities', function() {
		$timeout(function() {
			$('.selectpicker').selectpicker('refresh');
		});
	});

	$scope.$watch('districts', function() {
		$timeout(function() {
			$('.selectpicker').selectpicker('refresh');
		});
	});

	$scope.save = function() {
		if (ApartmentValidationModel.validate()) {
			var ap = {
				"cityId" : $scope.city.id,
				"street" : $scope.apForm.street.$viewValue,
				"rooms" : $scope.apForm.rooms.$viewValue - 0,
				"adType" : $("input[name=adType]").val() - 0,
				"period" : $("input[name=period]").val() - 0,
				"price" : $scope.apForm.price.$viewValue - 0,
				"title" : $scope.apForm.title.$viewValue,
				"description" : $scope.apForm.description.$viewValue,
				"mobileNumber" : $scope.apForm.mobileNumber.$viewValue,
				"email" : $scope.apForm.email.$viewValue,
				"fu" : $scope.apForm.fu.$viewValue,
				"ai" : $scope.apForm.ai.$viewValue,
				"fr" : $scope.apForm.fr.$viewValue,
				"ca" : $scope.apForm.ca.$viewValue,
				"wa" : $scope.apForm.wa.$viewValue,
				"fi" : $scope.apForm.fi.$viewValue,
				"it" : $scope.apForm.it.$viewValue,
				"bo" : $scope.apForm.bo.$viewValue,
				"ne" : $scope.apForm.ne.$viewValue,
				"district" : $scope.apForm.district.$viewValue - 0,
				"pLat" : $scope.apForm.pLat.$viewValue - 0,
				"pLong" : $scope.apForm.pLong.$viewValue - 0
			};
			console.debug("Save apartment:", ap);

			// var result = Apartment.save(ap, function() {
			// consolstreete.debug("Save result:", result);
			// });
		}
	};

	$scope.showPosition = function() {
		window.clearTimeout(pTm);

		if (!$scope.cityWrapper) {
			pTm = setTimeout(function() {
				var country = "Украина";
				var city = $scope.city.name;
				var street = $scope.apForm.street.$viewValue;
				var address = country + ", " + city + ", " + street

				geocoder.geocode({
					'address' : address
				}, function(results, status) {
					if (status == google.maps.GeocoderStatus.OK) {
						map.setCenter(results[0].geometry.location);
						map.setZoom(13);
						currentMarker.setPosition(results[0].geometry.location);

						$scope.apForm.pLat.$viewValue = results[0].geometry.location.lat();
						$scope.apForm.pLong.$viewValue = results[0].geometry.location.lng();
					} else {
						console.warn('Geocode was not successful for the following reason: ' + status);
						map.setCenter(UKRAINE_LOCATION);
						currentMarker.setPosition(UKRAINE_LOCATION);
						map.setZoom(5);

						$scope.apForm.pLat.$viewValue = 0;
						$scope.apForm.pLong.$viewValue = 0;
					}
				});
			}, 1000);
		}
	}

	function initialize() {
		geocoder = new google.maps.Geocoder();
		var mapOptions = {
			zoom : 8,
			center : UKRAINE_LOCATION,
			draggable : true
		}
		map = new google.maps.Map(document.getElementById('map'), mapOptions);

		currentMarker = new google.maps.Marker({
			map : map,
			draggable : true,
		});

		google.maps.event.addListener(currentMarker, "dragend", function(event) {
			var point = currentMarker.getPosition();

			$scope.apForm.pLat.$viewValue = point.lat();
			$scope.apForm.pLong.$viewValue = point.lng();
		});
	}
}

function TextValidator(ctrl, options, callback) {

	this.validate = function() {
		var value = (typeof (ctrl.$viewValue) == "undefined") ? "" : ctrl.$viewValue;

		if (options && options.isBlank) {
			var isBlank = (value === '');
			ctrl.$setValidity('isBlank', !isBlank);
		}

		if (options && options.maxLength) {
			var invalidLen = !isBlank && (value.length > options.maxLength);
			ctrl.$setValidity('invalidLen', !invalidLen);
		}

		if (options && options.pattern) {
			var invalidChars = !invalidLen && !isBlank && !options.pattern.test(value);
			ctrl.$setValidity('invalidChars', !invalidChars);
		}

		var result = !isBlank && !invalidLen && !invalidChars

		ctrl.$invalid = !result;
		ctrl.$valid = result;

		if (callback) {
			callback(result);
		}
		return result;
	}
}

var ApartmentValidationModel = angular.module('ApartmentValidation', []);
ApartmentValidationModel.validators = [];
ApartmentValidationModel.validate = function() {
	var result = true;
	for ( var i in this.validators) {
		var validator = this.validators[i];
		if (!validator.validate() && result) {
			result = false;
		}
	}
	return result;
}

ApartmentValidationModel.directive('validStreet', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			var validator = new TextValidator(ctrl, {
				isBlank : true,
				maxLength : 20
			}, function() {
				angular.element($("#FormController")).scope().showPosition();
			});

			ApartmentValidationModel.validators.push(validator);

			ctrl.$parsers.unshift(function(viewValue) {
				validator.validate();
			})
		}
	}
}).directive('validTitle', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			var validator = new TextValidator(ctrl, {
				isBlank : true,
				maxLength : 70
			});

			ApartmentValidationModel.validators.push(validator);

			ctrl.$parsers.push(function(viewValue) {
				validator.validate();
			});
		}
	}
}).directive('validRooms', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			var validator = new TextValidator(ctrl, {
				isBlank : true,
			});

			ApartmentValidationModel.validators.push(validator);

			ctrl.$parsers.unshift(function(viewValue) {
				validator.validate();
			});
		}
	}
}).directive('validPrice', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			var validator = new TextValidator(ctrl, {
				isBlank : true,
				maxLength : 10,
				pattern : /^[0-9]+$/
			});

			ApartmentValidationModel.validators.push(validator);

			ctrl.$parsers.unshift(function(viewValue) {
				validator.validate();
			});
		}
	}
}).directive('validDescription', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			var validator = new TextValidator(ctrl, {
				isBlank : true,
				maxLength : 500
			});

			ApartmentValidationModel.validators.push(validator);

			ctrl.$parsers.unshift(function(viewValue) {
				validator.validate();
			});
		}
	}
}).directive('validMobile', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			var validator = new TextValidator(ctrl, {
				isBlank : true,
				maxLength : 20,
				pattern : /^[0-9()+ -]+$/
			});

			ApartmentValidationModel.validators.push(validator);

			ctrl.$parsers.unshift(function(viewValue) {
				validator.validate();
			});
		}
	}
}).directive('validEmail', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			var validator = new TextValidator(ctrl, {
				isBlank : true,
				maxLength : 40,
				pattern : /^[\w._-]+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/
			});

			ApartmentValidationModel.validators.push(validator);

			ctrl.$parsers.unshift(function(viewValue) {
				validator.validate();
			});
		}
	}
});
