# Oscar API

API REST desenvolvida em **Spring Boot** para gerenciamento de **Filmes** e **Atores** indicados ao Oscar. Projeto desenvolvido para fins de estudo na FIAP.

A aplicação foi refatorada para seguir uma separação de responsabilidades mais clara, utilizando **Controllers**, **Services**, **Repositories**, **Models** e **DTOs**.

---

## 📋 Sumário

- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Novidades da versão atual](#-novidades-da-versão-atual)
- [Arquitetura da Aplicação](#-arquitetura-da-aplicação)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como rodar a aplicação](#-como-rodar-a-aplicação)
  - [1. Baixando a imagem do Docker Hub](#1-baixando-a-imagem-do-docker-hub)
  - [2. Subindo o banco de dados MySQL](#2-subindo-o-banco-de-dados-mysql)
  - [3. Variáveis de ambiente necessárias](#3-variáveis-de-ambiente-necessárias)
  - [4. Executando a aplicação com docker run](#4-executando-a-aplicação-com-docker-run)
  - [5. Acessando o Swagger](#5-acessando-o-swagger--openapi)
  - [6. Encerrando os containers](#6-encerrando-os-containers)
- [Rodando a partir do código-fonte (desenvolvimento local)](#️-rodando-a-partir-do-código-fonte-desenvolvimento-local)
- [Rodando com SQL Server (profile dev)](#-rodando-com-sql-server-profile-dev)
- [Endpoints Disponíveis](#-endpoints-disponíveis)
- [Modelos, DTOs e campos esperados](#-modelos-dtos-e-campos-esperados)
- [Exemplos de Requisições](#-exemplos-de-requisições)
- [Autor](#-autores)

---

## 🚀 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 4.0.3**
  - Spring Web MVC
  - Spring Data JPA
  - Spring Boot DevTools
  - Spring Validation
- **MySQL 8**
- **SQL Server** (profiles `dev` e `prd`)
- **Maven** (gerenciador de dependências — wrapper `mvnw` incluído)
- **Lombok** (redução de boilerplate)
- **ModelMapper** (mapeamento entre DTOs e Models)
- **SpringDoc OpenAPI / Swagger UI** (documentação interativa)

---

## ✅ Pré-requisitos

Antes de começar, você precisa ter instalado na sua máquina:

- [Java JDK 17+](https://adoptium.net/)
- [Docker](https://www.docker.com/products/docker-desktop/) e [Docker Compose](https://docs.docker.com/compose/install/)
- [Git](https://git-scm.com/) (para clonar o repositório)

> 💡 **Não é necessário ter o Maven instalado!** O projeto inclui o Maven Wrapper (`mvnw` / `mvnw.cmd`).

---

## 🆕 Novidades da versão atual

Esta versão inclui uma refatoração estrutural da API, com foco em organização, manutenção e padronização das entradas e saídas dos endpoints.

### Camada de Service

Foi criada uma camada de serviço para centralizar as regras de acesso e manipulação dos dados:

- `FilmeService`
- `AtorService`

Os Controllers não acessam mais diretamente os Repositories. Agora eles chamam os Services, que por sua vez utilizam os Repositories para persistência e consulta no banco de dados.

### DTOs de entrada e saída

Foram adicionados DTOs para separar os dados recebidos e retornados pela API dos Models persistidos no banco.

Para **Filmes**:

- `FilmeCreateRequest`
- `FilmeUpdateRequest`
- `FilmeResponse`
- `FilmeMapper`

Para **Atores**:

- `AtorCreateRequest`
- `AtorUpdateRequest`
- `AtorResponse`
- `AtorMapper`

Essa separação evita expor diretamente as entidades JPA nos endpoints e permite controlar melhor quais campos entram no cadastro, quais campos entram na atualização e quais campos são devolvidos na resposta.

### Geração automática de ID no Model

Os identificadores agora são gerados automaticamente pelo banco/JPA com `@GeneratedValue(strategy = GenerationType.AUTO)`:

- `Filme.id`
- `Ator.id`

Com isso, o campo `id` **não deve ser enviados no corpo das requisições POST**. Ele é retornado pela API depois que o registro é criado.

### Controllers refatorados

Os Controllers foram ajustados para:

- Receber DTOs de request com `@RequestBody`.
- Validar entradas com `@Valid`.
- Usar os Mappers para converter DTOs em Models e Models em DTOs de resposta.
- Delegar operações de criação, consulta, atualização e remoção para a camada de Service.
- Retornar `ResponseEntity` com status HTTP adequado, como `201 Created`, `200 OK`, `204 No Content` e `404 Not Found`.

---

## 🧱 Arquitetura da Aplicação

A aplicação está organizada em camadas:

| Camada | Responsabilidade |
|--------|------------------|
| `controller` | Expõe os endpoints REST e recebe as requisições HTTP. |
| `dto` | Define objetos de entrada, saída e mapeamento entre DTOs e Models. |
| `service` | Centraliza a lógica de aplicação e intermedia Controller e Repository. |
| `repository` | Realiza a comunicação com o banco usando Spring Data JPA. |
| `model` | Representa as entidades JPA persistidas no banco de dados. |

Fluxo principal da API:

```text
Requisição HTTP
      ↓
Controller
      ↓
DTO / Mapper
      ↓
Service
      ↓
Repository
      ↓
Banco de Dados
```

---

## 📂 Estrutura do Projeto

```text
oscar_api/
├── src/
│   └── main/
│       ├── java/br/com/fiap/oscar_api/
│       │   ├── Application.java
│       │   ├── controller/
│       │   │   ├── FilmeController.java
│       │   │   └── AtorController.java
│       │   ├── dto/
│       │   │   ├── FilmeCreateRequest.java
│       │   │   ├── FilmeMapper.java
│       │   │   ├── FilmeResponse.java
│       │   │   ├── FilmeUpdateRequest.java
│       │   │   ├── AtorCreateRequest.java
│       │   │   ├── AtorMapper.java
│       │   │   ├── AtorResponse.java
│       │   │   └── AtorUpdateRequest.java
│       │   ├── model/
│       │   │   ├── Filme.java
│       │   │   └── Ator.java
│       │   ├── repository/
│       │   │   ├── FilmeRepository.java
│       │   │   └── AtorRepository.java
│       │   └── service/
│       │       ├── FilmeService.java
│       │       └── AtorService.java
│       └── resources/
│           └── application.properties
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## ▶️ Como rodar a aplicação

A forma recomendada de executar a aplicação é a partir da imagem publicada no **Docker Hub**. Não é necessário clonar o repositório, instalar o Java ou compilar o projeto — basta ter o **Docker** instalado.

Imagem oficial: [`sskah/oscar_api:1.0.0`](https://hub.docker.com/r/sskah/oscar_api)

Siga os passos abaixo na ordem.

---

### 1. Baixando a imagem do Docker Hub

Faça o download da imagem da aplicação:

```bash
docker pull sskah/oscar_api:1.0.0
```

Para confirmar que a imagem foi baixada com sucesso:

```bash
docker images
```

---

### 2. Subindo o banco de dados MySQL

A aplicação depende de um banco **MySQL**. Suba um container antes de iniciar a API:

```bash
docker run -d \
  --name mysql \
  --rm \
  -e MYSQL_ROOT_PASSWORD=root_pwd \
  -e MYSQL_USER=new_user \
  -e MYSQL_PASSWORD=my_pwd \
  -p 3306:3306 \
  mysql
```

> ⏳ Aguarde alguns segundos até o MySQL inicializar completamente antes de seguir para o próximo passo.

---

### 3. Variáveis de ambiente necessárias

A imagem recebe toda a configuração por variáveis de ambiente, passadas com a flag `-e` no `docker run`:

| Variável | Descrição | Valor de exemplo |
|----------|-----------|------------------|
| `DB_SERVER_URL` | Host do servidor MySQL | `host.docker.internal` |
| `DB_SERVER_PORT` | Porta do servidor MySQL | `3306` |
| `DB_SCHEMA` | Nome do schema/banco de dados | `oscar` |
| `DB_USER` | Usuário do banco de dados | `root` |
| `DB_PWD` | Senha do banco de dados | `root_pwd` |
| `SPRING_PROFILES_ACTIVE` | Profile ativo da aplicação (`default` ou `prd`) | `default` |

> 💡 **Sobre o `host.docker.internal`:** esse hostname permite que o container da API acesse o MySQL rodando na máquina host. No **Linux**, adicione também `--add-host=host.docker.internal:host-gateway` ao comando `docker run`.

---

### 4. Executando a aplicação com `docker run`

O comando mapeia a porta **8080**, define o **profile** e passa as **variáveis de ambiente** de conexão com o banco.

#### Profile `default` — recomendado para testar a imagem

Cria o banco e as tabelas automaticamente. É o profile ideal para o primeiro teste, pois não exige nenhuma preparação prévia do banco:

```bash
docker run -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=oscar \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=default \
  sskah/oscar_api:1.0.0
```

#### Profile `prd` — execução em produção

Não altera o schema do banco. Exige que o banco **e as tabelas já existam** previamente:

```bash
docker run -p 8080:8080 \
  -e DB_SERVER_URL=host.docker.internal \
  -e DB_SERVER_PORT=3306 \
  -e DB_SCHEMA=oscar \
  -e DB_USER=root \
  -e DB_PWD=root_pwd \
  -e SPRING_PROFILES_ACTIVE=prd \
  sskah/oscar_api:1.0.0
```

#### Diferença entre os profiles

| Comportamento | `default` | `prd` |
|---------------|-----------|-------|
| Cria o banco se não existir (`createDatabaseIfNotExist`) | ✅ Sim | ❌ Não |
| Cria/atualiza tabelas (`spring.jpa.hibernate.ddl-auto`) | `update` | `none` |
| Exibe as queries SQL no log (`spring.jpa.show-sql`) | `true` | `false` |
| Exige banco e tabelas pré-existentes | Não | Sim |
| Uso indicado | Desenvolvimento e testes | Produção |

> ⚠️ Ao usar o profile `prd` sem que o banco e as tabelas existam, a aplicação iniciará mas as requisições aos endpoints falharão. Para o primeiro teste da imagem, use o profile `default`.

A aplicação estará pronta quando o log exibir:

```text
Started Application in X.XXX seconds
```

---

### 5. Acessando o Swagger
Com a aplicação rodando, acesse a documentação interativa pelo navegador em:

```text
http://localhost:8080/
```

| Recurso | URL | Descrição |
|---------|-----|-----------|
| **Swagger UI** | [http://localhost:8080/](http://localhost:8080/) | Interface gráfica para testar os endpoints |

Pelo Swagger UI é possível testar todos os endpoints diretamente do navegador: basta expandir a operação desejada, clicar em **Try it out**, preencher os dados e clicar em **Execute**.

Para validar rapidamente pelo terminal:

```bash
curl http://localhost:8080/api/v2/filmes
```

---

### 6. Encerrando os containers

Para parar a aplicação, pressione `Ctrl + C` no terminal onde o `docker run` está sendo executado. Em seguida, remova o container do banco:

```bash
docker stop oscar_api_mysql
docker rm oscar_api_mysql
```

---

## 🛠️ Rodando a partir do código-fonte (desenvolvimento local)

Alternativa à imagem do Docker Hub, para quem deseja alterar o código da aplicação.

### 1. Subindo o Banco de Dados

```bash
docker run -d \
  --name mysql \
  --rm \
  -e MYSQL_ROOT_PASSWORD=root_pwd \
  -e MYSQL_USER=new_user \
  -e MYSQL_PASSWORD=my_pwd \
  -p 3306:3306 \
  mysql
```

### 2. Rodando a API Spring Boot

Com o banco de dados rodando, abra um terminal na raiz do projeto e execute:

No Windows (PowerShell):

```powershell
.\mvnw spring-boot:run "-Dspring-boot.run.profiles=prd" 
```
> O comando "-Dspring-boot.run.profiles=" serve para definir o profile que sera utilizado para execução, sendo possível selecionar "default" ou "prd".

A aplicação subirá em `http://localhost:8080` e as tabelas `filmes` e `atores` serão criadas automaticamente pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`).

### 3. Construindo a imagem Docker localmente

O projeto inclui um `Dockerfile` com build multi-estágio (Maven + Eclipse Temurin 17):

```bash
docker build -t oscar_api:versão .
```

---

## 🗄️ Rodando com SQL Server (profile `dev`)

Os profiles `dev` e `prd` usam **SQL Server** (`application-dev.properties` / `application-prd.properties`). Siga os passos abaixo no **Windows (PowerShell)**.

### 1. Subindo o container do SQL Server

```powershell
docker run -d `
  --name sqlserver `
  --rm `
  -e MSSQL_SA_PASSWORD=1q2w3e4R@ `
  -e "ACCEPT_EULA=Y" `
  -p 1433:1433 `
  mcr.microsoft.com/mssql/server:latest
```

> No Linux/macOS (bash), troque a crase `` ` `` no fim das linhas por `\`.

> ⚠️ O SQL Server exige **senha forte** (mínimo de 8 caracteres, com letra maiúscula, minúscula, número e símbolo). Com uma senha fraca o container não inicializa e a API retorna `Login failed for user 'sa'`.

Aguarde alguns segundos e confira se o container está em execução:

```powershell
docker ps
```

### 2. Criando o banco de dados

Diferente do profile MySQL, a URL do SQL Server **não cria o banco automaticamente**. Crie o banco `api` antes de subir a API:

```powershell
docker exec -it sqlserver /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "1q2w3e4R@" -C -Q "CREATE DATABASE api"
```

Para conferir se o banco foi criado:

```powershell
docker exec -it sqlserver /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "1q2w3e4R@" -C -Q "SELECT name FROM sys.databases"
```

> As tabelas `filmes` e `atores` são criadas automaticamente pelo Hibernate (`spring.jpa.hibernate.ddl-auto=update`) na primeira execução.

### 3. Definindo as variáveis de ambiente

No mesmo terminal em que a aplicação será executada:

```powershell
$env:DB_SERVER_URL="localhost"
$env:DB_SERVER_PORT="1433"
$env:DB_SCHEMA="api"
$env:DB_USER="sa"
$env:DB_PWD="1q2w3e4R@"
$env:SPRING_PROFILES_ACTIVE="dev"
```

| Variável | Descrição | Valor |
|----------|-----------|-------|
| `DB_SERVER_URL` | Host do SQL Server | `localhost` |
| `DB_SERVER_PORT` | Porta do SQL Server | `1433` |
| `DB_SCHEMA` | Nome do banco de dados | `api` |
| `DB_USER` | Usuário do banco | `sa` |
| `DB_PWD` | Senha do usuário `sa` | `1q2w3e4R@` |
| `SPRING_PROFILES_ACTIVE` | Profile ativo | `dev` |

> 💡 As variáveis definidas com `$env:` valem apenas para a sessão atual do PowerShell. Se abrir um novo terminal, defina-as novamente.

### 4. Executando a aplicação

Na raiz do projeto:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

A aplicação estará pronta quando o log exibir:

```text
Started Application in X.XXX seconds
```

Acesse o Swagger em [http://localhost:8080/](http://localhost:8080/).

> ⚠️ No profile `dev` a versão da API é **`v1`** (`api.version=v1`), então os endpoints ficam em `/api/v1/filmes` e `/api/v1/atores`.

```powershell
curl http://localhost:8080/api/v1/filmes
```

### 5. Encerrando o SQL Server

Pressione `Ctrl + C` para parar a API e depois:

```powershell
docker stop sqlserver
```

> Como o container foi criado com `--rm`, ele é removido ao parar — **e os dados do banco são perdidos**. Na próxima execução, repita o passo 2 para criar o banco novamente.

### Problemas comuns

| Erro | Causa provável | Solução |
|------|----------------|---------|
| `Login failed for user 'sa'` | Banco `api` não existe | Execute o passo 2 |
| `Login failed for user 'sa'` | Senha diferente da usada no `docker run` ou senha fraca | Confira `DB_PWD` e recrie o container com senha forte |
| `Connection refused` / `TCP/IP connection failed` | Container parado ou ainda inicializando | Verifique com `docker ps` e aguarde alguns segundos |

---

## 🔌 Endpoints Disponíveis

Todos os endpoints utilizam o prefixo configurado em `application.properties`:

```properties
api.version=v2
```

Portanto, o prefixo atual é:

```text
/api/v2
```

### Filmes — `/api/v2/filmes`

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/api/v2/filmes` | Cadastra um novo filme | `FilmeCreateRequest` |
| GET | `/api/v2/filmes` | Lista todos os filmes | Não possui |
| GET | `/api/v2/filmes/{id}` | Busca um filme pelo ID | Não possui |
| PUT | `/api/v2/filmes/{id}` | Atualiza um filme existente | `FilmeUpdateRequest` |
| DELETE | `/api/v2/filmes/{id}` | Remove um filme | Não possui |

### Atores — `/api/v2/atores`

| Método | Endpoint | Descrição | Body |
|--------|----------|-----------|------|
| POST | `/api/v2/atores` | Cadastra um novo ator | `AtorCreateRequest` |
| GET | `/api/v2/atores` | Lista todos os atores | Não possui |
| GET | `/api/v2/atores/{id}` | Busca um ator pelo ID | Não possui |
| PUT | `/api/v2/atores/{id}` | Atualiza um ator existente | `AtorUpdateRequest` |
| DELETE | `/api/v2/atores/{id}` | Remove um ator | Não possui |

---

## 📦 Modelos, DTOs e campos esperados

### Filme

Entidade persistida: `Filme`

| Campo | Tipo | Observação |
|-------|------|------------|
| `id` | `Long` | Gerado automaticamente. |
| `titulo` | `String` | Obrigatório. |
| `categoria` | `String` | Opcional. |
| `orcamento` | `BigDecimal` | Obrigatório. |
| `estudio` | `String` | Obrigatório. |

> `categoria` é a categoria do Oscar em que o filme foi indicado (ex.: Melhor Filme) e `orcamento` é o orçamento em milhões de dólares.

#### `FilmeCreateRequest`

Usado no `POST /api/v2/filmes`.

```json
{
  "titulo": "Oppenheimer",
  "categoria": "Melhor Filme",
  "orcamento": 100,
  "estudio": "Universal Pictures"
}
```

#### `FilmeUpdateRequest`

Usado no `PUT /api/v2/filmes/{id}`.

```json
{
  "titulo": "Oppenheimer - Versão Estendida",
  "categoria": "Melhor Direção",
  "orcamento": 110,
  "estudio": "Universal Pictures"
}
```

#### `FilmeResponse`

Resposta retornada pela API.

```json
{
  "id": 1,
  "titulo": "Oppenheimer",
  "categoria": "Melhor Filme",
  "orcamento": 100,
  "estudio": "Universal Pictures"
}
```

---

### Ator

Entidade persistida: `Ator`

| Campo | Tipo | Observação |
|-------|------|------------|
| `id` | `Long` | Gerado automaticamente. |
| `nome` | `String` | Obrigatório. |
| `cache` | `BigDecimal` | Obrigatório. |
| `notaCritica` | `BigDecimal` | Obrigatório. |
| `qtdIndicacoes` | `Integer` | Obrigatório. |

> `cache` é o cachê do ator em milhões de dólares, `notaCritica` é a nota média da crítica (0 a 10) e `qtdIndicacoes` é a quantidade de indicações ao Oscar.

#### `AtorCreateRequest`

Usado no `POST /api/v2/atores`.

```json
{
  "nome": "Cillian Murphy",
  "cache": 10.5,
  "notaCritica": 9.2,
  "qtdIndicacoes": 1
}
```

#### `AtorUpdateRequest`

Usado no `PUT /api/v2/atores/{id}`.

```json
{
  "nome": "Cillian Murphy",
  "cache": 12.0,
  "notaCritica": 9.2,
  "qtdIndicacoes": 2
}
```

#### `AtorResponse`

Resposta retornada pela API.

```json
{
  "id": 1,
  "nome": "Cillian Murphy",
  "cache": 10.5,
  "notaCritica": 9.2,
  "qtdIndicacoes": 1
}
```

---

## 🧪 Exemplos de Requisições

### Cadastrar um Filme

> O campo `id` não deve ser enviado. Ele é gerado automaticamente.

```bash
curl -X POST http://localhost:8080/api/v2/filmes \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Oppenheimer",
    "categoria": "Melhor Filme",
    "orcamento": 100,
    "estudio": "Universal Pictures"
  }'
```

### Listar Filmes

```bash
curl http://localhost:8080/api/v2/filmes
```

### Buscar Filme por ID

```bash
curl http://localhost:8080/api/v2/filmes/1
```

### Atualizar um Filme

```bash
curl -X PUT http://localhost:8080/api/v2/filmes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Oppenheimer - Versão Estendida",
    "categoria": "Melhor Direção",
    "orcamento": 110,
    "estudio": "Universal Pictures"
  }'
```

### Remover um Filme

```bash
curl -X DELETE http://localhost:8080/api/v2/filmes/1
```

---

### Cadastrar um Ator

> O campo `id` não deve ser enviado. Ele é gerado automaticamente.

```bash
curl -X POST http://localhost:8080/api/v2/atores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Cillian Murphy",
    "cache": 10.5,
    "notaCritica": 9.2,
    "qtdIndicacoes": 1
  }'
```

### Listar Atores

```bash
curl http://localhost:8080/api/v2/atores
```

### Buscar Ator por ID

```bash
curl http://localhost:8080/api/v2/atores/1
```

### Atualizar um Ator

```bash
curl -X PUT http://localhost:8080/api/v2/atores/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Cillian Murphy",
    "cache": 12.0,
    "notaCritica": 9.2,
    "qtdIndicacoes": 2
  }'
```

### Remover um Ator

```bash
curl -X DELETE http://localhost:8080/api/v2/atores/1
```

---

## 👨‍💻 Autores

Karine Nascimento Honório da Silva - RM: 558810
Projeto desenvolvido para a disciplina de Microsservices — **FIAP - 3°SIR**.

---