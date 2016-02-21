var module = angular.module('app', []);

module.controller('appController', ['$scope', '$http', '$interval', function($scope, $http, $interval) {
    
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
    
    $scope.guns = [
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
      ];

    $scope.enableGun = function(gun) {
        gun.status="active";
        //tell database this
    }
    
    $scope.getData = function() {
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
    
    $scope.pollDB = function (message) {
        
        var intervalPeriod = 5000;
        
        var rep = $interval(function () {
            $scope.getData();
        }, intervalPeriod);
        
    }
    
    var init = function() {
        $scope.getData();
        $scope.pollDB();
    };
    
    init();
    
}]);