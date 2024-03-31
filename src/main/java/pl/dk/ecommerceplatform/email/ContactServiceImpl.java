package pl.dk.ecommerceplatform.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.email.dtos.ContactDto;
import pl.dk.ecommerceplatform.email.dtos.ContactResponseDto;
import pl.dk.ecommerceplatform.error.exceptions.email.ContactNotFoundException;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class ContactServiceImpl implements ContactService {

    private final JavaMailSender javaMailSender;
    private final ContactRepository contactRepository;

    @Value("${app.mail.username}")
    private String email;
    private final Logger logger = UtilsService.getLogger(this.getClass());

    @Override
    @Transactional
    public ContactDto sendContactMessage(ContactDto contactDto) {
        Contact contactToSave = ContactDtoMapper.map(contactDto);
        Contact savedContact = contactRepository.save(contactToSave);
        return ContactDtoMapper.map(savedContact);
    }

    @Override
    @Transactional
    public void sendResponseMessage(ContactResponseDto contactResponseDto) {
        Long contactId = contactResponseDto.contactId();
        String replyMessage = contactResponseDto.response();
        Contact contact = contactRepository.findById(contactId).
                orElseThrow(() -> new ContactNotFoundException("Contact with id %d not found".formatted(contactId)));
        contact.setReplyDate(LocalDateTime.now());
        contact.setReplyMessage(replyMessage);
    }
}
