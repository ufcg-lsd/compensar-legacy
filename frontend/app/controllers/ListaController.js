angular.module('app')
    .controller('ListaController',  function($rootScope,localStorageService,$scope,$http,AuthService, QuestoesService, $mdDialog, Notification,$routeParams)
    {
        $rootScope.printLista = true;
        let listaAtiva = localStorageService.get("listaAtiva");
        console.log(listaAtiva);
        let addHTML = (html) => {document.querySelector("#impressao").innerHTML += html;}
        addHTML(
        `
            <h1 class="ql-align-center">${listaAtiva.nomeLista}</h1>
            <h3 class="ql-align-center">Autor: ${listaAtiva.autor.nome}</h3>
            <h3 class="ql-align-center">Aluno: ______________________________________________________________</h3><br><br>
        `);
        let i = 1;
        for(questao of listaAtiva.questoes) {
            addHTML(`<h3>Questão ${i++}:</h3><br>${questao.enunciado}<br>`);
        }
        
        
    });