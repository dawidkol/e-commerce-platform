package pl.dk.ecommerceplatform.email;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.email.dtos.ContactDto;

import java.net.URI;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
class EmailController {

    private final EmailService emailService;

    @PostMapping("/contact")
    public ResponseEntity<ContactDto> sendEmail(@Valid @RequestBody ContactDto contactDto) {
        ContactDto createdContactDto = emailService.sendContactMessage(contactDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdContactDto.id())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
}
