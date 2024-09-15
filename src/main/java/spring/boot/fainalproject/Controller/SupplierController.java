package spring.boot.fainalproject.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import spring.boot.fainalproject.API.ApiResponse;
import spring.boot.fainalproject.DTO.SupplierDTO;
import spring.boot.fainalproject.Model.Offer;
import spring.boot.fainalproject.Model.RecyclingRequest;
import spring.boot.fainalproject.Model.User;
import spring.boot.fainalproject.Repository.PriceOfferRepository;
import spring.boot.fainalproject.Service.SupplierService;

;import java.util.List;

@RestController
@RequestMapping("/api/v1/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;
    private final PriceOfferRepository priceOfferRepository;

    @GetMapping
    public ResponseEntity getAllSuppliers() {
       return ResponseEntity.status(200).body(supplierService.getAllSuppliers());
    }
    @PostMapping
    public ResponseEntity registerSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        supplierService.registerSupplier(supplierDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Successes add supplier"));
    }
    @PutMapping
    public ResponseEntity updateSupplier(@AuthenticationPrincipal User user, @Valid @RequestBody SupplierDTO supplierDTO) {
        supplierService.updateSupplier(user.getId(), supplierDTO);
        return ResponseEntity.status(200).body(supplierService.getAllSuppliers());
    }
    @DeleteMapping
    public ResponseEntity deleteSupplier(@AuthenticationPrincipal User user, @PathVariable Integer id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.status(200).body(supplierService.getAllSuppliers());
    }
    // Accept a FacilityRequest
    @PutMapping("/accept-request/{facilityRequestId}")
    public ResponseEntity acceptFacilityRequest(@AuthenticationPrincipal User user,
                                                @PathVariable Integer facilityRequestId) {
        supplierService.acceptFacilityRequest(user.getId(), facilityRequestId);
        return ResponseEntity.status(200).body(new ApiResponse("Facility request accepted successfully"));
    }

    // Reject a FacilityRequest
    @PutMapping("/reject-request/{facilityRequestId}")
    public ResponseEntity rejectFacilityRequest(@AuthenticationPrincipal User user,
                                                @PathVariable Integer facilityRequestId) {
        supplierService.rejectFacilityRequest(user.getId(), facilityRequestId);
        return ResponseEntity.status(200).body(new ApiResponse("Facility request rejected successfully"));
    }
    @GetMapping("/offers")
    public ResponseEntity<List<Offer>> getSupplierOffers(@AuthenticationPrincipal User user) {
        List<Offer> offers = supplierService.getOffersBySupplierId(user.getId());
        return ResponseEntity.status(200).body(offers);
    }

    // Endpoint to update and check the supplier's badge
//    @PutMapping("/update-badge")
//    public ResponseEntity<ApiResponse> updateSupplierBadge(@AuthenticationPrincipal User user) {
//        // Update the badge for the logged-in supplier
//        supplierService.updateSupplierBadge(user.getId());
//
//        return ResponseEntity.status(200).body(new ApiResponse("Badge updated based on the approved price offers"));
//    }


//    @GetMapping("/requests-recycle")
//    public ResponseEntity getUpcomingRequests(
//            @AuthenticationPrincipal User user) {
//
//        List<RecyclingRequest> requests = supplierService.getUpcomingRequests( user.getId());
//
//        return ResponseEntity.status(200).body(requests);
//    }
}
