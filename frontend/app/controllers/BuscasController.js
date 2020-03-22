/* global host */
angular.module('app')
    .controller('BuscasController', function ($rootScope, $scope, QuestoesService,$location, $sce,UserService, $http, AuthService, Notification) {

    $scope.highlightType = true;

    $scope.setHighlightType = (type) => {$scope.highlightType = type;}

    $scope.closeInnerModal = (index) => {
        $("#addCompetencia" + index).modal('toggle');
    }

    window.onmouseup = () => {
        let parents = document.querySelectorAll("#areaSelecaoCriacaoQuestao");
        let selection = window.getSelection();
        if (selection.rangeCount > 0 && !window.getSelection().isCollapsed) {
            let range = selection.getRangeAt(0);
            for(let p of parents) {
                if (isOrContains(p, range.commonAncestorContainer)) {
                    highlight($scope.highlightType);
                    $rootScope.avaliacao.maisInfo[p.getAttribute("comp")] = p.innerHTML;
                    return;
                }
            }
        }
    };

    $rootScope.activetab = $location.path();
    
    $rootScope.Questoes = [];

    $rootScope.totalQuestoes = 0;
    $rootScope.totalPags = 0; 
    $rootScope.pageNumber = 0; 


    $rootScope.pagination = {
        current: 0
    };

    $scope.search = {
        competencias: ["COMP_COLETA","COMP_PARALELIZAÇÃO","COMP_ANÁLISE",
        "COMP_REPRESENTAÇÃO","COMP_DECOMPOSIÇÃO","COMP_ABSTRAÇÃO","COMP_SIMULAÇÃO",
        "COMP_AUTOMAÇÃO","COMP_ALGORITMOS", "COMP_TODAS"],
        estados: ["RASCUNHO", "PEND_AVALIACAO", "PUBLICADA", "REJEITADA"],
        autor: "",
        enunciado: "",
        fonte: "",
        tipo: "",
        conteudo: []
    };

    $rootScope.apenasAutor = false;
    $scope.editingAprovacao = false;

    $rootScope.avaliacao = {
        competencias: {
            "COMP_ABSTRAÇÃO": "false",
            "COMP_ALGORITMOS": "false",
            "COMP_ANÁLISE": "false",
            "COMP_AUTOMAÇÃO": "false",
            "COMP_COLETA": "false",
            "COMP_DECOMPOSIÇÃO": "false",
            "COMP_PARALELIZAÇÃO": "false",
            "COMP_REPRESENTAÇÃO": "false",
            "COMP_SIMULAÇÃO": "false"
        },
        maisInfo: {
            "COMP_ABSTRAÇÃO": "",
            "COMP_ALGORITMOS": "",
            "COMP_ANÁLISE": "",
            "COMP_AUTOMAÇÃO": "",
            "COMP_COLETA": "",
            "COMP_DECOMPOSIÇÃO": "",
            "COMP_PARALELIZAÇÃO": "",
            "COMP_REPRESENTAÇÃO": "",
            "COMP_SIMULAÇÃO": ""
        },
        confianca: 0,
        obsAvaliacao: "",
        obsQuestao: "",
        avaliacaoPublicacao: "PRONTA"
    }
    $rootScope.lastQuery = {}


    $scope.pageChangeHandler = function(newPage) {

        if (newPage === 'anterior' && $rootScope.pagination.current > 0) {
            $rootScope.pagination.current = $rootScope.pagination.current - 1;
        } else if (newPage === 'proxima' && $rootScope.pageNumber < ($rootScope.totalPags - 1)) {
            $rootScope.pagination.current = $rootScope.pagination.current + 1;
        } else if (newPage === 'primeiraPag') {
            $rootScope.pagination.current = 0;
        } else if (newPage === 'ultimaPag') {
            $rootScope.pagination.current = $rootScope.totalPags - 1;
        }

        $scope.sendQuery($scope.search.enunciado,$scope.search.autor,$scope.search.fonte,
            $scope.search.tipo, $scope.search.competencias, $scope.search.conteudo);
    };

    $scope.setPageStart = function() {
        $rootScope.pagination.current = 0;
    }

    $scope.competenciaList = ["COMP_COLETA","COMP_PARALELIZAÇÃO","COMP_ANÁLISE",
    "COMP_REPRESENTAÇÃO","COMP_DECOMPOSIÇÃO","COMP_ABSTRAÇÃO","COMP_SIMULAÇÃO",
    "COMP_AUTOMAÇÃO","COMP_ALGORITMOS", "COMP_TODAS"];

    $scope.questao = {};
    $scope.checkAll = true;

    $scope.toggleCheck = function($event) {
        if($event.currentTarget.id === "customCheck1") {
            if (!$scope.checkAll) {
                $scope.checkAll = true;
                $scope.search.competencias = angular.copy($scope.competenciaList);
                } else {
                $scope.checkAll = false;
            }
        } else {
            if(!$scope.search.competencias.includes($event.currentTarget.value)) {
                $scope.checkAll = false;
                for(let i = $scope.search.competencias.length-1; i >= 0; i--) {
                    if ($scope.search.competencias[i] === "COMP_TODAS") {
                        $scope.search.competencias.splice(i, 1);
                        document.querySelector("#customCheck1").checked = false;
                        //uncheck all
                    }
                }
            } else {
                if($scope.search.competencias.length === 9) {
                    $scope.checkAll = true;
                    document.querySelector("#customCheck1").checked = true;
                    $scope.search.competencias.push("COMP_TODAS");
                }
            }
        }
    };
    

    $scope.sendQuery = function (enunciadoSearch,autorSearch,fonteSearch,tipoSearch, competenciasSearch, conteudoSearch) {
        $rootScope.loading = true;

        setTimeout(function() { 
            if (!enunciadoSearch) enunciadoSearch = "null";
            if (!autorSearch) autorSearch = "null";
            if (!fonteSearch) fonteSearch = "null";
            if (!tipoSearch)  tipoSearch = "null";


            let query = {
                enunciado:  enunciadoSearch,
                competencias: competenciasSearch ? competenciasSearch : [],
                estados: $scope.search.estados ? $scope.search.estados : [],
                autor: autorSearch,
                fonte: fonteSearch,
                tipo: tipoSearch,
                conteudo: conteudoSearch ? conteudoSearch : []
            }
            $rootScope.lastQuery = query;
            QuestoesService.sendQuery(query, $rootScope.pagination.current , 4, $rootScope.apenasAutor);
        }, 10);

        $rootScope.painelListas = false;
    };

    $scope.sendNewQuery = function(competenciasSearch, conteudoSearch) {
        $scope.search.competencias = competenciasSearch;
        $scope.search.conteudo = conteudoSearch;

        $scope.sendQuery('', '', '', '', competenciasSearch, conteudoSearch);

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
    $scope.setApenasAutor = function () {
        limpaTabsQuestoes();
        $rootScope.nomeListaEscolhida = "";
        $scope.search.autor = $rootScope.email_usuario;
        $rootScope.apenasAutor = true;
        $scope.setPageStart();
        $scope.sendQuery($scope.search.enunciado,$scope.search.autor,$scope.search.fonte,$scope.search.tipo,
            $scope.search.competencias, $scope.search.conteudo);
    };

    $scope.setTodasQuestoes = function () {
        limpaTabsQuestoes();
        $rootScope.nomeListaEscolhida = "";
        $scope.search.autor = "";
        $rootScope.apenasAutor = false;
        $scope.sendQuery($scope.search.enunciado,$scope.search.autor,$scope.search.fonte,$scope.search.tipo,
            $scope.search.competencias, $scope.search.conteudo);
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
        tempEnunciado: "",
        tipo : "",
        conteudo : [],
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
        $scope.editingAprovacao = editingAprovacao;
        $rootScope.loading = false;
        $scope.inputError = false;
        $scope.alertEspelho = false;

        
        $scope.update.fonte = questao.fonte;
        $scope.update.tempEnunciado = questao.enunciado;
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

        if ($scope.editingAprovacao) {
            $('#editarAprovacao').modal('toggle');
            $('#editarAprovacao').modal({backdrop: 'static', keyboard: false});
            
            $('a[href$="#editarAprovacao"]').on( "click", function() {
                $('#editarAprovacao').modal('show');
            });
        }
        $().ready(function() {
            $rootScope.updateSelect('.editionSelect').then(() => {
                $('.editionSelect').val($scope.update.conteudo);
                $('.editionSelect').selectpícker("refresh");
            });
        });
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
            QuestoesService.aprovaQuestao(questao,novaQuestao).then((response) => {
                if(response.status == 200) {
                    QuestoesService.sendQuery($rootScope.lastQuery, $rootScope.pagination.current, 4, $rootScope.apenasAutor);
                }
            });
            $("#editarAprovacao").modal('toggle');
        } else {
            QuestoesService.atualizaQuestao(questao,novaQuestao);
            var  index = $rootScope.Questoes.indexOf(questao);
            var id = "#myModal" + index;
            $(id).modal('toggle');
        }
    };

    $scope.espelho = "nao";
    $scope.setEspelho = function () {
        if ($scope.espelho === "sim") $scope.espelho = "nao";
        else $scope.espelho = "sim";
    }

    $scope.inputError = false;
    $scope.checkEdicaoInput = function (questao) {
        if ($scope.update.conteudo === [] || typeof $scope.update.conteudo === 'undefined' ||
        $scope.update.tempEnunciado === "" ||  $scope.update.tempEnunciado === null ||
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
            $scope.update.espelho === null || typeof $scope.update.espelho === 'undefined')) {
            $scope.inputError = true;
        } else if (!$scope.editingAprovacao && $scope.update.enunciado !== $scope.update.tempEnunciado) {
            QuestoesService.getCompetencias($scope.update.tempEnunciado).then(() => {
                $rootScope.repaginaCompetencias($rootScope.competencias);
                $('#ModalEdicao').modal('toggle');
                $('#ModalEdicao').modal({backdrop: 'static', keyboard: false});
                
                $('a[href$="#ModalEdicao"]').on( "click", function() {
                    $('#ModalEdicao').modal('show');
                });
                $scope.update.enunciado = $scope.update.tempEnunciado;
            });
            
        } else {
            $scope.update.enunciado = $scope.update.tempEnunciado;
            $scope.inputError = false;
            $scope.sendUpdate(questao);
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
        $rootScope.updateSelect('.selectpicker');
    });

    $(function () {
        $('[data-toggle="popover"]').popover()
    })

    $(function () {
        $('.modal-bt').tooltip()
    })



    $scope.showResults = function() {
        $location.path("/questoes");
        $rootScope.blockSearch = true;
    };
    $scope.getApenasAutor = function () {
        return $rootScope.apenasAutor;
    };

    if (!$rootScope.blockSearch) {
        $scope.sendNewQuery($scope.search.competencias, $scope.search.conteudo);
    }
    $rootScope.blockSearch = false;



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
        for(let key of Object.keys($rootScope.avaliacao.competencias)) {
            $rootScope.avaliacao.competencias[key] = "false";
        }
        $rootScope.avaliacao.confianca = 0;
        $rootScope.avaliacao.obsAvaliacao = "";
        $rootScope.avaliacao.obsQuestao = "";
        $rootScope.avaliacao.avaliacaoPublicacao = "PRONTA";
        $rootScope.questaoSobAvaliacao = null;
        QuestoesService.getQuestaoPendente().then(() => {
            if ($rootScope.questaoSobAvaliacao !== null) {
            $('#ModalAvaliacao').modal('toggle');
            $('#ModalAvaliacao').modal({backdrop: 'static', keyboard: false})

            $('a[href$="#ModalAvaliacao"]').on( "click", function() {
                $('#ModalAvaliacao').modal('show');
            });
            let tmp = $rootScope.questaoSobAvaliacao.enunciado;
            $rootScope.avaliacao.maisInfo = {
                "COMP_ABSTRAÇÃO": tmp,
                "COMP_ALGORITMOS": tmp,
                "COMP_ANÁLISE": tmp,
                "COMP_AUTOMAÇÃO": tmp,
                "COMP_COLETA": tmp,
                "COMP_DECOMPOSIÇÃO": tmp,
                "COMP_PARALELIZAÇÃO": tmp,
                "COMP_REPRESENTAÇÃO": tmp,
                "COMP_SIMULAÇÃO": tmp
            };
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

    $scope.rejeitaQuestao = function(questao) {
        QuestoesService.rejeitaQuestao(questao);
    };

    $scope.sendAvaliacao = function () {

        let arr = [];
        for(let key of Object.keys($rootScope.avaliacao.competencias)) {
            if ($rootScope.avaliacao.competencias[key] === "true") {
                arr.push(key);
            }
        }

        let arr2 = [];
        for(let key of Object.keys($rootScope.avaliacao.competencias)) {
            if ($rootScope.avaliacao.competencias[key] === "true") {
                arr2.push(key+"|"+$rootScope.avaliacao.maisInfo[key]);
            }
        }

        let avaliacao = {
            observacaoAvaliacao: $rootScope.avaliacao.obsAvaliacao,
            observacaoQuestao: $rootScope.avaliacao.obsQuestao,
            avaliacaoPublicacao: $rootScope.avaliacao.avaliacaoPublicacao,
            questao: $rootScope.questaoSobAvaliacao.id,
            competencias: arr,
            infoCompetencias: arr2,
            confianca: $rootScope.avaliacao.confianca
        }

        $http.post(host + 'avaliacao', avaliacao, AuthService.getAuthorization()).
            then(function (response) {
                $("#ModalAvaliacao").modal('toggle');
                hideModals();
                Notification.success('Avaliação criada com sucesso!');
            },function(err){
                if (err.status == 400) {
                    $rootScope.forceSignOut();
                } else {
                    Notification.error("Falha no envio da avaliação");
                }
            }
        )

    };

    $scope.isJudge = function() {
        let permissions = AuthService.getPermissions();
        return permissions != null && permissions.indexOf('JUDGE') !== -1;
    };

});



