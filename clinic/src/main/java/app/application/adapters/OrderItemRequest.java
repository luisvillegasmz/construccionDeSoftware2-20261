package main.java.app.application.adapters;

import app.domain.models.ItemType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    
    private Long itemId;        // ID del medicamento, procedimiento o ayuda diagnóstica
    private ItemType itemType;  // MEDICINE, PROCEDURE, o MEDICALSUPPORT
}
