# Financeiro - Frontend

Este é um aplicativo desenvolvido com Angular e TailwindCSS que permite aos usuários registrar transações positivas e negativas, com o objetivo de fornecer controle sobre gastos e o saldo disponível.

## Funcionalidades

- **Login**: Permite que os usuários façam login para acessar suas informações.
- **Cadastro**: Novo usuário pode se cadastrar para começar a usar o app.
- **Recuperação de Senha**: Permite ao usuário recuperar a senha caso a tenha esquecido.
- **Resetar Senha**: Usuários podem redefinir suas senhas com segurança.
- **Cadastrar Transações**: Registro de transações financeiras, tanto positivas (receitas) quanto negativas (despesas).
- **Dashboard**: Visualização das transações registradas, organizadas por mês. Exibe o total gasto e o saldo disponível.

## Como Rodar o Projeto

### Requisitos

- [Backend](/backend/README.md)
- Node.js
- Angular CLI

### Instalação Desenvolvimento

1. Clone o repositório para sua máquina local:

```bash
git clone https://github.com/criticow/findemo.git
cd findemo/frontend
```

2. Instale as dependências:

```bash
npm install
```

3. Para rodar o projeto em modo de desenvolvimento:

```bash
ng serve
```

4. Abra o navegador e acesse http://localhost:4200/ para visualizar o aplicativo.

### Construção para Produção

1. Clone o repositório para sua máquina local:

```bash
git clone https://github.com/criticow/findemo.git
cd findemo/frontend
```

2. Instale as dependências:

```bash
npm install
```

3. Para construir o aplicativo para produção:
```bash
ng build --configuration production
```

4. Construa usando docker:

```bash
docker build -t financeiroapp .
```

5. Rode usando docker:

```bash
docker run -d -p 4201:80 --network financeiro-app-network --name financeiroapp financeiroapp
```