angular.module('app')
  .factory('UserService', function ($localStorage, AuthService, $http,$q) {
    const service = {};
    deferred = $q.defer();

    service.isRegistered = function () {
      $http.get('https://localhost:8080/api/usuario/' + AuthService.getUserDetails().email).
        then(function (response) {
          deferred.resolve(response.status == 200);
        },function(response){
          deferred.resolve(response.status == 200);
   
        }).catch(function(response){deferred.resolve(response.status == 200);});

        return deferred.promise;
    }

    service.getEmail = function () {
      return AuthService.getUserDetails().email;
    }

    service.getName = function () {
      return AuthService.getUserDetails().name;
    }

    return service;
  });