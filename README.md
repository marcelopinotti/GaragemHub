# Garagem Hub

API Backend para gestão de **frotas** e **personagens** de múltiplos universos.
Construída com **Spring Boot** para conectar heróis, mundos e veículos em um sistema escalável — como uma garagem onde cada personagem pode ter sua coleção de carros.

> **Cardinalidade / ideia do domínio:**
> - Um **Personagem** pode ter **vários Carros**.
> - Um **Carro** tem **no máximo um dono** (um Personagem).
>
> **Regra de universo/desenho:** personagens só podem se associar a carros do **mesmo** universo (ex.: **DC ↔ DC**).

---

## Visão geral
O Garagem Hub resolve um problema simples de explicar e legal de usar:
- cadastrar personagens (heróis/vilões) com seu universo;
- cadastrar carros (veículos) com seus atributos;
- **atribuir carros a personagens** respeitando as regras de negócio;
- consultar, editar e remover registros via API e telas Thymeleaf.

---

## Arquitetura (organização de pastas)
A estrutura está separada por **módulos de domínio** (Carros / Personagem), onde cada módulo concentra seus componentes (Controller, Service, Repository, DTO/Mapper/Model).

```
HeroGarage/
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  └─ marcelo/HeroGarage/
│  │  │     ├─ HeroGarageApplication.java
│  │  │     ├─ Carros/
│  │  │     │  ├─ CarrosController.java        # API REST
│  │  │     │  ├─ CarrosControllerUi.java      # Telas (Thymeleaf)
│  │  │     │  ├─ CarrosService.java           # Regras de negócio
│  │  │     │  ├─ CarrosRepository.java        # Persistência (JPA)
│  │  │     │  ├─ CarrosModel.java             # Entidade (JPA)
│  │  │     │  ├─ CarrosDTO.java               # Transporte de dados
│  │  │     │  └─ CarrosMapper.java            # Conversões DTO ↔ Model
│  │  │     ├─ Personagem/
│  │  │     │  ├─ PersonagemController.java    # API REST
│  │  │     │  ├─ PersonagemControllerUi.java  # Telas (Thymeleaf)
│  │  │     │  ├─ PersonagemService.java       # Regras de negócio
│  │  │     │  ├─ PersonagemRepository.java    # Persistência (JPA)
│  │  │     │  ├─ PersonagemModel.java         # Entidade (JPA)
│  │  │     │  ├─ PersonagemDTO.java           # Transporte de dados
│  │  │     │  └─ PersonagemMapper.java        # Conversões DTO ↔ Model
│  │  │     └─ exception/                      # Exceptions de domínio
│  │  └─ resources/
│  │     ├─ application.properties
│  │     ├─ db/migrations/                     # Flyway SQL migrations
│  │     ├─ static/
│  │     │  ├─ css/
│  │     │  └─ js/
│  │     └─ templates/                         # Páginas Thymeleaf
│  └─ test/
│     └─ java/...                              # Testes unitários
├─ docker-compose.yml
├─ Dockerfile
├─ pom.xml
└─ README.md
```

---

## Stack e dependências principais
- **Java 21** (ver `pom.xml`)
- **Spring Boot**
  - Spring Web (API)
  - Thymeleaf (UI)
  - Validation
- **Spring Data JPA / Hibernate**
- **Flyway** (migrations em `src/main/resources/db/migrations`)
- **MySQL** (via Docker Compose)
- **Lombok**
- **spring-dotenv** (carrega variáveis a partir de `.env`)

---

## Pré-requisitos
- Java **21** instalado (JDK)
- Docker + Docker Compose (para subir o MySQL)

---

## Configuração (variáveis de ambiente)
A aplicação lê as credenciais do banco via `.env` (**não commite esse arquivo**).

Variáveis usadas em `src/main/resources/application.properties`:
- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`

Variável usada no `docker-compose.yml`:
- `DATABASE_ROOT_PASSWORD`

### Criando seu `.env`
1) Copie o arquivo de exemplo:

```bash
cp .env.example .env
```

2) Ajuste os valores no `.env`.

Exemplo compatível com o `docker-compose.yml` deste projeto:

```dotenv
DATABASE_URL=jdbc:mysql://localhost:3307/HeroGarage
DATABASE_USERNAME=herogarage
DATABASE_PASSWORD=herogarage
DATABASE_ROOT_PASSWORD=root
```

### Importante: URL do MySQL (host vs Docker)
- **App rodando na sua máquina (recomendado):**
  - use `jdbc:mysql://localhost:3307/HeroGarage` (porta 3307 exposta pelo Docker)
- **App rodando dentro do Docker, na mesma rede do MySQL:**
  - use `jdbc:mysql://mysql:3306/HeroGarage` (service name `mysql` do compose)

---

## Como rodar

### Opção A (recomendado): API local + MySQL no Docker
1) Suba o MySQL:

```bash
docker compose up -d
```

2) Rode a aplicação:

```bash
./mvnw spring-boot:run
```

A API sobe, por padrão, em `http://localhost:8080`.

### Opção B: rodar os testes

```bash
./mvnw test
```

---

## Banco de dados, migrations e Flyway
- O Flyway roda **automaticamente** ao iniciar a aplicação e aplica as migrations em `src/main/resources/db/migrations`.
- A configuração `spring.jpa.hibernate.ddl-auto=validate` faz o Hibernate **validar** se o schema existe.
  - Se as tabelas não existirem (ou estiverem diferentes), a aplicação pode falhar na inicialização.

---

## Portas
- MySQL (Docker): **3307** no host → **3306** no container (`3307:3306`)
- API (Spring Boot): **8080** (padrão)

---

## Troubleshooting

### (1) Access denied
- Usuário/senha do MySQL no `.env` não batem com o container.
- Confirme `DATABASE_USERNAME` e `DATABASE_PASSWORD`.

### (2) Communications link failure
- MySQL não subiu ou a URL/porta está errada.
- Se estiver usando Docker Compose do projeto, a URL local costuma ser `jdbc:mysql://localhost:3307/HeroGarage`.

### (3) Falha no Flyway / schema divergente
- Pode acontecer se você reaproveitar um banco já existente com migrations diferentes.
- Solução comum: usar um banco “limpo”/novo ou alinhar o histórico do Flyway.

---

## Notas sobre Docker (comando `docker run` vs `docker compose`)
Se você **já usa** o `docker-compose.yml` do projeto, prefira **não** criar outro container com `docker run`, porque:
- você pode ter **conflito de porta** (ex.: 3306/3307) e
- pode acabar com **dois MySQL diferentes**, cada um com um conjunto de dados.

O comando abaixo cria um **novo container** MySQL com uma senha e banco iniciais:

```bash
docker run --name some-mysql \
  -e MYSQL_ROOT_PASSWORD=123 \
  -e MYSQL_DATABASE=HeroGarage \
  -d mysql:oraclelinux9
```

Isso **não apaga automaticamente** o seu banco antigo, mas:
- se você já tem um container MySQL rodando com o mesmo nome, o comando falha;
- se você mapear a mesma porta usada pelo outro, dá conflito;
- se você usar um volume persistente, os dados ficam no volume (não “somem” ao recriar o container).

**Sobre `MYSQL_ROOT_PASSWORD`:**
- Essa variável **não precisa existir no seu Windows**. Ela é só um parâmetro que você passa para o container MySQL na hora de criá-lo.
- No `docker-compose.yml` do projeto, ela vem do `.env` via `DATABASE_ROOT_PASSWORD`.

Se você já tem um banco conectado e quer **manter os dados**, não recrie o container do zero sem entender onde os dados estão (volume) e sem backup.
