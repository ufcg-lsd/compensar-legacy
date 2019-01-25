angular.module('app')
    .controller('QuestoesController', function ($rootScope, $scope, QuestoesService,$sce,$location) {


        $rootScope.activetab = $location.path();

        $scope.search = "";
        $rootScope.Questoes = [];
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

        $scope.isAutor = function (autor) {
            return autor === $rootScope.nome_usuario;
        }

        $scope.tuplaAlternativa = function (alternativa) {
            const correta = alternativa.split(":");
            const texto = correta[1].split(",");
            const textoCorreta = [texto[0],correta[2]];

            return textoCorreta;  
        }


        $scope.removeQuestao = function (questao,tipo) {   
           QuestoesService.removeQuestao(questao,tipo);
        }

        QuestoesService.getQuestoes().then(function (value) {
            $rootScope.Questoes = value;
        });

    
    });