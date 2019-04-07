angular.module('app')
    .controller('ListaQuestoesController', function($rootScope,$scope, $location, QuestoesService,UserService)
    {
 
      $rootScope.activetab = $location.path();

      $scope.nomeLista = "";

      $rootScope.listas = [];

      $scope.openQuestoes = function () {
        $rootScope.painelListaEmContrucao = true;
        $rootScope.minhasListas = false;

        $rootScope.questoes = [];
        $rootScope.nomeLista = $scope.nomeLista;
        $location.path("/questoes");
      }

      $scope.cancela = function() {
        $rootScope.painelListaEmContrucao = false;

        $scope.nomeLista = "";
        $rootScope.questoes = [];
        $location.path("/addLista");
      }

      $scope.addQuestao = function(questao) {
        $rootScope.questoes.push(questao);
      }

      $scope.removeQuestao = function(questao) {
        var  index = $rootScope.questoes.indexOf(questao);

        $rootScope.questoes.splice(index, 1);
      }

      $scope.isAdded = function (questao) {
        return  $scope.checkSameQuestao(questao);
      }

      $scope.checkSameQuestao = function (questao) {
        var equals = false;
        for (i = 0; i < $rootScope.questoes.length; i++) {
          questaoAdded = $rootScope.questoes[i];
  
          if (questaoAdded.id == questao.id) equals = true;
        }
  
        return equals;
      };


      $scope.getPainelListaEmContrucao = function() {
        return $rootScope.painelListaEmContrucao;
    }

  // Edição
 // minhasListas ->  (so remover) 
 // Outros --> edição (add/remover)

    $scope.getMinhasListas = function() {

      return $rootScope.minhasListas;
    }

    $scope.sendListaQuestao = function() {

      lista = {
        nomeLista: $rootScope.nomeLista,
        email: UserService.getEmail(),
        questoes: $rootScope.questoes
      }
      QuestoesService.sendListaQuestao(lista);

      $rootScope.painelListaEmContrucao = false;
      $rootScope.questoes = [];
      $location.path("/addLista");
    };

    $scope.setListaEmConstrucao = function () {
      $rootScope.minhasListas = false;
      $rootScope.painelListas = true;
    };

    $scope.exibeLista = function (lista) {
      $rootScope.listaEmExibicao = lista;
      $rootScope.nomeLista = lista.nomeLista;

      $rootScope.listaEmEdicao = false;
      $rootScope.painelListas = true;
      $rootScope.minhasListas = true; 
      QuestoesService.getListaQuestoesById(lista);
    }

    $scope.removeLista = function() {
      var  index = $rootScope.listas.indexOf($rootScope.listaEmExibicao);
      QuestoesService.removeLista($rootScope.listaEmExibicao);
      $rootScope.listas.splice(index,1);
    }


    $scope.atualizaLista = function() {
      $rootScope.listaEmEdicao = true;
      $rootScope.painelListaEmContrucao = true;
      $rootScope.minhasListas = false; 

      console.log($rootScope.listaEmEdicao);

      $scope.nomeLista =  $rootScope.listaEmExibicao.nomeLista;
      $rootScope.questoes = $rootScope.listaEmExibicao.questoes;
    }

    $scope.sendUpdateLista = function() {

      novaLista = {
        nomeLista: $rootScope.nomeLista,
        email: UserService.getEmail(),
        questoes: $rootScope.questoes
      }

      QuestoesService.sendUpdateLista($rootScope.listaEmExibicao, novaLista);

      $rootScope.painelListaEmContrucao = false;
      $rootScope.listaEmEdicao = false;
      $rootScope.questoes = [];
      $scope.nomeLista = "";

      $location.path("/addLista");

    }

    $scope.getListaEmEdicao = function() {
      return $rootScope.listaEmEdicao;
    }

    $scope.updateNomeLista = function() {
      $rootScope.nomeLista = $scope.nomeLista;
    }

    $scope.getName = function() {
      return $rootScope.nomeLista;
    }
    
    QuestoesService.getListaQuestoes();




  });

