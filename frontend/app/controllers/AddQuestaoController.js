angular.module('app')
    .controller('AddQuestaoController',  function($scope,$http,UserService)
    {
        $scope.title = '';
        $scope.changeDetected = false;

        $scope.fonte = "";
        $scope.enunciado = "";
        $scope.espelho = "";
        $scope.tipo = "";

        $scope.isObjective = function(){ return $scope.tipo === "Objetiva"};


        $scope.sendQuestionSubjective = function () {

            questaoSubj = {
                autor: $scope.autor,
                autor:  UserService.getName(),
                enunciado: $scope.enunciado,
                espelho: $scope.espelho,
                fonte: $scope.fonte,
                imagem: $scope.imagem,
                tipo: $scope.tipo
            };

            $http.post('http://localhost:5458/api/questaoSubj', questaoSubj).
                then(function (response) {
                    if (response.status == 200) {
                        window.alert("Questão enviada com Sucesso!");
                        $location.path("/addQuestao");
                    }
                    else {
                        window.alert("Falha no envio da Questão");
                        $location.path("/addQuestao");
                    }
                },function(){
                    $location.path("/addQuestao");
                }
            )

        }

        $scope.editorCreated = function (editor) {
            console.log(editor);
        };
        $scope.contentChanged = function (editor, html, text) {
              $scope.changeDetected = true;
            console.log('editor: ', editor, 'html: ', html, 'text:', text);
        };



    });