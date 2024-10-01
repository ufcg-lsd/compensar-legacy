/* global host */
angular.module('app')
    .controller('CursosController',  function($scope, $rootScope, $location,$http, $sce, AuthService,Notification)
    {
        $rootScope.activetab = $location.path();
        $().ready(function() {
            $('.carousel').carousel({interval: false});
        });
        $scope.erro = {tituloErro: "", descricaoErro: ""};

        $scope.compList = ["COMP_ABSTRAÇÃO", "COMP_ALGORITMOS", "COMP_ANÁLISE", "COMP_AUTOMAÇÃO", "COMP_COLETA", "COMP_DECOMPOSIÇÃO", "COMP_PARALELIZAÇÃO", "COMP_REPRESENTAÇÃO", "COMP_SIMULAÇÃO"];

        $scope.cursoAvaliacao = null;
        $scope.cursoCriacao = null;

        $scope.progressoAvaliacao = {
            modulo: -1,
            estado: "",
            respostas: [false, false]
        };

        $scope.progressoCriacao = {
            modulo: -1,
            estado: "",
            respostas: [""]
        };

        $scope.estadoAtual = {
            curso: -1,
            modulo: -1,
            estado: "",
            respostasAnteriores: true
        };

        $scope.comparaCompetencias = (comp1, comp2) => {
            if (comp1.length !== comp2.length) return false;
            for(let x of comp1) {
                if (comp2.indexOf(x) === -1) return false;
            }
            return true;
        }

        $scope.insideCurso;

        $scope.goToCursoAvaliacao = () => {
            $scope.estadoAtual.curso = 0;
            if ($scope.progressoAvaliacao.modulo != -1) {
                $scope.estadoAtual.modulo = $scope.progressoAvaliacao.modulo;
                $scope.estadoAtual.estado = $scope.progressoAvaliacao.estado;
            } else {
                $scope.estadoAtual.modulo = -1;
            }
            $scope.updateTreeView();
        }

        $scope.goToCursoCriacao = () => {
            $scope.estadoAtual.curso = 1;
            $scope.estadoAtual.modulo = -1;
            if ($scope.progressoCriacao.modulo != -1) {
                $scope.estadoAtual.modulo = $scope.progressoCriacao.modulo;
                $scope.estadoAtual.estado = $scope.progressoCriacao.estado;
            } else {
                $scope.estadoAtual.modulo = -1;
            }
            $scope.updateTreeView();
        }

        $scope.goToMain = () => {
            $scope.estadoAtual.curso = -1;
        }

        $scope.goToModulo = (id) => {
            let progresso = ($scope.estadoAtual.curso === 0) ? $scope.progressoAvaliacao : $scope.progressoCriacao;
            if (id > progresso.modulo && progresso.modulo !== -1) {
                Notification.error("Você precisa completar os módulos anteriores a este primeiro!");
                return;
            }
            $scope.estadoAtual.modulo = id;
            if (id === progresso.modulo) {
                $scope.estadoAtual.estado = progresso.estado;
            } else if (id === 10) {
                $scope.estadoAtual.estado = "FINALIZADO";
            } else {
                $scope.estadoAtual.estado = "DESCRICAO";
            }
            $scope.updateTreeView();
        }

        let allEstadoEquals = (arr, val) => {
            for (let elem of arr) {
                if (val !== elem.estado) return false;
            }
            return true;
        }

        $scope.getIcon = (modulos) => {
            if (!modulos || allEstadoEquals(modulos, "INATIVO")) return "fa-times-circle text-danger";
            if (allEstadoEquals(modulos, "FINALIZADO")) return "fa-check-circle text-success";
            return "fa-angle-right text-primary";
        }

        $scope.getIconCriacao = () => {
            //return "fa-angle-right text-primary";
            return "fa-times-circle text-danger";
            //return "fa-check-circle text-success";
        }

        pauseVideo = () => {
            let iframes = document.getElementsByTagName("iframe");
            for(let iframe of iframes) {
                iframe.contentWindow.postMessage('{"event":"command","func":"' + 'pauseVideo' +   '","args":""}', '*');
            }
        }

        $scope.getTreeView = () => {
            let treeView = []
            let progressoAtual = ($scope.estadoAtual.curso === 0) ? $scope.progressoAvaliacao : $scope.progressoCriacao;
            let cursoAtual = ($scope.estadoAtual.curso === 0) ? $scope.cursoAvaliacao : $scope.cursoCriacao;
            let acabou = progressoAtual.modulo === -1;
            treeView.push({text: "Introdução", state: {selected: $scope.estadoAtual.modulo === 0}});
            treeView.push({
                text: "Competências",
                nodes: [],
                state: {
                    selected: $scope.estadoAtual.modulo >= 1 && $scope.estadoAtual.modulo <= 9,
                    disabled: progressoAtual.modulo === 0
                }
            });
            for(let i = 1; i < cursoAtual.length-1; i++) {
                let tmpNode = {
                    text: $rootScope.repaginaComp(cursoAtual[i].nome),
                    state: {
                        selected: $scope.estadoAtual.modulo === i,
                        disabled: !acabou && progressoAtual.modulo < i
                    }
                };
                treeView[1].nodes.push(tmpNode);
            }
            treeView.push({text: "Avaliação final", state: { selected: $scope.estadoAtual.modulo === 10, disabled: !acabou && progressoAtual.modulo < 10}});
            return treeView;
        }

        $scope.updateTreeView = () => {
            $('#treeview1').treeview({
                data: $scope.getTreeView(),
                multiSelect: true,
                expandIcon: 'fas fa-angle-right',
                collapseIcon: 'fas fa-angle-down',
            });
            $('#treeview1').on('nodeSelected', function(event, data) {
                event.stopPropagation();
                console.log(data);
                for(let node of $('#treeview1').treeview('getSelected')) {
                    if (node.nodeId !== data.nodeId) {
                        $('#treeview1').treeview('unselectNode', [ node.nodeId, { silent: true } ]);
                    }
                }
                if (data.nodeId === 1) {
                    $('#treeview1').treeview('selectNode', [ 2, { silent: true } ]);
                } else if (data.nodeId >= 2 && data.nodeId <= 10) {
                    $('#treeview1').treeview('selectNode', [ 1, { silent: true } ]);
                }
                if (data.nodeId <= 1) {
                    $scope.goToModulo(data.nodeId);
                } else {
                    $scope.goToModulo(data.nodeId-1);
                }
            });
            $('#treeview1').on('nodeUnselected', function(event, data) {
                event.stopPropagation();
                $('#treeview1').treeview('selectNode', [ data.nodeId, {} ]);
            });
        }

        

        $scope.populaDadosAvaliacao = (dados, permitirFinalizado, semCursoDefinido) => {
            $scope.cursoAvaliacao = dados;
            let lastFinalizado = -1;
            $scope.progressoAvaliacao.modulo = -1;
            $scope.progressoAvaliacao.estado = "DESCRICAO";
            for(let i = $scope.cursoAvaliacao.length-1; i >= 0; i--) {
                $scope.cursoAvaliacao[i].video = $sce.trustAsResourceUrl($scope.cursoAvaliacao[i].video);
                $scope.cursoAvaliacao[i].descricao = $sce.trustAsHtml($scope.cursoAvaliacao[i].descricao);
                let estado = $scope.cursoAvaliacao[i].estado;
                let exemplos = $scope.cursoAvaliacao[i].exemplos || [];
                let textoExemplos = $scope.cursoAvaliacao[i].textoExemplos || [];
                for(let j = 0; j < exemplos.length; j ++) {
                    exemplos[j] = $sce.trustAsResourceUrl(exemplos[j]);
                    textoExemplos[j] = $sce.trustAsHtml(textoExemplos[j]);
                }
                $scope.cursoAvaliacao[i].exemplos = exemplos;
                $scope.cursoAvaliacao[i].textoExemplos = textoExemplos;
                if (estado !== "FINALIZADO") {
                    $scope.progressoAvaliacao.modulo = i;
                    $scope.progressoAvaliacao.estado = estado;
                } else if (lastFinalizado === -1) {lastFinalizado = i;}
            }
            if (($scope.progressoAvaliacao.estado == "DESCRICAO" || $scope.progressoAvaliacao.modulo == 10) && permitirFinalizado === true) {
                $scope.estadoAtual.modulo = lastFinalizado;
                $scope.estadoAtual.estado = "FINALIZADO";
            } else {
                $scope.estadoAtual.modulo = $scope.progressoAvaliacao.modulo;
                $scope.estadoAtual.estado = $scope.progressoAvaliacao.estado;
            }
            if ($scope.progressoAvaliacao.modulo === 10) {
                $scope.progressoAvaliacao.respostas = [false, false, false, false, false, false, false, false, false];
            } else {
                $scope.progressoAvaliacao.respostas = [false, false];
            }
            if (!semCursoDefinido) {
                $scope.updateTreeView();
            }
            
        }

        $scope.nextStepAvaliacao = () => {
            if ($scope.estadoAtual.modulo === $scope.progressoAvaliacao.modulo && $scope.estadoAtual.estado === $scope.progressoAvaliacao.estado) {
                $rootScope.loading = true;
                $http.post(host + 'cursoAvaliacao', $scope.progressoAvaliacao.respostas, AuthService.getAuthorization()).then(
                    response => {
                        $rootScope.loading = false;
                        if (response.data.mensagem) {
                            $scope.erro = {tituloErro: "Erro ao prosseguir no curso", descricaoErro: response.data.mensagem};
                            $('#error-modal').modal('toggle');
                            $('#error-modal').modal({backdrop: 'static', keyboard: false});
                            $scope.populaDadosAvaliacao(response.data.cursoAvaliacao, false);
                        } else {
                            $scope.populaDadosAvaliacao(response.data.cursoAvaliacao, true);
                        }
                    }, err => {
                        $location.path("/cursos");
                        Notification.error("Falha ao verificar cursos");
                    });
                    return;
            }
            if ($scope.estadoAtual.estado == "DESCRICAO") {
                if  ($scope.estadoAtual.modulo === 0) {
                    $scope.estadoAtual.modulo++;
                    $scope.estadoAtual.estado = "DESCRICAO";
                } else {
                    $scope.estadoAtual.estado = "EXEMPLOS";
                }
            } else if ($scope.estadoAtual.estado == "EXEMPLOS") {
                if ($scope.estadoAtual.modulo === $scope.progressoAvaliacao.modulo || ($scope.estadoAtual.modulo === 9 && $scope.progressoAvaliacao.modulo === 10)) {
                    $scope.estadoAtual.estado = "PRATICA";
                } else {
                    $scope.estadoAtual.estado = "DESCRICAO";
                }
                if ($scope.estadoAtual.modulo !== $scope.progressoAvaliacao.modulo) {
                    if ($scope.estadoAtual.modulo === 9 && $scope.progressoAvaliacao.modulo === -1) {
                        $scope.estadoAtual.modulo = -1;
                    } else {
                        $scope.estadoAtual.modulo++;
                    }
                }
            } else if ($scope.estadoAtual.estado == "FINALIZADO") {
                if ($scope.estadoAtual.modulo === 9) {
                    $scope.estadoAtual.estado = "PRATICA";
                } else {
                    $scope.estadoAtual.estado = "DESCRICAO";
                }
                if ($scope.estadoAtual.modulo !== $scope.progressoAvaliacao.modulo) {
                    if ($scope.estadoAtual.modulo === 10) {
                        $scope.estadoAtual.modulo = -1;
                    } else {
                        $scope.estadoAtual.modulo++;
                    }
                }
            }
            $scope.updateTreeView();
        }

        $scope.prevStep = () => {
            if ($scope.estadoAtual.estado == "EXEMPLOS") {
                $scope.estadoAtual.estado = "DESCRICAO";
            } else if ($scope.estadoAtual.estado == "PRATICA") {
                $scope.estadoAtual.estado = "EXEMPLOS";
            }
        }

        $scope.populaDadosCriacao = (dados, permitirFinalizado, semCursoDefinido) => {
            $scope.cursoCriacao = dados;
            let lastFinalizado = -1;
            $scope.progressoCriacao.modulo = -1;
            $scope.progressoCriacao.estado = "DESCRICAO";
            for(let i = $scope.cursoCriacao.length-1; i >= 0; i--) {
                $scope.cursoCriacao[i].video = $sce.trustAsResourceUrl($scope.cursoCriacao[i].video);
                $scope.cursoCriacao[i].descricao = $sce.trustAsHtml($scope.cursoCriacao[i].descricao);
                let estado = $scope.cursoCriacao[i].estado;
                let exemplos = $scope.cursoCriacao[i].exemplos || [];
                let textoExemplos = $scope.cursoCriacao[i].textoExemplos || [];
                for(let j = 0; j < exemplos.length; j ++) {
                    exemplos[j] = $sce.trustAsResourceUrl(exemplos[j]);
                    textoExemplos[j] = $sce.trustAsHtml(textoExemplos[j]);
                }
                $scope.cursoCriacao[i].exemplos = exemplos;
                $scope.cursoCriacao[i].textoExemplos = textoExemplos;
                if (estado !== "FINALIZADO") {
                    $scope.progressoCriacao.modulo = i;
                    $scope.progressoCriacao.estado = estado;
                } else if (lastFinalizado === -1) {lastFinalizado = i;}
            }
            if ($scope.progressoCriacao.estado == "DESCRICAO" && permitirFinalizado === true) {
                $scope.estadoAtual.modulo = lastFinalizado;
                $scope.estadoAtual.estado = "FINALIZADO";
            } else {
                $scope.estadoAtual.modulo = $scope.progressoCriacao.modulo;
                $scope.estadoAtual.estado = $scope.progressoCriacao.estado;
            }
            if ($scope.progressoCriacao.modulo === 10) {
                $scope.progressoCriacao.respostas = ["", "", "", "", "", "", "", "", ""]; //id das questões
            } else {
                $scope.progressoCriacao.respostas = [""]; //enunciado
            }
            if (!semCursoDefinido) {
                $scope.updateTreeView();
            }
            $rootScope.Questoes = $scope.cursoCriacao[10].questoesDetalhadas;
        }

        $scope.nextStepCriacao = () => {
            if ($scope.estadoAtual.modulo === $scope.progressoCriacao.modulo && $scope.estadoAtual.estado === $scope.progressoCriacao.estado) {
                $rootScope.loading = true;
                $http.post(host + 'cursoCriacao', $scope.progressoCriacao.respostas, AuthService.getAuthorization()).then(
                    response => {
                        $rootScope.loading = false;
                        if (response.data.mensagem) {
                            $scope.erro = {tituloErro: "Erro ao prosseguir no curso", descricaoErro: response.data.mensagem};
                            $('#error-modal').modal('toggle');
                            $('#error-modal').modal({backdrop: 'static', keyboard: false});
                            $scope.populaDadosCriacao(response.data.cursoCriacao, false);
                        } else {
                            $scope.populaDadosCriacao(response.data.cursoCriacao, true);
                        }
                    }, err => {
                        $location.path("/cursos");
                        Notification.error("Falha ao verificar cursos");
                    });
            } else if ($scope.estadoAtual.estado == "DESCRICAO") {
                $scope.estadoAtual.estado = "EXEMPLOS";
            } else if ($scope.estadoAtual.estado == "EXEMPLOS") {
                if ($scope.estadoAtual.modulo === $scope.progressoCriacao.modulo || ($scope.estadoAtual.modulo === 9 && $scope.progressoCriacao.modulo === 10)) {
                    $scope.estadoAtual.estado = "PRATICA";
                } else {
                    $scope.estadoAtual.estado = "DESCRICAO";
                }
                if ($scope.estadoAtual.modulo !== $scope.progressoCriacao.modulo) {
                    if ($scope.estadoAtual.modulo === 9 && $scope.progressoCriacao.modulo === -1) {
                        $scope.estadoAtual.modulo = -1;
                    } else {
                        $scope.estadoAtual.modulo++;
                    }
                }
            } else if ($scope.estadoAtual.estado == "FINALIZADO") {
                if ($scope.estadoAtual.modulo === 9) {
                    $scope.estadoAtual.estado = "PRATICA";
                } else {
                    $scope.estadoAtual.estado = "DESCRICAO";
                }
                if ($scope.estadoAtual.modulo !== $scope.progressoCriacao.modulo) {
                    if ($scope.estadoAtual.modulo === 10) {
                        $scope.estadoAtual.modulo = -1;
                    } else {
                        $scope.estadoAtual.modulo++;
                    }
                }
            }
            $scope.updateTreeView();
        }

        $scope.getAvaliacaoCriacao = () => {
            if ($rootScope.Questoes.length === 0) return 'SEM_RESPOSTA';
            if ($rootScope.Questoes[0].estado === 'RASCUNHO') return 'RASCUNHO';
            if ($rootScope.Questoes[0].estado === 'PEND_AVALIACAO' || $rootScope.Questoes[0].estado === 'PEND_APROVACAO') return 'PENDENTE';
            if ($rootScope.Questoes[0].estado === 'REJEITADA' || !$scope.comparaCompetencias($rootScope.Questoes[0].competencias, $rootScope.Questoes[0].competenciasAvaliacoes[1])) {
                return 'REJEITADA';
            }
            return 'ACEITA';
        }


        $scope.podeCriarResposta = () => {
            let aceitaveis = ['SEM_RESPOSTA', 'RASCUNHO', 'REJEITADA'];
            return aceitaveis.indexOf($scope.getAvaliacaoCriacao()) !== -1;
        }

        $scope.podeProsseguir = () => {
            return $scope.getAvaliacaoCriacao() === 'ACEITA';
        }

        $scope.getCursos = () => {
            $http.get(host + 'cursos', AuthService.getAuthorization()).then(
            response => {
                $scope.populaDadosAvaliacao(response.data.cursoAvaliacao, false, true);
                $scope.populaDadosCriacao(response.data.cursoCriacao, false, true);
            }, err => {
                $location.path("/cursos");
                Notification.error("Falha ao verificar cursos");
            });
        }

        $scope.newQuestion = (modulo, questao) => {
            console.log(modulo);
            console.log(questao);
            $http.post(host + 'cursoCriacao/newQuestion', questao.id, AuthService.getAuthorization()).then(
                response => {
                    $rootScope.loading = false;
                    let tempCurso = $scope.estadoAtual.curso;
                    let tempModulo = $scope.estadoAtual.modulo;
                    if (response.data.mensagem) {
                        $scope.erro = {tituloErro: "Erro ao cadastrar nova questão", descricaoErro: response.data.mensagem};
                        $('#error-modal').modal('toggle');
                        $('#error-modal').modal({backdrop: 'static', keyboard: false});
                        $scope.populaDadosCriacao(response.data.cursoCriacao, false);
                    } else {
                        $scope.populaDadosCriacao(response.data.cursoCriacao, true);
                    }
                    $scope.estadoAtual.curso = tempCurso;
                    $scope.estadoAtual.modulo = tempModulo;
                }, err => {
                    $location.path("/cursos");
                    Notification.error("Falha ao verificar cursos");
                });
            
        }

        $scope.getCompName = (i) => {
            if ($scope.estadoAtual.modulo !== 10) return $scope.compList
            return $scope.compList[i];
        }

        $scope.getCursos();

    });