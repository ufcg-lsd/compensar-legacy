# Ambiente de Estudo ao Pensamento Computacional 1.0

## Iniciando

Essas instruções vão ajudar você a utilizar o projeto e rodar em sua máquina local para desenvolvimento e testes.

### Pré-Requisitos

É necessário que você execute os comandos contidos no seguinte arquivo para utilizar um banco de dados MongoDB. Pode ser utilizado ainda uma interface MongoDB, como o Robo3T, para melhor interação com os dados.

```
aepc/backend/src/main/resources/import.sql
```

### Executar
Para executar o projeto execute como JavaApplication a seguinte classe:

```
aepc/backend/src/main/java/springboot/AepcApplication.java
```

## Testando a Aplicação

### Segurança 

A segurança nos endpoints está implemetada utilizando HTTP Basic e busca dos usuários no banco de dados utilizando o serviço que implementa UserDetailsService, do Spring Security. Cada usuário possui uma determinada permissão.

Usuários para testes pré-defindos com um tipo de permissão se encontram também no seguinte arquivo:

```
aepc/backend/src/main/resources/import.sql
```

Obs.: Há endpoints específicos para criação de um usuário, e de adição de uma permissão a um usuário (Mais a frente)


Para login e acesso aos endpoints, é utilizado o email e senha de três digitos. No mesmo arquivo se encontram a inserção de alguns usuários exemplo.


### API REST
Para obter uma visualização interativa da API com os endpoints acesse, localmente, o seguinte endereço:

```
http://localhost:8080/swagger-ui.html
```

É possível realizar requisições para testar os endpoints utilizando a interação citada acima, se preferir, existem outras ferramentas (Ex.: Postman).

### Endpoint Base

```
http://localhost:8080/api/...
```

### Testes de Unidade

Para execução dos testes de unidade execute como JunitTest o seguinte pacote:

```
aepc/backend/src/test/java/springboot/aepcinitializr
```

## Construído Com

* [SpringBoot](https://spring.io/projects/spring-boot) - O Framework Web usado
* [Maven](https://maven.apache.org/) - Gerenciamento de dependências
* [Swagger](https://swagger.io/) - Construção de API





