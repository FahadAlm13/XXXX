package spring.boot.fainalproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.boot.fainalproject.Model.RecyclingRequest;

import java.util.List;

@Repository
public interface RecyclingRequestRepository extends JpaRepository<RecyclingRequest, Integer> {
    RecyclingRequest findRecyclingRequestById(Integer id);

    @Query("SELECT r FROM RecyclingRequest r WHERE r.price_offer.id = :priceOfferId")
    RecyclingRequest findByPriceOfferId(@Param("priceOfferId") Integer priceOfferId);

    @Query("SELECT rr FROM RecyclingRequest rr WHERE rr.price_offer.status = 'PENDING'")
    List<RecyclingRequest> findAllWithPendingPriceOffer();

    @Query("SELECT COUNT(r) FROM RecyclingRequest r WHERE r.supplier_recycle.id =?1 AND r.status = 'APPROVED'")
    int countApprovedRequestsBySupplier(@Param("supplierId") Integer supplierId);


    // Custom query to find all recycling requests by supplier ID
//    @Query("SELECT r FROM RecyclingRequest r WHERE r.supplier_recycle.id = :supplierId")
//    List<RecyclingRequest> findRecyclingRequestsBySupplierId(Integer supplierId);

//    @Query("SELECT COUNT(r) FROM RecyclingRequest r JOIN r.supplier_recycle s WHERE s.id = :supplierId")
//    int countRecyclingRequestsBySupplier(@Param("supplier_Id") Integer supplierId);
//
//    @Query("SELECT r FROM RecyclingRequest r")
//    List<RecyclingRequest> findAllRecyclingRequests();
}
