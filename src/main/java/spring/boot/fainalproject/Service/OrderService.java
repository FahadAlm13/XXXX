package spring.boot.fainalproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.boot.fainalproject.API.ApiException;
import spring.boot.fainalproject.Model.*;
import spring.boot.fainalproject.Repository.*;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;
    private final FacilityRepository facilityRepository;
    private final ProductRepository productRepository;

    //extra endpoint
    public List<Order> getAllOrder(Integer userId){
        if (!orderRepository.findOrdersByFacilityId(userId).isEmpty()){
            return orderRepository.findOrdersByFacilityId(userId);
        } else if (!orderRepository.findOrdersByCustomerId(userId).isEmpty()) {
            return orderRepository.findOrdersByCustomerId(userId);
        }else {
            throw new ApiException("You dont have any orders");
        }
    }

    //extra endpoint
    public Order getOrderById(Integer orderId,Integer userId){
        if (orderRepository.findByOrderIdAndCustomerId(orderId,userId)!=null){
            return orderRepository.findByOrderIdAndCustomerId(orderId,userId);
        } else if (orderRepository.findOrderByIdAndFacilityId(orderId,userId)!=null) {
            return orderRepository.findOrderByIdAndFacilityId(orderId,userId);
        }else {
            throw new ApiException("You dont have any order by this id: "+orderId);
        }
    }

    //extra endpoint
    // this method change
    public void addNewOrder(Integer productId, Order order, Integer userId) {
        // Find the user
        User user = authRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }

        // Find the product
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new ApiException("Product not found");
        }

        // Check the user role
        if (user.getRole().equals("CUSTOMER")) {
            Customer customer = customerRepository.findCustomerById(user.getId());
            order.setCustomer_orders(customer);

            // Validate order quantity
            if (order.getQuantity() > product.getQuantity()) {
                throw new ApiException("Order quantity is greater than product quantity");
            } else {
                if (order.getProducts() == null) {
                    order.setProducts(new HashSet<>()); // Initialize it here
                }
                int totalAmount = 0;
                for (int i = 1; i <= order.getQuantity(); i++) {
                    totalAmount += product.getPrice();
                }
                if (order.getShippingMethod().equals("Priority")) {
                    order.setTotalAmount(totalAmount + 24);
                } else if (order.getShippingMethod().equals("Express")) {
                    order.setTotalAmount(totalAmount + 50);
                } else {
                    order.setTotalAmount(totalAmount);
                }
                // Update product quantity and manage relationships
                product.setQuantity(product.getQuantity() - order.getQuantity());
                order.getProducts().add(product);
                product.getOrders().add(order);

                // Save both order and product
                customerRepository.save(customer);
                orderRepository.save(order);
                productRepository.save(product);
            }
        } else if (user.getRole().equals("FACILITY")) {
            if (order.getQuantity() > product.getQuantity()) {
                throw new ApiException("Order quantity is greater than product quantity");
            } else {
                Facility facility = facilityRepository.findFacilityById(user.getId());
                order.setFacility_orders(facility);

                if (order.getProducts() == null) {
                    order.setProducts(new HashSet<>()); // Initialize it here
                }

                int totalAmount = 0;
                for (int i = 1; i <= order.getQuantity(); i++) {
                    totalAmount += product.getPrice();
                }
                if (order.getShippingMethod().equals("Priority")) {
                    order.setTotalAmount(totalAmount + 24);
                } else if (order.getShippingMethod().equals("Express")) {
                    order.setTotalAmount(totalAmount + 50);
                } else {
                    order.setTotalAmount(totalAmount);
                }
                // Manage relationships
                order.getProducts().add(product);
                product.getOrders().add(order);

                // Save both order and product
                facilityRepository.save(facility);
                orderRepository.save(order);
                productRepository.save(product);
            }
        } else {
            throw new ApiException("Invalid role to add order");
        }
    }

    // this method change
    public void updateOrder(Order order, Integer orderId, Integer userId) {
        // Find the existing order
        Order oldOrder = orderRepository.findOrderById(orderId);
        if (oldOrder == null) {
            throw new ApiException("Order not found");
        }

        // Find the user
        User user = authRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }

        // Find the product
        Product product = productRepository.findProductById(orderId);
        if (product == null) {
            throw new ApiException("Product not found");
        }

        // Calculate the quantity change
        int quantityChange = order.getQuantity() - oldOrder.getQuantity();

        // Check the user role and update accordingly
        if (user.getRole().equalsIgnoreCase("customer")) {
            Customer customer = customerRepository.findCustomerById(userId);
            if (customer == null) {
                throw new ApiException("Customer not found");
            }
            oldOrder.setCustomer_orders(customer);

            // Check if the new quantity is valid
            if (quantityChange > 0) {
                // Increase quantity
                if (order.getQuantity() > product.getQuantity() + oldOrder.getQuantity()) {
                    throw new ApiException("Order quantity is greater than product quantity");
                }
                // Subtract the new quantity from the product's stock
                product.setQuantity(product.getQuantity() - quantityChange);
            } else {
                // Decrease quantity
                product.setQuantity(product.getQuantity() + (-quantityChange));
            }

            // Calculate total amount
            int totalAmount = 0;
            for (int i = 1; i <= order.getQuantity(); i++) {
                totalAmount += product.getPrice();
            }
            if (order.getShippingMethod().equalsIgnoreCase("Priority")) {
                oldOrder.setTotalAmount(totalAmount + 24);
            } else if (order.getShippingMethod().equalsIgnoreCase("Express")) {
                oldOrder.setTotalAmount(totalAmount + 50);
            } else {
                oldOrder.setTotalAmount(totalAmount);
            }

            // Update the existing order details
            oldOrder.setQuantity(order.getQuantity());
            oldOrder.setProductName(order.getProductName());
            oldOrder.setShippingMethod(order.getShippingMethod());

            // Save the updated order and product
            customerRepository.save(customer);
            orderRepository.save(oldOrder);
            productRepository.save(product);
        } else if (user.getRole().equalsIgnoreCase("facility")) {
            Facility facility = facilityRepository.findFacilityById(userId);
            if (facility == null) {
                throw new ApiException("Facility not found");
            }
            oldOrder.setFacility_orders(facility);

            // Check if the new quantity is valid
            if (quantityChange > 0) {
                // Increase quantity
                if (order.getQuantity() > product.getQuantity() + oldOrder.getQuantity()) {
                    throw new ApiException("Order quantity is greater than product quantity");
                }
                // Subtract the new quantity from the product's stock
                product.setQuantity(product.getQuantity() - quantityChange);
            } else {
                // Decrease quantity
                product.setQuantity(product.getQuantity() + (-quantityChange));
            }

            // Calculate total amount
            int totalAmount = 0;
            for (int i = 1; i <= order.getQuantity(); i++) {
                totalAmount += product.getPrice();
            }
            if (order.getShippingMethod().equalsIgnoreCase("Priority")) {
                oldOrder.setTotalAmount(totalAmount + 24);
            } else if (order.getShippingMethod().equalsIgnoreCase("Express")) {
                oldOrder.setTotalAmount(totalAmount + 50);
            } else {
                oldOrder.setTotalAmount(totalAmount);
            }

            // Update the existing order details
            oldOrder.setQuantity(order.getQuantity());
            oldOrder.setProductName(order.getProductName());
            oldOrder.setShippingMethod(order.getShippingMethod());

            // Save the updated order and product
            facilityRepository.save(facility);
            orderRepository.save(oldOrder);
            productRepository.save(product);
        } else {
            throw new ApiException("Invalid role to update order");
        }
    }

    // cancel
    public void deleteOrder(Integer orderId,Integer userId){
        if (orderRepository.findByOrderIdAndCustomerId(orderId,userId)!=null){
            Order order = orderRepository.findByOrderIdAndCustomerId(orderId,userId);
            order.getProducts().remove(productRepository.findProductById(orderId));
            orderRepository.deleteById(orderId);
        }else if (orderRepository.findOrderByIdAndFacilityId(orderId,userId)!=null){
            Order order = orderRepository.findOrderByIdAndFacilityId(orderId,userId);
            order.getProducts().remove(productRepository.findProductById(orderId));
            orderRepository.deleteById(orderId);
        }else {
            throw new ApiException("Order not found");
        }
    }
}

