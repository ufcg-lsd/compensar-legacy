angular.module('app')
    .controller('AddQuestaoController',  function($rootScope,$location,$scope,$http,UserService, QuestoesService, $mdDialog)
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

        $scope.resposta = {}

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

            $http.post('https://compensar.herokuapp.com/api/questao', questaoSubj).
                then(function (response) {
                    if (response.status == 200) {
                        $scope.showAlertaCriacao();
                        $location.path("/buscas");
                    }
                    else {
                        window.alert("Falha no envio da Questão");
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



            $http.post('https://compensar.herokuapp.com/api/questao', questaoObj).
                then(function (response) {
                    if (response.status == 200) {
                        $scope.showAlertaCriacao();
                        $location.path("/buscas");
                    }
                    else {
                        window.alert("Falha no envio da Questão");
                        $location.path("/buscas");
                    }
                },function(){
                    $location.path("/buscas");
                }
            )

        }

        $scope.showAlertaCriacao = function() {
            $mdDialog.show(
              $mdDialog.alert()
                .parent(angular.element(document.querySelector('#popupContainer')))
                .clickOutsideToClose(true)
                .title('Questão criada com sucesso!')
                .textContent('Você pode consultá-la na aba "Buscar Questões".')
                .ariaLabel('Alert Dialog Demo')
                .ok('Entendi!')
            );
        };
        
        $rootScope.loading = false;
        $rootScope.competencias = "";
        $scope.getCompetencias = function () {
            $rootScope.loading = true;
            QuestoesService.getCompetencias($scope.enunciado);
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
    
        $scope.espelho = "nao";
        $scope.setEspelho = function () {
            console.log($scope.espelho);
            if ($scope.espelho === "sim") $scope.espelho = "nao";
            else $scope.espelho = "sim";
            console.log($scope.espelho);
        }

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
            if ($scope.enunciado === "" || $scope.enunciado === null  ) {
                $scope.inputError = true;
            } else if (passo === "anterior") {
                $scope.prevStep();
            } else if (passo === "proximo") {
                $scope.nextStep();
            } else {
                $scope.getCompetencias();
                    $('#Modal').modal({backdrop: 'static', keyboard: false})  

                    $('a[href$="#Modal"]').on( "click", function() {
                        $('#Modal').modal('show');
                    });  
                        
            }
        }


        $scope.checkPasso3 = function(tipo) {
            if ($scope.conteudo === "" || $scope.conteudo === 'undefined') {
                $scope.inputError = true;
            } else if (tipo === "objetiva") {
                $scope.sendQuestionObjective();
            } else if (tipo === "subjetiva") {
                $scope.sendQuestionSubjective();
            }
        }



    });