package ua.oleksii.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.oleksii.bank.model.Contact;
import ua.oleksii.bank.repository.ContactRepository;

import java.sql.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class ContactController {

    private final ContactRepository contactRepository;

    @PostMapping("/contact")
    @PreFilter("filterObject.subject != 'Test'")
    public Contact saveContactInquiryDetails(@RequestBody List<Contact> contacts) {
        if (!contacts.isEmpty()) {
            var contact = contacts.getFirst();
            contact.setContactId(getServiceReqNumber());
            contact.setCreateDt(new Date(System.currentTimeMillis()));
            return contactRepository.save(contact);
        }
        var contact = new Contact();
        contact.setSubject("Test subject");
        contact.setContactId("test-contact-id");
        contact.setCreateDt(new Date(System.currentTimeMillis()));
        contact.setMessage("Test message");
        return contact;

    }

    public String getServiceReqNumber() {
        Random random = new Random();
        int ranNum = random.nextInt(999999999 - 9999) + 9999;
        return "SR" + ranNum;
    }
}
