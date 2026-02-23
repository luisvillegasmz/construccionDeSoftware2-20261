package main.java.app.domain.services;

import app.application.adapters.OrderItemRequest;
import app.domain.Exceptions.BusinessException;
import app.domain.models.Order;
import app.domain.models.OrderItem;
import app.domain.models.Item;
import app.domain.models.ItemType;
import app.domain.models.Patient;
import app.domain.models.User;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CreateOrder {
    
    /**
     * Crea una nueva orden médica validando todas las reglas de negocio
     * 
     * @param patientId ID del paciente
     * @param doctorId ID del médico
     * @param items Lista de items (medicamentos, procedimientos o ayudas diagnósticas)
     * @return Orden creada
     * @throws BusinessException si alguna validación falla
     */
    public Order execute(Long patientId, Long doctorId, List<OrderItemRequest> items) {
        
        // Validación 1: Verificar que hay items
        if (items == null || items.isEmpty()) {
            throw new BusinessException("La orden debe contener al menos un item");
        }
        
        // Validación 2: REGLA PRINCIPAL - Ayuda diagnóstica no puede ir con medicamentos ni procedimientos
        validateMedicalSupportRule(items);
        
        // Validación 3: No puede haber items duplicados (mismo itemId)
        validateNoDuplicateItems(items);
        
        // Validación 4: Verificar que paciente y doctor existen (simularemos esto)
        // En tu implementación real deberías buscarlos en la BD
        Patient patient = findPatientById(patientId);
        User doctor = findDoctorById(doctorId);
        
        // Crear la orden
        Order order = new Order();
        order.setPatient(patient);
        order.setDoctor(doctor);
        order.setDate(new Date());
        
        // Crear los OrderItems con numeración secuencial desde 1
        List<OrderItem> orderItems = createOrderItems(items);
        order.setOrderItems(orderItems);
        
        // Aquí guardarías la orden en la base de datos
        // return orderRepository.save(order);
        
        return order;
    }
    
    /**
     * VALIDACIÓN CRÍTICA DEL DOCUMENTO:
     * "Cuando se receta una ayuda diagnóstica NO puede recetarse procedimiento ni 
     * medicamento ya que no se tiene certeza del diagnóstico"
     */
    private void validateMedicalSupportRule(List<OrderItemRequest> items) {
        boolean hasMedicalSupport = false;
        boolean hasMedicine = false;
        boolean hasProcedure = false;
        
        for (OrderItemRequest item : items) {
            if (item.getItemType() == ItemType.MEDICALSUPPORT) {
                hasMedicalSupport = true;
            }
            if (item.getItemType() == ItemType.MEDICINE) {
                hasMedicine = true;
            }
            if (item.getItemType() == ItemType.PROCEDURE) {
                hasProcedure = true;
            }
        }
        
        // Si hay ayuda diagnóstica, NO puede haber medicamentos ni procedimientos
        if (hasMedicalSupport && (hasMedicine || hasProcedure)) {
            throw new BusinessException(
                "ERROR: Una orden con ayuda diagnóstica NO puede contener medicamentos ni procedimientos. " +
                "Primero debe verse el resultado de la ayuda diagnóstica."
            );
        }
    }
    
    /**
     * VALIDACIÓN: No puede existir dos elementos dentro de la misma orden que 
     * correspondan al mismo ítem (mismo itemId), a pesar de que uno corresponda 
     * a un medicamento y el otro a un procedimiento
     */
    private void validateNoDuplicateItems(List<OrderItemRequest> items) {
        Set<Long> itemIds = new HashSet<>();
        
        for (OrderItemRequest item : items) {
            if (itemIds.contains(item.getItemId())) {
                throw new BusinessException(
                    "ERROR: El item con ID " + item.getItemId() + 
                    " está duplicado en la orden. No se permiten items repetidos."
                );
            }
            itemIds.add(item.getItemId());
        }
    }
    
    /**
     * Crea los OrderItems con numeración secuencial desde 1
     * Según el documento: "cada medicamento será un ítem comenzando siempre desde el 1"
     */
    private List<OrderItem> createOrderItems(List<OrderItemRequest> items) {
        List<OrderItem> orderItems = new ArrayList<>();
        
        int itemNumber = 1; // Los ítems empiezan desde 1
        
        for (OrderItemRequest itemRequest : items) {
            // Buscar el Item en la base de datos (medicamento, procedimiento o ayuda)
            Item item = findItemById(itemRequest.getItemId());
            
            if (item == null) {
                throw new BusinessException(
                    "El item con ID " + itemRequest.getItemId() + " no existe"
                );
            }
            
            // Crear el OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setItemType(itemRequest.getItemType());
            // Aquí también podrías agregar el número de ítem si lo añades al modelo
            
            orderItems.add(orderItem);
            itemNumber++;
        }
        
        return orderItems;
    }
    
    // ========== MÉTODOS AUXILIARES (SIMULADOS) ==========
    // En tu implementación real, estos métodos consultarían la base de datos
    
    private Patient findPatientById(Long patientId) {
        // Aquí deberías usar tu repositorio:
        // return patientRepository.findById(patientId)
        //     .orElseThrow(() -> new BusinessException("Paciente no encontrado"));
        
        // Simulación temporal
        if (patientId == null) {
            throw new BusinessException("El ID del paciente no puede ser nulo");
        }
        Patient patient = new Patient();
        patient.setId(patientId);
        return patient;
    }
    
    private User findDoctorById(Long doctorId) {
        // Aquí deberías usar tu repositorio:
        // User doctor = userRepository.findById(doctorId)
        //     .orElseThrow(() -> new BusinessException("Doctor no encontrado"));
        // 
        // Validar que el usuario sea médico:
        // if (doctor.getRole() != Role.DOCTOR) {
        //     throw new BusinessException("El usuario no es un médico");
        // }
        // return doctor;
        
        // Simulación temporal
        if (doctorId == null) {
            throw new BusinessException("El ID del doctor no puede ser nulo");
        }
        User doctor = new User();
        doctor.setId(doctorId);
        return doctor;
    }
    
    private Item findItemById(Long itemId) {
        // Aquí deberías usar tu repositorio:
        // return itemRepository.findById(itemId)
        //     .orElseThrow(() -> new BusinessException("Item no encontrado"));
        
        // Simulación temporal
        if (itemId == null) {
            throw new BusinessException("El ID del item no puede ser nulo");
        }
        Item item = new Item();
        item.setId(itemId);
        item.setName("Item " + itemId);
        item.setPrice(100.0);
        return item;
    }
}
