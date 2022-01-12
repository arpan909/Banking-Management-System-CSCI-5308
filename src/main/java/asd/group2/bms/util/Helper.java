package asd.group2.bms.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@Component
public class Helper {

  static final private String DIGITS = "0123456789";

  final private Random random = new SecureRandom();

  final private Date date = new Date();

  char randomChar() {
    return DIGITS.charAt(random.nextInt(DIGITS.length()));
  }

  public String generateRandomDigits(int length) {
    StringBuilder sb = new StringBuilder();
    while (length > 0) {
      length--;
      sb.append(randomChar());
    }
    return sb.toString();
  }

  public CardDetails generateCardDetails() {
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    int month = localDate.getMonthValue();
    int currentYear = localDate.getYear();

    String cardNumber = generateRandomDigits(16);
    String expiryMonth = String.valueOf(month);
    String expiryYear = String.valueOf(currentYear + 4);

    String pin = String.format("%04d", random.nextInt(AppConstants.FOUR_DIGIT));
    String cvv =
        String.format("%06d", random.nextInt(AppConstants.SIX_DIGIT));

    CardDetails cardDetails = new CardDetails();
    cardDetails.setCardNumber(cardNumber);
    cardDetails.setExpiryMonth(expiryMonth);
    cardDetails.setExpiryYear(expiryYear);
    cardDetails.setPin(pin);
    cardDetails.setCvv(cvv);

    return cardDetails;
  }

}
