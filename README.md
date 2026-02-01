# Spring Ollama RAG

Este projeto demonstra como criar uma aplicação Spring Boot que utiliza RAG (Retrieval-Augmented Generation) com Ollama rodando localmente. A aplicação é capaz de responder perguntas baseadas exclusivamente em um documento PDF fornecido.

## Tecnologias Utilizadas

*   **Spring Boot:** Framework para criação da aplicação web.
*   **LangChain4j:** Biblioteca para integração com LLMs (Large Language Models) em Java.
*   **Ollama:** Ferramenta para rodar LLMs localmente.
*   **Apache PDFBox:** Para extração de texto de arquivos PDF.

## Pré-requisitos

1.  **Java 17** ou superior instalado.
2.  **Maven** instalado.
3.  **Ollama** instalado e rodando.

## Configuração do Ollama

O projeto está configurado para usar o modelo `llama3.2`. Certifique-se de tê-lo baixado no seu Ollama local:

```bash
ollama pull llama3.2
```

O Ollama deve estar rodando na porta padrão `11434`.

### Sobre o Modelo Llama 3.2

O Llama 3.2 é um modelo de linguagem aberto (open weights) desenvolvido pela Meta. Ele é otimizado para eficiência e desempenho em tarefas de raciocínio, codificação e resumo. Por ser um modelo "open", seus pesos estão disponíveis para uso e adaptação pela comunidade, permitindo rodá-lo localmente sem depender de APIs externas pagas, garantindo maior privacidade dos dados.

## Estrutura do Projeto e Resposta Estruturada

O projeto utiliza a classe `RadarResponse` para garantir que as respostas do modelo sigam um formato JSON estruturado e previsível.

### Classe `RadarResponse`

Em vez de receber um texto livre do LLM, definimos uma estrutura clara para a resposta:

```java
public class RadarResponse {
    @Description("Nome do tópico ou tecnologia (Blip)")
    private String technologyName;

    @Description("A recomendação oficial: Adote, Experimente, Avalie ou Evite")
    private String ring;

    @Description("Explicação técnica baseada no documento")
    private String explanation;
    
    // Getters e Setters...
}
```

O LangChain4j instrui o modelo a preencher esses campos com base na análise do documento PDF. Isso facilita o consumo da resposta por front-ends ou outros serviços, pois garante que sempre teremos o nome da tecnologia, a recomendação (anel) e a explicação separadamente.

## Cache Semântico (`SemanticCacheService`)

Para otimizar o tempo de resposta e simular um "aprendizado" do sistema, implementamos um cache semântico.

Diferente de um cache tradicional que compara strings exatas, o cache semântico utiliza **embeddings** (vetores numéricos que representam o significado do texto) para identificar perguntas similares.

**Como funciona:**
1.  Quando uma pergunta chega, ela é convertida em um vetor (embedding).
2.  O sistema compara esse vetor com os vetores das perguntas já respondidas e armazenadas em memória.
3.  Se a similaridade (cosseno) for superior a **70%**, o sistema retorna a resposta armazenada anteriormente, evitando uma nova chamada lenta ao modelo LLM.
4.  Caso contrário, a pergunta é processada pelo Ollama e o novo par (pergunta vetorizada + resposta) é salvo no cache.

Isso permite que perguntas como "O que é Java?" e "Me explique sobre Java" sejam tratadas como a mesma consulta, retornando a resposta instantaneamente na segunda vez.

## Como Executar

1.  Coloque o seu arquivo PDF (ex: `document.pdf`) na pasta `src/main/resources`.
2.  Compile e execute o projeto:

```bash
mvn spring-boot:run
```

3.  Acesse o endpoint de chat:

```bash
curl "http://localhost:8080/chat?message=Qual%20a%20recomendação%20para%20Platform%20Engineering?"
```

Ou via navegador: `http://localhost:8080/chat?message=Sua pergunta aqui`
