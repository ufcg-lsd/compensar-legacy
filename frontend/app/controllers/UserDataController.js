angular.module('app')
    .controller('UserDataController', function ($scope, $rootScope, $location, AuthService, UserService,$http) {

        $location.path('/questoes');

        $rootScope.activetab = $location.path();
        $rootScope.user_email = AuthService.getUserDetails().email;
        $scope.instituicao_usuario = "";

        $http.get('https://localhost:8080/api/aluno/' + AuthService.getUserDetails().email).
        then(function (response) { $scope.instituicao_usuario = response.data.nomeInstituicao});
    
        $scope.send = function send() {

            usuario = {
                nome: UserService.getName(),
                nomeInstituicao: $scope.instituicao_usuario,
                email: UserService.getEmail(),
                ativo: true
            };

            $http.put('https://localhost:8080/api/aluno/' + usuario.email, usuario).
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

