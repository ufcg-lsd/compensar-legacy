angular.module('app')
    .controller('ContatoController',  function($scope, $location,$http)
    {
        $scope.username = "";
        $scope.email = "";
        $scope.message = "";
        $scope.assunto = "";
      
            $scope.sendEmail = function() {
 
                email = {
                    email: $scope.email,
                    message: $scope.message,
                    subject: $scope.assunto,
                    username: $scope.username
                };
    
                $http.post('http://localhost:5458/api/email', email).
                then(function (response) {
                    if (response.status == 200) {
                        window.alert("Email enviado com Sucesso!");
                        $location.path("/login");
                    }
                    else {
                        window.alert("Falha no envio do email!");
                        $location.path("/login");
                    }
                },function(){
                    $location.path("/login");
                }
                );
            };


        $scope.validaEmail = function(){
            return validaString();
        }


       function validaString() {
            return isNaN($scope.nome) && isNaN($scope.email) && isNaN($scope.mensagem);
        }
    });