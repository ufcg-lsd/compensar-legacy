angular.module('app')
  .controller('LoginController', function ($rootScope, $location, AuthService, $localStorage, UserService, $http) {

    if (AuthService.isLogged()) {
        $location.path("/questoes");
      }

      $rootScope.activetab = $location.path();

      $rootScope.$on('event:social-sign-in-success', function (event, userDetails) {

        $http.get('https://prematriculabackend.herokuapp.com/api/aluno/' + AuthService.getUserDetails().email).
          then(function (response) {
            $rootScope.registered = response.status == 200;
          }, function () { 
            $rootScope.registered = false; 
          })
  
          .then(
            function () {
              if ($rootScope.registered) {
                $location.path("/questoes");
              } 
              else {
                $location.path("/signup");
              }
            }
          );
 
      });

});