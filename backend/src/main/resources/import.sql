-- [Permanente]

-- BD é criado automaticamente nos arquivos de execução.


-- [Exemplos para popular] 

-- Cria usuarios para testes.

db.usuario.insert({
    "_id" : "marcelo@gmail.com",
    "nome" : "marcelo",
    "nomeInstituicao" : "ufcg",
    "senha" : "$2a$10$88JUBC8rMyoFe0M.kzifweoZsnrKemsdPXbKYUGn53V4KlbxAwwZ6",
    "email" : "marcelo@gmail.com",
    "ativo" : true
})

db.usuario.insert({
    "_id" : "erick@gmail.com",
    "nome" : "erick",
    "nomeInstituicao" : "ufcg",
    "senha" : "$2a$10$88JUBC8rMyoFe0M.kzifweoZsnrKemsdPXbKYUGn53V4KlbxAwwZ6",
    "email" : "erick@gmail.com",
    "ativo" : true
})

db.usuario.insert({
    "_id" : "prof@gmail.com",
    "nome" : "prof",
    "nomeInstituicao" : "ufcg",
    "senha" : "$2a$10$88JUBC8rMyoFe0M.kzifweoZsnrKemsdPXbKYUGn53V4KlbxAwwZ6",
    "email" : "prof@gmail.com",
    "ativo" : true
})


-- Tipos de permissões aceitas
------
-- ALL -> Representa o papel de um gerenciador do sistema
-- RESPONDE_QUESTOES -> Representa o papel de um aluno
-- CRIA_QUESTOES -> Representa o papel de um professor
-- READ_ONLY -> Representa o papel de um leitor da aplicação, não necessariamente cadastrado

db.usuario_permissao.insert({
    "_id" : "marcelo@gmail.com",
    "email" : "marcelo@gmail.com",
    "permissao" : "ALL"
})

db.usuario_permissao.insert({
    "_id" : "erick@gmail.com",
    "email" : "erick@gmail.com",
    "permissao" : "RESPONDE_QUESTOES"
})

db.usuario_permissao.insert({
    "_id" : "prof@gmail.com",
    "email" : "prof@gmail.com",
    "permissao" : "CRIA_QUESTOES"
})























