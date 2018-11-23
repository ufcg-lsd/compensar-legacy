angular.module('app')
  .factory('AuthService', function(socialLoginService) {
   const service = {};

    service.getUserDetails = function ($window) {
      return $window.localStorage.UserDetails;
    },
    service.setUserDetails = function (UserDetails,$window) {
      $window.localStorage.UserDetails = UserDetails;
      },
   
    service.logout = function ($window) {
      socialLoginService.logout();
      $window.localStorage.clear();
    },

    service.isLogged = function(){ return localStorage.UserDetails !== undefined};

    return service;
  
});