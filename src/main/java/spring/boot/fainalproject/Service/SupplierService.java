package spring.boot.fainalproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import spring.boot.fainalproject.API.ApiException;
import spring.boot.fainalproject.DTO.SupplierDTO;
import spring.boot.fainalproject.Model.*;
import spring.boot.fainalproject.Repository.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final AuthRepository  authRepository;
    private final OfferRepository offerRepository;
    private final FacilityRequestRepository facilityRequestRepository;
    private final PriceOfferRepository priceOfferRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }
    public void registerSupplier(SupplierDTO supplierDTO) {
        User user = new User();
        user.setUsername(supplierDTO.getUsername());
        user.setName(supplierDTO.getName());
        String hash = new BCryptPasswordEncoder().encode(supplierDTO.getPassword());
        user.setPassword(hash);
        user.setRole("SUPPLIER");
        authRepository.save(user);

        Supplier supplier = new Supplier();
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhoneNumber(supplierDTO.getPhoneNumber());
        supplier.setCommercialRegister(supplierDTO.getCommericalRegister());
        supplier.setLicenseNumber(supplierDTO.getLicenseNumber());

        supplier.setUser(user);
        supplierRepository.save(supplier);
    }
    public void updateSupplier(Integer supplier_id, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findSupplierById(supplier_id);
        if (supplier == null) {
            throw new ApiException("Supplier not found");
        }
        User user = supplier.getUser();
        user.setUsername(supplierDTO.getUsername());
        user.setName(supplierDTO.getName());
        String hash = new BCryptPasswordEncoder().encode(supplierDTO.getPassword());
        user.setPassword(hash);
        user.setRole("SUPPLIER");

        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhoneNumber(supplierDTO.getPhoneNumber());
        supplier.setCommercialRegister(supplierDTO.getCommericalRegister());
        supplier.setLicenseNumber(supplierDTO.getLicenseNumber());

        supplier.setUser(user);

        authRepository.save(user);
        supplierRepository.save(supplier);
    }

    public void deleteSupplier(Integer supplier_id) {
        Supplier supplier = supplierRepository.findSupplierById(supplier_id);
        if (supplier == null) {
            throw new ApiException("Supplier not found");
        }
        User user = supplier.getUser();
        authRepository.delete(user);
        supplierRepository.delete(supplier);
    }
    // Method for accepting a FacilityRequest
    public void acceptFacilityRequest(Integer supplierId, Integer facilityRequestId) {
        // Get the supplier
        Supplier supplier = supplierRepository.findSupplierById(supplierId);

        // Get the FacilityRequest by ID
        FacilityRequest facilityRequest = facilityRequestRepository.findFacilityRequestById(facilityRequestId);

        // Check if the supplier has made an offer for this request
        Offer offer = offerRepository.findOfferBySupplierAndFacilityRequest(supplier, facilityRequest);

        // Set the status of the FacilityRequest to APPROVED
        facilityRequest.setStatus("APPROVED");
        facilityRequestRepository.save(facilityRequest);

        // Approve the supplier's offer
        offer.setStatus("APPROVED");
        offerRepository.save(offer);

        // Reject all other offers for this request
        List<Offer> otherOffers = offerRepository.findByFacilityRequestAndStatusNot(facilityRequest, "APPROVED");
        for (Offer otherOffer : otherOffers) {
            otherOffer.setStatus("REJECTED");
            offerRepository.save(otherOffer);
        }
    }

    // Method for rejecting a FacilityRequest
    public void rejectFacilityRequest(Integer supplierId, Integer facilityRequestId) {
        // Get the supplier
        Supplier supplier = supplierRepository.findSupplierById(supplierId);

        // Get the FacilityRequest by ID
        FacilityRequest facilityRequest = facilityRequestRepository.findFacilityRequestById(facilityRequestId);

        // Get the supplier's offer for this FacilityRequest
        Offer offer = offerRepository.findOfferBySupplierAndFacilityRequest(supplier, facilityRequest);

        // Set the offer's status to REJECTED
        offer.setStatus("REJECTED");
        offerRepository.save(offer);
    }


    public List<Offer> getOffersBySupplierId(Integer supplierId) {
        return offerRepository.findAllBySupplierId(supplierId);
    }


    // Method to count approved price offers and assign badges to the supplier
//    public void updateSupplierBadge(Integer supplierId) {
//        // Find the supplier by ID
//        Supplier supplier = supplierRepository.findSupplierById(supplierId);
//        if (supplier == null) {
//            throw new ApiException("Supplier not found");
//        }
//
//        // Count the number of approved price offers for this supplier
//        int approvedCount = priceOfferRepository.countApprovedPriceOffersBySupplier(supplierId);
//
//        // Assign badge based on the number of approvals
//        if (approvedCount >= 15) {
//            supplier.setBadge("GOLD");
//
//        } else if (approvedCount >= 10) {
//            supplier.setBadge("SILVER");
//        } else if (approvedCount >= 5) {
//            supplier.setBadge("BRONZE");
//            supplierRepository.save(supplier);
//        } else {
//            supplier.setBadge("IRON");
//        }
//
//        // Save the updated supplier
//        supplierRepository.save(supplier);
//    }
}


//    public List<RecyclingRequest> getUpcomingRequests(Integer supplierId) {
//        return recyclingRequestRepository.findRecyclingRequestsBySupplierId(supplierId);
//    }

