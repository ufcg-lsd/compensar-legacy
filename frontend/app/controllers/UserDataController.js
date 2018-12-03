angular.module('app')
    .controller('UserDataController', function ($scope, $rootScope, $location, AuthService, UserService,$http) {

        $location.path('/userdata');

        $rootScope.activetab = $location.path();
        $rootScope.user_email = AuthService.getUserDetails().Email;
        $scope.instituicao_usuario = "";
        $scope.cargo_usuario = "";
        $scope.cidade_usuario = "";
        $scope.idade_usuario = "";


        $http.get('http://localhost:5458/api/usuario/' + AuthService.getUserDetails().Email).
        then(function (response) { 
            $scope.instituicao_usuario = response.data.nomeInstituicao,
            $scope.cargo_usuario = response.data.cargo,
            $scope.idade_usuario = response.data.idade,
            $scope.cidade_usuario = response.data.cidade
        });
    
        $scope.send = function send() {

            usuario = {
                ativo: true,
                cargo: $scope.cargo_usuario,
                cidade: $scope.cidade_usuario,
                email: UserService.getEmail(),
                idade: $scope.idade_usuario,
                nome:  UserService.getName(),
                nomeInstituicao: $scope.instituicao_usuario 
            };

            $http.put('http://localhost:5458/api/usuario/' + usuario.email, usuario).
            then(function (response) {

                if (response.status == 200) {
                    window.alert("Usuario atualizado com Sucesso!");
                    $location.path("/userdata");
                }

                else {
                    window.alert("Falha ao Atualizar dados!");
                    $location.path("/userdata");
                }
            })
        }

        $scope.validaCadastro = function () {

            return !isNaN($scope.nomeInstituicao);
        }



    });

