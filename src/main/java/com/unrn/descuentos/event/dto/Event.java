package com.unrn.descuentos.event.dto;

import com.unrn.descuentos.event.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event<K, T> {
    private EventType eventType;
    private K key;
    private T data;
}
