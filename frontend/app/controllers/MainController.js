angular.module('app')
    .controller('MainController', function ($rootScope, $http, $location, AuthService,$scope, ProfileService,$mdDialog, Notification) {
        $rootScope.activetab = $location.path();

        $rootScope.isLogged = AuthService.isLogged();

        if ($rootScope.isLogged) {
            ProfileService.update_user_profile();
        }
        else {
            ProfileService.update_visitant_profile();
        }

        $rootScope.$on('$routeChangeStart', function (angularEvent, newUrl) {

            /*
            if (!AuthService.isLogged()) {
                $location.path("/login");
            }
            else {
                AuthService.update_view();
            }
            */
        });

    });