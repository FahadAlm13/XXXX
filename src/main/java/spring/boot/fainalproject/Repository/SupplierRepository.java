package spring.boot.fainalproject.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.boot.fainalproject.Model.Supplier;
@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Integer> {
    Supplier findSupplierById(Integer id);


}
