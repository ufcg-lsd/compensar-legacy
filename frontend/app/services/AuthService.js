angular.module('app')
  .factory('AuthService', function($rootScope,localStorageService,socialLoginService) {
   const service = {};

   
    service.getUserDetails = function () {
      return localStorageService.get("user");
    },
    service.setUserDetails = function (UserDetails) {
        localStorageService.set("user",UserDetails);
      },
   
    service.logout = function () {
      localStorageService.remove("user");
    },

    service.isLogged = function(){ return localStorageService.get("user") !== null};

    return service;
  
});