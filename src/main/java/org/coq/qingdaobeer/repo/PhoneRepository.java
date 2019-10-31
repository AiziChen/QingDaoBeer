package org.coq.qingdaobeer.repo;

import org.coq.qingdaobeer.record.Phone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface PhoneRepository extends CrudRepository<Phone, Integer> {
    @Query("SELECT p from Phone p")
    Page<Phone> findByPage(Pageable pageable);

    @Query("SELECT p from Phone p WHERE p.phone = :pn")
    Phone findByPhoneNumber(String pn);
}
