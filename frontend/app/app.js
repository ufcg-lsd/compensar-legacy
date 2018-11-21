
var app = angular.module('app',['ngStorage','ngRoute','socialLogin','tld.csvDownload']);
var host = "";

app.config(function($routeProvider, $locationProvider) {
    
    // remove o # da url
    $locationProvider.html5Mode(true);

    $routeProvider

      .when('/login', {
          templateUrl: 'index.html',
          controller: 'LoginController'
      })

      .when('/contato', {
        templateUrl: 'app/views/Contato.html',
        controller: 'ContatoController'
      })

      .when('/signup', {	
        templateUrl: 'app/views/SignUp.html',	
        controller: 'SignUpController',
        requireAuth: true,
        requireNotRegistered: true
    })	

    .when('/userdata', {
        templateUrl: 'app/views/UserData.html',
        controller: 'UserDataController',
        requireAuth: true,
        requireRegistered:true
      
    })

    .when('/questoes', {	
        templateUrl: 'app/views/Questoes.html',	
        controller: 'QuestoesController',
        requireAuth: true,
        requireNotRegistered: true
    })	

    .when('/addQuestao', {	
        templateUrl: 'app/views/CriaQuestao.html',	
        controller: 'CriaQuestaoController',
        requireAuth: true,
        requireNotRegistered: true
    })	
      .otherwise({
        redirectTo: '/login'
      });

    });


    app.config(function(socialProvider){
      socialProvider.setGoogleKey("363497084086-sj4dhuvvkmcivpbl0h2fgrrvnm0229og.apps.googleusercontent.com");
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