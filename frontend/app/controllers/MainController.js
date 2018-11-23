angular.module('app')
    .controller('MainController', function ($rootScope, $http, $location, AuthService, ProfileService, $window) {
        $rootScope.activetab = $location.path();

        $rootScope.isLogged = AuthService.isLogged();

        if ($rootScope.isLogged) {
            ProfileService.update_user_profile();
        }
        else {
            ProfileService.update_visitant_profile();
        }

        $rootScope.$on('$routeChangeStart', function (angularEvent, newUrl) {

            if (!AuthService.isLogged()) $location.path("/login");

            else {

                $http.get('https://localhost:8001/api/aluno/' + AuthService.getUserDetails().email).
                    then(
                        function (response) {
                            $rootScope.status = response.status == 200;
                        },
                        function () {
                            $rootScope.status = false;
                        }).
                    then(
                        function () {

                            if (newUrl.requireAuth && !AuthService.isLogged()) {
                                $location.path("/login");
                            }
                            else if (newUrl.requireRegistered && !$rootScope.status) {
                                $location.path("/signup");
                            }
                            else if (!AuthService.isLogged() && $rootScope.status) {
                                $location.path("/login");
                            }
                            else {
                                $location.path("/questoes");
                            }

                        }
                    );



            }
        });


        $rootScope.$on('event:social-sign-out-success', function (event, logoutStatus) {
            ProfileService.update_visitant_profile();
            $rootScope.isLogged = false;
            $location.path("/login");
            $window.location.href = '/login';
        });
    });