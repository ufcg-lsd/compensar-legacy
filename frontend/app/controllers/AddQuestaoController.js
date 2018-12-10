angular.module('app')
    .controller('AddQuestaoController',  function($rootScope,$scope,$http,UserService)
    {

        $scope.editorModules = {
            formula: true, 
            toolbar: [
              ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
              ['blockquote'],

              //[{ 'header': 1 }, { 'header': 2 }],               // custom button values
              [{ 'list': 'ordered' }, { 'list': 'bullet' }],
              [{ 'script': 'sub' }, { 'script': 'super' }],      // superscript/subscript
              [{ 'indent': '-1' }, { 'indent': '+1' }],          // outdent/indent
              [{ 'direction': 'rtl' }],                         // text direction

              [{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown
              [{ 'header': [1, 2, 3, 4, 5, 6, false] }],

              [{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme
              //[{ 'font': [] }],
              [{ 'align': [] }],

              //['clean'],                                         // remove formatting button

              ['formula','link']                         // link and image, video
            ]
          }

        $scope.fonte = "";
        $scope.enunciado = "";
        $scope.espelho = "";
        $scope.tipo = "";

        $scope.title = '';
        $scope.changeDetected = false;


        $scope.isObjective = function(){ return $scope.tipo === "Objetiva"};

        $scope.resposta = {}

        $scope.sendQuestionSubjective = function () {

            questaoSubj = {
                autor:  UserService.getName(),
                enunciado: $scope.enunciado,
                espelho:  $scope.resposta.espelho,
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




        $scope.alternativas = {}
        $scope.corretas = {}


        $scope.sendQuestionObjective = function () {

            questaoObj = {
                alternativas: [
                    {
                        correta: $scope.corretas.Value1,
                        texto: $scope.alternativas.alternativa1
                      },
                    {
                        correta: $scope.corretas.Value2,
                        texto: $scope.alternativas.alternativa2
                      },
                      {
                        correta: $scope.corretas.Value3,
                        texto: $scope.alternativas.alternativa3
                      },
                      {
                        correta: $scope.corretas.Value4,
                        texto: $scope.alternativas.alternativa4
                      },
                      {
                        correta: $scope.corretas.Value5,
                        texto: $scope.alternativas.alternativa5
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