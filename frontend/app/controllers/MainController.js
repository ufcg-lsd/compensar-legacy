angular.module('app')
    .controller('MainController', function ($rootScope, $http, $location, AuthService,$scope, ProfileService) {
        $rootScope.activetab = $location.path();

        $rootScope.isLogged = AuthService.isLogged();

        if ($rootScope.isLogged) {
            ProfileService.update_user_profile();
        }
        else {
            ProfileService.update_visitant_profile();
        }

    });