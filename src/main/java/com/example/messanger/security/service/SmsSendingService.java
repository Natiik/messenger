package com.example.messanger.security.service;

import com.example.messanger.exception.SmsSendingException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsSendingService {
    @Value("${messenger.twillio.account-sid}")
    private String ACCOUNT_SID;

    @Value("${messenger.twillio.auth-token}")
    private String AUTH_TOKEN;

    @Value("${messenger.twillio.mes-ser-id}")
    private String MESSAGING_SERVICE_ID;

    private final static String SMS_TEMPLATE = "Your code is %s ";

    public void sendTmpPassword(String phoneNumber, String password){
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            MessageCreator creator = Message.creator(
                    new PhoneNumber(phoneNumber), MESSAGING_SERVICE_ID,
                    SMS_TEMPLATE.formatted(password));
            creator.create();
        }catch (Exception e){
            log.error("Failed to send sms with password to phoneNumber {}", phoneNumber, e);
            throw new SmsSendingException(e.getMessage());
        }
    }
}
