angular.module('app')
    .controller('MainController', function ($rootScope, $http, $location, AuthService,UserService, ProfileService, $window) {
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

                $http.get('https://compensar.herokuapp.com/api/usuario/' + AuthService.getUserDetails().Email).
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
                            else if (newUrl.requireRegistered && !UserService.isRegistered()) {
                                $location.path("/signup");
                            }

                        }
                    );
            }
        });






    });