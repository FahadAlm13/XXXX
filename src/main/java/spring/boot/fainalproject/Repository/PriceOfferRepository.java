package spring.boot.fainalproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import spring.boot.fainalproject.Model.PriceOffer;
import spring.boot.fainalproject.Model.RecyclingRequest;
import spring.boot.fainalproject.Model.Supplier;

import java.util.List;

@Repository
public interface PriceOfferRepository extends JpaRepository<PriceOffer, Integer> {
    PriceOffer findPriceOfferById(Integer id);

    boolean existsBySupplierAndRecyclingRequest(Supplier supplier, RecyclingRequest recyclingRequest);

    List<PriceOffer> findByRecyclingRequestAndStatus(RecyclingRequest recyclingRequest, String status);

//    @Query("SELECT COUNT(po) FROM PriceOffer po JOIN po.suppliers s WHERE s.id = :supplierId AND po.status = 'APPROVED'")
//    int countApprovedPriceOffersBySupplier(@Param("supplierId") Integer supplierId);


}
