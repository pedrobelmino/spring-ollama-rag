package br.com.pedrobelmino.langchain4j.ollama;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.CosineSimilarity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SemanticCacheService {

    private final CustomerSupportAgent agent;
    private final EmbeddingModel embeddingModel;
    
    private final List<CacheEntry> cache = new ArrayList<>();

    public SemanticCacheService(CustomerSupportAgent agent, EmbeddingModel embeddingModel) {
        this.agent = agent;
        this.embeddingModel = embeddingModel;
    }

    public RadarResponse chat(String userMessage) {
        Embedding currentEmbedding = embeddingModel.embed(userMessage).content();

        for (CacheEntry entry : cache) {
            double similarity = CosineSimilarity.between(currentEmbedding, entry.embedding);
            
            if (similarity > 0.70) {
                return entry.response;
            }
        }

        RadarResponse response = agent.chat(userMessage);

        cache.add(new CacheEntry(currentEmbedding, response));
        
        return response;
    }

    private static class CacheEntry {
        Embedding embedding;
        RadarResponse response;

        public CacheEntry(Embedding embedding, RadarResponse response) {
            this.embedding = embedding;
            this.response = response;
        }
    }
}
