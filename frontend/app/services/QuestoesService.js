angular.module('app')
  .factory('QuestoesService', function($rootScope,$http,$q,$window) {
   const service = {};
   deferred = $q.defer();

   service.getQuestoesSubj = function () {

     $http.get('http://localhost:5458/api/questaoSubj').
       then(function (response) {
         $rootScope.QuestoesSubj = response.data;
         deferred.resolve(response.data);
       }, function (response) {
         deferred.resolve([]);
       });

     return deferred.promise;
   }

   service.getQuestoesObj = function () {

    $http.get('http://localhost:5458/api/questaoObj').
      then(function (response) {
        $rootScope.QuestoesObj = response.data;
        deferred.resolve(response.data);
      }, function (response) {
        deferred.resolve([]);
      });

    return deferred.promise;
  }

  
  service.removeQuestaoSubj = function (questaoSubj) {
    console.log(questaoSubj);
    $http.delete('http://localhost:5458/api/questaoSubj/' + questaoSubj).
      then(function (response) {
        if (response.status == 200) {
          $window.alert("Questão Removida com Sucesso!");
          $location.path("/questoes");
    
        }

      }, function () {
        
        $window.alert("Falha ao Remover Questão!");
      }).catch(function (response) { deferred.resolve([]); });


  
    }

    return service;

  });