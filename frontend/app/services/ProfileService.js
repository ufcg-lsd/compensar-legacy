angular.module('app')
  .factory('ProfileService', function($rootScope,AuthService) {
   const service = {};

   service.update_user_profile = function(){
        $rootScope.profile_image = AuthService.getUserDetails().ImageUrl;
        $rootScope.nome_usuario = AuthService.getUserDetails().Name;
    
   },

   service.update_visitant_profile = function(){
    $rootScope.profile_image = "/../../images/profile.png";
    $rootScope.nome_usuario = "Visitante";
   }



    return service;
  
});