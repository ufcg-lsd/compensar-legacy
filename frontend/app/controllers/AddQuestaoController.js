angular.module('app')
    .controller('AddQuestaoController',  function($rootScope,$location,$scope,$http,UserService, AuthService, QuestoesService, $mdDialog, Notification)
    {
        $rootScope.activetab = $location.path();

        // Ativadores das opções de edição no Quill Editor
        $rootScope.editorModules = {
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
              [{ 'font': [] }],
              [{ 'align': [] }],

              //['clean'],                                         // remove formatting button

              ['formula','link','image']                         // link and image, video
            ]
        }

        $scope.fonte = "";
        $scope.enunciado = "";
        $scope.tipo = "";
        $scope.conteudo = "";

        $scope.title = '';
        $scope.changeDetected = false;


        $scope.isObjective = function(){ 
            return $scope.tipo === "Objetiva"
        };


        $scope.sendQuestionSubjective = function () {
            questaoSubj = {
                autor:  UserService.getName(),
                conteudo: $scope.conteudo,
                enunciado: $scope.enunciado,
                espelho:  $scope.resposta.espelho,
                fonte: $scope.fonte,
                competencias: $scope.competencias,
                tipo: $scope.tipo
            };

            $http.post(host + 'questao', questaoSubj, AuthService.getAuthorization()).
                then(function (response) {
                    if (response.status == 200) {
                        Notification.success('Questão criada com sucesso!');
                        $location.path("/buscas");
                    }
                    else {
                        Notification.error("Falha no envio da questão");
                        $location.path("/buscas");
                    }
                },function(){
                    $location.path("/buscas");
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
                conteudo: $scope.conteudo,
                enunciado: $scope.enunciado,
                fonte: $scope.fonte,
                competencias: $scope.competencias,
                tipo: $scope.tipo
            };



            $http.post(host + 'questao', questaoObj, AuthService.getAuthorization()).
                then(function (response) {
                    if (response.status == 200) {
                        Notification.success('Questão criada com sucesso!');
                        $location.path("/buscas");
                    }
                    else {
                        Notification.error("Falha no envio da questão");
                        $location.path("/buscas");
                    }
                },function(){
                    $location.path("/buscas");
                }
            )

        }
        
        $rootScope.loading = false;
        $rootScope.competencias = "";
        $scope.getCompetencias = function () {
            $rootScope.loading = true;
            return QuestoesService.getCompetencias($scope.enunciado);
        };

        $scope.repaginaCompetencia = function (competencia) {
            var compSplitted = competencia.split("_");
            return compSplitted[1];
        };

        $scope.editorCreated = function (editor) {
        };

        $scope.contentChanged = function (editor, html, text) {
            $scope.changeDetected = true;
        };
    

        $().ready(function() {
            $('.selectpicker').selectpicker();
        });

        $scope.step = 1;
 
        $scope.nextStep = function() {
            $scope.step++;
            $scope.inputError = false;
        }
 
        $scope.prevStep = function() {
            $scope.step--;
            $scope.inputError = false;
        }

        $scope.inputError = false;
        $scope.checkPasso1 = function(passo) {
            if ($scope.fonte === "" || typeof $scope.fonte === 'undefined'  || $scope.tipo === "" || 
                typeof $scope.tipo === 'undefined') {
                console.log("oi");
                $scope.inputError = true;
            } else if (passo === "proximo") {
                $scope.nextStep();
            } else if (passo === "anterior") {
                $scope.prevStep();
            }
        }

        $scope.checkPasso2 = function(passo) {
            if ($scope.enunciado === null || $scope.enunciado.trim() === "") {
                $scope.inputError = true;
            } else if (passo === "anterior") {
                $scope.prevStep();
            } else if (passo === "proximo") {
                $scope.alertEspelho = false;
                $scope.nextStep();
            } else {
                $scope.getCompetencias().then(() => {
                    $('#Modal').modal({backdrop: 'static', keyboard: false})  

                    $('a[href$="#Modal"]').on( "click", function() {
                        $('#Modal').modal('show');
                    });
                });
            }
        }

        $scope.resposta = { espelho: ""}

        $scope.checkPasso3 = function(tipo) {
            $scope.alertEspelho = false;
            $scope.inputError = false;
            if ($scope.conteudo === "" || $scope.conteudo === 'undefined') {
                $scope.inputError = true;
            } else if (tipo === "objetiva" && (( typeof $scope.corretas.Value1 === 'undefined' &&
                typeof $scope.corretas.Value2 === 'undefined' && typeof $scope.corretas.Value3 === 'undefined' &&
                typeof $scope.corretas.Value4 === 'undefined' && typeof $scope.corretas.Value5 === 'undefined') ||
                $scope.alternativas.alternativa1 === "" || $scope.alternativas.alternativa2 === "" ||
                $scope.alternativas.alternativa3 === "" || $scope.alternativas.alternativa4 === "" || 
                $scope.alternativas.alternativa5 === "")) {

                $scope.inputError = true;
            } else if (tipo === "subjetiva" && ($scope.resposta.espelho === "" || 
                $scope.resposta.espelho === null || $scope.resposta.espelho === 'undefined')) {
                $scope.inputError = true;
            }  else if (tipo === "objetiva") {
                $scope.sendQuestionObjective();
            } else {
                $scope.sendQuestionSubjective();
            }
            console.log($scope.inputError);

        }


    });