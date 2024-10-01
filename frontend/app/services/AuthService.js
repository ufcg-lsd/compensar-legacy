/* global host, signOut*/
angular.module('app')
  .factory('AuthService', function(localStorageService, $http, $rootScope, Notification, $location, $interval) {
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
      localStorageService.remove("Permissions");
      localStorageService.remove("listaAtiva");
    },

    service.getAuthorization = function () {
      return { headers: { Authorization: localStorageService.get("Authorization")}};
    }

    service.isLogged = function(){ return localStorageService.get("user") !== null};

    service.update_view = () => {
      if (localStorageService.get("Authorization") === null) {
        $location.path("/login");
        return;
      }
      $http.get(host + 'auth/authenticate', service.getAuthorization())
        .then(function () {
          $rootScope.registered = true;
          $http.get(host + 'usuario', service.getAuthorization()).
            then(function (response) {
    
                $rootScope.instituicao_usuario = response.data.nomeInstituicao,
                $rootScope.cargo_usuario = response.data.cargo,
                $rootScope.idade_usuario = response.data.idade,
                $rootScope.cidade_usuario = response.data.cidade
                service.setPermissions(response.data.permissoes);
            });
        }, function (err) {
          $rootScope.registered = false;
          if (err.status == 404) {
            if ($location.path() !== "/signup") {
              Notification.info("Complete seu cadastro");
              $location.path("/signup");
            }
          } else if (err.status == 400) {
            $rootScope.forceSignOut();
          } else {
            if($rootScope.loaded === true) {
              $rootScope.loaded = false;
              signOut();
              Notification.error("Algo inexperado aconteceu, você precisa fazer login novamente!");
            }
          }
        });
    };

    return service;
  
});