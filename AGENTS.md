# AGENTS.md — spring-ollama-rag

Demonstração de RAG com Spring Boot + LangChain4j sobre Ollama local: carrega um PDF do classpath,
vetoriza, e responde perguntas formatadas em JSON (`RadarResponse` — nome da tecnologia, anel
"Adote/Experimente/Avale/Evite" e explicação). Tem cache semântico por similaridade de cosseno.

## Stack (lida de pom.xml)

- Java 17 (`<java.version>17</java.version>`)
- Spring Boot 3.2.5 (parent)
- LangChain4j 0.31.0 (`<langchain4j.version>`), em quatro artefatos:
  `langchain4j-spring-boot-starter`, `langchain4j-ollama`,
  `langchain4j-embeddings-all-minilm-l6-v2`, `langchain4j-document-parser-apache-pdfbox`
- `spring-boot-starter-web`; `spring-boot-starter-test` (sem teste escrito)
- Coordenadas: `br.com.pedrobelmino` / `spring-ollama-rag` / `1.0-SNAPSHOT`

## Comandos

- build: `mvn clean package`
- test: `mvn test`  (não existe nenhum teste no projeto)
- run: `mvn spring-boot:run`  (porta 8080, definida em `server.port`)
- chamada: `curl "http://localhost:8080/chat?message=Sua pergunta"`
- pré-requisito externo: `ollama pull llama3.2` e Ollama escutando em `localhost:11434`
- lint: não existe no projeto
- Docker/compose: não existe no projeto
- wrapper `mvnw`: não existe — usar o `mvn` do sistema

## Estrutura

- `src/main/java/br/com/pedrobelmino/langchain4j/ollama/` — `Application` (main)
- `.../ollama/ChatController.java` — `GET /chat?message=` (default "Hello")
- `.../ollama/CustomerSupportAgent.java` — interface do `AiService`
- `.../ollama/RadarResponse.java` — POJO com `@Description` nos campos (força o JSON)
- `.../ollama/RagConfiguration.java` — todos os beans: modelo Ollama, embeddings, store, retriever, agent
- `.../ollama/SemanticCacheService.java` — cache em memória com `CosineSimilarity > 0.70`
- `src/main/resources/application.properties` — só `spring.application.name` e `server.port`
- `src/main/resources/document.pdf` — base de conhecimento lida no boot (não versionar outro PDF sem avisar)

## Convenções observadas

- Tudo no pacote raiz: projeto pequeno de propósito, sem camadas `service/` ou `repository/` separadas
  (exceto o `SemanticCacheService`, anotado `@Service`).
- Configuração por `@Bean` no `RagConfiguration`, não por propriedades do starter do LangChain4j.
- Saída sempre tipada: `RadarResponse` com `@Description` por campo, em vez de `String` livre.
- Nomes de métodos em inglês (`chat`, `getPriceForCity` inexistente aqui) e comentários em inglês
  dentro do `RagConfiguration`.
- Memória de chat com `MessageWindowChatMemory.withMaxMessages(10)`.

## Regras

- Não existe segredo no projeto (Ollama é local) — se aparecer chave de nuvem, só por variável de
  ambiente, nunca em `application.properties` nem em `.env` commitado.
- Não mover `document.pdf` nem trocar o nome: o `RagConfiguration` resolve `classpath:document.pdf`.
- Não alterar o contrato de `/chat` (`?message=` -> JSON `RadarResponse`) sem atualizar a
  documentação/teste do consumidor.

## Pegadinhas reais

- Modelo e URL do Ollama estão HARDCODED em `RagConfiguration` (`http://localhost:11434`,
  `llama3.2`). Não estão em `application.properties`: mudar modelo = editar código e recompilar.
- O bean de `EmbeddingStore` lê o PDF no boot. Se o Ollama estiver fora do ar, a aplicação não sobe.
  Sem `document.pdf` no classpath ela sobe, mas só imprime um warning e responde sem contexto.
- Primeira execução baixa o modelo de embeddings all-MiniLM-L6-v2 (~90 MB) da rede; offline, falha.
- Trocar o PDF exige `mvn clean package`: um PDF antigo pode permanecer em `target/classes`.
- `SemanticCacheService` guarda tudo em `ArrayList` sem limite nem sincronização — não é thread-safe.
  Aceitável em demo, não em produção.
- Retriever configurado com `maxResults(2)` e `minScore(0.6)`: perguntas fora do documento retornam
  resposta genérica, não erro.
