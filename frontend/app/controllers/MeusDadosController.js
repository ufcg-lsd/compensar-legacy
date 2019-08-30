angular.module('app')
    .controller('MeusDadosController', function ($scope, $rootScope, $location, AuthService, UserService,$http,Notification) {


        $rootScope.activetab = $location.path();
        $location.path('/meusdados');


        $rootScope.user_email = UserService.getEmail();
        $scope.instituicao_usuario = "";
        $scope.cargo_usuario = "";
        $scope.cidade_usuario = "";
        $scope.idade_usuario = "";

        $scope.loading = true;



                $http.get(host + 'usuario/', AuthService.getAuthorization()).
                then(function (response) { 
                    console.log($scope.loading);
        
                    $scope.instituicao_usuario = response.data.nomeInstituicao,
                    $scope.cargo_usuario = response.data.cargo,
                    $scope.idade_usuario = response.data.idade,
                    $scope.cidade_usuario = response.data.cidade
                    $scope.loading = false;

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

            $http.put(host + 'usuario/', user, AuthService.getAuthorization()).
            then(function (response) {

                if (response.status == 200) {
                    Notification.success("Usuario atualizado com Sucesso!");
                    $location.path("/meusdados");
                }

                else {
                    Notification.error("Falha ao Atualizar dados!");
                    $location.path("/meusdados");
                }
            })
        }

        $scope.validaCadastro = function () {

            return !isNaN($scope.nomeInstituicao);
        }

    });

