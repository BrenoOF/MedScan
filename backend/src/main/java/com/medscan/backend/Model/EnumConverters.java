package com.medscan.backend.Model;

import com.medscan.backend.Model.Agendamento.ClassificacaoRisco;
import com.medscan.backend.Model.Agendamento.Status;
import com.medscan.backend.Model.Mensagem.RemetenteTipo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

public final class EnumConverters {

    private EnumConverters() {
    }

    @Converter
    public static class StatusConverter extends LabelConverter<Status> {

        public StatusConverter() {
            super(Map.of(
                    Status.PENDENTE, "Pendente",
                    Status.AGENDADO, "Agendado",
                    Status.REJEITADO, "Rejeitado"
            ));
        }
    }

    @Converter
    public static class ClassificacaoRiscoConverter extends LabelConverter<ClassificacaoRisco> {

        public ClassificacaoRiscoConverter() {
            super(Map.of(
                    ClassificacaoRisco.NAO_URGENTE, "Não Urgente",
                    ClassificacaoRisco.POUCO_URGENTE, "Pouco Urgente",
                    ClassificacaoRisco.URGENTE, "Urgente",
                    ClassificacaoRisco.MUITO_URGENTE, "Muito Urgente",
                    ClassificacaoRisco.EMERGENCIA, "Emergência"
            ));
        }
    }

    @Converter
    public static class RemetenteTipoConverter extends LabelConverter<RemetenteTipo> {

        public RemetenteTipoConverter() {
            super(Map.of(
                    RemetenteTipo.PACIENTE, "Paciente",
                    RemetenteTipo.ATENDENTE, "Atendente"
            ));
        }
    }

    public abstract static class LabelConverter<E extends Enum<E>>
            implements AttributeConverter<E, String> {

        private final Map<E, String> labels;

        protected LabelConverter(Map<E, String> labels) {
            this.labels = labels;
        }

        @Override
        public String convertToDatabaseColumn(E value) {
            if (value == null) {
                return null;
            }

            String label = labels.get(value);
            if (label == null) {
                throw new IllegalArgumentException("Enum sem rótulo no banco: " + value);
            }
            return label;
        }

        @Override
        public E convertToEntityAttribute(String label) {
            if (label == null) {
                return null;
            }

            for (Map.Entry<E, String> entry : labels.entrySet()) {
                if (entry.getValue().equals(label)) {
                    return entry.getKey();
                }
            }
            throw new IllegalArgumentException("Rótulo de enum desconhecido: " + label);
        }
    }
}