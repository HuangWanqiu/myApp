'use strict';
angular.module('myApp')
.controller('indexCtrl',['$scope','$state','$stateParams',function($scope,$state,$stateParams) {
	// alert("nihao");
	// $state.go("register");
	$scope.login = function(){
		console.log("用户名："+$scope.username);
		console.log("用户名："+$scope.password);
	}
	
}]);