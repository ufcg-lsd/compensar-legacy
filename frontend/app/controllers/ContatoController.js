angular.module('app')
    .controller('ContatoController',  function($scope, $location,$http)
    {
        $scope.nome = "";
        $scope.email = "";
        $scope.mensagem = "";
        $scope.assunto = "";
      
            $scope.sendEmail = function() {

                email = {
                    email: $scope.email,
                    mensagem: $scope.mensagem,
                    assunto: $scope.assunto,
                    nome: $scope.nome
                };
    
                $http.post('http://localhost:5458/api/email', email).
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