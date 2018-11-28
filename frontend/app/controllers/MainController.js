angular.module('app')
    .controller('MainController', function ($rootScope,$scope, $http, $location, AuthService, ProfileService, $window) {
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

                $http.get('/api/usuario/' + AuthService.getUserDetails().email).
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
                        }
                    );



            }
        });

        $rootScope.$on('event:social-sign-in-success', function (event, userDetails) {

            $rootScope.email = userDetails.email;
            AuthService.setUserDetails(userDetails);

            $rootScope.isLogged = true;
            ProfileService.update_user_profile();
            $rootScope.$apply();
   
        });


        $rootScope.$on('event:social-sign-out-success', function (event, logoutStatus) {
            ProfileService.update_visitant_profile();
            $rootScope.isLogged = false;
            if ($scope.$root.$$phase != '$apply' && $scope.$root.$$phase != '$digest') $scope.$apply();
            $location.path("/login");
            $window.location.href = '/login';
        });
    });