angular.module('app')
    .controller('BuscasController', function ($rootScope, $scope, QuestoesService,$location, $sce,UserService) {

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
            } else if (newPage === 'primeiraPag') {
                $scope.pagination.current = 0;
            } else if (newPage === 'ultimaPag') {
                $scope.pagination.current = $rootScope.totalPags - 1;
            }

            $scope.sendQuery($scope.enunciadoSearch,$scope.autorSearch,$scope.fonteSearch,
                $scope.tipoSearch, $scope.questao.competencias, $scope.conteudoSearch, "paginacao");
        };

        $scope.setPageStart = function() {
            $scope.pagination.current = 0;
        }
 
        $scope.checkAll = false;
        $scope.toggleCheck = function() {
            if (!$scope.checkAll) {
                $scope.checkAll = true;
                $scope.questao.competencias = angular.copy($scope.competencias);
              } else {
                $scope.checkAll = false;
                $scope.questao.competencias = [];
            }
        };

        $scope.minhasQuestoes = false;
        $scope.sendQuery = function (enunciadoSearch,autorSearch,fonteSearch,tipoSearch, competenciasSearch, conteudoSearch, tipo) {

            if (tipo === "novaBusca" ) {
                $scope.enunciadoSearch = enunciadoSearch;
                if (!$scope.minhasQuestoes) $scope.autorSearch = autorSearch;
                $scope.fonteSearch = fonteSearch;
                $scope.tipoSearch = tipoSearch;
                $scope.questao.competencias = competenciasSearch;
                $scope.conteudoSearch = conteudoSearch;
            } 

            setTimeout(function(){  
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
            }, 10);

            $rootScope.painelListas = false;

     
        };

        $scope.allEmpty = function () {
            return !$scope.enunciadoSearch && $scope.questao.competencias.length === 0 && !$scope.autorSearch
            && !$scope.fonteSearch && !$scope.tipoSearch && (!$scope.conteudoSearch || ($scope.conteudoSearch === "Qualquer Um"));
        }

        $scope.enunciadoShow = "";
        $scope.espelhoShow = "";

        $scope.updateViewQuill = function(text,tipo) {
            if (tipo === "enunciado") {
                $scope.enunciadoShow = $sce.trustAsHtml(text);
            } else {
                $scope.espelhoShow = $sce.trustAsHtml(text);
            }
        };

        $rootScope.nomeListaEscolhida = "";
        $scope.setMinhasQuestoes = function () {
            $rootScope.nomeListaEscolhida = "";
            $scope.minhasQuestoes = true;
            $scope.autorSearch = $rootScope.nome_usuario;
            $scope.setPageStart();
            $scope.sendQuery($scope.enunciadoSearch,$scope.autorSearch,$scope.fonteSearch,$scope.tipoSearch,
                $scope.questao.competencias, $scope.conteudoSearch, 'buscaNormal');
        };

        $scope.setTodasQuestoes = function () {
            $rootScope.nomeListaEscolhida = "";
            $scope.minhasQuestoes = false;
            $scope.autorSearch = "";
            $scope.sendQuery($scope.enunciadoSearch,$scope.autorSearch,$scope.fonteSearch,$scope.tipoSearch,
                $scope.questao.competencias, $scope.conteudoSearch, 'buscaNormal');
        };

        $rootScope.minhasListas = false;
        $rootScope.painelListas = false;


        $scope.getListas = function () {
            return $rootScope.painelListas;
        };

        $scope.isObjective = function (tipo) {
            return tipo === "Objetiva";
        };

        $scope.temQuestao = function () {
            return $rootScope.Questoes.length !== 0;
        };

        $scope.isAutor = function (autor) {
            return autor === $rootScope.nome_usuario;
        };

        $scope.isNull = function(atributo) {
            return atributo === null;
        };

        $scope.removeQuestao = function (questao) {   
           QuestoesService.removeQuestao(questao);
           var  index = $rootScope.Questoes.indexOf(questao);
           $rootScope.Questoes.splice(index,1);
           var id = "#myModal" + index;
           $(id).modal('toggle');
        };
             
        $scope.update = {
            enunciado : "",
            tipo : "",
            conteudo : "",
            espelho: "",
            fonte: "",
            alternativas: [],
            corretas: []
        };

        $scope.atualizaQuestao = function (questao) {
            $scope.update.fonte = questao.fonte;
            $scope.update.enunciado = questao.enunciado;
            $scope.update.tipo = questao.tipo;

            if (questao.tipo  === "Objetiva") {
                for (i = 0; i < (questao.alternativas.length); i++) {
                    $scope.update.corretas[i] = questao.alternativas[i].correta;
                    $scope.update.alternativas[i] = questao.alternativas[i].texto;
                }
            } else {
                $scope.update.espelho = questao.espelho;
            }

            $('.selectpicker').selectpicker('refresh');
        };




       $scope.sendUpdate = function(questao) {
            novaQuestao = {
                autor:  UserService.getName(),
                conteudo: $scope.update.conteudo,
                enunciado: $scope.update.enunciado,
                fonte: $scope.update.fonte,
                tipo: $scope.update.tipo
            };

        if ($scope.updateTipo === "Objetiva") {
            novaQuestao.alternativas = [
                {
                    correta: $scope.update.corretas[0],
                    texto: $scope.update.alternativas[0]
                },
                {
                    correta: $scope.update.corretas[1],
                    texto: $scope.update.alternativas[1]
                },
                {
                    correta: $scope.update.corretas[2],
                    texto: $scope.update.alternativas[2]
                },
                {
                    correta: $scope.update.corretas[3],
                    texto: $scope.update.alternativas[3]
                },
                {
                    correta: $scope.update.corretas[4],
                    texto: $scope.update.alternativas[4]
                }
            ];

        } else {
            novaQuestao.espelho = $scope.update.espelho;
        }

        QuestoesService.atualizaQuestao(questao,novaQuestao);

        var  index = $rootScope.Questoes.indexOf(questao);
        var id = "#myModal" + index;
        $(id).modal('toggle');

    }



        $scope.competenciasRepaginadas = [];
        $scope.amostraCompetencias = "";
        $scope.repaginaCompetencias = function (competencias) {
            $scope.competenciasRepaginadas = [];
            $scope.amostraCompetencias = "";
            for (i = 0; i < (competencias.length); i++) {
                var compSplitted = competencias[i].split("_");
                var compLowerCase = compSplitted[1].toLowerCase();
                $scope.competenciasRepaginadas[i] = compLowerCase.charAt(0).toUpperCase() + compLowerCase.slice(1);
                if (i === (competencias.length - 1) && i === 0) {
                    $scope.amostraCompetencias += (compLowerCase.charAt(0).toUpperCase() + compLowerCase.slice(1) + ".");
                } else if (i === (competencias.length - 1)) {
                    $scope.amostraCompetencias += ("e " + compLowerCase.charAt(0).toUpperCase() + compLowerCase.slice(1) + ".");
                }
                else {
                    $scope.amostraCompetencias += (compLowerCase.charAt(0).toUpperCase() + compLowerCase.slice(1) + ", ");
                }
            }      
        }


        $scope.alternativasLetras = ["a","b","c","d","e"];

        $scope.getLetter = function(index) {
            return $scope.alternativasLetras[index];
        };

        $scope.isUpdateObjective = function() { 
            return $scope.update.tipo === "Objetiva";
        };

        $(document).ready(function() {
            $('.selectpicker').selectpicker();
        });

        $(function () {
            $('[data-toggle="popover"]').popover()
        })

        $(function () {
            $('.modal-bt').tooltip()
        })



        $scope.setLocation = function() {
            $location.path("\questoes");
        }

        QuestoesService.getQuestoes($scope.pagination.current , 5);
        

        $scope.options = [
            {name: 'Adição', group: 'Group1'},
            {name: 'Conjuntos', group: 'Group1'},
            {name: 'Conjuntos Numéricos', group: 'Group1'},
            {name: 'Critérios de Divisibilidade', group: 'Group1'},
            {name: 'Divisão de Frações', group: 'Group1'},
            {name: 'Equação do Primeiro Grau', group: 'Group1'},
            {name: 'Equação do Segundo Grau', group: 'Group1'},
            {name: 'Estatística', group: 'Group2'}
          ];

                 // Ativadores das opções de edição no Quill Editor
        $rootScope.editorModules = {
            formula: true, 
            toolbar: [
              ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
              ['blockquote'],

              //[{ 'header': 1 }, { 'header': 2 }],               // custom button values
              [{ 'list': 'ordered' }, { 'list': 'bullet' }],
              [{ 'script': 'sub' }, { 'script': 'super' }],      // superscript/subscript
              [{ 'indent': '-1' }, { 'indent': '+1' }],          // outdent/indent
              [{ 'direction': 'rtl' }],                         // text direction

              [{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown
              [{ 'header': [1, 2, 3, 4, 5, 6, false] }],

              [{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme
              [{ 'font': [] }],
              [{ 'align': [] }],

              //['clean'],                                         // remove formatting button

              ['formula','link','image']                         // link and image, video
            ]
          }


    });



