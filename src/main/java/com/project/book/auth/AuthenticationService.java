package com.project.book.auth;

import com.project.book.email.EmailService;
import com.project.book.email.EmailTemplateName;
import com.project.book.role.RoleRepository;
import com.project.book.user.Token;
import com.project.book.user.TokenRepository;
import com.project.book.user.User;
import com.project.book.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository ;
    private final EmailService emailService ;

    @Value("${spring.application.mailing.frontend.activation-url}")
    private String activationUrl ;


    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(()-> new RuntimeException("User Role not found"));
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build() ;
        userRepository.save(user);
        sendValidationEmail(user) ;

    }

    private void sendValidationEmail(User user) throws MessagingException {
        String newToken = generateActivationToken(user) ;
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT ,
                activationUrl ,
                newToken,
                "Account Activation"
        );

    }

    private String generateActivationToken(User user) {
        String generatedToken = generateActivationCode(6) ;
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build() ;

        tokenRepository.save(token) ;
        return generatedToken ;

    }

    private String generateActivationCode(int length) {
        String chars = "0123456789";
        StringBuilder stringBuilder = new StringBuilder(length);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++ ){
            int randomIndex = random.nextInt(chars.length());
            stringBuilder.append(chars.charAt(randomIndex));
        }

        return stringBuilder.toString() ;
    }


}
