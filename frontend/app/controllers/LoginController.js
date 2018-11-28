angular.module('app')
  .controller('LoginController', function ($rootScope, $location, AuthService, $http) {
  
    $rootScope.logout = function(){
      AuthService.logout();
    }

    /*
    if (AuthService.isLogged()) {
        $location.path("/signup");
    }
    */

      $rootScope.activetab = $location.path();

      $rootScope.$on('event:social-sign-in-success', function (event, userDetails) {

        $http.get('/api/usuario/' + AuthService.getUserDetails().email).
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