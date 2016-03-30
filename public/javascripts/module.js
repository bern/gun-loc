var module = angular.module('app', []);

module.controller('appController', ['$scope', '$http', '$interval', function($scope, $http, $interval) {

    $scope.userData = [];

    $scope.data = {
      "username": "johndoe",
      "password": "yolo",
      "_id": "56c8eebcec16105415f2eeb8",
      "information": {
        "firearm_licensee_num": "123456789.01",
        "licensee_phone_num": "+17274224360",
        "street_address": "201 N Goodwin Ave",
        "city": "Urbana",
        "state": "IL",
        "zip": 61801
      },
      "guns": [
          {
              "acquisition_date": "20 Feb, 2016",
              "firearm_type": "handgun",
              "manufacturer": "glock",
              "model": "17",
              "caliber_guage": "9mm",
              "serial_num": "12345678",
              "status": "active"
          },
          {
              "acquisition_date": "18 Feb, 2016",
              "firearm_type": "shotgun",
              "manufacturer": "benelli",
              "model": "cordoba",
              "caliber_guage": "12 guage",
              "serial_num": "87654321",
              "status": "alert"
          },
          {
              "acquisition_date": "16 Feb, 2016",
              "firearm_type": "rifle",
              "manufacturer": "browning",
              "model": "x-bolt medallion",
              "caliber_guage": "lol idk",
              "serial_num": "13247586",
              "status": "off"
          }
      ]
    };

//    $scope.guns = [
//          {
//              "acquisition_date": "20 Feb, 2016",
//              "firearm_type": "handgun",
//              "manufacturer": "glock",
//              "model": "17",
//              "caliber_guage": "9mm",
//              "serial_num": "12345678",
//              "status": "active"
//          },
//          {
//              "acquisition_date": "18 Feb, 2016",
//              "firearm_type": "shotgun",
//              "manufacturer": "benelli",
//              "model": "cordoba",
//              "caliber_guage": "12 guage",
//              "serial_num": "87654321",
//              "status": "alert"
//          },
//          {
//              "acquisition_date": "16 Feb, 2016",
//              "firearm_type": "rifle",
//              "manufacturer": "browning",
//              "model": "x-bolt medallion",
//              "caliber_guage": "lol idk",
//              "serial_num": "13247586",
//              "status": "off"
//          }
//      ];

    $scope.gunActive = function(gun) {
        $scope.toggleOnOff(gun._id);
    }

    $scope.gunOff = function(gun) {
        $scope.toggleOnOff(gun._id);
    }

    $scope.gunReset = function(gun) {
        //gun.status = "active";
        $scope.reset(gun._id);
    }

    $scope.getUserInfo = function() {
        console.log("Fetching user info...");
        $http({
          method: 'GET',
          url: '/user'
        }).then(function successCallback(response) {
            console.log("Success!");
            console.log(response);
            $scope.data = response.data;
          }, function errorCallback(response) {
            console.log("Oops...");
            console.log(response);
        });
    }

    $scope.getData = function(id) {
        console.log("Fetching data...");
        $http({
          method: 'GET',
          url: '/gun'
        }).then(function successCallback(response) {
            console.log("Success!");
            console.log(response);
            $scope.guns = response.data;
          }, function errorCallback(response) {
            console.log("Oops...");
            console.log(response);
        });
    }

    $scope.reset  = function(id) {
        console.log("Resetting alert...");
        $http({
          method: 'GET',
          url: '/reset/' + id
        }).then(function successCallback(response) {
            console.log("Gun reset!");
            console.log(response);
            $scope.getData();
          }, function errorCallback(response) {
            console.log("Oops...");
            console.log(response);
        });
    }

    $scope.toggleOnOff = function(id) {
      $http({
        method: 'GET',
        url: '/onOff/' + id
      }).then(function successCallback(response) {
        console.log("Gun " + response.status);
        $scope.getData();
      }, function errorCallback(response) {
        console.log("Oops...");
        console.log(response);
      });
    }

    $scope.pollDB = function (message) {

        var intervalPeriod = 2000;

        var rep = $interval(function () {
            $scope.getData();
        }, intervalPeriod);

    }

    var init = function() {
        $scope.getUserInfo();
        $scope.getData();
        $scope.pollDB();
    };

    init();

}]);
