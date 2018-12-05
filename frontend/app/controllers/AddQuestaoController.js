angular.module('app')
    .controller('AddQuestaoController',  function($scope,$rootScope,$http)
    {

        $scope.title = '';
        $scope.changeDetected = false;

        $scope.autor = "";
        $rootScope.tipo = "";
        $scope.fonte = "";
        $scope.enunciado = "";
        $scope.objetiva = $rootScope.isObjective;


        $rootScope.isObjective = $rootScope.tipo == "Objetiva";


        $scope.editorCreated = function (editor) {
            console.log(editor);
        };
        $scope.contentChanged = function (editor, html, text) {
              $scope.changeDetected = true;
            console.log('editor: ', editor, 'html: ', html, 'text:', text);
        };



    });