angular.module('app')
    .controller('BuscasController', function ($rootScope, $scope, QuestoesService,$location, $sce) {

        $rootScope.activetab = $location.path();
        
        $rootScope.Questoes = [];
    
        $scope.enunciado = "";
        $scope.espelho = "";
        $scope.autorSearch = "";
        $scope.minhasQuestoes = false;

        $scope.conteudoSearch = "";
        $scope.competencias = ["'COLETA'","'PARALELIZAÇÃO'","'ANÁLISE'","'REPRESENTAÇÃO'","'DECOMPOSIÇÃO'","'ABSTRAÇÃO'","'SIMULAÇÃO'","'AUTOMAÇÃO'","'ALGORITMOS'"];
        $scope.questao = {
            competencias: []
        };

        $scope.uncheckAll = function() {
            $scope.questao.competencias = [];
        };
        $scope.checkAll = function() {
            $scope.questao.competencias = angular.copy($scope.competencias);
        };

        $scope.sendQuery = function (enunciadoSearch, autorSearch, fonteSearch, tipoSearch) { 
            console.log(autorSearch);
            
            if ($scope.allEmpty(enunciadoSearch, autorSearch, fonteSearch, tipoSearch)) {
                QuestoesService.getQuestoes();
            } else {

                if (!enunciadoSearch) enunciadoSearch = "null";
                if ($scope.questao.competencias.length === 0) $scope.questao.competencias = ["null"]; 
                if (!autorSearch) autorSearch = "null";
                if (!fonteSearch) fonteSearch = "null";
                if (!tipoSearch)  tipoSearch = "null";
                if (!$scope.conteudoSearch || ($scope.conteudoSearch === "Qualquer Um")) $scope.conteudoSearch = "null";


                query = {
                    enunciado:  enunciadoSearch,
                    competencias: $scope.questao.competencias,
                    autor: autorSearch,
                    fonte: fonteSearch,
                    tipo: tipoSearch,
                    conteudo: $scope.conteudoSearch
                }

                $scope.questao.competencias = [];
                QuestoesService.sendQuery(query);
            }
            
            $location.path("/questoes");

        };

        $scope.allEmpty = function (enunciadoSearch,autorSearch,fonteSearch,tipoSearch) {
            return !enunciadoSearch && $scope.questao.competencias.length === 0 && !autorSearch
            && !fonteSearch && !tipoSearch && (!$scope.conteudoSearch || ($scope.conteudoSearch === "Qualquer Um"));
        }


        $(document).ready(function() {
            $('.selectpicker').selectpicker();
        });

        $scope.updateViewQuill = function(text,tipo) {
            if (tipo === "enunciado") {
                $scope.enunciado = $sce.trustAsHtml(text);
            } else {
                $scope.espelho = $sce.trustAsHtml(text);
            }
        };

        $scope.setMinhasQuestoes = function () {
            $scope.minhasQuestoes = true;
            $scope.sendQuery('',$rootScope.nome_usuario,'','');
            $scope.autorSearch = $rootScope.nome_usuario;
        }

        $scope.setTodasQuestoes = function () {
            $scope.minhasQuestoes = false;
            QuestoesService.getQuestoes();
            $scope.autorSearch = "";
        }

        $scope.isObjective = function (tipo) {
            return tipo === "Objetiva";
        }


        $scope.temQuestao = function () {
            return $rootScope.Questoes.length !== 0;
        }

        $scope.isAutor = function (autor) {
            return autor === $rootScope.nome_usuario;
        }

        $scope.isNull = function(atributo) {
            return atributo === null;
        }

        $scope.removeQuestao = function (questao,tipo) {   
           QuestoesService.removeQuestao(questao,tipo);
        }

        /**
        QuestoesService.getQuestoes().then(function (value) {
            $rootScope.Questoes = value;
        });
         */

    });