# Atividade Prática ATVI - AutoBots

**Aluno:** João Gabriel Barros Guimarães  
**Curso:** 3º DSM - Desenvolvimento Web III  
**Professor:** Prof. Dr. Eng. Gerson Penha  
**Avaliação:** Av1 - AutoBots  

---

## O que foi feito

- Ajuste das entidades e relacionamentos no banco de dados.
- Criação dos repositórios de dados.
- Implementação do CRUD completo (cadastro, listagem, busca, atualização e exclusão) para todas as entidades.
- Configuração do Easter Egg no cabeçalho das respostas HTTP.
- Criação e execução dos testes automatizados de integração.

---

## Resultados Obtidos no Terminal

```text
Microsoft Windows [versão 10.0.26200.9457]
(c) Microsoft Corporation. Todos os direitos reservados.

C:\Users\Biel_\Downloads\dwIII\atvi-autobots-microservico-spring>curl -i http://localhost:8080/cliente/clientes
HTTP/1.1 200 
X-Easter-Egg: quantum-gravity-zero-volume
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 22 Sep 2026 18:43:46 GMT

[{"id":1,"nome":"Pedro Alcântara de Bragança e Bourbon","nomeSocial":"Dom Pedro","dataNascimento":"2002-06-15T18:42:33.288+00:00","dataCadastro":"2026-09-22T18:42:33.288+00:00","documentos":[{"id":1,"tipo":"RG","numero":"1500"},{"id":2,"tipo":"RG","numero":"00000000001"}],"endereco":{"id":1,"estado":"Rio de Janeiro","cidade":"Rio de Janeiro","bairro":"Copacabana","rua":"Avenida Atlântica","numero":"1702","codigoPostal":"22021001","informacoesAdicionais":"Hotel Copacabana palace"},"telefones":[{"id":1,"ddd":"21","numero":"981234576"}]}]
C:\Users\Biel_\Downloads\dwIII\atvi-autobots-microservico-spring>curl.exe -i -X POST http://localhost:8080/cliente/cadastro -H "Content-Type: application/json" -d "{\"nome\":\"Maria Souza\",\"nomeSocial\":\"Maria\",\"dataNascimento\":\"1995-05-10\",\"dataCadastro\":\"2026-09-22\",\"endereco\":{\"estado\":\"SP\",\"cidade\":\"Sao Jose dos Campos\",\"bairro\":\"Centro\",\"rua\":\"Rua Central\",\"numero\":\"50\",\"codigoPostal\":\"12200000\"},\"telefones\":[{\"ddd\":\"12\",\"numero\":\"988887777\"}],\"documentos\":[{\"tipo\":\"CPF\",\"numero\":\"11122233344\"}]}"
HTTP/1.1 201 
X-Easter-Egg: quantum-gravity-zero-volume
Content-Length: 0
Date: Tue, 22 Sep 2026 18:44:37 GMT


C:\Users\Biel_\Downloads\dwIII\atvi-autobots-microservico-spring>curl.exe -i http://localhost:8080/cliente/2
HTTP/1.1 200 
X-Easter-Egg: quantum-gravity-zero-volume
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 22 Sep 2026 18:44:47 GMT

{"id":2,"nome":"Maria Souza","nomeSocial":"Maria","dataNascimento":"1995-05-10T00:00:00.000+00:00","dataCadastro":"2026-09-22T00:00:00.000+00:00","documentos":[{"id":3,"tipo":"CPF","numero":"11122233344"}],"endereco":{"id":2,"estado":"SP","cidade":"Sao Jose dos Campos","bairro":"Centro","rua":"Rua Central","numero":"50","codigoPostal":"12200000","informacoesAdicionais":null},"telefones":[{"id":2,"ddd":"12","numero":"988887777"}]}
C:\Users\Biel_\Downloads\dwIII\atvi-autobots-microservico-spring>curl.exe -i http://localhost:8080/cliente/999
HTTP/1.1 404 
X-Easter-Egg: quantum-gravity-zero-volume
Content-Length: 0
Date: Tue, 22 Sep 2026 18:44:57 GMT


C:\Users\Biel_\Downloads\dwIII\atvi-autobots-microservico-spring>curl.exe -i -X PUT http://localhost:8080/cliente/atualizar -H "Content-Type: application/json" -d "{\"id\":2,\"nome\":\"Maria Souza Atualizada\"}"
HTTP/1.1 200 
X-Easter-Egg: quantum-gravity-zero-volume
Content-Length: 0
Date: Tue, 22 Sep 2026 18:45:12 GMT


C:\Users\Biel_\Downloads\dwIII\atvi-autobots-microservico-spring>curl.exe -i -X DELETE http://localhost:8080/cliente/excluir/2
HTTP/1.1 200 
X-Easter-Egg: quantum-gravity-zero-volume
Content-Length: 0
Date: Tue, 22 Sep 2026 18:45:18 GMT


C:\Users\Biel_\Downloads\dwIII\atvi-autobots-microservico-spring>curl.exe -i -X POST http://localhost:8080/telefone/cadastro -H "Content-Type: application/json" -d "{\"ddd\":\"11\",\"numero\":\"977776666\"}"
HTTP/1.1 201 
X-Easter-Egg: quantum-gravity-zero-volume
Content-Length: 0
Date: Tue, 22 Sep 2026 18:45:25 GMT


C:\Users\Biel_\Downloads\dwIII\atvi-autobots-microservico-spring>curl.exe -i http://localhost:8080/telefone/telefones
HTTP/1.1 200 
X-Easter-Egg: quantum-gravity-zero-volume
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 22 Sep 2026 18:45:31 GMT

[{"id":1,"ddd":"21","numero":"981234576"},{"id":3,"ddd":"11","numero":"977776666"}]
C:\Users\Biel_\Downloads\dwIII\atvi-autobots-microservico-spring>curl.exe -i -X DELETE http://localhost:8080/telefone/excluir/2
HTTP/1.1 404 
X-Easter-Egg: quantum-gravity-zero-volume
Content-Length: 0
Date: Tue, 22 Sep 2026 18:45:37 GMT
```
