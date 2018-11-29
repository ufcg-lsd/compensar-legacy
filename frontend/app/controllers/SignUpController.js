angular.module('app')
    .controller('SignUpController', function ($scope, $location, UserService, $http,$q) {
        deferred = $q.defer();

        $scope.nomeInstituicao = "";

        UserService.isRegistered().then(function (value) {
            this.isRegistered = value;
            
        });

        $scope.sendSignUp = function () {
            usuario = {
                ativo: true,
                email: UserService.getEmail(),
                nome:  UserService.getName(),
                nomeInstituicao: $scope.nomeInstituicao
            };

            $http.post('http://localhost:5458/api/usuario', usuario).
                then(function (response) {
                    if (response.status == 200) {
                        window.alert("Cadastro efetuado com Sucesso!");
                        $location.path("/userdata");
                    }
                    else {
                        window.alert("Falha no Cadastro");
                        $location.path("/signup");
                    }
                },function(){
                    $location.path("/signup");
                }
            )
        }










        $scope.validaCadastro = function () {

            return !isNaN($scope.nomeInstituicao);
        }


    });
