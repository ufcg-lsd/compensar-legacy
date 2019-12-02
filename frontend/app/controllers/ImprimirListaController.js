angular.module('app')
    .controller('ListaController',  function($rootScope,localStorageService,$scope)
    {
        $rootScope.printLista = true;
        let listaAtiva = localStorageService.get("listaAtiva");
        console.log(listaAtiva);
        let addHTML = (html, div) => {document.querySelector(`#${div}`).innerHTML += html;}
        let addLista = (html) => {addHTML(html, "impressaoLista")}
        let addRespostas = (html) => {addHTML(html, "impressaoRespostas")}
        addLista(
        `
            <h1 class="ql-align-center">${listaAtiva.nomeLista}</h1>
            <h3 class="ql-align-center">Autor: ${listaAtiva.autor}</h3>
            <h3 class="ql-align-center">Aluno: ______________________________________________________________</h3><br>
        `);
        let i = 1;
        for(let questao of listaAtiva.questoes) {
            addLista(`<h3>Questão ${i++}:</h3><br>${questao.enunciado}<br>`);
            if (questao.tipo === "Objetiva") {
                addLista(`<p>a) ${questao.alternativas[0].texto}<p>`);
                addLista(`<p>b) ${questao.alternativas[1].texto}<p>`);
                addLista(`<p>c) ${questao.alternativas[2].texto}<p>`);
                addLista(`<p>d) ${questao.alternativas[3].texto}<p>`);
                addLista(`<p>e) ${questao.alternativas[4].texto}<p><br>`);
            }
        }
        addRespostas(
        `
            <h1 class="ql-align-center">${listaAtiva.nomeLista}</h1>
            <h3 class="ql-align-center">Autor: ${listaAtiva.autor}</h3>
            <h3 class="ql-align-center">Folha de Respostas</h3><br>
        `);
        i = 1;
        for(let questao of listaAtiva.questoes) {
            addRespostas(`<h3>Questão ${i++}:</h3><br>`);
            if (questao.tipo === "Objetiva") {
                let alts = questao.alternativas;
                addRespostas(alts[0].correta ? `<strong><p>a) ${alts[0].texto}<p></strong>` : `<p>a) ${alts[0].texto}<p>`);
                addRespostas(alts[1].correta ? `<strong><p>b) ${alts[1].texto}<p></strong>` : `<p>b) ${alts[1].texto}<p>`);
                addRespostas(alts[2].correta ? `<strong><p>c) ${alts[2].texto}<p></strong>` : `<p>c) ${alts[2].texto}<p>`);
                addRespostas(alts[3].correta ? `<strong><p>d) ${alts[3].texto}<p></strong>` : `<p>d) ${alts[3].texto}<p>`);
                addRespostas(alts[4].correta ? `<strong><p>e) ${alts[4].texto}<p></strong>` : `<p>e) ${alts[4].texto}<p><br>`);
            } else {
                addRespostas(`<p>Espelho de correção: ${questao.espelho}<p><br>`);
            }
        }
        
        $scope.imprimeLista = function() {
            document.querySelector("#impressaoRespostas").style.display = "none";
            document.querySelector("#controle").style.display = "none";
            window.print();
            document.querySelector("#impressaoRespostas").style.display = "block";
            document.querySelector("#controle").style.display = "block";
        }

        $scope.imprimeRespostas = function() {
            document.querySelector("#impressaoLista").style.display = "none";
            document.querySelector("#controle").style.display = "none";
            window.print();
            document.querySelector("#impressaoLista").style.display = "block";
            document.querySelector("#controle").style.display = "block";
        }

    });