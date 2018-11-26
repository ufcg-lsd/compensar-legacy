
var app = angular.module('app',['ngStorage','ngRoute','socialLogin']);
var host = "";

app.config(function($routeProvider, $locationProvider) {
    
    // remove o # da url
    $locationProvider.html5Mode(true);

    $routeProvider

    .when('/login', {
        templateUrl: '/app/views/Login.html',
        controller: 'LoginController'
    })	
      .when('/signup', {	
        templateUrl: '/app/views/SignUp.html',	
        controller: 'SignUpController',
        requireAuth: true,
        requireNotRegistered: true
    })	

    .when('/userdata', {
        templateUrl: '/app/views/UserData.html',
        controller: 'UserDataController',
        requireAuth: true,
        requireRegistered:true
      
    })

    .when('/questoes', {	
        templateUrl: '/app/views/SignUp.html',	
        controller: 'SignUpController',
        requireAuth: true,
        requireNotRegistered: true
    })	

    .when('/addQuestao', {	
        templateUrl: '/app/views/SignUp.html',	
        controller: 'SignUpController',
        requireAuth: true,
        requireNotRegistered: true
    })	
      .otherwise({
        redirectTo: '/login'
      });

    });


    app.config(function(socialProvider){
      socialProvider.setGoogleKey("363497084086-sj4dhuvvkmcivpbl0h2fgrrvnm0229og.apps.googleusercontent.com");
  });



