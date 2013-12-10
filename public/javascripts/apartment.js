function FormController1($scope, $resource) {

	// REST

	var Wines = $resource('/ua/Севастополь');

	$scope.todos = [ {
		text : 'learn angular',
		done : true
	}, {
		text : 'build an angular app',
		done : false
	} ];

	$("input[type=submit]").click(function(e) {
		console.log("sending..");
		$scope.apartments = Wines.query();

		postSaveApartment();
		return false;
	});

	function postSaveApartment() {
		var ap = {
			"cityId" : 1,
			"street" : "Street",
			"roomTypeId" : 1,
			"adType" : 0,
			"period" : 0,
			"rentById" : 1,
			"price" : 100500,
			"title" : "Title",
			"description" : "Description",
			"mobileNumber" : "911",
			"postedDate" : 1385682772048,
			"fu" : true,
			"ai" : true,
			"fr" : true,
			"ca" : true,
			"wa" : true,
			"fi" : true,
			"it" : true,
			"bo" : true,
			"ne" : true,
			"district" : 0,
			"archived" : true
		};

		Apartment.save(ap)
	}

	function callbackPostSaveApartment(resultCode, data, message) {
		console.log(resultCode, data, message);
	}
}

function FormController($scope, $resource, $timeout) {
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

	$("#formSubmitBtn").addClass("disabled");

	$scope.loadCities = function() {
		var provinceId = $scope.province
		console.debug("provinceId:", provinceId);

		var cities = City.query({
			provinceId : provinceId
		}, function(a, b) {
			$scope.cities = cities;
			$scope.cityWrapper = false;
			$scope.districtWrapper = true;
		});
	}

	$scope.loadDistricts = function() {
		var cityId = $scope.city
		console.debug("cityId:", cityId);

		var districts = District.query({
			cityId : cityId
		}, function() {
			$scope.districts = districts;
			console.debug("districts:", districts);

			if (districts.length > 0) {
				$scope.districtWrapper = false;
			} else {
				$scope.districtWrapper = true;
			}
		});
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

	$scope.formAllGood = function() {
		if ($scope.priceGood && $scope.titleGood) {
			$("#formSubmitBtn").removeClass("disabled");
		} else {
			$("#formSubmitBtn").addClass("disabled");
		}
		return true;
	}

	$scope.save = function() {
		var ap = {
			"cityId" : $scope.apForm.city.$viewValue,
			"street" : $scope.apForm.street.$viewValue,
			"rooms" : $scope.apForm.rooms.$viewValue,
			"adType" : $scope.apForm.adType.$viewValue,
			"period" : $scope.apForm.period.$viewValue,
			// "rentById" : $scope.apForm.rentById.$viewValue,
			"price" : $scope.apForm.price.$viewValue,
			"title" : $scope.apForm.title.$viewValue,
			"description" : $scope.apForm.description.$viewValue,
			"mobileNumber" : $scope.apForm.mobileNumber.$viewValue,
			"fu" : $scope.apForm.fu.$viewValue,
			"ai" : $scope.apForm.ai.$viewValue,
			"fr" : $scope.apForm.fr.$viewValue,
			"ca" : $scope.apForm.ca.$viewValue,
			"wa" : $scope.apForm.wa.$viewValue,
			"fi" : $scope.apForm.fi.$viewValue,
			"it" : $scope.apForm.it.$viewValue,
			"bo" : $scope.apForm.bo.$viewValue,
			"ne" : $scope.apForm.ne.$viewValue,
			"district" : $scope.apForm.district.$viewValue
		};
		console.debug("Save apartment:", ap);

		var result = Apartment.save(ap, function() {
			console.debug("Save result:", results);
		});
	};

	$(function() {
		$('.selectpicker').selectpicker();
		$('.selectpicker[name=city]').on({
			change : function(e) {
				console.log('changed 1');
			}
		});
	});
}

angular.module('ApartmentValidation', []).directive('validStreet', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				var isBlank = viewValue === ''
				var invalidLen = !isBlank && (viewValue.length > 20)
				ctrl.$setValidity('isBlank', !isBlank)
				ctrl.$setValidity('invalidLen', !invalidLen)
				scope.titleGood = !isBlank && !invalidLen
			})
		}
	}
}).directive('validTitle', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				var isBlank = viewValue === ''
				var invalidLen = !isBlank && (viewValue.length > 70)
				ctrl.$setValidity('isBlank', !isBlank)
				ctrl.$setValidity('invalidLen', !invalidLen)
				scope.titleGood = !isBlank && !invalidLen
			})
		}
	}
}).directive('validPrice', function() {
	return {
		require : 'ngModel',
		link : function(scope, elm, attrs, ctrl) {
			ctrl.$parsers.unshift(function(viewValue) {
				var isBlank = viewValue === ''
				var invalidChars = !isBlank && !/^[0-9]+$/.test(viewValue)
				var invalidLen = !isBlank && !invalidChars && (viewValue.length > 10)
				ctrl.$setValidity('isBlank', !isBlank)
				ctrl.$setValidity('invalidChars', !invalidChars)
				ctrl.$setValidity('invalidLen', !invalidLen)
				scope.priceGood = !isBlank && !invalidChars && !invalidLen
			})
		}
	}
})