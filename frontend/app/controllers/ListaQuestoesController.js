angular.module('app')
    .controller('ListaQuestoesController', function($rootScope,$scope, $location,AuthService,$window,$http)
    {
 
      $rootScope.activetab = $location.path();

      $scope.nomeLista = "";
      $scope.emailLista = "";
      $scope.questoes = [];

      $scope.openQuestoes = function () {
        $rootScope.painelLista = true;
        $location.path("/questoes");
      }

      $scope.cancela = function() {
        $rootScope.painelLista = false;
        $location.path("/addLista");
      }

      $scope.addQuestao = function(questao) {
        $scope.questoes.push(questao);
      }

      $scope.removeQuestao = function(questao) {
        var  index = $scope.questoes.indexOf(questao);

        $scope.questoes.splice(index, 1);
        $location.path("/questoes");

      }

      $scope.isAdded = function (questao) {
        console.log($scope.questoes.includes(questao));
        return $scope.questoes.includes(questao);
      }

      $scope.painelListaQuestoes = function() {
        console.log($rootScope.painelLista);
        return $rootScope.painelLista;
    }

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  });

