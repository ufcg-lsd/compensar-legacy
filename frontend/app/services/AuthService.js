/* global host, signOut*/
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

    service.setPermissions = function (token) {
      localStorageService.set("Permissions",token);
    },

    service.getPermissions = function () {
      return localStorageService.get("Permissions");
    },
   
    service.logout = () => {
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
        .then(function () {
          $rootScope.registered = true;
          $http.get(host + 'usuario/', AuthService.getAuthorization()).
            then(function (response) {
    
                $scope.instituicao_usuario = response.data.nomeInstituicao,
                $scope.cargo_usuario = response.data.cargo,
                $scope.idade_usuario = response.data.idade,
                $scope.cidade_usuario = response.data.cidade
                AuthService.setPermissions(response.data.permissoes);
            });
        }, function (err) {
          $rootScope.registered = false;
          if (err.status == 404) {
            Notification.info("Complete seu cadastro");
            $location.path("/signup");
          } else if (err.status == 400) {
            signOut();
            Notification.warning("Seu login expirou, por favor faça login novamente!");
          } else {
            signOut();
            Notification.error("Algo inexperado aconteceu, você precisa fazer login novamente!");
          }
        });
    };

    return service;
  
});