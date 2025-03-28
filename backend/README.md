# Financeiro - Backend

Este é o backend de um aplicativo para controle de transações financeiras, desenvolvido com Spring Boot. Ele oferece diversas funcionalidades, como o gerenciamento de transações e autenticação com JWT.

## Funcionalidades

- **Login**: Permite que os usuários façam login com autenticação JWT.
- **Cadastro**: Novo usuário pode se cadastrar para usar a plataforma.
- **Recuperação de Senha**: Permite ao usuário recuperar a senha caso tenha esquecido.
- **Resetar Senha**: Usuários podem redefinir suas senhas de forma segura.
- **Criar Transação**: Registra transações financeiras (positivas ou negativas).
- **Buscar Transações por Data de Criação**: Permite buscar transações com base na data de criação.
- **Buscar Transações por Mês**: Busca transações de um mês específico.
- **Buscar Anos + Meses com Transações**: Retorna os anos e meses que possuem transações registradas.

## Funcionalidades de Segurança

- **Autenticação JWT**: Utiliza um filtro para verificar se o token JWT está sendo fornecido via Bearer Token.
- **Roteamento Seguro**: As rotas que começam com `/auth` são livres (não exigem autenticação), enquanto as demais são protegidas.
- **Validação**: Utiliza a Spring Validation para garantir que os dados enviados nas requisições sejam válidos.

## Como Rodar o Projeto

### Requisitos

- JDK 21
- Maven
- Banco de Dados (PostgreSQL)
- Docker

### Instalação Desenvolvimento

1. Clone o repositório para sua máquina local:

```bash
git clone https://github.com/criticow/findemo.git
cd findemo/backend
```

2. Crie uma rede para a aplicação:

```bash
docker network create financeiro-app-network
```

3. Crie a imagem do postgres:

```bash
docker build -f Dockerfile.postgres -t postgres-container .
```

5. Rode o container postgres:

```bash
docker run -d -p 5433:5432 --network financeiro-app-network --name postgres-container postgres-container
```

6. Instale as dependências:

```bash
mvn clean install
```

7. Rode o app:

```bash
mvn spring-boot:run
```

8. Teste:

Abra o link baixo no navegador.

```bash
http://localhost:8080/
```

A resposta deverá ser algo semelhante à

```json
{"message":"Sevidor online!","timestamp":"2025-03-27T13:37:16.226388277"}
```

### Construção em Produção

1. Clone o repositório para sua máquina local:

```bash
git clone https://github.com/criticow/findemo.git
cd findemo/backend
```

2. Crie uma rede para a aplicação:

```bash
docker network create financeiro-app-network
```

3. Crie a imagem do postgres:

```bash
docker build -f Dockerfile.postgres -t postgres-container .
```

5. Rode o container postgres:

```bash
docker run -d -p 5433:5432 --network financeiro-app-network --name postgres-container postgres-container
```

6. Instale as dependências:

```bash
mvn clean install
```

7. Cria a imagem do spring:

```bash
docker build -f Dockerfile.spring -t financeiroapi .
```

8. Rode o container spring:

```bash
docker run -d -p 8081:8080 --network financeiro-app-network --name financeiroapi financeiroapi
```

9. Teste:

Abra o link baixo no navegador.

```bash
http://localhost:8081/
```

A resposta deverá ser algo semelhante à

```json
{"message":"Sevidor online!","timestamp":"2025-03-27T13:37:16.226388277"}
```