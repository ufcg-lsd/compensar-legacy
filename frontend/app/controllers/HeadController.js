angular.module('app')
    .controller('HeadController', function($rootScope,$scope, $location,AuthService,$window,$http)
    {
 
   $rootScope.activetab = $location.path();
  

    $rootScope.logout = function(){
        AuthService.logout();
    }

    let usuarios = [];
    let listaQuestoes = [];
    let result = [];


    $scope.alert = function(){
        $window.alert("O Resultado está no console!");
    }


        $http.get('https://localhost:8001/api/usuario').
        then(function (response) {
          usuarios = response.data;
          
         
        }, function (response) {
          usuarios = [];
        }).then(function(){
        $http.get('https://localhost:8001/api/listaquestoes').
        then(function (response) {
          listaQuestoes = response.data;
        
          for (i = 0; i < listaQuestoes.length; i++) {
            const lista = listaQuestoes[i];
            const usuario = usuarios.filter( k => k.email === lista.email)[0];
            resultLista = {
              nome: usuario.nome,
              email: usuario.email,
              nomeInstituicao: usuario.nomeInstituicao,
              questoesEscolhidas: lista.questoes
          }
          result.push(resultLista);
           }

          console.log(result);
  
        }, function (response) {
          listaQuestoes = [];
        });
        })
    });

