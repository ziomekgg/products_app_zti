package products.app.zti.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import products.app.zti.model.Reservation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String to, String storeName, List<Reservation> items) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Hardware Store | Potwierdzenie Rezerwacji");

        StringBuilder body = new StringBuilder();
        body.append("Twoja rezerwacja została przyjęta!\n\n");
        body.append("Punkt odbioru: ").append(storeName).append("\n");
        body.append("Przedmioty:\n");
        for (Reservation r : items) {
            body.append("- ").append(r.getProduct().getName()).append(" (x").append(r.getQuantity()).append(")\n");
        }
        body.append("\nZapraszamy po odbiór w ciągu 48h.");

        message.setText(body.toString());
        mailSender.send(message);
    }
}