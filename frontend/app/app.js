
var app = angular.module('app',['ngQuill','LocalStorageModule','ngRoute','ngSanitize','checklist-model']);
var host = "";

app.config(function($routeProvider, $locationProvider) {
    
    // remove o # da url
    $locationProvider.html5Mode(true);



    $routeProvider

    .when('/login', {
        templateUrl: '/app/views/Login.html',
        controller: 'LoginController'
    })	
      .when('/signup', {	
        templateUrl: '/app/views/SignUp.html',	
        controller: 'SignUpController',
        requireAuth: true,
        requireNotRegistered: true
    })	

    .when('/meusdados', {
        templateUrl: '/app/views/MeusDados.html',
        controller: 'MeusDadosController',
        requireAuth: true,
        requireRegistered:true
      
    })

    .when('/questoes', {	 
        templateUrl: '/app/views/Questoes.html',	
        controller: 'BuscasController',
        requireAuth: true,
        requireRegistered: true
    })	

    .when('/buscas', {	
        templateUrl: '/app/views/Buscas.html',	
        controller: 'BuscasController',
        requireAuth: true,
        requireRegistered: true
    })	

    .when('/addQuestao', {	
        templateUrl: '/app/views/AddQuestao.html',	
        controller: 'AddQuestaoController',
        requireAuth: true,
        requireRegistered: true
    })	
      .otherwise({
        redirectTo: '/login'
      });

    });



  app.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);

app.config(function (localStorageServiceProvider) {
    localStorageServiceProvider
      .setStorageType('sessionStorage')
      .setNotify(true, true)
  });

  app.config(['ngQuillConfigProvider', function (ngQuillConfigProvider) {
    ngQuillConfigProvider.set();
}]);

