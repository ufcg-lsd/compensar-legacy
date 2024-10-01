/* global host, isOrContains, highlight */
angular.module('app')
    .controller('AddQuestaoController',  function($rootScope,$location,$scope,$http, $sce, AuthService, QuestoesService, $mdDialog, Notification, $route)
    {
        $rootScope.activetab = $location.path();
        $scope.trechoSelecionado = "abc";
        $scope.highlightType = true;

        $scope.setHighlightType = (type) => {$scope.highlightType = type;}

        window.onmouseup = () => {
            let parents = document.querySelectorAll("#areaSelecaoCriacaoQuestao");
            let selection = window.getSelection();
            if (selection.rangeCount > 0 && !window.getSelection().isCollapsed) {
                let range = selection.getRangeAt(0);
                for(let p of parents) {
                    if (isOrContains(p, range.commonAncestorContainer)) {
                        highlight($scope.highlightType);
                        $rootScope.avaliacao.maisInfo[p.getAttribute("comp")] = p.innerHTML;
                        return;
                    }
                }
            }
        };

        $scope.toggleAddCompetencia = function (index) {
            $("#myModal"+index).modal('toggle');
        }

        $scope.fonte = "";
        $scope.enunciado = "";
        $scope.tipo = "";
        $scope.conteudo = [];
        $rootScope.avaliacao = {
            competencias: {
                "COMP_ABSTRAÇÃO": "false",
                "COMP_ALGORITMOS": "false",
                "COMP_ANÁLISE": "false",
                "COMP_AUTOMAÇÃO": "false",
                "COMP_COLETA": "false",
                "COMP_DECOMPOSIÇÃO": "false",
                "COMP_PARALELIZAÇÃO": "false",
                "COMP_REPRESENTAÇÃO": "false",
                "COMP_SIMULAÇÃO": "false"
            },
            maisInfo: {
                "COMP_ABSTRAÇÃO": "",
                "COMP_ALGORITMOS": "",
                "COMP_ANÁLISE": "",
                "COMP_AUTOMAÇÃO": "",
                "COMP_COLETA": "",
                "COMP_DECOMPOSIÇÃO": "",
                "COMP_PARALELIZAÇÃO": "",
                "COMP_REPRESENTAÇÃO": "",
                "COMP_SIMULAÇÃO": ""
            },
            confianca: 0,
            obsAvaliacao: "",
            obsQuestao: "",
            avaliacaoPublicacao: "PRONTA"
        }

        $scope.title = '';
        $scope.changeDetected = false;
        $scope.enunciadoShow = "";


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
                infoCompetenciasAvaliacao: avaliacao.infoCompetencias,
                confiancaAvaliacao: avaliacao.confianca,
                obsAvaliacao: avaliacao.observacao
            };

            $http.post(host + 'questao', questaoSubj, AuthService.getAuthorization()).
                then(function (response) {
                    Notification.success('Questão criada com sucesso!');
                    if ($scope.$parent && $scope.$parent.newQuestion) {
                        $scope.$parent.newQuestion($scope.contId, response.data);
                    } else {
                        $route.reload();
                    }
                },function(err){
                    if (err.status == 400) {
                        $rootScope.forceSignOut();
                    } else {
                        Notification.error("Falha no envio da questão");
                    }
                }
            )

        }




        $scope.alternativas = {}
        $scope.corretas = {}

        $scope.getAvaliacao = () => {
            let arr = [];
            for(let key of Object.keys($rootScope.avaliacao.competencias)) {
                if ($rootScope.avaliacao.competencias[key] === "true") {
                    arr.push(key);
                }
            }
            let arr2 = [];
            for(let key of Object.keys($rootScope.avaliacao.competencias)) {
                if ($rootScope.avaliacao.competencias[key] === "true") {
                    arr2.push(key+"|"+$rootScope.avaliacao.maisInfo[key]);
                }
            }
            return {competencias: arr, confianca: $rootScope.avaliacao.confianca, observacao: $rootScope.avaliacao.obsAvaliacao, infoCompetencias: arr2};
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
                infoCompetenciasAvaliacao: avaliacao.infoCompetencias,
                confiancaAvaliacao: avaliacao.confianca,
                obsAvaliacao: avaliacao.observacao
            };



            $http.post(host + 'questao', questaoObj, AuthService.getAuthorization()).
                then(function (response) {
                    Notification.success('Questão criada com sucesso!');
                    if ($scope.$parent && $scope.$parent.newQuestion) {
                        $scope.$parent.newQuestion($scope.contId, response.data);
                    } else {
                        $route.reload();
                    }
                },function(err){
                    if (err.status == 400) {
                        $rootScope.forceSignOut();
                    } else {
                        Notification.error("Falha no envio da questão");
                    }
                }
            )

        }
        
        $rootScope.loading = false;
        $rootScope.competencias = [];
        $scope.getCompetencias = function () {
            $rootScope.loading = true;
            return QuestoesService.getCompetencias($scope.enunciado);
        };

        $scope.contentChanged = function () {
            $scope.changeDetected = true;
        };
    

        $().ready(function() {
            $rootScope.updateSelect('.selectpicker');
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
                // TODO: Comentei este trecho pra que funcione a criação de questões já que o classificador está off
                // $scope.getCompetencias().then((response) => {
                //     if(response.status === 200) {
                //         let tmp = $sce.trustAsHtml($scope.enunciado);
                //         $rootScope.avaliacao.maisInfo = {
                //             "COMP_ABSTRAÇÃO": tmp,
                //             "COMP_ALGORITMOS": tmp,
                //             "COMP_ANÁLISE": tmp,
                //             "COMP_AUTOMAÇÃO": tmp,
                //             "COMP_COLETA": tmp,
                //             "COMP_DECOMPOSIÇÃO": tmp,
                //             "COMP_PARALELIZAÇÃO": tmp,
                //             "COMP_REPRESENTAÇÃO": tmp,
                //             "COMP_SIMULAÇÃO": tmp
                //         };
                        $scope.nextStep();
                //     } else {
                //         document.querySelector(".ql-editor").contentEditable = true;
                //     }
                // });
            }
        }

        $scope.resposta = { espelho: ""}

        $scope.checkPasso3 = function(passo) {
            $scope.alertEspelho = false;
            $scope.inputError = false;
            if (passo === "anterior") {
                $scope.prevStep();
            } else if ($scope.conteudo === [] || typeof $scope.conteudo === 'undefined') {
                $scope.inputError = true;
            } else if ($scope.tipo === "Objetiva" && (( typeof $scope.corretas.Value1 === 'undefined' &&
                typeof $scope.corretas.Value2 === 'undefined' && typeof $scope.corretas.Value3 === 'undefined' &&
                typeof $scope.corretas.Value4 === 'undefined' && typeof $scope.corretas.Value5 === 'undefined') ||
                $scope.alternativas.alternativa1 === "" || $scope.alternativas.alternativa2 === "" ||
                $scope.alternativas.alternativa3 === "" || $scope.alternativas.alternativa4 === "" || 
                $scope.alternativas.alternativa5 === "")) {

                $scope.inputError = true;
            } else if ($scope.tipo === "Subjetiva" && ($scope.resposta.espelho === "" || 
                $scope.resposta.espelho === null || typeof $scope.resposta.espelho === 'undefined')) {
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
        $scope.addCompetencia = function() {
            $scope.enunciadoShow = $sce.trustAsHtml($scope.enunciado);
            /*
            $('#addCompetencia').modal('toggle');
            $('#addCompetencia').modal({backdrop: 'static', keyboard: false});
            
            $('a[href$="#addCompetencia"]').on( "click", function() {
                $('#addCompetencia').modal('show');
            });*/
        }
    });