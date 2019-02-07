angular.module('app')
    .controller('SignUpController', function ($scope, $location, UserService,AuthService, $http,$q) {
        deferred = $q.defer();

        $scope.nomeInstituicao = "";
        $scope.cargo = "";
        $scope.cidade = "";

        UserService.isRegistered().then(function (value) {
            this.isRegistered = value;
            console.log(value);
        }).then( function(){   
            if(this.isRegistered) {
                $location.path('/questoes')
            } else {
                $location.path('/signup')
            }
            });

  

                $http.get('http://localhost:5458/api/usuario/' + AuthService.getUserDetails().Email).
                  then(function (response) {
                    $rootScope.registered = response.status == 200;
                  }, function () { 
                    $rootScope.registered = false; 
                  })
            
                  .then(
                    function () {
                      if ($rootScope.registered) {
                        $location.path("/questoes");
                        $window.location.href = '/questoes';
            
                      } 
                      else {
                        $location.path("/signup");
                        $window.location.href = '/signup';
            
                      }
                    }
                  );
            
        


        $scope.sendSignUp = function () {
            usuario = {
                ativo: true,
                cargo: $scope.cargo,
                cidade: $scope.cidade,
                email: UserService.getEmail(),
                idade: $scope.idade,
                nome:  UserService.getName(),
                nomeInstituicao: $scope.nomeInstituicao
            };

            $http.post('http://localhost:5458/api/usuario', usuario).
                then(function (response) {
                    if (response.status == 200) {
                        window.alert("Cadastro efetuado com Sucesso!");
                        $location.path("/questoes");
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
