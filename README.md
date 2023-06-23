# AgroTech Fields - Measure Shelter Project

Este projeto é uma **API REST** em *Java / Spring Boot*, desenvolvido individualmente do zero para
apresentação do desafio final no **Curso de Aceleração Java** da
**[Trybe](https://www.betrybe.com/).**

---

## 📜 Sumário

---

## ℹ️ Contexto

A empresa **AgroTech Fields**[^1] é líder de mercado em soluções tecnológicas para o
agronegócio no
Brasil.

Um cliente produtor de soja contratou seus serviços para desenvolver um sistema integrado de
monitoração de dados metereológicos, com o intuito de analisá-los e otimizar a produção dos grãos.

O sistema é composto por estações meterológicas inteligentes chamadas de **_Ilhas_** que são
instaladas dentro da área de cultivo. Elas são autônomas e se comunicam com um servidor backend
através da infraestrutura de rede celular 4G / 5G. As medições coletadas por elas são: temperatura
(ar / solo), umidade (ar / solo), velocidade e direção do vento, irradição solar, intensidade e
precipitação das chuvas. Estes dados são enviados a cada 5 minutos ao servidor. Há também um
**_Satélite_** que envia ao servidor uma fotografia da área a cada 20 dias.

O desafio deste projeto é construir a **API** do servidor backend (*Application Programming
Interface*) usando a arquitetura **REST** (*Representational State Transfer*) em camadas **MSC** (
*Model / Service / Controller*). A API **_Measure Shelter_** implementará os recursos *C.R.U.D.* (
*create/read/update/delete*) para gerenciamento das Ilhas, Medidas, Usuários e Imagens.

Como requisitos do projeto, a API deverá ser desenvolvida em linguagem **Java**, framework
**Spring**, banco de dados em *NoSQL* **Mongo**, gerenciamento de pacotes **Maven** e containers
**Docker**.

A comunicação com o servidor deverá ser autenticada através de *login* do usuário, gerando o *token*
de autorização em **JWT** (*JSON Web Token*). O acesso aos recursos da API deverá ser autorizada por
meio do *token*, conforme o papel do usário.

Uma aplicação frontend (não incluída) consolidará o acesso à API, permitindo o gerenciamento geral
do sistema: cadastramento de usuários e ilhas, registro e ativação das ilhas como usuário para que
elas se autentiquem com o servidor, acesso as medidas geradas pelas ilhas, acesso às fotos geradas
pelo satélite etc.

[^1]: Empresa fictícia.

---

## 🛠️ Tecnologias

* Java 17.0.6 LTS
* Spring 3.1.0 (Boot, Web, Actuator, Validation, Security, Devtools, Test, Data MongoDB)
* Auth0 JWT 4.4.0
* JUnit 5
* Maven 3.9.1
* Docker 24.0.2 e Docker Compose 1.29.2
* Checkstyle 3.3.0

Ambiente de desenvolvimento:

* Ubuntu 22.04 LTS
* Eclipse IDE 4.27.0
* Git / GitHub / CI actions para rodar executar Checkstyle e Maven Tests

---

## 🪵 Recursos

> O detalhamento dos recursos é mostrado na
> seção [Detalhamento dos recursos](#️👁️‍🗨️️-detalhamento-dos-recursos)

### Login

* Autenticar o *usuário* / *senha* para geração do *token*.

### Usuário (*user*)

* Listar todos os usuários;
* Buscar um usuário pelo *id*;
* Criar um usuário do tipo administrativo, comum ou satélite;
* Registrar uma ilha existente, criando um usuário do tipo ilha;
* Atualizar o usuário;
* Atualizar a senha de um usuário do tipo ilha;
* Alternar o tipo do usuário (admistrativo / comum) pelo *id*;
* Alternar a habilitação do usuário (ativado / desativado) pelo *id*;*  * Apagar um usário pelo
  *id*.

#### Ilha (*isle*)

* Listar todas as ilhas;
* Buscar uma ilha pelo *id*;
* Buscar uma ilha pelo *serial number*;
* Atualizar uma ilha pelo *id*;
* Habilitar/desabilitar uma ilha pelo *id*;
* Apagar uma ilha pelo *id*.

#### Medição (*measure*)

* Listar todas as medições;
* Buscar uma medição pelo *id*;
* Buscar todas as medições de uma ilha pelo seu *id*;
* Atualizar uma medição pelo *id*;
* Apagar uma medição pelo *id*.

### Imagens (*image*)

* Listar todas as imagens;
* Buscar uma imagem pelo *id*;
* Buscar uma imgagem pelo *filename.png*;
* Apagar uma imagem pelo *id*.

---

## 🚀 Execução

Você pode clonar este projeto para executá-lo em sua máquina.

```
git clone git@github.com:tiagosathler/measureshelter.git
```

### 📋 Pré-requisitos

Para executar esta aplicação a partir de sua IDE você deverá ter o Java JDK na versão 17 e Mongo na
versão 5. Importe este projeto como *Maven Project* e execute-o como *Java Application* ou como
*Spring Boot App*.

Para executar a partir de um container você deverá ter o Docker na versão 24 e Docker compose na
versão 1.29.

> 💡 **_Dica_**: Para testar a API use o [Postman](https://www.postman.com/). Use o arquivo **Json**
> com a coleção de todos os recurso disponíveis, basta importá-lo:
> [measureshelter.json](https://github.com/tiagosathler/measureshelter/tree/master/postman/measureshelter.json].

### 🐳 Usando Docker

> ⚡ **_Importante_** : Caso seu sistema operacional já tenha um serviço do Mongo em
> execução será necessário desativá-lo antes. O docker compose subirá um container do
> Mongo e provavelmente entrará em conflito com a porta do serviço existente.
> Se tiver dúvidas de como desativar o Mongo consulte
[esta página](https://help.hcltechsw.com/sametime/11.5/admin/starting_and_stopping_mongodb.html).

Para iniciar a aplicação, entre na pasta do projeto e execute no terminal:

```
docker-compose -f docker-compose.yml up -d --build
```

O docker compose orquestrará 3 serviços (containers) em sequência:

1. **mongo_db** - serviço do Mongo na versão 6.0.6 rodando em *mongo_db:27017*
2. **tests** - serviço para testar a aplicação
3. **api** - aplicação rodando em modo produção na porta em *localhost:8080*

Observe que o serviço **_api_** só entrará em operação se o **_mongo_db_** estiver
saudável e **_tests_** concluir com êxito e sem erros. Isto pode demorar um pouco, tenha
paciência
!!! ☕

> A aplicação iniciará com o usuário do tipo administrator **root** e senha **123456** no banco
> de dados. Use-o para fazer *login* através do Postman. O token gerado estará automaticamente
> pronto para usar nas demais requisições (na aba *Authorization* / *Type: Bearer token* ) do
> Postman.

> 💡 Dica: se você tem o [Docker Desktop](https://www.docker.com/products/docker-desktop/)
> instalado em seu sistema você poderá visualizar facilmente os logs dos serviços.
> Mas se preferir, poderá visualizá-los a partir do terminal usando comando: `docker container
logs <nome_do_container> -f`

Para interromper os containers execute o comando:

```
docker-compose down
```

## ⚙️ Executando os testes

A maioria dos testes desenvolvidos são do tipo integração. É possível executá-los
manualmente a partir de sua IDE. Não é necessário ter o Mongo instalado.

Para executar os testes a partir do terminal:

```
mvn test
```

### 🔩 Análise dos testes:

Por serem do tipo integração, os testes simulam a interação com a API, fazendo as chamadas aos
recursos e verificando as respostas. A técnica utiliza o **MockMvc** do Spring, que sobe uma
aplicação isolada da aplicação real. Usa-se também o **Testcontainer** para criar um serviço
temporário do
Mongo. Outros testes de integração foram feitos para verificar a autorização dos usuários aos
recursos restritos. Por exemplo: um usuário autenticado do tipo ilha não pode acessar o recurso de
criar
novos usuários, mesmo com um token válido. Para aumentar a cobertura dos testes, foram feitos
também testes unitários das classes de domínio e DTO.

> 🧪 *Este projeto alcançou mais 99% de cobertura!!*

---

## 👁️‍🗨️️ Detalhamento dos recursos

### 1. Login

<br>

#### 1.1. Efetuar login

```http
POST /login
```

* Permissões: sem restrições

<details>
  <summary>
    Faz o login com usuário e senha e recebe um token de autenticação
  </summary><br>

* Request (application/json)
    * Body
      ```json
      {
        "username": "root",
        "password": "123456"
      }
      ```
      Restrições:
        * *username*: não pode ser nulo ou vazio;
        * *password*: não pode ser nulo ou vazio;
        * verifica se o usuário existe no banco de dados

* Response (application/json)
    * Status: `200 Ok`
    * Body:
      ```json
      {
        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJBZ3JvX1RlY2hmaWVsZHMiLCJpYXQiOjE2ODc1MjU2MTQsInN1YiI6InJvb3QiLCJleHAiOjE2ODc2MTIwMTR9.o-5Caw89HFaTHZag6WBjGQeOpNzRFRK8mJUpp9Pg8Es"
      }
      ```

</details>

<br>

### 2. User

Gerenciamento dos usuários.

<br>

#### 2.1. Criar usuário comum:

```http
POST /user
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Adiciona um usuário e senha ao banco de dados e recebe os dados do usuário inserido.
  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "username": "someuser",
        "password": "somepassword"
      }
      ```
      Restrições:
        * *username*: não pode ser nulo ou vazio, deve ter entre 4 a 10 caracteres, não pode ter
          o padrão de um número serial;
        * *password*: não pode ser nulo ou vazio, deve ter entre 6 a 12 caracteres;
        * verifica se já existe algum usuário com o mesmo *username*.

    * Response (application/json)
        * Status: `201 Created`
        * Body:
          ```json
          {
            "username": "someuser",
            "id": "64959dc4a55e5711ae4ff473",
            "role": "ROLE_USER",
            "accountNonExpired": true,
            "enable": true,
            "credentialsNonExpired": true,
            "accountNonLocked": true
          }
          ```

</details>

<br>
<br>

#### 2.2. Criar usuário administrador:

```http
POST /user?isAdmin=true
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Adiciona um usuário e senha do tipo ROLE_ADMIN ao banco de dados.
  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "username": "someadmin",
        "password": "somepassword"
      }
      ```
      Restrições:
        * *username*: não pode ser nulo ou vazio, deve ter entre 4 a 10 caracteres, não pode ter
          o padrão de um número serial;
        * *password*: não pode ser nulo ou vazio, deve ter entre 6 a 12 caracteres;
        * verifica se já existe algum usuário com o mesmo *username*.

    * Response (application/json)
        * Status: `201 Created`
        * Body:
          ```json
          {
            "username": "someadmin",
            "id": "64959ebba55e5711ae4ff474",
            "role": "ROLE_ADMIN",
            "accountNonExpired": true,
            "enable": true,
            "credentialsNonExpired": true,
            "accountNonLocked": true
          }
          ```

</details>

<br>
<br>

#### 2.3. Criar um usuário Satélite:

```http
POST /user?isSat=true
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Adiciona um usuário e senha do tipo ROLE_SAT (satélite) ao banco de dados.
  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "username": "someasat",
        "password": "somepassword"
      }
      ```
      Restrições:
        * *username*: não pode ser nulo ou vazio, deve ter entre 4 a 10 caracteres, não pode ter
          o padrão de um número serial;
        * *password*: não pode ser nulo ou vazio, deve ter entre 6 a 12 caracteres;
        * verifica se já existe algum usuário com o mesmo *username*.

    * Response (application/json)
        * Status: `201 Created`
        * Body:
          ```json
          {
            "username": "somesat",
            "id": "6495a066a55e5711ae4ff475",
            "role": "ROLE_SAT",
            "accountNonExpired": true,
            "enable": true,
            "credentialsNonExpired": true,
            "accountNonLocked": true
          }
          ```

</details>

<br>
<br>

#### 2.4. Criar usuário registrando uma Ilha existente:

```http
GET /user
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Adiciona um usuário e senha do tipo ROLE_ISLE ao banco de dados (registrando ilha existente).
  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "username": "ISLE000001",
        "password": "somepassword"
      }
      ```
      Restrições:
        * *username*: não pode ser nulo ou vazio, deve ter 10 caracteres seguindo o padrão de
          um número serial (somente letras maiúsculas e números);
        * *password*: não pode ser nulo ou vazio, deve ter entre 6 a 12 caracteres;
        * verifica se a ilha existe no banco de dados;
        * verifica se a ilha já está cadastrada como usuário.

    * Response (application/json)
        * Status: `201 Created`
        * Body:
          ```json
          {
            "username": "ISLE000001",
            "id": "6495a066a55e5711ae4ff475",
            "role": "ROLE_ISLE",
            "accountNonExpired": true,
            "enable": true,
            "credentialsNonExpired": true,
            "accountNonLocked": true
          }
          ```

</details>

<br>
<br>

#### 2.5. Buscar todos usuários:

```http
GET /user
```

* Permissões: **ROLE_ADMIN** e **ROLE_USER**

<details>
  <summary>
    Lista todos os usuários cadastrados no banco de dados.
  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

    * Response (application/json)
        * Status: `200 Ok`
        * Body:
          ```json
          [
            {
              "username": "somesat",
              "id": "6495a066a55e5711ae4ff475",
              "role": "ROLE_SAT",
              "accountNonExpired": true,
              "enable": true,
              "credentialsNonExpired": true,
              "accountNonLocked": true
            },
            {
              "username": "ISLE000001",
              "id": "6495a066a55e5711ae4ff475",
              "role": "ROLE_ISLE",
              "accountNonExpired": true,
              "enable": true,
              "credentialsNonExpired": true,
              "accountNonLocked": true
           }      
          ]
          ```

</details>

<br>
<br>

#### 2.6. Buscar usuário pelo **id**:

```http
GET /user/{id}
```

* Permissões: **ROLE_ADMIN** e **ROLE_USER**

<details>
  <summary>
    Busca um usuário pelo seu <i>id</i>.
  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

    * Response (application/json)
        * Status: `200 Ok`
        * Body:
          ```json
          {
            "username": "ISLE000001",
            "id": "6495a066a55e5711ae4ff475",
            "role": "ROLE_ISLE",
            "accountNonExpired": true,
            "enable": true,
            "credentialsNonExpired": true,
            "accountNonLocked": true
          }
          ```

</details>

<br>
<br>

#### 2.7. Atualizar o usuário autenticado / autorizado:

```http
PUT /user
```

* Permissões: **ROLE_ADMIN** e **ROLE_USER**

<details>
  <summary>
    Atualiza usuário e senha do atual usuário autenticado / autorizado, isto é, o 
usuário do token.
  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "username": "changedusername",
        "password": "changedpassword"
      }
      ```
      Restrições:
        * *username*: não pode ser nulo ou vazio, deve ter entre 4 a 10 caracteres, não pode ter
          o padrão de um número serial;
        * *password*: não pode ser nulo ou vazio, deve ter entre 6 a 12 caracteres;
        * verifica se já existe algum usuário com o mesmo *username*.

    * Response (application/json)
        * Status: `200 Ok`
        * Body:
          ```json
          {
            "username": "changedusername",
            "id": "6495a066a55e5711ae4ff475",
            "role": "ROLE_SAT",
            "accountNonExpired": true,
            "enable": true,
            "credentialsNonExpired": true,
            "accountNonLocked": true
          }
          ```

</details>

<br>
<br>

#### 2.8. Atualizar a senha de um usuário Ilha:

```http
PUT /user/isle
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Atualiza somente a senha de uma ilha já registrada.
  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "username": "ISLE000001",
        "password": "changedpassword"
      }
      ```
      Restrições:
        * *username*: não pode ser nulo ou vazio, deve ter 10 caracteres seguindo o padrão de
          um número serial (somente letras maiúsculas e números);
        * *password*: não pode ser nulo ou vazio, deve ter entre 6 a 12 caracteres;
        * verifica se a ilha existe no banco de dados;
        * não altera o *username*, apenas *password*;

    * Response (application/json)
        * Status: `200 Ok`
        * Body:
          ```json
          {
            "username": "ISLE000001",
            "id": "6495a066a55e5711ae4ff475",
            "role": "ROLE_SAT",
            "accountNonExpired": true,
            "enable": true,
            "credentialsNonExpired": true,
            "accountNonLocked": true
          }
          ```

</details>

<br>
<br>

#### 2.9. Alterna o tipo de usuário pelo *id*:

```http
PATCH /user/{id}/toggle/role
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Alterna (<i>toggle</i>) entre <strong>ROLE_ADMIN</strong> e <strong>ROLE_USER</strong> de um usuário 
pelo seu <i>id</i>.
  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

    * Response (application/json)
        * Status: `200 Ok`
        * Body:
          ```json
          {
            "role": "ROLE_USER"
          }
          ```
          ou
          ```json
          {
            "role": "ROLE_ADMIN"
          }
          ```

</details>

<br>
<br>

#### 2.10. Alterna a habilitação do usuário pelo *id*:

```http
PATCH /user/{id}/toggle/enable
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Alterna (<i>toggle</i>) a habilitação (<i>isEnable</i>) de um usuário pelo seu <i>id</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

    * Response (application/json)
        * Status: `200 Ok`
        * Body:
          ```json
          {
            "isEnable": false
          }
          ```
          ou
          ```json
          {
            "isEnabled": true
          }
          ```

</details>

<br>
<br>

#### 2.11. Apaga o usuário pelo *id*:

```http
DELETE /user/{id}
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Apaga (<i>delete</i>) um usuário pelo seu <i>id</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

    * Response (application/json)
        * Status: `204 NoContent`

</details>

<br>
<br>

### 3. Isle

Gerenciamento das ilhas.

<br>

#### 3.1. Criar ilha

```http
POST /isle
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Adiciona uma ilha (<i>isle</i>) ao banco de dados.

  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "serialNumber": "ISLE000001",
        "latitude": -20.23,
        "longitude": 20.00,
        "altitude": 1000,
        "samplingInterval": 10
      }
      ```
      Restrições:
        * *serialNumber*: não pode ser nulo ou vazio, deve ter 10 caracteres seguindo o padrão
          de
          um número serial (somente letras maiúsculas e números);
        * *latitude*: não pode ser nulo ou vazio, um número decimal de -90 a 90 (excluding);
        * *longitude*: não pode ser nulo ou vazio, um número decimal de -180 a 180 (excluding);
        * *altitude*: não pode ser nulo ou vazio, um número decimal positivo;
        * *samplingInterval*: opcional, um número inteiro entre 1 a 3600 (valor default 5
          minutos);
        * verifica se já existe alguma ilha com o mesmo *serialNumber*.

* Response (application/json)
    * Status: `201 Created`
    * Body:
      ```json
      {
        "id": "6495b977a55e5711ae4ff476",
        "serialNumber": "ISLE000001",
        "latitude": -20.23,
        "longitude": 20.00,
        "altitude": 1000,
        "isItWorking": true,
        "samplingInterval": 10
      }
      ```

</details>

<br>
<br>

#### 3.2. Buscar todas as ilhas:

```http
GET /isle/serial/{serialNumber}
```

* Permissões: **ROLE_ADMIN**, **ROLE_USER** e **ROLE_ISLE**

<details>
  <summary>
    Lista todas as ilhas (<i>isles</i>) cadastradas no banco de dados.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

* Response (application/json)
    * Status: `200 Ok`
    * Body:
      ```json
      [
        {
          "id": "647912bef16f7379e1d7a66f",
          "serialNumber": "ISLE000001",
          "latitude": -20.85,
          "longitude": 25.55,
          "altitude": 1000,
          "isItWorking": true,
          "samplingInterval": 10
        },
        {
          "id": "6487762ce3b261555525a1f0",
          "serialNumber": "ISLE000002",
          "latitude": -22.15,
          "longitude": 26.66,
          "altitude": 656.23,
          "isItWorking": true,
          "samplingInterval": 5
        }
      ]
      ```

</details>

<br>
<br>

#### 3.3. Buscar ilha pelo **id**:

```http
GET /isle/{id}
```

* Permissões: **ROLE_ADMIN**, **ROLE_USER** e **ROLE_ISLE**

<details>
  <summary>
    Busca uma ilha (<i>isle</i>) pelo seu <i>id</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

* Response (application/json)
    * Status: `200 Ok`
    * Body:
      ```json
      {
        "id": "647912bef16f7379e1d7a66f",
        "serialNumber": "ISLE000001",
        "latitude": -20.85,
        "longitude": 25.55,
        "altitude": 1000,
        "isItWorking": true,
        "samplingInterval": 10
      }
      ```

</details>

<br>
<br>

#### 3.4. Buscar ilha pelo **serialNumber**:

```http
GET /isle/serial/{serialNumber}
```

* Permissões: **ROLE_ADMIN**, **ROLE_USER** e **ROLE_ISLE**

<details>
  <summary>
    Busca uma ilha (<i>isle</i>) pelo seu <i>serialNumber</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

* Response (application/json)
    * Status: `200 Ok`
    * Body:
      ```json
      {
        "id": "647912bef16f7379e1d7a66f",
        "serialNumber": "ISLE000001",
        "latitude": -20.85,
        "longitude": 25.55,
        "altitude": 1000,
        "isItWorking": true,
        "samplingInterval": 10
      }
      ```

</details>

<br>
<br>

#### 3.5. Atualizar uma ilha pelo **id**

```http
PUT /isle/{id}
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Atualiza uma ilha (<i>isle</i>) pelo seu <i>id</i> ao banco de dados.

  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "serialNumber": "ISLE000005",
        "latitude": 20.23,
        "longitude": -20.00,
        "altitude": 500,
        "samplingInterval": 2
      }
      ```
      Restrições:
        * *serialNumber*: não pode ser nulo ou vazio, deve ter 10 caracteres seguindo o padrão
          de
          um número serial (somente letras maiúsculas e números);
        * *latitude*: não pode ser nulo ou vazio, um número decimal de -90 a 90 (excluding);
        * *longitude*: não pode ser nulo ou vazio, um número decimal de -180 a 180 (excluding);
        * *altitude*: não pode ser nulo ou vazio, um número decimal positivo;
        * *samplingInterval*: opcional, um número inteiro entre 1 a 3600 (valor default 5
          minutos);
        * verifica se já existe alguma ilha com o mesmo *serialNumber*;
        * se a ilha já estiver cadastrada como usuário então atualiza também o *username* com o
          novo *serialNumber*.

* Response (application/json)
    * Status: `202 Accepted`
    * Body:
      ```json
      {
        "id": "6495b977a55e5711ae4ff476",
        "serialNumber": "ISLE000005",
        "latitude": 20.23,
        "longitude": 20.00,
        "altitude": 500,
        "isItWorking": true,
        "samplingInterval": 2
      }
      ```

</details>

<br>
<br>

#### 3.6. Apaga a ilha pelo **id**:

```http
DELETE /isle/{id}
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Apaga (<i>delete</i>) uma ilha pelo seu <i>id</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

* Response
    * Status: `204 NoContent`

</details>

<br>
<br>

### 4. Measure

Gerenciamento das medições.

<br>

#### 4.1. Criar medição

```http
POST /measure
```

* Permissões: **ROLE_ISLE**

<details>
  <summary>
    Adiciona uma medição (<i>measure</i>) ao banco de dados por meio de um usuário ilha 
autenticado / autorizado.

  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "airTemp": 30,
        "gndTemp": 30.66,
        "windSpeed": 4.21,
        "windDirection": 200,
        "irradiance": 1060.54,
        "pressure": 1050.24,
        "airHumidity": 40.23,
        "gndHumidity": 66.77,
        "precipitation": 0,
        "rainIntensity": 0
      }
      ```
      Restrições:
        * *airTemp*: temperatura do ar em ºC, não pode ser nulo ou vazio, um número decimal de -20
          a 50;
        * *gndTemp*: temperatura do solo em ºC, não pode ser nulo ou vazio, um número decimal de
          -30 a 60;
        * *windSpeed*: velocidade do vento em m/s, não pode ser nulo ou vazio, um número decimal de
          0 a 30;
        * *windDirection*: direção do vento em º, não pode ser nulo ou vazio, um
          número decimal de 0 a 360 (excluding);
        * *irradiance*: irradiação solar em Wh/m², não pode ser nulo ou vazio, um
          número decimal de 0 a 1500;
        * *pressure*: pressão atmosférica em hPa, não pode ser nulo ou vazio, um
          número decimal de 100 a 1200;
        * *airHumidity*: umidade relativa do ar em %, não pode ser nulo ou vazio, um
          número decimal de 0 a 100;
        * *gndHumidity*: umidade relativa do solo em %, não pode ser nulo ou vazio, um
          número decimal de 0 a 100;
        * *precipitation*: coluna de precipitação de chuva em mm, não pode ser nulo ou vazio, um
          número decimal de 0 a 1000;
        * *rainIntensity*: intensidade da chuva em mm/h, não pode ser nulo ou vazio, um
          número decimal de 0 a 1000;

* Response (application/json)
    * Status: `201 Created`
    * Body:
      ```json
      {
        "id": "6495c350a55e5711ae4ff477",
        "isleId": "647912bef16f7379e1d7a66f",  
        "airTemp": 30,
        "gndTemp": 30.66,
        "windSpeed": 4.21,
        "windDirection": 200,
        "irradiance": 1060.54,
        "pressure": 1050.24,
        "airHumidity": 40.23,
        "gndHumidity": 66.77,
        "precipitation": 0,
        "rainIntensity": 0,
        "timestamp": "2023-06-23T13:07:44.71164777"
      }
      ```

</details>

<br>
<br>

#### 4.2. Buscar todas as medições

```http
GET /measure
```

* Permissões: **ROLE_ADMIN**, **ROLE_USER** e **ROLE_ISLE**

<details>
  <summary>
    Lista todas as medições (<i>measures</i>) do banco de dados.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`


* Response (application/json)
    * Status: `200 Ok`
    * Body:
      ```json
      [
        {
          "id": "6495c350a55e5711ae4ff477",
          "isleId": "647912bef16f7379e1d7a66f",  
          "airTemp": 30,
          "gndTemp": 30.66,
          "windSpeed": 4.21,
          "windDirection": 200,
          "irradiance": 1060.54,
          "pressure": 1050.24,
          "airHumidity": 40.23,
          "gndHumidity": 66.77,
          "precipitation": 0,
          "rainIntensity": 0,
          "timestamp": "2023-06-23T13:07:44.71164777"
        },
        {
          "id": "6495c492a55e5711ae4ff478",
          "isleId": "647912bef16f7379e1d7a66f",  
          "airTemp": 33,
          "gndTemp": 37.66,
          "windSpeed": 2.1,
          "windDirection": 190,
          "irradiance": 860.54,
          "pressure": 1060.24,
          "airHumidity": 45.2,
          "gndHumidity": 56.7,
          "precipitation": 2,
          "rainIntensity": 1,
          "timestamp": "2023-06-23T13:13:06.840558658"
        }
      ]
      ```

</details>

<br>
<br>

#### 4.3. Buscar todas as medições de uma ilha pelo **id**

```http
GET /measure/isle/{id}
```

* Permissões: **ROLE_ADMIN**, **ROLE_USER** e **ROLE_ISLE**

<details>
  <summary>
    Lista todas as medições (<i>measures</i>) do banco de dados de uma ilha pelo seu <i>id</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`


* Response (application/json)
    * Status: `200 Ok`
    * Body:
      ```json
      [
        {
          "id": "6495c350a55e5711ae4ff477",
          "isleId": "647912bef16f7379e1d7a66f",  
          "airTemp": 30,
          "gndTemp": 30.66,
          "windSpeed": 4.21,
          "windDirection": 200,
          "irradiance": 1060.54,
          "pressure": 1050.24,
          "airHumidity": 40.23,
          "gndHumidity": 66.77,
          "precipitation": 0,
          "rainIntensity": 0,
          "timestamp": "2023-06-23T13:07:44.71164777"
        },
        {
          "id": "6495c492a55e5711ae4ff478",
          "isleId": "647912bef16f7379e1d7a66f",  
          "airTemp": 33,
          "gndTemp": 37.66,
          "windSpeed": 2.1,
          "windDirection": 190,
          "irradiance": 860.54,
          "pressure": 1060.24,
          "airHumidity": 45.2,
          "gndHumidity": 56.7,
          "precipitation": 2,
          "rainIntensity": 1,
          "timestamp": "2023-06-23T13:13:06.840558658"
        }
      ]
      ```

</details>

<br>
<br>

#### 4.4. Buscar por uma medição pelo **id**

```http
GET /measure/{id}
```

* Permissões: **ROLE_ADMIN**, **ROLE_USER** e **ROLE_ISLE**

<details>
  <summary>
    Busca uma medição (<i>measure</i>) no banco de dados seu <i>id</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`


* Response (application/json)
    * Status: `200 Ok`
    * Body:
      ```json
      {
        "id": "6495c350a55e5711ae4ff477",
        "isleId": "647912bef16f7379e1d7a66f",  
        "airTemp": 30,
        "gndTemp": 30.66,
        "windSpeed": 4.21,
        "windDirection": 200,
        "irradiance": 1060.54,
        "pressure": 1050.24,
        "airHumidity": 40.23,
        "gndHumidity": 66.77,
        "precipitation": 0,
        "rainIntensity": 0,
        "timestamp": "2023-06-23T13:07:44.71164777"
      }
      ```

</details>

<br>
<br>

#### 4.5. Atualiza uma medição pelo **id**

```http
PUT /measure/{id}
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Atualiza uma medição (<i>measure</i>) pelo seu <i>id</i> sem alterar a ilha que o gerou.

  </summary><br>

* Request (application/json)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body
      ```json
      {
        "airTemp": 40,
        "gndTemp": 20.66,
        "windSpeed": 5.21,
        "windDirection": 100,
        "irradiance": 1090.54,
        "pressure": 1060.24,
        "airHumidity": 50.23,
        "gndHumidity": 76.77,
        "precipitation": 6,
        "rainIntensity": 2
      }
      ```
      Restrições:
        * *airTemp*: temperatura do ar em ºC, não pode ser nulo ou vazio, um número decimal de -20
          a 50;
        * *gndTemp*: temperatura do solo em ºC, não pode ser nulo ou vazio, um número decimal de
          -30 a 60;
        * *windSpeed*: velocidade do vento em m/s, não pode ser nulo ou vazio, um número decimal de
          0 a 30;
        * *windDirection*: direção do vento em º, não pode ser nulo ou vazio, um
          número decimal de 0 a 360 (excluding);
        * *irradiance*: irradiação solar em Wh/m², não pode ser nulo ou vazio, um
          número decimal de 0 a 1500;
        * *pressure*: pressão atmosférica em hPa, não pode ser nulo ou vazio, um
          número decimal de 100 a 1200;
        * *airHumidity*: umidade relativa do ar em %, não pode ser nulo ou vazio, um
          número decimal de 0 a 100;
        * *gndHumidity*: umidade relativa do solo em %, não pode ser nulo ou vazio, um
          número decimal de 0 a 100;
        * *precipitation*: coluna de precipitação de chuva em mm, não pode ser nulo ou vazio, um
          número decimal de 0 a 1000;
        * *rainIntensity*: intensidade da chuva em mm/h, não pode ser nulo ou vazio, um
          número decimal de 0 a 1000;

* Response (application/json)
    * Status: `200 Ok`
    * Body:
      ```json
      {
        "id": "6495c350a55e5711ae4ff477",
        "isleId": "647912bef16f7379e1d7a66f",  
        "airTemp": 40,
        "gndTemp": 20.66,
        "windSpeed": 5.21,
        "windDirection": 100,
        "irradiance": 1090.54,
        "pressure": 1060.24,
        "airHumidity": 50.23,
        "gndHumidity": 76.77,
        "precipitation": 6,
        "rainIntensity": 2,
        "timestamp": "2023-06-23T13:07:44.71164777"
      }
      ```

</details>

<br>
<br>

#### 4.6. Apaga uma medição pelo **id**

```http
DELETE /measure/{id}
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Apaga (<i>delete</i>) uma medição (<i>measure</i>) pelo seu <i>id</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

* Response
    * Status: `204 NoContent`

</details>

<br>

### 5. Image

Gerenciamento das imagens geradas por satélite.

<br>

#### 5.1. Criar imagem

```http
POST /image
```

* Permissões: **ROLE_SAT**

<details>
  <summary>
    Adiciona uma imagem (<i>image</i>) ao banco de dados por meio de um usuário satélite 
autenticado / autorizado.

  </summary><br>

* Request (Multipart/form-data)
    * Headers
        * Authorization: `"Bearer <token>"`
    * Body:
        * Form-data Multipart file: **filename.png**
            * Restrições:
                * *filename*: deve ser composto apenas por palavras, dígitos, hífens ou underlines,
                  sem caracteres especiais e espaços. Extensão do arquivo tem que ser **png**.

* Response (application/json)
    * Status: `201 Created`
    * Body:
      ```json
      {
        "id": "6495c9b6a55e5711ae4ff479",
        "name": "filename.png",
        "timestamp": "2023-06-23T13:35:02.8998137"
      }
      ```

</details>

<br>
<br>

#### 5.2. Buscar todas as imagens

```http
GET /image
```

* Permissões: **ROLE_ADMIN** e **ROLE_USER**

<details>
  <summary>
    Lista todas as imagens (<i>images</i>) do banco de dados (apenas informações)

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

* Response (application/json)
    * Status: `200 Ok`
    * Body:
      ```json
      [
        {
          "id": "6495c9b6a55e5711ae4ff479",
          "name": "filename.png",
          "timestamp": "2023-06-23T13:35:02.8998137"
        },
        {
          "id": "6495c9b6a55e5711ae4ff479",
          "name": "image01.png",
          "timestamp": "2023-06-23T13:35:02.8998137"
        }
      ]
      ```

</details>

<br>
<br>

#### 5.3. Buscar uma imagem pelo **filename**

```http
GET /image/name/{filename}
```

* Permissões: **ROLE_ADMIN** e **ROLE_USER**

<details>
  <summary>
    Busca uma imagem (<i>images</i>) do banco de dados pelo seu <i>filename</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

* Response (image/png):
    * Status: `200 Ok` (retorna o conteúdo da imagem)

</details>

<br>
<br>

#### 5.4. Buscar uma imagem pelo **id**

```http
GET /image/id/{id}
```

* Permissões: **ROLE_ADMIN** e **ROLE_USER**

<details>
  <summary>
    Busca uma imagem (<i>images</i>) do banco de dados pelo seu <i>id</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

* Response (image/png):
    * Status: `200 Ok` (retorna o conteúdo da imagem)

</details>

<br>
<br>

#### 5.5. Apagar uma imagem pelo **id**

```http
DELETE /image/{id}
```

* Permissões: **ROLE_ADMIN**

<details>
  <summary>
    Apaga (<i>delete</i>) uma imagem (<i>images</i>) do banco de dados pelo seu <i>id</i>.

  </summary><br>

* Request
    * Headers
        * Authorization: `"Bearer <token>"`

* Response:
    * Status: `204 NoContent`

</details>

<br>

---

## ⌛ Duração

Este projeto iniciou em 28/05/2023 e finalizou em 25/06/2023.

Tempo total mensurado pelo [Wakatime](https://wakatime.com/) neste projeto:

[![wakatime](https://wakatime.com/badge/github/tiagosathler/measureshelter.svg)](https://wakatime.com/badge/github/tiagosathler/measureshelter)

---

## ✒️ Autor

***Tiago Heringer Siqueira Sathler*** foi aluno do **Curso de Desenvolvimento Web** oferecido pela
Trybe, ingressou na turma 14-A em junho de 2021 e formou em setembro de 2022 pela turma 15-B.

[LinkedIn](https://www.linkedin.com/in/tiagosathler/)

---

## 🎁 Expressões de gratidão

Agradeço a Trybe pela oportunidade de estudar ***Curso de Aceleração em Java***,
na qual pude desenvolver 13 projetos agregadores (avaliados) e mais 47 exercícios durante os meses
de Abril a Maio de 2023.

---

> Template de referência para este documento
> oferecido [aqui](https://gist.github.com/lohhans/f8da0b147550df3f96914d3797e9fb89)