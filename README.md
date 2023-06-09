# DScommerce
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/ElvesAguiar/dscommerce/blob/main/LICENSE) 

# Sobre o projeto
Esse Projeto é uma aplicação Web feita em Java Spring/Rest.

A API pode apresentar um catálogo de produtos, os quais podem ser filtrados pelo nome do produto. A partir
desse catálogo, o usuário pode selecionar um produto para ver seus detalhes através de um ID. O usuário pode fazer um pedido no carrinho de compras. 


## Modelo conceitual
![Modelo Conceitual](https://github.com/ElvesAguiar/dscommerce/blob/main/Captura%20de%20tela%202023-06-05%20105505.png)


# Tecnologias utilizadas
## Back end
- Java
- Spring Boot
- JPA / Hibernate
- Maven
- Banco de dados: H2

# Como executar o projeto

## Back end
Pré-requisitos: Java 11, 
spring tools suite (ou qualquer IDE com suporte para o Spring boot), 
terminal do gitbash

```bash
# clonar repositório
git clone git@github.com:ElvesAguiar/dscommerce.git

# entrar na pasta do projeto back end
cd dscommerce
```
# Executar o projeto o projeto na sua IDE
#(no spring tools suite -> no canto inferior direito -> local-> dslist -> botão direito do mouse no ícone verde -> (Re)start) 

# Para fazer requisições
Pré-requisitos: Postman
-necessário fazer login usando requisição "POST" http://localhost:8080/oauth2/token->
(Enviroment Postman díponível em : https://github.com/ElvesAguiar/dscommerce/blob/main/enviroment.pdf)
-Inicie uma coleção->  faça uma requisição "GET" (exemplo - http://localhost:8080/products)

# Autor

Elves Nogueira de Aguiar

linkedin.com/in/elves-aguiar-91a1551a0/

