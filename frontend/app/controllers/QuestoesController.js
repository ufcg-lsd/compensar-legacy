angular.module('app')
    .controller('QuestoesController', function ($rootScope, $scope, QuestoesService,$sce,$location) {
        
        $rootScope.Questoes = [];


        $rootScope.activetab = $location.path();

    
        $scope.enunciado = "";
        $scope.espelho = "";


        $scope.updateViewQuill = function(text,tipo) {
            if (tipo === "enunciado") {
                $scope.enunciado = $sce.trustAsHtml(text);
            } else {
                $scope.espelho = $sce.trustAsHtml(text);
            }
        };

        
        $scope.isObjective = function (tipo) {
            return tipo === "Objetiva";
        }

        $scope.temQuestao = function () {
            return $rootScope.Questoes !== [];
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