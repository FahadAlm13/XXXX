package spring.boot.fainalproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.boot.fainalproject.Model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findOrderById(Integer id);


    @Query("SELECT o FROM Order o WHERE o.customer_orders.id = ?1")
    List<Order> findAllByCustomer_orders(Integer userId);

    @Query("SELECT o FROM Order o WHERE o.id = ?1 AND o.customer_orders.id = ?2")
    Order findByOrderIdAndCustomerId(Integer orderId, Integer customerId);

    @Query("SELECT o FROM Order o WHERE o.id = ?1 AND o.facility_orders.id = ?2")
    Order findOrderByIdAndFacilityId(Integer orderId, Integer facilityId);

    @Query("SELECT o FROM Order o WHERE o.facility_orders.id = ?1")
    List<Order> findOrdersByFacilityId(Integer facilityId);

    @Query("SELECT o FROM Order o WHERE o.customer_orders.id = ?1")
    List<Order> findOrdersByCustomerId(Integer customerId);
}
