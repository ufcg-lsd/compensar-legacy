angular.module('app')
  .factory('QuestoesService', function($rootScope,$http,$q,$window) {
   const service = {};
   deferred = $q.defer();

   service.getQuestoes = function (pageNumber, usersPerPage) {
      $http.get('http://localhost:5458/api/questao/' + pageNumber + '/' + usersPerPage).
      then(function (response) {
        $rootScope.Questoes = response.data.content;
        $rootScope.totalQuestoes = response.data.totalElements;
        $rootScope.pageNumber = response.data.number;
        $rootScope.totalPags = response.data.totalPages;
        
        deferred.resolve(response.data);
      }, function (response) {
        deferred.resolve([]);
      });

    return deferred.promise;
   },


   service.sendQuery = function (query, pageNumber, usersPerPage) {
    $http.get('http://localhost:5458/api/questao/search/'+ query.enunciado + '/' + query.competencias 
    + '/' + query.autor + '/' + query.fonte + '/' + query.tipo + '/' + query.conteudo + '/' + pageNumber + '/' + usersPerPage).
      then(function (response) {
        $rootScope.Questoes = response.data.content;
        $rootScope.totalQuestoes = response.data.totalElements;
        $rootScope.pageNumber = response.data.number;
        $rootScope.totalPags = response.data.totalPages;
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