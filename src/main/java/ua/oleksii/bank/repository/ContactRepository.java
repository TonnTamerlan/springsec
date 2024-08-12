package ua.oleksii.bank.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.oleksii.bank.model.Contact;

@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {


}
