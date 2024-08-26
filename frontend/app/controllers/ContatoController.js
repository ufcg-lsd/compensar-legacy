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
                },function(err){
                    if (err.status == 400) {
                        $rootScope.forceSignOut();
                    } else {
                        Notification.error("Falha no envio do email!");
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