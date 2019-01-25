angular.module('app')
  .factory('UserService', function (AuthService,$rootScope,$http,$q) {
    const service = {};
    deferred = $q.defer();

    service.isRegistered = function () {
      $http.get('http://localhost:5458/api/usuario/' + AuthService.getUserDetails().Email).
        then(function (response) {
          $rootScope.registered = response.status == 200;
        }, function () { 
          $rootScope.registered = false; 
        })

        return $rootScope.registered;
    }

    service.getEmail = function () {
      return AuthService.getUserDetails().Email;
    }

    service.getName = function () {
      return AuthService.getUserDetails().Name;
    }

    return service;
  });