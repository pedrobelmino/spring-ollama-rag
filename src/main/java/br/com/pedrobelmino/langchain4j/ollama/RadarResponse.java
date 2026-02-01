package br.com.pedrobelmino.langchain4j.ollama;

import dev.langchain4j.model.output.structured.Description;

public class RadarResponse {
    @Description("Nome do tópico ou tecnologia (Blip)")
    private String technologyName;

    @Description("A recomendação oficial: Adote, Experimente, Avalie ou Evite")
    private String ring;

    @Description("Explicação técnica baseada no documento")
    private String explanation;

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
        this.technologyName = technologyName;
    }

    public String getRing() {
        return ring;
    }

    public void setRing(String ring) {
        this.ring = ring;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
