/* eslint-disable no-unused-vars */
//var host = "https://compensar.herokuapp.com/api/";
var host = "http://localhost:5458/api/";
/* eslint-enable no-unused-vars */

var app = angular.module('app',['ngQuill','LocalStorageModule','ngRoute','ngSanitize','checklist-model','ngMaterial','angular-loading-bar', 'ui-notification']);

app.run(function($rootScope, $interval, AuthService, $http, $timeout, $sce) {
    $rootScope.competenciasRepaginadas = [];
    $rootScope.listas = [];
    $rootScope.listasRequest = false;
    $rootScope.blockSearch = false;
    $rootScope.repaginaComp = function (competencia) {
        //console.log(competencia);
        var compLowerCase = competencia.split("_")[1].toLowerCase();
        return compLowerCase.charAt(0).toUpperCase() + compLowerCase.slice(1);
    }

    $rootScope.repaginaCompetencias = function(competencias) {
        //console.log(competencias);
        if (typeof competencias === 'undefined') {
            return "";
        }
        if (competencias.length == 0) return "Nenhuma."
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

  $rootScope.repaginaConteudo = function(conteudo) {
      //console.log(competencias);
      if (typeof conteudo === 'undefined') {
        return "";
      }
      let conteudoRepaginado = "";
      for (let i = 0; i < (conteudo.length); i++) {
          if (i === (conteudo.length - 1) && i === 0) {
            conteudoRepaginado += conteudo[i] + ".";
          } else if (i === (conteudo.length - 1)) {
            conteudoRepaginado += " e " + conteudo[i] + ".";
          } else if (i === 0) {
            conteudoRepaginado += conteudo[i];
          } else {
            conteudoRepaginado += ", " + conteudo[i];
          }
      }
      return conteudoRepaginado;
  }

  $rootScope.trustedHTML = function(text) {
    return $sce.trustAsHtml(text);
  }

    $interval(AuthService.update_view, 5000);

    $rootScope.updateSelect = function(selector) {
      return $http.get(host + 'conteudo', AuthService.getAuthorization())
      .then(function (response) {
        if ($("select"+selector) !== null) {
          $("select"+selector).empty();
          for(let conteudo of response.data) {
            $("select"+selector).append('<option>'+conteudo+'</option>');
          }
          $().ready(function() {
            $("select"+selector).selectpicker("refresh");
          });
        }
      }, function (err) {
        $timeout(function() {$rootScope.updateSelect(selector);}, 2000);
      });
    };
});

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
    .when('/cursos', {	
      templateUrl: '/app/views/Cursos.html',	
      controller: 'CursosController',
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


app.config(['cfpLoadingBarProvider', function(cfpLoadingBarProvider) {
  cfpLoadingBarProvider.parentSelector = '#loading-bar-container';
  cfpLoadingBarProvider.includeSpinner = false;
  cfpLoadingBarProvider.latencyThreshold = 500;
}])



