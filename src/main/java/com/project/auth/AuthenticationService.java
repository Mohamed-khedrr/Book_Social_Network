package com.project.auth;

import com.project.email.EmailService;
import com.project.email.EmailTemplateName;
import com.project.role.RoleRepository;
import com.project.security.JwtService;
import com.project.user.Token;
import com.project.user.TokenRepository;
import com.project.user.User;
import com.project.user.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository ;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository ;
    private final EmailService emailService ;
    private final AuthenticationManager authenticationManager ;
    private final JwtService jwtService;

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


    public LoginResponse login(LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail() ,
                        request.getPassword()
                )
        );
        var user = ((User) auth.getPrincipal()) ;

//        Extra claims provided in the token
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("fullName", user.getFullName());
        claims.put("id", user.getId());

        String jwtToken = jwtService.generateToken(claims , user);
//            String jwtToken  = "Hello" ;
        return LoginResponse.builder()
                .token(jwtToken)
                .build() ;
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token expired , A new token has been sent");
        }

        User user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
