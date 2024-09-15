package spring.boot.fainalproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.fainalproject.Model.FacilityRequest;
import spring.boot.fainalproject.Model.Offer;
import spring.boot.fainalproject.Model.Supplier;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer,Integer> {
    Offer findOfferById(Integer id);

    List<Offer> findOfferByFacilityRequestId(Integer facilityRequestId);



    Offer findOfferBySupplierAndFacilityRequest(Supplier supplier, FacilityRequest facilityRequest);

    List<Offer> findByFacilityRequestAndStatusNot(FacilityRequest facilityRequest, String status);

    List<Offer> findAllBySupplierId(Integer supplierId);
}
