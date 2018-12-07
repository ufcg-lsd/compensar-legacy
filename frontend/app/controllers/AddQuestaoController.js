angular.module('app')
    .controller('AddQuestaoController',  function($rootScope,$scope,$http,UserService)
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
                        window.alert("Questão enviada com Sucesso! \n Você pode consultá-la na aba Questões.");
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

        $rootScope.alternativa1 = "";
        $scope.alternativa2 = "";
        $scope.alternativa3 = "";
        $scope.alternativa4 = "";
        $scope.alternativa5 = "";

        $scope.corretaValue1 = "";



        $scope.sendQuestionObjective = function () {

            questaoObj = {
                alternativas: [
                    {
                        correta: $scope.corretaValue1,
                        texto: $rootScope.alternativa1
                      },
                    {
                        correta: $scope.corretaValue2,
                        texto: $scope.alternativa2
                      },
                      {
                        correta: $scope.corretaValue3,
                        texto: $scope.alternativa3
                      },
                      {
                        correta: $scope.corretaValue4,
                        texto: $scope.alternativa4
                      },
                      {
                        correta: $scope.corretaValue5,
                        texto: $scope.alternativa5
                      }
                ],
                autor:  UserService.getName(),
                enunciado: $scope.enunciado,
                espelho: $scope.espelho,
                fonte: $scope.fonte,
                imagem: $scope.imagem,
                tipo: $scope.tipo
            };

            $http.post('http://localhost:5458/api/questaoObj', questaoObj).
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