angular.module('app')
  .factory('QuestoesService', function($rootScope,$http,$q,$window,Notification,$location, AuthService) {
   const service = {};
   deferred = $q.defer();

   service.getQuestoes = function (pageNumber, usersPerPage) {
      $http.get(host + 'questao/' + pageNumber + '/' + usersPerPage, AuthService.getAuthorization()).
      then(function (response) {
        $rootScope.Questoes = response.data.content;
        $rootScope.totalQuestoes = response.data.totalElements;
        $rootScope.pageNumber = response.data.number;
        $rootScope.totalPags = response.data.totalPages;
        $rootScope.loading = false;

        
        deferred.resolve(response.data);
      }, function (response) {
        deferred.resolve([]);
      });

    return deferred.promise;
   },


   service.sendQuery = function (query, pageNumber, usersPerPage) {
    $http.get(host + 'questao/search/'+ query.enunciado + '/' + query.competencias 
    + '/' + query.autor + '/' + query.fonte + '/' + query.tipo + '/' + query.conteudo + '/' + pageNumber + '/' + usersPerPage, AuthService.getAuthorization()).
      then(function (response) {
        $rootScope.Questoes = response.data.content;
        $rootScope.totalQuestoes = response.data.totalElements;
        $rootScope.pageNumber = response.data.number;
        $rootScope.totalPags = response.data.totalPages;
        $rootScope.loading = false;


        if (query.enunciado !== "") $rootScope.adicionaMarcadores(query.enunciado,query.competencias);


        deferred.resolve(response.data);
     
      }, function (response) {
        deferred.resolve([]);

        

      });

    return deferred.promise;
   },


  
  service.removeQuestao = function (questao) {

        $http.delete(host + 'questao/' + questao.id, AuthService.getAuthorization()).
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
    $rootScope.loading = true;

    $http.put(host + 'questao/' + questao.id, novaQuestao, AuthService.getAuthorization()).
      then(function (response) {
        if (response.status == 200) {
            var  index = $rootScope.Questoes.indexOf(questao);
            $rootScope.Questoes[index] = response.data;
            console.log(questao.competencias);

            console.log(response.data.competencias);
            $rootScope.loading = false;

            $rootScope.showAlertaEdicao();
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
      $http.post(host + 'listaquestoes', lista, AuthService.getAuthorization()).
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
    $http.get(host + 'listaquestoes', AuthService.getAuthorization()).
      then(function (response) {
        $rootScope.listas = response.data;
        deferred.resolve(response.data);
      }, function (response) {
        deferred.resolve([]);
      });

  return deferred.promise;
 },

 service.getListaQuestoesById = function (lista) {
  $http.get(host + 'listaquestoes/' + lista.id, AuthService.getAuthorization()).
  then(function (response) {
    $rootScope.Questoes = response.data.questoes;
    deferred.resolve(response.data);
  }, function (response) {
    deferred.resolve([]);
  });

  return deferred.promise;
 },

   
 service.removeLista = function (lista) {

  $http.delete(host + 'listaquestoes/delete/' + lista.id, AuthService.getAuthorization()).
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
  $http.put(host + 'listaquestoes/' + lista.id, novaLista, AuthService.getAuthorization()).
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
},

service.getCompetencias = function (enunciado) {
  return $http.post(host + 'competencias', enunciado, AuthService.getAuthorization()).
    then(function (response) {
        if (response.status == 200) {
          $rootScope.competencias = response.data;
          $rootScope.loading = false;
          console.log($rootScope.competencias)

        }
        else {
            window.alert("Falha no feedback das competências");
            $rootScope.loading = false;
        }
    },function() {
    }
  )
}


    return service;
  });