/*
 * @ (#) OrderDetailId.java      1.0      1/17/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package model;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 1/17/2025
 * @version: 1.0
 */

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class OrderDetailId implements Serializable {

    @Column(name = "item_id")
    private String itemId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "topping_id")
    private String toppingId;

}