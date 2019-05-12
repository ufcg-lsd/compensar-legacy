angular.module('app')
    .controller('MeusDadosController', function ($scope, $rootScope, $location, AuthService, UserService,$http) {


        $rootScope.activetab = $location.path();
        $location.path('/meusdados');


        $rootScope.user_email = UserService.getEmail();
        $scope.instituicao_usuario = "";
        $scope.cargo_usuario = "";
        $scope.cidade_usuario = "";
        $scope.idade_usuario = "";


        $http.get('https://compensar.herokuapp.com/api/usuario/' + AuthService.getUserDetails().Email).
        then(function (response) { 
            $scope.instituicao_usuario = response.data.nomeInstituicao,
            $scope.cargo_usuario = response.data.cargo,
            $scope.idade_usuario = response.data.idade,
            $scope.cidade_usuario = response.data.cidade
        });
    
        $scope.updateUser = function updateUser() {

            user = {
                ativo: true,
                cargo: $scope.cargo_usuario,
                cidade: $scope.cidade_usuario,
                email: UserService.getEmail(),
                idade: $scope.idade_usuario,
                nome:  UserService.getName(),
                nomeInstituicao: $scope.instituicao_usuario 
            };

            $http.put('https://compensar.herokuapp.com/api/usuario/' + AuthService.getUserDetails().Email, user).
            then(function (response) {

                if (response.status == 200) {
                    window.alert("Usuario atualizado com Sucesso!");
                    $location.path("/meusdados");
                }

                else {
                    window.alert("Falha ao Atualizar dados!");
                    $location.path("/meusdados");
                }
            })
        }

        $scope.validaCadastro = function () {

            return !isNaN($scope.nomeInstituicao);
        }



    });

