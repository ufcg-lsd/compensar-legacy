# Ambiente de Estudo ao Pensamento Computacional 1.0

## Iniciando

Essas instruções vão ajudar você a utilizar o projeto e rodar em sua máquina local para desenvolvimento e testes.

### Pré-Requisitos

É necessário que você execute os comandos SQL contidos no seguinte arquivo para utilizar um banco de dados MySql na aplicação utilizando um servidor (Ex.:XAMPP).

```
aepc/backend/aepc-db.sql
```

### Executar
Para executar o projeto execute como JavaApplication a seguinte classe:

```
aepc/backend/src/main/java/springboot/AepcApplication.java
```

## Testando a Aplicação

### API REST
Para obter uma visualização interativa da API acesse, localmente, o seguinte endereço:

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

## Segurança 

O username e password para acessar os endpoints (Momentâneamente) são:

```
login: aepc
senha: 123

```


## Construído Com

* [SpringBoot](https://spring.io/projects/spring-boot) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [SWAGGER](https://swagger.io/) - Construção de API





