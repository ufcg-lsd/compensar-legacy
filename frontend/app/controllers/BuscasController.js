angular.module('app')
    .controller('BuscasController', function ($rootScope, $scope, QuestoesService,$location, $window) {

        $rootScope.activetab = $location.path();

        $scope.enunciadoSearch = "";
        $scope.fonteSearch = "";
        $scope.tipoSearch = "";
        $scope.autorSearch = "";
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

        $scope.sendQuery = function () {

            if ($scope.allEmpty()) {
                QuestoesService.getQuestoes();
            } else {

                if (!$scope.enunciadoSearch) $scope.enunciadoSearch = "null";
                if ($scope.questao.competencias.length === 0) $scope.questao.competencias = ["null"]; 
                if (!$scope.autorSearch) $scope.autorSearch = "null";
                if (!$scope.fonteSearch) $scope.fonteSearch = "null";
                if (!$scope.tipoSearch)  $scope.tipoSearch = "null";
                if (!$scope.conteudoSearch || ($scope.conteudoSearch === "Qualquer Um")) $scope.conteudoSearch = "null";


                query = {
                    enunciado:  $scope.enunciadoSearch,
                    competencias: $scope.questao.competencias,
                    autor: $scope.autorSearch,
                    fonte: $scope.fonteSearch,
                    tipo: $scope.tipoSearch,
                    conteudo: $scope.conteudoSearch
                }

                $scope.questao.competencias = [];
                QuestoesService.sendQuery(query);
            }

            
            $location.path("/questoes");

            $rootScope.Questoes = [];
        };

        $scope.allEmpty = function () {
            return !$scope.enunciadoSearch && $scope.questao.competencias.length === 0 && !$scope.autorSearch
            && !$scope.fonteSearch && !$scope.tipoSearch && (!$scope.conteudoSearch || ($scope.conteudoSearch === "Qualquer Um"));
        }


        $(document).ready(function() {
            $('.selectpicker').selectpicker();
        });

    });