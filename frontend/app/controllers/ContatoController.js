/* global host */
angular.module('app')
    .controller('ContatoController',  function($scope, $location,$http,$mdDialog,Notification)
    {
        $scope.username = "";
        $scope.email = "";
        $scope.message = "";
        $scope.assunto = "";
      
            $scope.sendEmail = function() {
 
                let email = {
                    email: $scope.email,
                    message: $scope.message,
                    subject: $scope.assunto,
                    username: $scope.username
                };
    
                $http.post(host + 'email', email).
                then(function (response) {
                    Notification.success("Mensagem enviada com sucesso!");        
                    $location.path("/login");
                },function(){
                    if (err.status == 400) {
                        signOut();
                        Notification.warning("Seu login expirou, por favor faça login novamente!");
                    } else {
                        Notification.error("Falha no envio do email!");
                        $location.path("/login");
                    }
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