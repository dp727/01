package com.community.marketplace.dto;

import com.community.marketplace.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemWithDistance {
    private Item item;
    private double distanceKm;
}
