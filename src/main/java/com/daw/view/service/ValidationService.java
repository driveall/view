package com.daw.view.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.util.regex.Pattern;

import static com.daw.view.Constants.DEFAULT_REGION;

@Service
public class ValidationService {
    private static final Pattern FULL_EMAIL_STRICT_PATTERN = Pattern
            .compile("[a-z0-9_.&!%+\\-']+@[a-z0-9\\-]+\\.+[a-z]{2,25}$");
    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    public boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        try {
            if (!phoneNumberUtil.isPossibleNumber(phoneNumberUtil.parse(phoneNumber, DEFAULT_REGION))) {
                return false;
            }
        } catch (NumberParseException ex) {
            return false;
        }
        return true;
    }

    public String formatPhoneNumber(String phoneNumber) {
        try {
            return phoneNumberUtil.format(phoneNumberUtil.parse(phoneNumber, DEFAULT_REGION),
                    PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException ex) {
            return null;
        }
    }

    public boolean validateEmailAddress(String emailAddress) {
        if (emailAddress == null || emailAddress.isEmpty()) {
            return false;
        }
        try {
            new InternetAddress(emailAddress).validate();
        } catch (Exception ex) {
            return false;
        }
        return FULL_EMAIL_STRICT_PATTERN.matcher(emailAddress).matches();
    }
}
