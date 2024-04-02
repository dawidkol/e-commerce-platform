package pl.dk.ecommerceplatform.email;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.email.dtos.ContactDto;
import pl.dk.ecommerceplatform.email.dtos.ContactResponseDto;

import java.net.URI;

@RestController
@RequestMapping("/contact")
@AllArgsConstructor
class ContactController {

    private final ContactService contactService;

    @PostMapping("")
    public ResponseEntity<ContactDto> postContactMessage(@Valid @RequestBody ContactDto contactDto) {
        ContactDto dto = contactService.postContactMessage(contactDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping("/response")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    public ResponseEntity<ContactResponseDto> sendResponse(@Valid @RequestBody ContactResponseDto contactResponseDto) {
        contactService.createResponseMessage(contactResponseDto);
        return ResponseEntity.ok(contactResponseDto);
    }
}