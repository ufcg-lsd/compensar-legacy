angular.module('app')
    .controller('BuscasController', function ($rootScope, $scope, QuestoesService,$location, $sce) {

        $rootScope.activetab = $location.path();
        
        $rootScope.Questoes = [];

        $rootScope.totalQuestoes = 0;
        $rootScope.totalPags = 0; 
        $rootScope.pageNumber = 0; 

        $scope.pagination = {
            current: 0
        };

        $scope.autorSearch = "";
        $scope.enunciadoSearch = "";
        $scope.fonteSearch = "";
        $scope.tipoSearch = "";
        $scope.conteudoSearch = "";

        $scope.competencias = ["COMP_COLETA","COMP_PARALELIZAÇÃO","COMP_ANÁLISE",
        "COMP_REPRESENTAÇÃO","COMP_DECOMPOSIÇÃO","COMP_ABSTRAÇÃO","COMP_SIMULAÇÃO",
        "COMP_AUTOMAÇÃO","COMP_ALGORITMOS"];

        $scope.questao = {
            competencias: []
        };

        $scope.pageChangeHandler = function(newPage) {

            if (newPage === 'anterior' && $scope.pagination.current > 0) {
                $scope.pagination.current = $scope.pagination.current - 1;
            } else if (newPage === 'proxima' && $rootScope.pageNumber < ($rootScope.totalPags - 1)) {
                $scope.pagination.current = $scope.pagination.current + 1;
            }

            $scope.sendQuery($scope.enunciadoSearch,$scope.autorSearch,$scope.fonteSearch,
                $scope.tipoSearch, $scope.questao.competencias, $scope.conteudoSearch, "paginacao");
        };

        $scope.setPageStart = function() {
            $scope.pagination.current = 0;
        }
 

        $scope.minhasQuestoes = false;
        $scope.checkAll = false;

        $scope.toggleCheck = function() {
            console.log($scope.questao.competencias);
            if (!$scope.checkAll) {
                $scope.checkAll = true;
                $scope.questao.competencias = angular.copy($scope.competencias);
              } else {
                $scope.checkAll = false;
                $scope.questao.competencias = [];
            }
            console.log($scope.questao.competencias);
        };

        $scope.sendQuery = function (enunciadoSearch,autorSearch,fonteSearch,tipoSearch, competenciasSearch, conteudoSearch, tipo) {

            if (tipo === "novaBusca") {
                $scope.enunciadoSearch = enunciadoSearch;
                if (!$scope.minhasQuestoes) $scope.autorSearch = autorSearch;
                $scope.fonteSearch = fonteSearch;
                $scope.tipoSearch = tipoSearch;
                $scope.questao.competencias = competenciasSearch;
                $scope.conteudoSearch = conteudoSearch;
            }

            if ($scope.allEmpty()) {
                QuestoesService.getQuestoes($scope.pagination.current , 5);
            } else {

                if (!enunciadoSearch) enunciadoSearch = "null";
                if (competenciasSearch.length === 0) competenciasSearch = ["null"]; 
                if (!autorSearch) autorSearch = "null";
                if (!fonteSearch) fonteSearch = "null";
                if (!tipoSearch)  tipoSearch = "null";
                if (!conteudoSearch || (conteudoSearch === "Qualquer Um")) conteudoSearch = "null";


                query = {
                    enunciado:  enunciadoSearch,
                    competencias: competenciasSearch,
                    autor: autorSearch,
                    fonte: fonteSearch,
                    tipo: tipoSearch,
                    conteudo: conteudoSearch
                }

                QuestoesService.sendQuery(query, $scope.pagination.current , 5);
            }
           
            $location.path("/questoes");

        };

        $scope.allEmpty = function () {
            return !$scope.enunciadoSearch && $scope.questao.competencias.length === 0 && !$scope.autorSearch
            && !$scope.fonteSearch && !$scope.tipoSearch && (!$scope.conteudoSearch || ($scope.conteudoSearch === "Qualquer Um"));
        }


        $(document).ready(function() {
            $('.selectpicker').selectpicker();
        });

        $scope.enunciado = "";
        $scope.espelho = "";

        $scope.updateViewQuill = function(text,tipo) {
            if (tipo === "enunciado") {
                $scope.enunciado = $sce.trustAsHtml(text);
            } else {
                $scope.espelho = $sce.trustAsHtml(text);
            }
        };

        $scope.setMinhasQuestoes = function () {
            $scope.minhasQuestoes = true;
            $scope.autorSearch = $rootScope.nome_usuario;
            $scope.sendQuery($scope.enunciadoSearch,$scope.autorSearch,$scope.fonteSearch,$scope.tipoSearch,$scope.questao.competencias, $scope.conteudoSearch, 'buscaNormal');
        }

        $scope.setTodasQuestoes = function () {
            $scope.minhasQuestoes = false;
            $scope.autorSearch = "";
            $scope.sendQuery($scope.enunciadoSearch,$scope.autorSearch,$scope.fonteSearch,$scope.tipoSearch,$scope.questao.competencias, $scope.conteudoSearch, 'buscaNormal');
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