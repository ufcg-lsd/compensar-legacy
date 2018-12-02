angular.module('app')
  .factory('UserService', function (AuthService, $http,$q) {
    const service = {};
    deferred = $q.defer();

    service.isRegistered = function () {
      $http.get('http://localhost:5458/api/usuario/' + AuthService.getUserDetails().Email).
        then(function (response) {
          deferred.resolve(response.status == 200);
        },function(response){
          deferred.resolve(response.status == 200);
   
        }).catch(function(response){deferred.resolve(response.status == 200);});

        return deferred.promise;
    }

    service.getEmail = function () {
      return AuthService.getUserDetails().Email;
    }

    service.getName = function () {
      return AuthService.getUserDetails().Name;
    }

    return service;
  });