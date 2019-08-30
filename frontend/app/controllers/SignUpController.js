angular.module('app')
    .controller('SignUpController', function ($scope, $location, UserService,AuthService, $http,$q, Notification, localStorageService) {
        deferred = $q.defer();

        $scope.nomeInstituicao = "";
        $scope.cargo = "";
        $scope.cidade = "";

        $scope.sendSignUp = function () {
            usuario = {
                cargo: $scope.cargo,
                cidade: $scope.cidade,
                idade: $scope.idade,
                nomeInstituicao: $scope.nomeInstituicao
            };

            $http.post(host + 'auth/signup/', usuario, AuthService.getAuthorization()).
                then(function (response) {
                    Notification.success("Cadastro efetuado com Sucesso!");
                    $location.path("/buscas");
                },function(err){
                    if (err.status == 400) {
                        Notification.error("Ocorreu um erro com o seu login, faça login novamente!");
                        $location.path("/login");
                    } else if (err.status == 409) {
                        Notification.error("Já existe um usuário cadastrado com esse email!");
                        $location.path("/login");
                    } else {
                        Notification.error("Falha inesperada no cadastro!");
                        $location.path("/signup");
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
        

        


    });
