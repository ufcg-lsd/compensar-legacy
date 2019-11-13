/* global host */
angular.module('app')
  .factory('QuestoesService', function($rootScope,$http,$q,Notification,$location, AuthService) {
   const service = {};
   let deferred = $q.defer();

   service.getQuestoes = function (pageNumber, usersPerPage) {
      $http.get(host + 'questao/' + pageNumber + '/' + usersPerPage, AuthService.getAuthorization()).
      then(function (response) {
        $rootScope.Questoes = response.data.content;
        $rootScope.totalQuestoes = response.data.totalElements;
        $rootScope.pageNumber = response.data.number;
        $rootScope.totalPags = response.data.totalPages;
        $rootScope.loading = false;

        
        deferred.resolve(response.data);
      }, function () {
        deferred.resolve([]);
      });

    return deferred.promise;
   },


   service.sendQuery = function (query, pageNumber, usersPerPage, apenasAutor) {
    $http.get(host + 'questao/' + ((apenasAutor) ? 'searchMy/' : 'search/') + query.enunciado + '/' + query.competencias 
    + '/'  + ((apenasAutor) ? '' : query.autor + '/') + query.fonte + '/' + query.tipo + '/' + query.conteudo + '/' + pageNumber + '/' + usersPerPage, AuthService.getAuthorization()).
      then(function (response) {
        $rootScope.Questoes = response.data.content;
        $rootScope.totalQuestoes = response.data.totalElements;
        $rootScope.pageNumber = response.data.number;
        $rootScope.totalPags = response.data.totalPages;
        $rootScope.loading = false;


        if (query.enunciado !== "") $rootScope.adicionaMarcadores(query.enunciado);


        deferred.resolve(response.data);
     
      }, function () {
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

          }).catch(function () { deferred.resolve([]); });
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
      let questoes = [];
      for(let questao of lista.questoes) {
        questoes.push(questao.id);
      }
      lista.questoes = questoes;
      $http.post(host + 'listaquestoes', lista, AuthService.getAuthorization()).
        then(function (response) {
            if (response.status == 200) {
                Notification.success('Lista criada com sucesso!');
                $location.path("/questoes");
                service.getListaQuestoes();
              }
            else {
                Notification.error('Falha no envio da lista!');
                $location.path("/questoes");
              }
        },function(){
          $location.path("/questoes");
        }
    )
  },

  service.getListaQuestoes = function () {
    $http.get(host + 'listaquestoes/0/100', AuthService.getAuthorization()).
      then(function (response) {
        $rootScope.listas = response.data.content;
        deferred.resolve(response.data.content);
      }, function () {
        deferred.resolve([]);
      });

  return deferred.promise;
 },

 service.getListaQuestoesById = function (lista) {
  $http.get(host + 'listaquestoes/' + lista.id, AuthService.getAuthorization()).
  then(function (response) {
    $rootScope.Questoes = response.data.questoes;
    deferred.resolve(response.data);
  }, function () {
    deferred.resolve([]);
  });

  return deferred.promise;
 },

   
 service.removeLista = function (lista) {

  $http.delete(host + 'listaquestoes/' + lista.id, AuthService.getAuthorization()).
    then(function (response) {
      if (response.status == 200) {
        $location.path("/questoes");
        Notification.success('Lista removida com sucesso!');
      } else {
        Notification.error('Falha ao remover lista!');
        $location.path("/questoes");      
      }

    }).catch(function () { deferred.resolve([]);         $location.path("/questoes");
  });
},

service.sendUpdateLista = function (lista,novaLista) {
  let questoes = [];
  for(let questao of novaLista.questoes) {
    questoes.push(questao.id);
  }
  novaLista.questoes = questoes;
  $http.put(host + 'listaquestoes/' + lista.id, novaLista, AuthService.getAuthorization()).
    then(function (response) {
      if (response.status == 200) {
          let index = 0;
          for(; index < $rootScope.listas.length; index++) {
            if ($rootScope.listas[index].id === lista.id) {
              break;
            }
          }
          $rootScope.listas[index] = response.data;
          $rootScope.listaEmExibicao = response.data;

          Notification.success('Lista atualizada com sucesso!');
          return response.data;
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
          Notification.success('Feedback das competências obtido com sucesso!');
        }
        else {
            Notification.error('Falha no feedback das competências!');
            $rootScope.loading = false;
        }
    },function() {}
  )
}

service.getQuestaoPendente = function() {
  return $http.get(host + 'questao/pendente/', AuthService.getAuthorization())
  .then(function(response) {
    if (response.status == 200) {
      $rootScope.questaoSobAvaliacao = response.data;
    } else {
      Notification.warning('Nenhuma questão pendente de sua avaliação!');
    }
  }, function() {
    Notification.warning('Nenhuma questão pendente de sua avaliação!');
  })
}


    return service;
  });