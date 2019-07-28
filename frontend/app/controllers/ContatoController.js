angular.module('app')
    .controller('ContatoController',  function($scope, $location,$http,$mdDialog)
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
    
                $http.post('https://compensar.herokuapp.com/api/email', email).
                then(function (response) {
                    if (response.status == 200) {
                        $scope.showAlertaEmail();                        
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


        $scope.showAlertaEmail = function() {
            $mdDialog.show(
              $mdDialog.alert()
                .parent(angular.element(document.querySelector('#popupContainer')))
                .clickOutsideToClose(true)
                .title('Mensagem enviado com sucesso!')
                .textContent('Obrigado por entrar em contato.')
                .ariaLabel('Alert Dialog Demo')
                .ok('Fechar')
            );
        };


        $scope.validaEmail = function(){
            return validaString();
        }


       function validaString() {
            return isNaN($scope.nome) && isNaN($scope.email) && isNaN($scope.mensagem);
        }
    });