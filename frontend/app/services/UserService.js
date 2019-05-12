angular.module('app')
  .factory('UserService', function (AuthService,$rootScope,$http,$q) {
    const service = {};
    deferred = $q.defer();

    service.isRegistered = function () {
      $http.get('https://compensar.herokuapp.com/api/usuario/' + AuthService.getUserDetails().Email).
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