angular.module('app')
    .controller('SignUpController', function ($scope, $location, UserService, $http,$q) {
        deferred = $q.defer();

        $scope.nomeInstituicao = "";

        UserService.isRegistered().then(function (value) {
            this.isRegistered = value;
            
        }).then( function(){   if(this.isRegistered)
        $location.path('/questoes')});

        $scope.sendSignUp = function () {

            usuario = {
                nome:  UserService.getName(),
                nomeInstituicao: $scope.nomeInstituicao,
                email: UserService.getEmail(),
                ativo: true
            };

            $http.post('/api/usuario', usuario).
                then(function (response) {
                    if (response.status == 200) {
                        window.alert("Cadastro efetuado com Sucesso!");
                        $location.path("/questoes");
                    }
                    else {
                        window.alert("Falha no Cadastro");
                        $location.path("/login");
                    }
                },function(){
                    window.alert("Falha no Cadastro");
                    $location.path("/login");
                }
            )
        }










        $scope.validaCadastro = function () {

            return !isNaN($scope.nomeInstituicao);
        }


    });
