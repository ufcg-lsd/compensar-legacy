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
            } else {
              $window.alert("Falha ao remover Questão!");        
            }

          }).catch(function (response) { deferred.resolve([]); });
  },


  service.atualizaQuestao = function (questao,novaQuestao) {
    console.log(novaQuestao.conteudo);

    $http.put('http://localhost:5458/api/questao/' + questao.id, novaQuestao).
      then(function (response) {
        if (response.status == 200) {
            var  index = $rootScope.Questoes.indexOf(questao);
            $rootScope.Questoes[index] = response.data;

            window.alert("Questão atualizada com Sucesso!");
            $location.path("/questoes");
        }
        else {
            window.alert("Falha ao atualizar Questão");
            $location.path("/questoes");
        }
    },function(){
        $location.path("/questoes");
    });
},

  service.sendListaQuestao = function (lista) {
      $http.post('http://localhost:5458/api/listaquestoes', lista).
        then(function (response) {
            if (response.status == 200) {
                window.alert("Lista criada com Sucesso!");
                $location.path("/addLista");
              }
            else {
                window.alert("Falha no envio da Lista");
                $location.path("/addLista");
              }
        },function(){
          $location.path("/addLista");
        }
    )
  },

  service.getListaQuestoes = function () {
    $http.get('http://localhost:5458/api/listaquestoes').
      then(function (response) {
        $rootScope.listas = response.data;
        deferred.resolve(response.data);
      }, function (response) {
        deferred.resolve([]);
      });

  return deferred.promise;
 },

 service.getListaQuestoesById = function (lista) {
  $http.get('http://localhost:5458/api/listaquestoes/' + lista.id).
  then(function (response) {
    $rootScope.Questoes = response.data.questoes;

    deferred.resolve(response.data);
  }, function (response) {
    deferred.resolve([]);
  });

  return deferred.promise;
 },

   
 service.removeLista = function (lista) {

  $http.delete('http://localhost:5458/api/listaquestoes/delete/' + lista.id).
    then(function (response) {
      if (response.status == 200) {
        $location.path("/questoes");
        $window.alert("Lista Removida com Sucesso!");        
      } else {
        $window.alert("Falha ao remover Lista!"); 
        $location.path("/questoes");      
      }

    }).catch(function (response) { deferred.resolve([]);         $location.path("/questoes");
  });
},

service.sendUpdateLista = function (lista,novaLista) {
  $http.put('http://localhost:5458/api/listaquestoes/' + lista.id, novaLista).
    then(function (response) {
      if (response.status == 200) {
          var  index = $rootScope.listas.indexOf(lista);
          $rootScope.listas[index] = response.data;

          window.alert("Lista atualizada com Sucesso!");
          $location.path("/questoes");
      }
      else {
          window.alert("Falha ao atualizar Lista");
          $location.path("/questoes");
      }
  },function(){
      $location.path("/questoes");
  });
}


    return service;
  });