angular.module('app')
  .factory('QuestoesService', function($rootScope,$http,$q,$window) {
   const service = {};
   deferred = $q.defer();

   service.getQuestoes = function () {
      $http.get('http://localhost:5458/api/questao').
      then(function (response) {
        $rootScope.Questoes = response.data;
        deferred.resolve(response.data);
      }, function (response) {
        deferred.resolve([]);
      });

    return deferred.promise;
   },


   service.sendQuery = function (query) {
    $http.get('http://localhost:5458/api/questao/search/'+ query.enunciado + '/' + query.competencias 
    + '/' + query.autor + '/' + query.fonte + '/' + query.tipo + '/' + query.conteudo).
      then(function (response) {
        $rootScope.Questoes = response.data;
        deferred.resolve(response.data);
      }, function (response) {
        deferred.resolve([]);
      });

    return deferred.promise;
   },


  
  service.removeQuestao = function (questao) {

        $http.delete('http://localhost:5458/api/questao/' + questao.id).
          then(function (response) {
            if (response.status == 200) {
              $location.path("/questoes");
              $window.alert("Questão Removida com Sucesso!");        
            }

          }, function () {
            
            $window.alert("Falha ao Remover Questão!");
          }).catch(function (response) { deferred.resolve([]); });
  }



    return service;
  });