angular.module('app')
    .controller('ContatoController',  function($scope, $location,$http)
    {
         $scope.nome = "";
        $scope.email = "";
        $scope.mensagem = "";
        $scope.assunto = "";
      
            $scope.sendEmail = function() {

                email = {
                    nome: $scope.nome,
                    email: $scope.email,
                    assunto: $scope.assunto,
                    mensagem: $scope.mensagem
                };
    
                $http.post('https://localhost:8080/api/email', email).
                    then(function (response) {
                        
                            window.alert("Email enviado com Sucesso!");
                            $location.path("/login");
                        },
                        function(){ window.alert("Falha no Envio de email");
                        $location.path("/login");}
   
                    )
            }


        $scope.validaEmail = function(){
            return validaString();
        }


       function validaString() {
            return isNaN($scope.nome) && isNaN($scope.email) && isNaN($scope.mensagem);
        }
    });