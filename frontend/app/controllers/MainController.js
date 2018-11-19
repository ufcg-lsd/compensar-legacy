angular.module('app')
    .controller('MainController', function ($rootScope, $http, $location, UserService, AuthService, ProfileService, $window, $localStorage) {
        $rootScope.activetab = $location.path();


        $rootScope.isLogged = AuthService.isLogged();

        $rootScope.user_type = "user";
        
        ProfileService.update_visitant_profile();
/* 
        if ($rootScope.isLogged) {
            ProfileService.update_user_profile();
        }
        else {
            ProfileService.update_visitant_profile();
        }
*/
        $rootScope.$on('$routeChangeStart', function (angularEvent, newUrl) {

            //if (!AuthService.isLogged()) 
            $location.path("/login");
/*
            else {

                $http.get('https://prematriculabackend.herokuapp.com/api/aluno/' + AuthService.getUserDetails().email).
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
                            else {
                                $location.path("/questoes");
                            }

                        }
                    );



            }
            */
        });

        $rootScope.$on('event:social-sign-in-success', function (event, userDetails) {
            ProfileService.update_user_profile();
/*
            $rootScope.email = userDetails.email;
            AuthService.setUserDetails(userDetails);
            if (UserService.isCCC()) {
                $rootScope.isCCC = true;
                $rootScope.isLogged = true;
                ProfileService.update_user_profile();
                update_user_type();
                $rootScope.$apply();

            }

            else {
                AuthService.logout();
                $location.path("/login");
                $window.location.href = '/';
                $localStorage.isCCC = false;


            }
*/

        });

        $rootScope.$on('event:social-sign-out-success', function (event, logoutStatus) {
            ProfileService.update_visitant_profile();
            /*
            $rootScope.isLogged = false;
            $location.path("/login");
            $window.location.href = '/login';
            */


        });
});