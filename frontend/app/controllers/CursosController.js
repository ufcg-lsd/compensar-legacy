/* global host */
angular.module('app')
    .controller('CursosController',  function($scope, $rootScope, $location,$http, $sce, AuthService,Notification)
    {
        $rootScope.activetab = $location.path();
        $().ready(function() {
            $('.carousel').carousel();
        });

        $scope.compList = ["COMP_ABSTRAÇÃO", "COMP_ALGORITMOS", "COMP_ANÁLISE", "COMP_AUTOMAÇÃO", "COMP_COLETA", "COMP_DECOMPOSIÇÃO", "COMP_PARALELIZAÇÃO", "COMP_REPRESENTAÇÃO", "COMP_SIMULAÇÃO"];

        $scope.cursoAvaliacao = null;

        $scope.idCurso = -1;
        $scope.idModulo = -1;
        $scope.estado = "";
        $scope.idAvaliacaoFinal = 0;

        $scope.respostaQuestoes = [false, false];

        $scope.progresso = {
            modulo: -1,
            estado: ""
        }

        $scope.populaDadosAvaliacao = (dados, permitirFinalizado) => {
            $scope.cursoAvaliacao = dados;
            let lastFinalizado = -1;
            $scope.progresso.modulo = -1;
            $scope.progresso.estado = "DESCRICAO";
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
                    $scope.progresso.modulo = i;
                    $scope.progresso.estado = estado;
                } else if (lastFinalizado === -1) {lastFinalizado = i;}
            }
            if ($scope.progresso.estado == "DESCRICAO" && permitirFinalizado === true) {
                $scope.idModulo = lastFinalizado;
                $scope.estado = "FINALIZADO";
            } else {
                $scope.idModulo = $scope.progresso.modulo;
                $scope.estado = $scope.progresso.estado;
            }
            if ($scope.progresso.modulo === 9) {
                $scope.respostaQuestoes = [false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false];
            } else {
                $scope.respostaQuestoes = [false, false];
            }
        }

        $scope.nextStepAvaliacao = () => {
            if ($scope.idModulo === $scope.progresso.modulo && $scope.estado === $scope.progresso.estado) {
                $rootScope.loading = true;
                $http.post(host + 'cursoAvaliacao/', $scope.respostaQuestoes, AuthService.getAuthorization()).then(
                    response => {
                        $rootScope.loading = false;
                        if (response.data.mensagem) {
                            Notification.info(response.data.mensagem);
                            $scope.populaDadosAvaliacao(response.data.cursoAvaliacao, false);
                        } else {
                            $scope.populaDadosAvaliacao(response.data.cursoAvaliacao, true);
                        }
                    }, err => {
                        $location.path("/cursos");
                        Notification.error("Falha ao verificar cursos");
                    });
            } else if ($scope.estado == "DESCRICAO") {
                $scope.estado = "EXEMPLOS";
            } else if ($scope.estado == "EXEMPLOS") {
                if ($scope.idModulo === $scope.progresso.modulo || ($scope.idModulo === 8 && $scope.progresso.modulo === 9)) {
                    $scope.estado = "PRATICA";
                } else {
                    $scope.estado = "DESCRICAO";
                }
                if ($scope.idModulo !== $scope.progresso.modulo) {
                    if ($scope.idModulo === 8 && $scope.progresso.modulo === -1) {
                        $scope.idModulo = -1;
                    } else {
                        $scope.idModulo++;
                    }
                }
            } else if ($scope.estado == "FINALIZADO") {
                if ($scope.idModulo === 8) {
                    $scope.estado = "PRATICA";
                } else {
                    $scope.estado = "DESCRICAO";
                }
                if ($scope.idModulo !== $scope.progresso.modulo) {
                    if ($scope.idModulo === 9) {
                        $scope.idModulo = -1;
                    } else {
                        $scope.idModulo++;
                    }
                }
            }
        }

        $scope.prevStepAvaliacao = () => {
            if ($scope.estado == "EXEMPLOS") {
                $scope.estado = "DESCRICAO";
            } else if ($scope.estado == "PRATICA") {
                $scope.estado = "EXEMPLOS";
            }
        }

        $scope.goToCursoAvaliacao = () => {
            $scope.idCurso = 0;
        }

        $scope.goToCursoCriacao = () => {
            $scope.idCurso = 1;
        }

        $scope.goToMain = () => {
            $scope.idCurso = -1;
        }

        $scope.goToModulo = (id) => {
            if (id > $scope.progresso.modulo && $scope.progresso.modulo !== -1) {
                Notification.error("Você precisa completar os módulos anteriores a este primeiro!");
                return;
            }
            $scope.idModulo = id;
            if (id === $scope.progresso.modulo) {
                $scope.estado = $scope.progresso.estado;
            } else if (id === 9) {
                $scope.estado = "FINALIZADO";
            } else {
                $scope.estado = "DESCRICAO";
            }
        }

        $scope.getCursoAvaliacao = () => {
            $http.get(host + 'cursoAvaliacao/new', AuthService.getAuthorization()).then(
            response => {
                $scope.populaDadosAvaliacao(response.data.cursoAvaliacao, false);
            }, err => {
                $location.path("/cursos");
                Notification.error("Falha ao verificar cursos");
            });
        }

        let allEstadoEquals = (arr, val) => {
            for (let elem of arr) {
                if (val !== elem.estado) return false;
            }
            return true;
        }

        $scope.getIcon = (modulos) => {
            if (allEstadoEquals(modulos, "INATIVO")) return "fa-times-circle text-danger";
            if (allEstadoEquals(modulos, "FINALIZADO")) return "fa-check-circle text-success";
            return "fa-angle-right text-primary";
        }

        $scope.getIconCriacao = () => {
            //return "fa-angle-right text-primary";
            return "fa-times-circle text-danger";
            //return "fa-check-circle text-success";
        }

        $scope.getCorrespModule = (idModulo, key) => {
            if (idModulo === -1) {
                return "";
            } else if (idModulo === 9) {
                return $rootScope.repaginaComp($scope.compList[parseInt(key/2)]);
            } else {
                return $rootScope.repaginaComp($scope.cursoAvaliacao[idModulo].nome);
            }
        }

        $scope.getCursoAvaliacao();

    });