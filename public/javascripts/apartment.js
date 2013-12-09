function FormController($scope, $resource) {

	var Apartment = $resource('/apartment/:id', {
		id : '@UserID'
	}, {
		update : {
			method : 'PUT'
		}
	});

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