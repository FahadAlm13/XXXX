package spring.boot.fainalproject.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.boot.fainalproject.API.ApiException;
import spring.boot.fainalproject.Model.PriceOffer;
import spring.boot.fainalproject.Model.RecyclingRequest;
import spring.boot.fainalproject.Model.Supplier;
import spring.boot.fainalproject.Repository.PriceOfferRepository;
import spring.boot.fainalproject.Repository.RecyclingRequestRepository;
import spring.boot.fainalproject.Repository.SupplierRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PriceOfferService {

    private final PriceOfferRepository priceOfferRepository;
    private final SupplierRepository supplierRepository;
    private final RecyclingRequestRepository recyclingRequestRepository;

    public List<PriceOffer> getAllPriceOffers() {
        return priceOfferRepository.findAll();
    }

    public PriceOffer getPriceOfferById(Integer id) {
        PriceOffer priceOffer = priceOfferRepository.findPriceOfferById(id);
        if (priceOffer == null) {
            throw new ApiException("Price Offer not found");
        }
        return priceOffer;
    }
    // Supplier creates a price offer
    public void createPriceOffer(Integer supplierId, Integer recyclingRequestId, double price) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ApiException("Supplier not found"));

        RecyclingRequest recyclingRequest = recyclingRequestRepository.findById(recyclingRequestId)
                .orElseThrow(() -> new ApiException("Recycling Request not found"));

        // Ensure the supplier has not already made an offer for this request
        if (priceOfferRepository.existsBySupplierAndRecyclingRequest(supplier, recyclingRequest)) {
            throw new ApiException("Price offer already submitted for this request.");
        }

        // Create and save the price offer
        PriceOffer priceOffer = new PriceOffer();
        priceOffer.setPrice(price);
        priceOffer.setStatus("PENDING");
        priceOffer.setSupplier(supplier);
        priceOffer.setRecyclingRequest(recyclingRequest);
        priceOfferRepository.save(priceOffer);
    }
    // Facility approves a price offer
    public void approvePriceOffer(Integer facilityId, Integer priceOfferId) {
        PriceOffer priceOffer = priceOfferRepository.findById(priceOfferId)
                .orElseThrow(() -> new ApiException("Price Offer not found"));

        RecyclingRequest recyclingRequest = priceOffer.getRecyclingRequest();
        if (!recyclingRequest.getFacility_recycle().getId().equals(facilityId)) {
            throw new ApiException("Facility not authorized to approve this offer.");
        }

        priceOffer.setStatus("APPROVED");
        priceOfferRepository.save(priceOffer);

        // Reject other offers for this recycling request
        List<PriceOffer> otherOffers = priceOfferRepository.findByRecyclingRequestAndStatus(recyclingRequest, "PENDING");
        otherOffers.forEach(offer -> {
            offer.setStatus("REJECTED");
            priceOfferRepository.save(offer);
        });
    }

    // Facility rejects a price offer
    public void rejectPriceOffer(Integer facilityId, Integer priceOfferId) {
        PriceOffer priceOffer = priceOfferRepository.findById(priceOfferId)
                .orElseThrow(() -> new ApiException("Price Offer not found"));

        RecyclingRequest recyclingRequest = priceOffer.getRecyclingRequest();
        if (!recyclingRequest.getFacility_recycle().getId().equals(facilityId)) {
            throw new ApiException("Facility not authorized to reject this offer.");
        }

        priceOffer.setStatus("REJECTED");
        priceOfferRepository.save(priceOffer);
    }
}
//    public void createPriceOffer(Integer recycle_id, Integer supplier_id, PriceOffer priceOffer) {
//        RecyclingRequest recyclingRequest = recyclingRequestRepository.findRecyclingRequestById(recycle_id);
//        if (recyclingRequest == null) {
//            throw new ApiException("Recycling Request not found");
//        }
//
//        Supplier supplier = supplierRepository.findSupplierById(supplier_id);
//        if (supplier == null) {
//            throw new ApiException("Supplier not found");
//        }
//
//        // Check if the supplier has already submitted a price offer for this recycling request
//        if (recyclingRequest.getPrice_offer() != null) {
//            for (Supplier existingSupplier : recyclingRequest.getPrice_offer().getSuppliers()) {
//                if (existingSupplier.getId() == supplier_id) {
//                    throw new ApiException("You have already submitted a price offer for this recycling request.");
//                }
//            }
//        }
//
//        priceOffer.setStatus("PENDING");
//
//        priceOffer.getSuppliers().add(supplier); // Ensure supplier is added to the price offer
//
//
//        recyclingRequest.setPrice_offer(priceOffer);// Set the price offer for the recycling request
//
//        priceOfferRepository.save(priceOffer);
//        recyclingRequestRepository.save(recyclingRequest); // Save the price offer

        // Update supplier's badge based on how many recycling requests they've participated in
//        int recyclingRequestCount = recyclingRequestRepository.countRecyclingRequestsBySupplier(supplier_id);
//
//        if (recyclingRequestCount >= 15) {
//            supplier.setBadge("GOLD");
//        } else if (recyclingRequestCount >= 10) {
//            supplier.setBadge("SILVER");
//        } else if (recyclingRequestCount >= 2) {
//            supplier.setBadge("BRONZE");
//        } else {
//            supplier.setBadge("IRON");
//        }
//
//        // Save the updated supplier with the new badge
//        supplierRepository.save(supplier);
//    }

//    public void updatePriceOffer(Integer priceOfferId, Integer supplierId, PriceOffer updatedPriceOffer) {
//        PriceOffer existingPriceOffer = priceOfferRepository.findPriceOfferById(priceOfferId);
//        if (existingPriceOffer == null) {
//            throw new ApiException("Price Offer not found");
//        }
//
//        // Ensure that the supplier is the one who created this price offer
//        boolean supplierFound = false;
//        for (Supplier supplier : existingPriceOffer.getSuppliers()) {
//            if (supplier.getId() == supplierId) {
//                supplierFound = true;
//                break;
//            }
//        }
//
//        if (!supplierFound) {
//            throw new ApiException("You do not have permission to update this price offer.");
//        }
//
//        existingPriceOffer.setPrice(updatedPriceOffer.getPrice());
//        existingPriceOffer.setStatus("PENDING");
//        priceOfferRepository.save(existingPriceOffer);
//    }
//    public void cancelPriceOffer(Integer priceOfferId, Integer supplierId) {
//        PriceOffer existingPriceOffer = priceOfferRepository.findPriceOfferById(priceOfferId);
//        if (existingPriceOffer == null) {
//            throw new ApiException("Price Offer not found");
//        }
//
//        // Ensure that the supplier is the one who created this price offer
//        boolean supplierFound = false;
//        for (Supplier supplier : existingPriceOffer.getSuppliers()) {
//            if (supplier.getId() == supplierId) {
//                supplierFound = true;
//                break;
//            }
//        }
//
//        if (!supplierFound) {
//            throw new ApiException("You do not have permission to delete this price offer.");
//        }
//        existingPriceOffer.setStatus("CANCELLED");
//        priceOfferRepository.save(existingPriceOffer);
//    }


    //}

