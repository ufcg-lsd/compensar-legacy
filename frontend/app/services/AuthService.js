angular.module('app')
  .factory('AuthService', function(localStorage,socialLoginService) {
   const service = {};

    service.getUserDetails = function () {
      return localStorage.UserDetails;
    },
    service.setUserDetails = function (UserDetails) {
      localStorage.UserDetails = UserDetails;
      },
   
    service.logout = function () {
      socialLoginService.logout();
      localStorage.clear();
    },

    service.isLogged = function(){ return localStorage.UserDetails !== undefined};

    return service;
  
});