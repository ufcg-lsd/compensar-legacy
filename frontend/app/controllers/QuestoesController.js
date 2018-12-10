angular.module('app')
    .controller('QuestoesController', function ($rootScope, $scope, QuestoesService) {

        $scope.search = "";
        $rootScope.QuestoesSubj = [];
        $rootScope.QuestoesObj = [];


        QuestoesService.getQuestoesSubj().then(function (value) {
            $rootScope.QuestoesSubj = value;
        });

        QuestoesService.getQuestoesObj().then(function (value) {
            $rootScope.QuestoesObj = value;
        });

        //fzr p questão objetiva --> da merge nos arrays --> embaralhar elementos
        //fzr array para minhas questões

        
        $scope.isObjective = function (tipo) {
            return tipo === "Objetiva";
        }

        $scope.isAutor = function (autor) {
            return autor === $rootScope.nome_usuario;
        }


        $scope.removeQuestaoSubj = function (questaoSubj) {
           QuestoesService.removeQuestaoSubj(questaoSubj);
           var  index = $rootScope.QuestoesSubj.indexOf(questaoSubj);
           $rootScope.QuestoesSubj.splice(index,1);
        }
    
    });