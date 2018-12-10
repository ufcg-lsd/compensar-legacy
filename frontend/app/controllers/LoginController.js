angular.module('app')
  .controller('LoginController', function (ProfileService,$scope,$window,$rootScope, $location, AuthService, $http) {
  


    $rootScope.login = function ($rootScope,profile) {
          $rootScope.isLogged = true;

          $location.path("/login");

          var perfil = {
            Name: profile.getName(),
            Email: profile.getEmail(),
            ImageUrl: profile.getImageUrl()
          };
        
          AuthService.setUserDetails(perfil);
          ProfileService.update_user_profile();
          update_view();

  };

    update_view =  function () {

      $http.get('http://localhost:5458/api/usuario/' + AuthService.getUserDetails().Email).
        then(function (response) {
          $rootScope.registered = response.status == 200;
        }, function () { 
          $rootScope.registered = false; 
        })

        .then(
          function () {
            if ($rootScope.registered) {
              $location.path("/questoes");
              $window.location.href = '/questoes';

            } 
            else {
              $location.path("/signup");
              $window.location.href = '/signup';

            }
          }
        );

    };



  $rootScope.sair = function ($rootScope) {

    $rootScope.isLogged = false;

    ProfileService.update_visitant_profile();
    $location.path("/login");
    $window.location.href = '/login';

    AuthService.logout();

    $rootScope.$apply();


  }




    //Desliza icones das competencias automaticamente sem desordenar

    $('#carouselExample').on('slide.bs.carousel', function (e) {

      var $e = $(e.relatedTarget);
      var idx = $e.index();
      var itemsPerSlide = 4;
      var totalItems = $('.carousel-item').length;
      
      if (idx >= totalItems-(itemsPerSlide-1)) {
          var it = itemsPerSlide - (totalItems - idx);
          for (var i=0; i<it; i++) {
              // append slides to end
              if (e.direction=="left") {
                  $('.carousel-item').eq(i).appendTo('.carousel-inner');
              }
              else {
                  $('.carousel-item').eq(0).appendTo('.carousel-inner');
              }
          }
      }
    });

});