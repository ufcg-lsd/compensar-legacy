package springboot.util;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

/**
 * Classe que permite definir operações de agregação personalizadas para
 * consultas MongoDB no contexto do Spring Data.
 */
public class CustomAggregationOperation implements AggregationOperation {

    private final Document aggregationOperationDocument;

    /**
     * Construtor que inicializa a operação de agregação com o documento
     * especificado.
     *
     * @param aggregationOperationDocument Documento BSON que representa a operação
     *                                     de agregação personalizada.
     * @throws IllegalArgumentException se o documento fornecido for nulo.
     */
    public CustomAggregationOperation(Document aggregationOperationDocument) {
        if (aggregationOperationDocument == null) {
            throw new IllegalArgumentException("O documento de operação de agregação não pode ser nulo.");
        }
        this.aggregationOperationDocument = aggregationOperationDocument;
    }

    /**
     * Converte a operação de agregação personalizada em um documento compatível com
     * o contexto de agregação do Spring Data.
     *
     * @param context Contexto de operação de agregação usado para mapear a
     *                operação.
     * @return Documento BSON mapeado para o contexto de agregação.
     */
    @Override
    public Document toDocument(AggregationOperationContext context) {
        return context.getMappedObject(aggregationOperationDocument);
    }
}
