package spring.boot.fainalproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.boot.fainalproject.API.ApiException;
import spring.boot.fainalproject.DTO.FacilityDTO;
import spring.boot.fainalproject.Model.*;
import spring.boot.fainalproject.Repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {
    private final FacilityRepository facilityRepository;
    private final AuthRepository authRepository;
    private final FacilityRequestRepository facilityRequestRepository;
    private final OfferRepository offerRepository;
    private final PriceOfferRepository priceOfferRepository;
    private final RecyclingRequestRepository recyclingRequestRepository;
    private final SupplierRepository supplierRepository;

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();

    }

    public void registerFacility(FacilityDTO facilityDTO) {
        User user = new User();
        user.setUsername(facilityDTO.getUsername());
        user.setName(facilityDTO.getName());

        String hashedPassword = new BCryptPasswordEncoder().encode(facilityDTO.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("FACILITY");

        authRepository.save(user);

        Facility facility = new Facility();

        facility.setEmail(facilityDTO.getEmail());
        facility.setPhoneNumber(facilityDTO.getPhoneNumber());
        facility.setCommericalRegister(facilityDTO.getCommericalRegister());
        facility.setLicenseNumber(facilityDTO.getLicenseNumber());

        facility.setUser(user);
        facilityRepository.save(facility);
    }

    public void updateFacility(Integer facility_id, FacilityDTO facilityDTO) {

        Facility existingFacility = facilityRepository.findFacilityById(facility_id);
        if (existingFacility == null) {
            throw new ApiException("Facility not found");
        }
        User user = existingFacility.getUser();
        user.setUsername(facilityDTO.getUsername());
        user.setName(facilityDTO.getName());
        String hashedPassword = new BCryptPasswordEncoder().encode(facilityDTO.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("FACILITY");

        existingFacility.setEmail(facilityDTO.getEmail());
        existingFacility.setPhoneNumber(facilityDTO.getPhoneNumber());
        existingFacility.setCommericalRegister(facilityDTO.getCommericalRegister());
        existingFacility.setLicenseNumber(facilityDTO.getLicenseNumber());

        existingFacility.setUser(user);

        authRepository.save(user);
        facilityRepository.save(existingFacility);
    }

    public void deleteFacility(Integer facility_id) {
        Facility existingFacility = facilityRepository.findFacilityById(facility_id);
        if (existingFacility == null) {
            throw new ApiException("Facility not found");
        }

        User user = existingFacility.getUser();
        authRepository.delete(user);
        facilityRepository.delete(existingFacility);
    }

    public void acceptOffer(Integer facilityId, Integer facilityRequestId, Integer offerId) {
        // Fetch the FacilityRequest by ID
        FacilityRequest facilityRequest = facilityRequestRepository.findFacilityRequestById(facilityRequestId);

        // Check if the facility associated with the request matches the provided facilityId
        if (!facilityRequest.getFacility().getId().equals(facilityId)) {
            throw new ApiException("Facility does not have permission to accept this offer");
        }

        // Fetch the Offer by ID
        Offer acceptedOffer = offerRepository.findOfferById(offerId);

        // Check if the offer belongs to the facility request
        if (!acceptedOffer.getFacilityRequest().getId().equals(facilityRequestId)) {
            throw new ApiException("Offer does not belong to the facility request");
        }

        // Set the status of the accepted offer to APPROVED
        acceptedOffer.setStatus("APPROVED");
        offerRepository.save(acceptedOffer);

        // Reject all other offers for this facility request
        List<Offer> otherOffers = offerRepository.findOfferByFacilityRequestId(facilityRequestId);
        for (Offer offer : otherOffers) {
            if (!offer.getId().equals(offerId)) {
                offer.setStatus("REJECTED");
                offerRepository.save(offer);
            }
        }
    }


    public List<FacilityRequest> getAllRequestsMadeByFacility(Integer userId) {
        Facility facility = facilityRepository.findFacilityById(userId);
        List<FacilityRequest> requests = facilityRequestRepository.findByFacility(facility);
        return requests;

    }


    public void rejectOffer(Integer facilityId, Integer facilityRequestId, Integer offerId) {

        FacilityRequest facilityRequest = facilityRequestRepository.findFacilityRequestById(facilityRequestId);


        if (!facilityRequest.getFacility().getId().equals(facilityId)) {
            throw new ApiException("Facility does not have permission to reject this offer");
        }


        Offer rejectedOffer = offerRepository.findOfferById(offerId);


        if (!rejectedOffer.getFacilityRequest().getId().equals(facilityRequestId)) {
            throw new ApiException("Offer does not belong to the facility request");
        }


        rejectedOffer.setStatus("REJECTED");
        offerRepository.save(rejectedOffer);
    }

    // للريسايكل
//    public void approvePriceOffer(Integer facilityId, Integer priceOfferId) {
//        // Fetch the PriceOffer by ID
//        PriceOffer priceOffer = priceOfferRepository.findPriceOfferById(priceOfferId);
//
//        // Ensure the status is PENDING before approving
//        if (!"PENDING".equals(priceOffer.getStatus())) {
//            throw new IllegalStateException("PriceOffer cannot be approved as it is not in PENDING status");
//        }
//
//
//        // Fetch the RecyclingRequest associated with this PriceOffer
//        RecyclingRequest recyclingRequest = recyclingRequestRepository.findByPriceOfferId(priceOfferId);
//
//        // Ensure the Facility is linked to the RecyclingRequest
//        if (!recyclingRequest.getFacility_recycle().getId().equals(facilityId)) {
//            throw new IllegalStateException("Facility does not have the right to approve this PriceOffer");
//        }
//
//        // Approve the PriceOffer
//        priceOffer.setStatus("APPROVED");
//        priceOfferRepository.save(priceOffer);
//
//        // Update the associated RecyclingRequest status to APPROVED
////        recyclingRequest.setStatus("APPROVED");
//        recyclingRequestRepository.save(recyclingRequest);
//    }
}

//        if (priceOffer.getSuppliers() != null && !priceOffer.getSuppliers().isEmpty()) {
//            // Fetch the first supplier
//            Supplier supplier = priceOffer.getSuppliers().iterator().next();
//            updateSupplierBadge(supplier);
//        } else {
//            throw new ApiException("No supplier found for this price offer.");
//        }
//    }
    // Badge update logic for supplier
//    private void updateSupplierBadge(Supplier supplier) {
//        int approvedRequestsCount = recyclingRequestRepository.countApprovedRequestsBySupplier(supplier.getId());
//
//        if (approvedRequestsCount >= 15) {
//            supplier.setBadge("GOLD");
//        } else if (approvedRequestsCount >= 10) {
//            supplier.setBadge("SILVER");
//        } else if (approvedRequestsCount >= 2) {
//            supplier.setBadge("BRONZE");
//        } else {
//            supplier.setBadge("IRON");
//        }
//
//        supplierRepository.save(supplier);
//    }

//    public void rejectPriceOffer(Integer facilityId, Integer priceOfferId) {
//        // Fetch the PriceOffer by ID
//        PriceOffer priceOffer = priceOfferRepository.findPriceOfferById(priceOfferId);
//
//        // Ensure the status is PENDING before rejecting
//        if (!"PENDING".equals(priceOffer.getStatus())) {
//            throw new IllegalStateException("PriceOffer cannot be rejected as it is not in PENDING status");
//        }
//
//        // Fetch the RecyclingRequest associated with this PriceOffer
//        RecyclingRequest recyclingRequest = recyclingRequestRepository.findByPriceOfferId(priceOfferId);
//
//        // Ensure the Facility is linked to the RecyclingRequest
//        if (!recyclingRequest.getFacility_recycle().getId().equals(facilityId)) {
//            throw new IllegalStateException("Facility does not have the right to reject this PriceOffer");
//        }
//
//        // Reject the PriceOffer
//        priceOffer.setStatus("REJECTED");
//        priceOfferRepository.save(priceOffer);
//    }

//}
