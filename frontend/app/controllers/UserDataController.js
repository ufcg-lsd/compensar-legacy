angular.module('app')
    .controller('UserDataController', function ($scope, $rootScope, $location, AuthService, UserService,$http) {

        $location.path('/userdata');

        $rootScope.activetab = $location.path();
        $rootScope.user_email = AuthService.getUserDetails().Email;
        $scope.instituicao_usuario = "";

        $http.get('http://localhost:5458/api/usuario/' + AuthService.getUserDetails().Email).
        then(function (response) { $scope.instituicao_usuario = response.data.nomeInstituicao});
    
        $scope.send = function send() {

            usuario = {
                nome: UserService.getName(),
                nomeInstituicao: $scope.instituicao_usuario,
                email: UserService.getEmail(),
                ativo: true
            };

            $http.put('http://localhost:5458/api/usuario/' + usuario.Email, usuario).
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

