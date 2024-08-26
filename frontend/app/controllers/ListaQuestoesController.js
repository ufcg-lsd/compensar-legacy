angular.module('app')
    .controller('ListaQuestoesController', function($rootScope,$scope, $location, QuestoesService,UserService, localStorageService, $route)
    {
 
      $rootScope.activetab = $location.path();

      $scope.nomeLista = "";

      $rootScope.loading = false;


      $scope.openQuestoes = function () {
        $rootScope.painelListaEmContrucao = true;
        $rootScope.minhasListas = false;
        $rootScope.listaEmEdicao = false;


        $rootScope.questoes = [];
        $rootScope.nomeLista = $scope.nomeLista;
        $location.path("/questoes");
      }

      $scope.criarLista = function () {
        $location.path("/addLista");
      }

      $scope.cancela = function() {
        $scope.nomeLista = "";
        $rootScope.questoes = [];

        if($rootScope.painelListaEmContrucao) {
          $rootScope.painelListaEmContrucao = false;
        } else {
          $rootScope.listaEmEdicao = false;
          $scope.exibeLista($rootScope.listaEmExibicao);
        }
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
        for (let i = 0; i < $rootScope.questoes.length; i++) {
          let questaoAdded = $rootScope.questoes[i];
  
          if (questaoAdded.id == questao.id) equals = true;
        }
  
        return equals;
      };


      $scope.getPainelListaEmContrucao = function() {
        return $rootScope.painelListaEmContrucao;
    }


    $scope.getMinhasListas = function() {

      return $rootScope.minhasListas;
    }

    $scope.sendListaQuestao = function() {

      let lista = {
        nomeLista: $rootScope.nomeLista,
        email: UserService.getEmail(),
        questoes: $rootScope.questoes
      }
      QuestoesService.sendListaQuestao(lista).then((response) => {
        if (response.status === 200) {
          $rootScope.painelListaEmContrucao = false;
          $rootScope.questoes = [];
          $location.path("/questoes");
        }
      });
    };

    $scope.setListaEmConstrucao = function () {
      limpaTabsQuestoes();

      $rootScope.minhasListas = false;
      $rootScope.painelListas = true;
    };


    $scope.exibeLista = function (lista) {
      limpaTabsQuestoes();
      document.querySelector(".nav-tabs .nav-item.dropdown .nav-link").classList.add("active");

      $rootScope.listaEmExibicao = lista;
      $rootScope.nomeLista = lista.nomeLista;

      $rootScope.listaEmEdicao = false;
      $rootScope.painelListas = true;
      $rootScope.minhasListas = true; 
      $rootScope.lista = lista;
      QuestoesService.getListaQuestoesById(lista);
    }

    $scope.removeLista = function() {
      let index = 0;
      for(; index < $rootScope.listas.length; index++) {
        if ($rootScope.listas[index].id === $rootScope.listaEmExibicao.id) {
          break;
        }
      }
      QuestoesService.removeLista($rootScope.listaEmExibicao).then((response) => {
        if (response.status === 200) {
          $rootScope.listas.splice(index,1);
          $route.reload();
        }
      });
      

    }


    $scope.atualizaLista = function() {
      limpaTabsQuestoes();
      $rootScope.listaEmEdicao = true;
      $rootScope.painelListaEmContrucao = false;
      $rootScope.minhasListas = false; 


      $scope.nomeLista =  $rootScope.listaEmExibicao.nomeLista;
      $rootScope.questoes = [...$rootScope.listaEmExibicao.questoes];
    }

    $scope.sendUpdateLista = function() {

      let novaLista = {
        nomeLista: $rootScope.nomeLista,
        email: UserService.getEmail(),
        questoes: $rootScope.questoes
      }

      QuestoesService.sendUpdateLista($rootScope.listaEmExibicao, novaLista).then((response) => {
        if (response.status === 200) {
          $rootScope.painelListaEmContrucao = false;
          $rootScope.listaEmEdicao = false;
          $rootScope.questoes = [];
          $scope.nomeLista = "";

          $scope.exibeLista($rootScope.listaEmExibicao);
        }
      });
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
    

    $scope.inputError = false;
    $scope.checkAddLista = function() {
        if ($scope.nomeLista === "" || typeof $scope.nomeLista === 'undefined') {
            $scope.inputError = true;
        } else {
            $scope.openQuestoes();
        }
    }

    $scope.imprimirLista = function() {
      localStorageService.set("listaAtiva", $rootScope.lista);
      window.open('/lista/', '_blank');
    }

    $(document).ready(function() {
      if ($location.path() === '/questoes' && !$rootScope.listasRequest) {
        QuestoesService.getListaQuestoes();
      }
    });
  });

