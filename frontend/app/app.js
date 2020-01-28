var app = angular.module('app',['ngQuill','LocalStorageModule','ngRoute','ngSanitize','checklist-model','ngMaterial','angular-loading-bar', 'ui-notification']);

app.run(function($rootScope, $interval, AuthService) {
    $rootScope.competenciasRepaginadas = [];
    $rootScope.listas = [];
    $rootScope.listasRequest = false;
    $rootScope.repaginaComp = function (competencia) {
        //console.log(competencia);
        var compLowerCase = competencia.split("_")[1].toLowerCase();
        return compLowerCase.charAt(0).toUpperCase() + compLowerCase.slice(1);
    }

    $rootScope.repaginaCompetencias = function(competencias) {
        //console.log(competencias);
        if (competencias === undefined) {
            return "";
        }
        let competenciasRepaginadas = [];
        let amostraCompetencias = "";
        for (let i = 0; i < (competencias.length); i++) {
            let repaginada = $rootScope.repaginaComp(competencias[i]);
            competenciasRepaginadas.push(repaginada);
            if (i === (competencias.length - 1) && i === 0) {
                amostraCompetencias += repaginada + ".";
            } else if (i === (competencias.length - 1)) {
                amostraCompetencias += " e " + repaginada + ".";
            } else if (i === 0) {
                amostraCompetencias += repaginada;
            } else {
                amostraCompetencias += ", " + repaginada;
            }
        }
        $rootScope.competenciasRepaginadas = competenciasRepaginadas;
        return amostraCompetencias;
    }

    $interval(AuthService.update_view, 5000);
});
/* eslint-disable no-unused-vars */
//var host = "https://compensar.herokuapp.com/api/";
var host = "http://localhost:5458/api/";
/* eslint-enable no-unused-vars */

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

    .when('/addLista', {	
      templateUrl: '/app/views/AddLista.html',	
      controller: 'ListaQuestoesController',
      requireAuth: true,
      requireRegistered: true
    })
    .when('/lista/', {	
      templateUrl: '/app/views/ImprimirLista.html',	
      controller: 'ListaController',
      requireAuth: true,
      requireRegistered: true
    })
      .otherwise({
        redirectTo: '/login'
      });


      $(document).ready(function() {
        $('.selectpicker').selectpicker();
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


app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
  cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
  cfpLoadingBarProvider.includeSpinner = false;

}])



