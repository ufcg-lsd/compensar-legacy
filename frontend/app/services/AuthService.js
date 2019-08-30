angular.module('app')
  .factory('AuthService', function(localStorageService, $http, $rootScope, Notification, $location) {
   const service = {};

   
    service.getUserDetails = function () {
      return localStorageService.get("user");
    },
    
    service.setUserDetails = function (UserDetails) {
        localStorageService.set("user",UserDetails);
    },

    service.setAuthorization = function (token) {
      localStorageService.set("Authorization",token);
    },
   
    service.logout = function () {
      localStorageService.remove("user");
      localStorageService.remove("Authorization");
    },

    service.getAuthorization = function () {
      return { headers: { Authorization: localStorageService.get("Authorization")}};
    }

    service.isLogged = function(){ return localStorageService.get("user") !== null};

    service.update_view = () => {
      if (localStorageService.get("Authorization") === null) {
        return;
      }
      $http.get(host + 'auth/authenticate/', service.getAuthorization())
        .then(function (response) {
          $rootScope.registered = true;
        }, function (err) {
          $rootScope.registered = false;
          if (err.status == 404) {
            Notification.info("Complete seu cadastro");
            $location.path("/signup");
          } else if (err.status == 400) {
            service.logout();
            Notification.warning("Seu login expirou, por favor faça login novamente!");
          } else {
            service.logout();
            Notification.error("Algo inexperado aconteceu, você precisa fazer login novamente!");
          }
        });
    };

    return service;
  
});