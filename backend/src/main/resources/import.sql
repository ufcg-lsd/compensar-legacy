-- [Permanente]

--Cria BD e usuário de acesso.
create database db_aepc;
create user 'marcelovitorino'@'localhost' identified by 'marcelopassword';
grant all on db_aepc.* to 'marcelovitorino'@'localhost';

-- Cria Enum de Competencias
ALTER TABLE questao_competencia MODIFY competencia ENUM('coleta','paralelização','analise','representacao','decomposicao','abstracao','simulacao','automacao');
-- Cria Enum de Permissao
ALTER TABLE usuario_permissao MODIFY permissao ENUM('ALL','RESPONDE_QUESTOES','CRIA_QUESTOES');


-- [Exemplos] 

-- Cria usuarios para testes.
insert into Usuario (id, nome,nome_instituicao,email, senha, ativo) values (1, 'marcelo', 'UFCG','marcelo@gmail.com', '$2a$10$ARppQC0FRWaGP4pnZqYbpuVyYOWIp4q1r2ViT3PGYK6BafD5PXFiS', true);
insert into Usuario (id, nome,nome_instituicao,email, senha, ativo) values (2, 'erick', 'UFCG','erick@gmail.com', '$2a$10$ARppQC0FRWaGP4pnZqYbpuVyYOWIp4q1r2ViT3PGYK6BafD5PXFiS', true);
insert into Usuario (id, nome,nome_instituicao,email, senha, ativo) values (3, 'prof', 'UFCG','prof@gmail.com', '$2a$10$ARppQC0FRWaGP4pnZqYbpuVyYOWIp4q1r2ViT3PGYK6BafD5PXFiS', true);

-- Adiciona permissão para um usuario.
insert into Usuario_Permissao (email, permissao) values ('marcelo@gmail.com', 'ALL');
insert into Usuario_Permissao (email, permissao) values ('erick@gmail.com', 'RESPONDE_QUESTOES');
insert into Usuario_Permissao (email, permissao) values ('prof@gmail.com', 'CRIA_QUESTOES');

