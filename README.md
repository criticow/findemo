
# Financeiro DEMO

Aplicativo demonstrativo de controle de transações financeiras desenvolvido em
angular e spring. Oferece diversas funcionalidades, como cadastro, autenticação,
lançamento de transações positivas e negativas, dashboard com resumo separado por
ano e mês.  

OBS: Recuperação de senha por email apenas na versão live demo ou caso as informações
SMTP sejam configuradas no [backend](/backend/src/main/resources/application.yaml).

## Live Demo

- Versão demonstrativa completa [link](https://findemoapp.waddahex.com)
- Caso queira testar o app spring use a coleção do [postman](/findemoapi.postman_collection.json)
- Playlist de videos demonstrando, instalando e rodando o app [Playlist](https://www.youtube.com/playlist?list=PLarKNWkNTTWRWHMlOwdBubJxjOweFPH01)

## Instalação

### Requisitos
- Docker

### Completa

1. Clone o projeto:

```bash
git clone https://github.com/criticow/findemo.git
cd findemo
```

2. Construa e rode via docker:

```bash
docker compose down
docker compose up --build
```

3. Teste o frontend:

Após inicialização no passo 2 abra o link abaixo.

```bash
http:\\localhost:4201
```

A página abaixo deverá estar disponivel.

![login_page](/screenshots/login_page.png)

4. Teste o backend:

Após inicialização no passo 2 abra o link abaixo.

```bash
http:\\localhost:8081
```

A resposta deverá ser algo semelhante à

```json
{"message":"Sevidor online!","timestamp":"2025-03-27T13:37:16.226388277"}
```


### Backend

Para instalação e execução apenas do backend [backend/README.md](/backend/README.md)

### Frontend

Para instalação e execução apenas do frontend [frontend/README.md](/frontend/README.md)


## Tecnologias Usadas

### Frontend
- **Angular**: Framework JavaScript para construção da aplicação.
- **TailwindCSS**: Framework CSS para estilização rápida e personalizada.

### Backend
- **Spring Boot**: Framework para construção de APIs REST.
- **JWT (JSON Web Token)**: Para autenticação segura de usuários.
- **Spring Validation**: Para validação dos dados nas requisições.
- **JPA/Hibernate**: Para interação com o banco de dados.
- **Spring Mail**: Para envio de emails via SMTP.
- **Spring Crypto**: Para criptografia de senhas.
