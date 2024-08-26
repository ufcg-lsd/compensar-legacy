angular.module('app')
  .controller('LoginController', function (ProfileService,$scope,$rootScope, $location, AuthService, Notification,  QuestoesService) {

    $rootScope.login = (profile, auth) => {
          $rootScope.isLogged = true;

          var perfil = {
            Name: profile.getName(),
            Email: profile.getEmail(),
            ImageUrl: profile.getImageUrl()
          };

          AuthService.setUserDetails(perfil);
          AuthService.setAuthorization("Bearer " + auth.id_token);
          ProfileService.update_user_profile();
          AuthService.update_view();
          $rootScope.$apply();
  };


  
  $rootScope.sair = () => {
    $rootScope.isLogged = false;

    AuthService.logout();
    ProfileService.update_visitant_profile();
    $location.path("/login");
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

// Quando o usuário scrolls down mostra o botão
window.onscroll = function() {$scope.scrollFunction()};

$scope.scrollFunction = function () {
  if (document.querySelector('#contato') != null && document.querySelector('#sobre') != null) {
    if (document.querySelector('#contato').getBoundingClientRect().bottom < window.screen.availHeight) {
      $rootScope.activetab = '/contato';
    } else if (document.querySelector('#sobre').getBoundingClientRect().top <= 25) {
      $rootScope.activetab = '/sobre';
    } else {
      $rootScope.activetab = '/login';
    }
  }

  if (document.getElementById("backToTop") !== null) {
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
      document.getElementById("backToTop").style.display = "block";
    } else {
      document.getElementById("backToTop").style.display = "none";
    }
  }
  $rootScope.$apply();
}

// Quando o usuário clicar sob o botão, volta para cima
$scope.topFunction = function () {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}

$(document).ready(function() {
  $scope.scrollFunction();
  if ($location.path() === '/questoes' && !$rootScope.listasRequest) {
    QuestoesService.getListaQuestoes();
  }
});
$rootScope.forceSignOut = function () {
  if ($rootScope.loaded === true || typeof $rootScope.loaded === undefined) {
    $rootScope.loaded = false;
    signOut();
    Notification.warning("Seu login expirou, por favor faça login novamente!");
    setTimeout(() => { $rootScope.loaded = true; }, 300);
  }
}
});