package com.porter.objects;

import com.porter.objects.order.Order;

/**
 * OrderBuilder.java
 * 
 * Desc: Builds Custom Order object
 * up to a first level of custom object complexity.
 * 
 * Object is too complex too bring the entire structure.
 * 
 * @author fpiagent
 * 
 */
public class OrderBuilder {
	
	public OrderBuilder() {
	}

	public Order build() {
		Order order = new Order();
		
		return order;
	}
}
