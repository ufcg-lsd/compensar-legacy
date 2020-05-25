/* global host */
angular.module('app')
    .controller('CursosController',  function($scope, $rootScope, $location,$http, $sce, AuthService,Notification)
    {
        $rootScope.activetab = $location.path();
        $().ready(function() {
            $('.carousel').carousel({interval: false});
        });

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
        }

        $scope.idCurso = -1;
        $scope.idModulo = -1;
        $scope.estado = "";

        

        $scope.goToCursoAvaliacao = () => {
            $scope.idCurso = 0;
            $scope.idModulo = -1;
        }

        $scope.goToCursoCriacao = () => {
            $scope.idCurso = 1;
            $scope.idModulo = -1;
        }

        $scope.goToMain = () => {
            $scope.idCurso = -1;
        }

        $scope.goToModulo = (id) => {
            let progresso = ($scope.idCurso === 0) ? $scope.progressoAvaliacao : $scope.progressoCriacao;
            if (id > progresso.modulo && progresso.modulo !== -1) {
                Notification.error("Você precisa completar os módulos anteriores a este primeiro!");
                return;
            }
            $scope.idModulo = id;
            if (id === progresso.modulo) {
                $scope.estado = progresso.estado;
            } else if (id === 9) {
                $scope.estado = "FINALIZADO";
            } else {
                $scope.estado = "DESCRICAO";
            }
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

        $scope.populaDadosAvaliacao = (dados, permitirFinalizado) => {
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
            if ($scope.progressoAvaliacao.estado == "DESCRICAO" && permitirFinalizado === true) {
                $scope.idModulo = lastFinalizado;
                $scope.estado = "FINALIZADO";
            } else {
                $scope.idModulo = $scope.progressoAvaliacao.modulo;
                $scope.estado = $scope.progressoAvaliacao.estado;
            }
            if ($scope.progressoAvaliacao.modulo === 9) {
                $scope.progressoAvaliacao.respostas = [false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false];
            } else {
                $scope.progressoAvaliacao.respostas = [false, false];
            }
        }

        $scope.nextStepAvaliacao = () => {
            if ($scope.idModulo === $scope.progressoAvaliacao.modulo && $scope.estado === $scope.progressoAvaliacao.estado) {
                $rootScope.loading = true;
                $http.post(host + 'cursoAvaliacao/', $scope.progressoAvaliacao.respostas, AuthService.getAuthorization()).then(
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
                if ($scope.idModulo === $scope.progressoAvaliacao.modulo || ($scope.idModulo === 8 && $scope.progressoAvaliacao.modulo === 9)) {
                    $scope.estado = "PRATICA";
                } else {
                    $scope.estado = "DESCRICAO";
                }
                if ($scope.idModulo !== $scope.progressoAvaliacao.modulo) {
                    if ($scope.idModulo === 8 && $scope.progressoAvaliacao.modulo === -1) {
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
                if ($scope.idModulo !== $scope.progressoAvaliacao.modulo) {
                    if ($scope.idModulo === 9) {
                        $scope.idModulo = -1;
                    } else {
                        $scope.idModulo++;
                    }
                }
            }
        }

        $scope.prevStep = () => {
            if ($scope.estado == "EXEMPLOS") {
                $scope.estado = "DESCRICAO";
            } else if ($scope.estado == "PRATICA") {
                $scope.estado = "EXEMPLOS";
            }
        }

        $scope.populaDadosCriacao = (dados, permitirFinalizado) => {
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
                $scope.idModulo = lastFinalizado;
                $scope.estado = "FINALIZADO";
            } else {
                $scope.idModulo = $scope.progressoCriacao.modulo;
                $scope.estado = $scope.progressoCriacao.estado;
            }
            if ($scope.progressoCriacao.modulo === 9) {
                $scope.progressoCriacao.respostas = ["", "", "", "", "", "", "", "", ""]; //id das questões
            } else {
                $scope.progressoCriacao.respostas = [""]; //enunciado
            }
        }

        $scope.nextStepCriacao = () => {
            if ($scope.idModulo === $scope.progressoCriacao.modulo && $scope.estado === $scope.progressoCriacao.estado) {
                $rootScope.loading = true;
                $http.post(host + 'cursoCriacao/', $scope.progressoCriacao.respostas, AuthService.getAuthorization()).then(
                    response => {
                        $rootScope.loading = false;
                        if (response.data.mensagem) {
                            Notification.info(response.data.mensagem);
                            $scope.populaDadosCriacao(response.data.cursoCriacao, false);
                        } else {
                            $scope.populaDadosCriacao(response.data.cursoCriacao, true);
                        }
                    }, err => {
                        $location.path("/cursos");
                        Notification.error("Falha ao verificar cursos");
                    });
            } else if ($scope.estado == "DESCRICAO") {
                $scope.estado = "EXEMPLOS";
            } else if ($scope.estado == "EXEMPLOS") {
                if ($scope.idModulo === $scope.progressoCriacao.modulo || ($scope.idModulo === 8 && $scope.progressoCriacao.modulo === 9)) {
                    $scope.estado = "PRATICA";
                } else {
                    $scope.estado = "DESCRICAO";
                }
                if ($scope.idModulo !== $scope.progressoCriacao.modulo) {
                    if ($scope.idModulo === 8 && $scope.progressoCriacao.modulo === -1) {
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
                if ($scope.idModulo !== $scope.progressoCriacao.modulo) {
                    if ($scope.idModulo === 9) {
                        $scope.idModulo = -1;
                    } else {
                        $scope.idModulo++;
                    }
                }
            }
        }

        $scope.getCursos = () => {
            $http.get(host + 'cursos/', AuthService.getAuthorization()).then(
            response => {
                $scope.populaDadosAvaliacao(response.data.cursoAvaliacao, false);
                $scope.populaDadosCriacao(response.data.cursoCriacao, false);
            }, err => {
                $location.path("/cursos");
                Notification.error("Falha ao verificar cursos");
            });
        }

        $scope.getCursos();

    });