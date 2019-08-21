angular.module('app')
    .controller('MainController', function ($rootScope, $http, $location, AuthService,$scope, ProfileService,$mdDialog) {
        $rootScope.activetab = $location.path();

        $rootScope.isLogged = AuthService.isLogged();

        if ($rootScope.isLogged) {
            ProfileService.update_user_profile();
        }
        else {
            ProfileService.update_visitant_profile();
        }

        $rootScope.$on('$routeChangeStart', function (angularEvent, newUrl) {

            if (!AuthService.isLogged()) {
                $location.path("/login");
            }
            else {

                $http.get(host + 'usuario/' + AuthService.getUserDetails().Email).
                    then(
                        function (response) {
                            $rootScope.status = response.status == 200;
                        },
                        function () {
                            $rootScope.status = false;
                        }).
                    then(
                        function () {

                            if (newUrl.requireRegistered && $rootScope.status === false) {
                                $location.path("/signup");
                                $scope.showAlertaCadastro();
                            }

                        }
                    );
            }
        });



        $scope.showAlertaCadastro = function() {
            $mdDialog.show(
              $mdDialog.alert()
                .parent(angular.element(document.querySelector('#popupContainer')))
                .clickOutsideToClose(true)
                .title('Você deve se cadastrar')
                .textContent('Você deve se cadastrar para acessar essa página.')
                .ariaLabel('Alert Dialog Demo')
                .ok('Entendi!')
            );
          };



    });