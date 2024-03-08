package pl.dk.ecommerceplatform.email;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dk.ecommerceplatform.email.dtos.CreateEmailDto;

@RestController
@RequestMapping("/email")
@AllArgsConstructor
class EmailController {

    private final EmailService emailService;

    @PostMapping("/contact")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody CreateEmailDto createEmailDto) {
        emailService.sendContactMessage(createEmailDto);
        return ResponseEntity.ok().build();
    }
}
