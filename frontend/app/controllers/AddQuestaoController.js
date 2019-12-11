/* global host */
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

        $scope.competenciasAutor = {
            "COMP_ABSTRAÇÃO": "false",
            "COMP_ALGORITMOS": "false",
            "COMP_ANÁLISE": "false",
            "COMP_AUTOMAÇÃO": "false",
            "COMP_COLETA": "false",
            "COMP_DECOMPOSIÇÃO": "false",
            "COMP_PARALELIZAÇÃO": "false",
            "COMP_REPRESENTAÇÃO": "false",
            "COMP_SIMULAÇÃO": "false"
        };

        $scope.fonte = "";
        $scope.enunciado = "";
        $scope.tipo = "";
        $scope.conteudo = "";
        $rootScope.avaliacao.confianca = 0;
        $rootScope.avaliacao.obsAvaliacao = "";

        $scope.title = '';
        $scope.changeDetected = false;


        $scope.isObjective = function(){ 
            return $scope.tipo === "Objetiva"
        };


        $scope.sendQuestionSubjective = function (avaliacao) {
            let questaoSubj = {
                conteudo: $scope.conteudo,
                enunciado: $scope.enunciado,
                espelho:  $scope.resposta.espelho,
                fonte: $scope.fonte,
                competencias: $rootScope.competencias,
                tipo: $scope.tipo,
                competenciasAvaliacao: avaliacao.competencias,
                confiancaAvaliacao: avaliacao.confianca,
                obsAvaliacao: avaliacao.observacao
            };

            $http.post(host + 'questao', questaoSubj, AuthService.getAuthorization()).
                then(function (response) {
                    Notification.success('Questão criada com sucesso!');
                    $location.path("/buscas");
                },function(err){
                    if (err.status == 400) {
                        signOut();
                        Notification.warning("Seu login expirou, por favor faça login novamente!");
                    } else {
                        Notification.error("Falha no envio da questão");
                        $location.path("/buscas");
                    }
                }
            )

        }




        $scope.alternativas = {}
        $scope.corretas = {}

        $scope.getAvaliacao = function() {
            let arr = [];
            for(let key of Object.keys($scope.competenciasAutor)) {
                if ($scope.competenciasAutor[key] === "true") {
                    arr.push(key);
                }
            }
            return {competencias: arr, confianca: $rootScope.avaliacao.confianca, observacao: $rootScope.avaliacao.obsAvaliacao};
        }


        $scope.sendQuestionObjective = function (avaliacao) {

            let questaoObj = {
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
                conteudo: $scope.conteudo,
                enunciado: $scope.enunciado,
                fonte: $scope.fonte,
                competencias: $rootScope.competencias,
                tipo: $scope.tipo,
                competenciasAvaliacao: avaliacao.competencias,
                confiancaAvaliacao: avaliacao.confianca,
                obsAvaliacao: avaliacao.observacao
            };



            $http.post(host + 'questao', questaoObj, AuthService.getAuthorization()).
                then(function (response) {
                    Notification.success('Questão criada com sucesso!');
                    $location.path("/buscas");
                },function(err){
                    if (err.status == 400) {
                        signOut();
                        Notification.warning("Seu login expirou, por favor faça login novamente!");
                    } else {
                        Notification.error("Falha no envio da questão");
                        $location.path("/buscas");
                    }
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

        $scope.contentChanged = function () {
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
                document.querySelector(".ql-editor").contentEditable = true;
            } else {
                $scope.alertEspelho = false;
                document.querySelector(".ql-editor").contentEditable = false;
                $scope.getCompetencias().then(() => {
                    $scope.nextStep();
                });
            }
        }

        $scope.resposta = { espelho: ""}

        $scope.checkPasso3 = function(passo) {
            $scope.alertEspelho = false;
            $scope.inputError = false;
            if (passo === "anterior") {
                $scope.prevStep();
            } else if ($scope.conteudo === "" || $scope.conteudo === 'undefined') {
                $scope.inputError = true;
            } else if ($scope.tipo === "Objetiva" && (( typeof $scope.corretas.Value1 === 'undefined' &&
                typeof $scope.corretas.Value2 === 'undefined' && typeof $scope.corretas.Value3 === 'undefined' &&
                typeof $scope.corretas.Value4 === 'undefined' && typeof $scope.corretas.Value5 === 'undefined') ||
                $scope.alternativas.alternativa1 === "" || $scope.alternativas.alternativa2 === "" ||
                $scope.alternativas.alternativa3 === "" || $scope.alternativas.alternativa4 === "" || 
                $scope.alternativas.alternativa5 === "")) {

                $scope.inputError = true;
            } else if ($scope.tipo === "Subjetiva" && ($scope.resposta.espelho === "" || 
                $scope.resposta.espelho === null || $scope.resposta.espelho === 'undefined')) {
                $scope.inputError = true;
            } else {
                $scope.nextStep();
                //$scope.sendQuestionSubjective();
            }
            console.log($scope.inputError);
        }

        $scope.checkPasso4 = function() {
            $scope.inputError = false;
            if ($scope.tipo === "Objetiva") {
                $scope.sendQuestionObjective($scope.getAvaliacao());
            } else {
                $scope.sendQuestionSubjective($scope.getAvaliacao());
            }
        }
    });