-- [Permanente]

--Cria BD e usuário de acesso.
create database db_aepc;
create user 'marcelovitorino'@'localhost' identified by 'marcelopassword';
grant all on db_aepc.* to 'marcelovitorino'@'localhost';

create database db_aepc_test;
grant all on db_aepc_test.* to 'marcelovitorino'@'localhost';


-- Cria Enum de Competencias
ALTER TABLE questao_competencia MODIFY competencia ENUM('coleta','paralelização','analise','representacao','decomposicao','abstracao','simulacao','automacao');


-- Cria Enum de Permissao

-- ALL -> Representa o papel de um gerenciador do sistema
-- RESPONDE_QUESTOES -> Representa o papel de um aluno
-- CRIA_QUESTOES -> Representa o papel de um professor
-- READ_ONLY -> Representa o papel de um leitor da aplicação, não necessariamente cadastrado
ALTER TABLE usuario_permissao MODIFY permissao ENUM('ALL','RESPONDE_QUESTOES','CRIA_QUESTOES','READ_ONLY');


-- [Exemplos] 

-- Cria usuarios para testes.
insert into usuario (id, nome,nome_instituicao,email, senha, ativo) values (1, 'marcelo', 'UFCG','marcelo@gmail.com', '$2a$10$ARppQC0FRWaGP4pnZqYbpuVyYOWIp4q1r2ViT3PGYK6BafD5PXFiS', true);
insert into usuario (id, nome,nome_instituicao,email, senha, ativo) values (2, 'erick', 'UFCG','erick@gmail.com', '$2a$10$ARppQC0FRWaGP4pnZqYbpuVyYOWIp4q1r2ViT3PGYK6BafD5PXFiS', true);
insert into usuario (id, nome,nome_instituicao,email, senha, ativo) values (3, 'prof', 'UFCG','prof@gmail.com', '$2a$10$ARppQC0FRWaGP4pnZqYbpuVyYOWIp4q1r2ViT3PGYK6BafD5PXFiS', true);


-- Adiciona permissão para um usuario.
insert into usuario_permissao (email, permissao) values ('marcelo@gmail.com', 'ALL');
insert into usuario_permissao (email, permissao) values ('erick@gmail.com', 'RESPONDE_QUESTOES');
insert into usuario_permissao (email, permissao) values ('prof@gmail.com', 'CRIA_QUESTOES');


-- Adição de questões

insert into questao (autor,enunciado,fonte,imagem,tipo) values ('marcelo@gmail.com','Quanto é o triplo do quadrado de 10','PISA',null,'subj');
insert into quest_subj (autor,enunciado,fonte,imagem,tipo) values ('marcelo@gmail.com','Quanto é o triplo do quadrado de 10','PISA',null,'subj');






















