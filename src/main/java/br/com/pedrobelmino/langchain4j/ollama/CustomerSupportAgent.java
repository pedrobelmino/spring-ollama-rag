package br.com.pedrobelmino.langchain4j.ollama;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface CustomerSupportAgent {

    @SystemMessage(
            "Você é um agente de suporte técnico especializado no Thoughtworks Technology Radar."+
            "Diretrizes de Resposta:" +
            " 1. Use estritamente os documentos fornecidos." +
            " 2. Identifique claramente o 'Blip' (tecnologia), o 'Quadrante' e o 'Anel' (Adote, Experimente, Avalie, Evite). "+
            " 3. Se a informação não estiver nos documentos, responda: 'Sinto muito, mas não possuo informações sobre esse tema no Radar Vol. 33 "+
            " 4. Priorize explicar o contexto de 'Engenharia de Contexto' e 'Protocolo MCP' quando relevante."
    )
    RadarResponse chat(@UserMessage String userMessage);
}