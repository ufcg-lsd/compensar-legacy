/* global host */
angular.module('app')
    .controller('BuscasController', function ($rootScope, $scope, QuestoesService,$location, $sce,UserService, $http, AuthService, Notification) {

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
        $scope.apenasAutor = false;
        $scope.avaliacaoPublicacao = "PRONTA";
        $scope.editingAprovacao = false;

        $scope.competenciasAvaliador = {
            "COMP_ABSTRAÇÃO": "false",
            "COMP_ALGORITMOS": "false",
            "COMP_ANÁLISE": "false",
            "COMP_AUTOMAÇÃO": "false",
            "COMP_COLETA": "false",
            "COMP_DECOMPOSIÇÃO": "false",
            "COMP_PARALELIZAÇÃO": "false",
            "COMP_REPRESENTAÇÃO": "false",
            "COMP_SIMULAÇÃO": "false"
        };

        $scope.confiancaAvaliacao = 0;
        $scope.obsAvaliacao = "";
        $scope.obsQuestao = "";


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
                $scope.tipoSearch, $scope.questao.competencias, $scope.conteudoSearch);
        };

        $scope.setPageStart = function() {
            $scope.pagination.current = 0;
        }

        $scope.competencias = ["COMP_COLETA","COMP_PARALELIZAÇÃO","COMP_ANÁLISE",
        "COMP_REPRESENTAÇÃO","COMP_DECOMPOSIÇÃO","COMP_ABSTRAÇÃO","COMP_SIMULAÇÃO",
        "COMP_AUTOMAÇÃO","COMP_ALGORITMOS"];

        $scope.questao = {
            competencias: []
        };
 
        $scope.checkAll = false;

        $scope.toggleCheck = function($event) {
            if($event.currentTarget.id === "customCheck1") {
                if (!$scope.checkAll) {
                    $scope.checkAll = true;
                    $scope.questao.competencias = angular.copy($scope.competencias);
                  } else {
                    $scope.checkAll = false;
                    $scope.questao.competencias = [];
                }
            } else {
                if(!$scope.questao.competencias.includes($event.currentTarget.value)) {
                    $scope.checkAll = false;
                    document.querySelector("#customCheck1").checked = false;
                    //uncheck all
                } else {
                    if($scope.questao.competencias.length === 9) {
                        $scope.checkAll = true;
                        document.querySelector("#customCheck1").checked = true;
                    }
                }
            }
        };
        

        $scope.minhasQuestoes = false;
        $scope.sendQuery = function (enunciadoSearch,autorSearch,fonteSearch,tipoSearch, competenciasSearch, conteudoSearch) {
            $rootScope.loading = true;

            setTimeout(function() {  
                if ($scope.allEmpty()) {
                    QuestoesService.getQuestoes($scope.pagination.current , 4);
                } else {

                    if (!enunciadoSearch) enunciadoSearch = "null";
                    if (competenciasSearch.length === 0) competenciasSearch = ["null"]; 
                    if (!autorSearch) autorSearch = "null";
                    if (!fonteSearch) fonteSearch = "null";
                    if (!tipoSearch)  tipoSearch = "null";
                    if (!conteudoSearch || (conteudoSearch === "Qualquer Um")) conteudoSearch = "null";


                    let query = {
                        enunciado:  enunciadoSearch,
                        competencias: competenciasSearch,
                        autor: autorSearch,
                        fonte: fonteSearch,
                        tipo: tipoSearch,
                        conteudo: conteudoSearch
                    }

                    QuestoesService.sendQuery(query, $scope.pagination.current , 4, $scope.apenasAutor);
            }}, 10);

            $rootScope.painelListas = false;
        };

        $scope.sendNewQuery = function(competenciasSearch, conteudoSearch) {
            $scope.questao.competencias = competenciasSearch;
            $scope.conteudoSearch = conteudoSearch;

            $scope.sendQuery('', '', '', '', competenciasSearch, conteudoSearch);

        }

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

        $rootScope.adicionaMarcadores = function (enunciado) {

            for (let i = 0; i < $rootScope.Questoes.length; i++) {
                var arraySubStringsEnunciado = $rootScope.Questoes[i].enunciado.split(" ");
                $scope.enunciadoAtualizado = "";
      
      
                for (let j = 0; j < arraySubStringsEnunciado.length; j++) {
      
      
                    if (enunciado == arraySubStringsEnunciado[j]) {
                        arraySubStringsEnunciado[j] = "<mark>" + arraySubStringsEnunciado[j] + "</mark>";
                    }
                    $scope.enunciadoAtualizado += arraySubStringsEnunciado[j];
      
                    if (j < (arraySubStringsEnunciado.length - 1)) $scope.enunciadoAtualizado += " ";
                }
      
                $rootScope.Questoes[i].enunciado = $scope.enunciadoAtualizado;
            }
            
        };


        $rootScope.nomeListaEscolhida = "";
        $scope.setMinhasQuestoes = function () {
            $rootScope.nomeListaEscolhida = "";
            $scope.minhasQuestoes = true;
            $scope.autorSearch = $rootScope.nome_usuario;
            $scope.apenasAutor = true;
            $scope.setPageStart();
            $scope.sendQuery($scope.enunciadoSearch,$scope.autorSearch,$scope.fonteSearch,$scope.tipoSearch,
                $scope.questao.competencias, $scope.conteudoSearch);
        };

        $scope.setTodasQuestoes = function () {
            $rootScope.nomeListaEscolhida = "";
            $scope.minhasQuestoes = false;
            $scope.autorSearch = "";
            $scope.apenasAutor = false;
            $scope.sendQuery($scope.enunciadoSearch,$scope.autorSearch,$scope.fonteSearch,$scope.tipoSearch,
                $scope.questao.competencias, $scope.conteudoSearch);
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
            return autor === $rootScope.email_usuario;
        };

        $scope.isNull = function(atributo) {
            return atributo === null;
        };

        $scope.removeQuestao = function (questao) {   
           QuestoesService.removeQuestao(questao);
        };

        $scope.publicaQuestao = function (questao) {   
            QuestoesService.publicaQuestao(questao);
         };
             
        $scope.update = {
            enunciado : "",
            tipo : "",
            conteudo : "",
            espelho: "",
            fonte: "",
            alternativas: [{
                correta: "",
                texto: "",
            },{
                correta: "",
                texto: "",
            },{
                correta: "",
                texto: "",
            },{
                correta: "",
                texto: "",
            },{
                correta: "",
                texto: "",
            }]
        };



        $scope.atualizaQuestao = function (questao, editingAprovacao) {
            $scope.editingAprovacao = (editingAprovacao === undefined) ? false : true;
            $rootScope.loading = false;
            $scope.inputError = false;
            $scope.alertEspelho = false;


            $scope.update.fonte = questao.fonte;
            $scope.update.enunciado = questao.enunciado;
            $scope.update.tipo = questao.tipo;
            $scope.update.conteudo = questao.conteudo;
            $rootScope.competencias = questao.competencias;

            if (questao.tipo  === "Objetiva") {
                for (let i = 0; i < (questao.alternativas.length); i++) {
                    $scope.update.alternativas[i].correta = questao.alternativas[i].correta;
                    $scope.update.alternativas[i].texto = questao.alternativas[i].texto;
                }
            } else {
                $scope.update.espelho = questao.espelho;
            }

            $('.selectpicker').selectpicker('refresh');
        };


       $scope.sendUpdate = function(questao) {
            $rootScope.loading = true;

            let novaQuestao = {
                autor:  UserService.getName(),
                conteudo: $scope.update.conteudo,
                enunciado: $scope.update.enunciado,
                competencias: $rootScope.competencias,
                fonte: $scope.update.fonte,
                tipo: $scope.update.tipo
            };

        if ($scope.update.tipo === "Objetiva") {
            novaQuestao.alternativas = $scope.update.alternativas;
        } else {
            novaQuestao.espelho = $scope.update.espelho;
        }

        
        if ($scope.editingAprovacao) {
            QuestoesService.aprovaQuestao(questao,novaQuestao);
        } else {
            QuestoesService.atualizaQuestao(questao,novaQuestao);
        }

        var  index = $rootScope.Questoes.indexOf(questao);
        var id = "#myModal" + index;
        $(id).modal('toggle');
    };

    $scope.espelho = "nao";
    $scope.setEspelho = function () {
        if ($scope.espelho === "sim") $scope.espelho = "nao";
        else $scope.espelho = "sim";
    }

    $scope.inputError = false;
    $scope.checkEdicaoInput = function (questao) {
        if ($scope.update.conteudo === "" || typeof $scope.update.conteudo === 'undefined' ||
        $scope.update.enunciado === "" ||  $scope.update.enunciado === null ||
        $scope.update.fonte === "" || typeof $scope.update.fonte === 'undefined' ||
        $scope.update.tipo === "" || typeof $scope.update.tipo === 'undefined') {
            $scope.inputError = true;
        } else if ($scope.update.tipo === "Objetiva" && (($scope.update.alternativas[0].correta === false &&
        $scope.update.alternativas[1].correta === false && $scope.update.alternativas[2].correta === false &&
        $scope.update.alternativas[3].correta === false && $scope.update.alternativas[4].correta === false) ||
        $scope.update.alternativas[0].texto === "" || $scope.update.alternativas[1].texto === "" ||
        $scope.update.alternativas[2].texto === "" || $scope.update.alternativas[3].texto === "" || 
        $scope.update.alternativas[4].texto === "")) {
            $scope.inputError = true;
        } else if ($scope.update.tipo === "Subjetiva" && 
            ($scope.update.espelho === "" || 
            $scope.update.espelho === null || $scope.update.espelho === 'undefined')) {
            $scope.inputError = true;
        } else if ($scope.update.enunciado !== questao.enunciado) {
            QuestoesService.getCompetencias($scope.update.enunciado).then(() => {
                $scope.repaginaCompetencias($rootScope.competencias);
                $('#ModalEdicao').modal('toggle');
                $('#ModalEdicao').modal({backdrop: 'static', keyboard: false});
                
                $('a[href$="#ModalEdicao"]').on( "click", function() {
                    $('#ModalEdicao').modal('show');
                });
                
                console.log("aaaa");
                $scope.questao.enunciado = $scope.update.enunciado;
            });
            
        } else {
            $scope.inputError = false;
            $scope.sendUpdate(questao);
        }
    }



        $scope.competenciasRepaginadas = [];
        $scope.amostraCompetencias = "";
        $scope.repaginaCompetencias = function (competencias) {
            $scope.competenciasRepaginadas = [];
            $scope.amostraCompetencias = "";
            for (let i = 0; i < (competencias.length); i++) {
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
            $location.path("/questoes");   
        };
        $scope.getMinhasQuestoes = function () {
            return $scope.minhasQuestoes;
        };


        QuestoesService.getQuestoes($scope.pagination.current , 4);
        

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



    $scope.getQuestaoPendente = function() {
        $rootScope.questaoSobAvaliacao = null;
        QuestoesService.getQuestaoPendente().then(() => {
            if ($rootScope.questaoSobAvaliacao !== null) {
            $('#ModalAvaliacao').modal('toggle');
            $('#ModalAvaliacao').modal({backdrop: 'static', keyboard: false})

            $('a[href$="#ModalAvaliacao"]').on( "click", function() {
                $('#ModalAvaliacao').modal('show');
            });
            }
        });
    };

    $scope.getQuestaoAvaliada = function() {
        $rootScope.questaoSobAvaliacao = null;
        QuestoesService.getQuestaoAvaliada().then((response) => {
            if (response.status == 200) {
                $scope.questao = response.data;
                $scope.atualizaQuestao($scope.questao, true);
            }
        });
    };

    $scope.aprovaQuestao = function(questao) {
        QuestoesService.aprovaQuestao(questao);
    };

    $scope.rejeitaQuestao = function(questao) {
        QuestoesService.rejeitaQuestao(questao);
    };

    $scope.sendAvaliacao = function () {

        let arr = [];
        for(let key of Object.keys($scope.competenciasAvaliador)) {
            if ($scope.competenciasAvaliador[key] === "true") {
                arr.push(key);
            }
        }

        let avaliacao = {
            observacaoAvaliacao: $scope.obsAvaliacao,
            observacaoQuestao: $scope.obsQuestao,
            avaliacaoPublicacao: $scope.avaliacaoPublicacao,
            questao: $rootScope.questaoSobAvaliacao.id,
            competencias: arr,
            confianca: $scope.confiancaAvaliacao
        }

        $http.post(host + 'avaliacao', avaliacao, AuthService.getAuthorization()).
            then(function (response) {
                if (response.status == 200) {
                    $("#ModalAvaliacao").modal('hide');
                    hideModals();
                    Notification.success('Avaliação criada com sucesso!');
                }
                else {
                    Notification.error("Falha no envio da questão");
                }
            },function(){
            }
        )

    };

    $scope.isJudge = function() {
        return AuthService.getPermissions().indexOf('JUDGE') !== -1;
    };

});



