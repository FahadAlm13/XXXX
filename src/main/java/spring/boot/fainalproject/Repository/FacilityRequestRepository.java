package spring.boot.fainalproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.fainalproject.Model.Facility;
import spring.boot.fainalproject.Model.FacilityRequest;

import java.util.List;

@Repository
public interface FacilityRequestRepository extends JpaRepository<FacilityRequest,Integer> {
    FacilityRequest findFacilityRequestById(Integer id);

    List<FacilityRequest> findByFacility(Facility facility);
}
