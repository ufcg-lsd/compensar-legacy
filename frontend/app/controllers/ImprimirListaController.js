angular.module('app')
    .controller('ImprimirListaController',  function($rootScope,$scope,$http,AuthService, QuestoesService, $mdDialog, Notification)
    {

        const exampleLista = {
            "id": "5d8a5faa80dbd13f195e5381",
            "nomeLista": "Lista 2",
            "autor": {
                "nome": "Joao Marcos Lima Medeiros",
                "idade": 19,
                "nomeInstituicao": "in",
                "cargo": "cargo",
                "cidade": "ci",
                "email": "joao.medeiros@ccc.ufcg.edu.br",
                "ativo": true
            },
            "questoes": [
                {
                    "id": "5d83d92e80dbd15fb1cca67b",
                    "tipo": "Objetiva",
                    "enunciado": "<p>Qual a letra A?</p>",
                    "competencias": [
                        "COMP_DECOMPOSIÇÃO"
                    ],
                    "fonte": "ENEM",
                    "autor": "Joao Marcos Lima Medeiros",
                    "espelho": null,
                    "conteudo": "Objetiva",
                    "alternativas": [
                        {
                            "texto": "A",
                            "correta": true
                        },
                        {
                            "texto": "B",
                            "correta": false
                        },
                        {
                            "texto": "C",
                            "correta": false
                        },
                        {
                            "texto": "D",
                            "correta": false
                        },
                        {
                            "texto": "E",
                            "correta": false
                        }
                    ],
                    "score": null
                },
                {
                    "id": "5d8a0a9d80dbd12cebe7e013",
                    "tipo": "Objetiva",
                    "enunciado": "<p>Quanto é 1 + 1?</p>",
                    "competencias": [
                        "COMP_DECOMPOSIÇÃO"
                    ],
                    "fonte": "ENEM",
                    "autor": "Joao Marcos Lima Medeiros",
                    "espelho": null,
                    "conteudo": "Objetiva",
                    "alternativas": [
                        {
                            "texto": "1",
                            "correta": false
                        },
                        {
                            "texto": "2",
                            "correta": true
                        },
                        {
                            "texto": "3",
                            "correta": false
                        },
                        {
                            "texto": "4",
                            "correta": false
                        },
                        {
                            "texto": "5",
                            "correta": false
                        }
                    ],
                    "score": null
                }
            ]
        }

        const pageWidth = 210, pageHeight = 297, margin = 10;



        const insereHeader = (doc, lista) => {
            doc.setFontType("bold");
            doc.setFontSize(22);
            doc.text(pageWidth/2, 15, lista.nomeLista, 'center');
            doc.setFontType("normal");
            doc.setFontSize(11);
            doc.text(margin, 25, "Autor: " + lista.autor.nome);
            doc.text(margin, 30, "Aluno:");
            doc.line(margin+11, 31, pageWidth-margin, 31);
        }

        const ajustaY = (doc, y) => {
            return y+10;
        }

        const insereQuestao = (doc, questao, y) => {
            y = ajustaY(doc, y);
            doc.addHTML(questao);
        }

        $scope.gelListaImpressa = () => {
            console.log("bbbbbbbbb");
            let lista = Object.assign({}, $rootScope.lista);
            var doc = new jsPDF();
            doc.setProperties({
                title: lista.nomeLista,
                author: lista.autor.nome,
            })
            doc.setFont("helvetica");
            insereHeader(doc, lista);
            let y = 35
            for (let questao of lista.questoes) {
                y = insereQuestao(doc, questao, y);
            }
            console.log(doc.output('datauri'));
            $('#pdfViewer').attr("src", doc.output('datauri'));
        }
    });