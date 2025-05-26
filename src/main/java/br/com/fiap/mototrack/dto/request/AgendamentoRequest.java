package br.com.fiap.mototrack.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 📦 DTO: AgendamentoRequest
 *
 * Representa os dados recebidos para agendamento de manutenção ou evento de uma moto.
 * Inclui validações e descrição para documentação Swagger.
 *
 * ---
 * @author Rafael
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class AgendamentoRequest {

    // ===========================
    // 🔗 Referência da Moto
    // ===========================

    /**
     * ID da moto a ser agendada.
     */
    @Schema(example = "1", description = "ID da moto a ser agendada")
    @NotNull(message = "O ID da moto é obrigatório.")
    private Long motoId;

    // ===========================
    // 📅 Dados do Agendamento
    // ===========================

    /**
     * Data e hora do agendamento.
     * Deve ser no presente ou futuro.
     */
    @Schema(
            example = "01/06/2025 14:00:00",
            description = "Data e hora do agendamento no formato dd/MM/yyyy HH:mm:ss (não pode ser passada)"
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @FutureOrPresent(message = "A data agendada não pode estar no passado.")
    private LocalDateTime dataAgendada;

    /**
     * Descrição do motivo do agendamento.
     */
    @Schema(example = "Troca de óleo e revisão geral", description = "Descrição da finalidade do agendamento")
    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;
}
