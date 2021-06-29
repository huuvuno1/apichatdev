package nguyenhuuvu.utils;

import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Component;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
public class TwilioUtil {
    public static void sendSms(String numberReceive, String message) {
        try {
            if (Strings.hasText(numberReceive))
                numberReceive = numberReceive.substring(1);
            Twilio.init("AC56692afc59bc138a3dd392d4c4a74360", "1d3ca07994d43555ddead9c8997c3b33");
            Message messages = Message.creator(
                    new com.twilio.type.PhoneNumber("+84" + numberReceive),
                    new com.twilio.type.PhoneNumber("+14095726830"),
                        message)
                    .create();
            System.out.println("da send");

        } catch (Exception e) {

            System.out.println("Loi khi send tin nhan den: " + numberReceive);
            e.printStackTrace();
        }
    }
}
