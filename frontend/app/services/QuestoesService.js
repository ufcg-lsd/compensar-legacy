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
              Notification.success('Questão removida com sucesso!');
              $location.path("/questoes");
            } else {
              Notification.error('Falha ao remover questão!');
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

            Notification.success('Questão atualizada com sucesso!');
            $location.path("/questoes");
        }
        else {
            Notification.error('Falha ao atualizar questão!');
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
                Notification.success('Lista criada com sucesso!');
                $location.path("/addLista");
              }
            else {
                Notification.error('Falha no envio da lista!');
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
        Notification.success('Lista removida com sucesso!');
      } else {
        Notification.error('Falha ao remover lista!');
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

          Notification.success('Lista atualizada com sucesso!');
          $location.path("/questoes");
      }
      else {
        Notification.error('Falha ao atualizar lista!');
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
            Notification.error('Falha no feedback das competências!');
            $rootScope.loading = false;
        }
    },function() {
    }
  )
}


    return service;
  });