/* global host */
angular.module('app')
    .controller('SignUpController', function ($scope, $rootScope, $location, UserService,AuthService, $http,$q, Notification) {

        $scope.nomeInstituicao = "";
        $scope.cargo = "";
        $scope.cidade = "";

        $scope.sendSignUp = function () {
            let usuario = {
                cargo: $scope.cargo,
                cidade: $scope.cidade,
                idade: $scope.idade,
                nomeInstituicao: $scope.nomeInstituicao
            };

            $http.post(host + 'auth/signup/', usuario, AuthService.getAuthorization()).
                then(function () {
                    Notification.success("Cadastro efetuado com Sucesso!");
                    $rootScope.registered = true;
                    $location.path("/buscas");
                },function(err){
                    if (err.status == 400) {
                        $rootScope.forceSignOut();
                    } else if (err.status == 409) {
                        Notification.error("Já existe um usuário cadastrado com esse email!");
                        $location.path("/login");
                    } else {
                        Notification.error("Falha inesperada no cadastro!");
                    }
                }
            )
        }

        $scope.inputError = false;
        $scope.validaCadastro = function() {
            if ($scope.cargo === "" || typeof $scope.cargo === 'undefined' ||
                $scope.cidade === "" || typeof $scope.cidade === 'undefined' ||
                $scope.idade === "" || typeof $scope.idade === 'undefined' ||
                $scope.nomeInstituicao === "" || typeof $scope.nomeInstituicao === 'undefined') {
                $scope.inputError = true;
            } else {
                $scope.sendSignUp();
            }
        }
        
        $scope.$on('$routeChangeStart', function($event, next, current) { 
            if (next != current && !$rootScope.registered) {
                $location.path("/signup");
            }
          });
        


    });
