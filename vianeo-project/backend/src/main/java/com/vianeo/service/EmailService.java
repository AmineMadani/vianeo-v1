package com.vianeo.service;

import com.vianeo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendVerificationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Vérification de votre compte Vianeo");
        message.setText(String.format(
            "Bonjour %s %s,\n\n" +
            "Merci de vous être inscrit sur Vianeo.\n\n" +
            "Veuillez cliquer sur le lien suivant pour vérifier votre adresse email :\n" +
            "http://localhost:8080/api/auth/verify?token=%s\n\n" +
            "Votre compte sera activé par un administrateur après vérification.\n\n" +
            "Cordialement,\n" +
            "L'équipe Vianeo",
            user.getPrenom(), user.getNom(), user.getVerificationToken()
        ));
        
        mailSender.send(message);
    }

    public void sendInvitationEmail(User user, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Invitation à rejoindre Vianeo");
        message.setText(String.format(
            "Bonjour %s %s,\n\n" +
            "Vous avez été invité à rejoindre la plateforme Vianeo.\n\n" +
            "Vos identifiants de connexion :\n" +
            "Email : %s\n" +
            "Mot de passe temporaire : %s\n\n" +
            "Veuillez vous connecter et changer votre mot de passe lors de votre première connexion.\n\n" +
            "Lien de connexion : http://localhost:4200/login\n\n" +
            "Cordialement,\n" +
            "L'équipe Vianeo",
            user.getPrenom(), user.getNom(), user.getEmail(), tempPassword
        ));
        
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(User user, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Réinitialisation de votre mot de passe Vianeo");
        message.setText(String.format(
            "Bonjour %s %s,\n\n" +
            "Vous avez demandé la réinitialisation de votre mot de passe.\n\n" +
            "Veuillez cliquer sur le lien suivant pour réinitialiser votre mot de passe :\n" +
            "http://localhost:4200/reset-password?token=%s\n\n" +
            "Ce lien expirera dans 24 heures.\n\n" +
            "Si vous n'avez pas demandé cette réinitialisation, ignorez ce message.\n\n" +
            "Cordialement,\n" +
            "L'équipe Vianeo",
            user.getPrenom(), user.getNom(), resetToken
        ));
        
        mailSender.send(message);
    }
}