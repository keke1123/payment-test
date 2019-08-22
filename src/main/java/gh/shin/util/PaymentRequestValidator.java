package gh.shin.util;

import gh.shin.web.value.PaymentRequest;

import static gh.shin.Constants.Category.BEAUTY;
import static gh.shin.Constants.Category.BOOKS;
import static gh.shin.Constants.Category.FASHION;
import static gh.shin.Constants.Category.FOODS;
import static gh.shin.Constants.Category.SPORTS;
import static gh.shin.Constants.LOCATIONS;
import static gh.shin.Constants.PaymentMethod.CARD;
import static gh.shin.Constants.PaymentMethod.CASH;

public class PaymentRequestValidator {
    private PaymentRequestValidator() {
    }

    public static ValidationResult validate(PaymentRequest request) {
        if (!request.getMethodType().equals(CARD) && !request.getMethodType().equals(CASH)) {
            return new ValidationResult(false, request.getMethodType() + " method type is invalid.");
        }
        if(!request.getItemCategory().equals(FOODS) && !request.getItemCategory().equals(BEAUTY)
            && !request.getItemCategory().equals(SPORTS) && !request.getItemCategory().equals(BOOKS)
            && !request.getItemCategory().equals(FASHION)){
            return new ValidationResult(false, request.getItemCategory() + " item category is invalid.");
        }
        if (!LOCATIONS.contains(request.getRegion())) {
            return new ValidationResult(false, request.getRegion() + " region is invalid.");
        }
        return new ValidationResult(true, "");
    }

    public static final class ValidationResult {
        private final boolean isValid;
        private final String msg;

        private ValidationResult(boolean isValid, String msg) {
            this.isValid = isValid;
            this.msg = msg;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getMsg() {
            return msg;
        }
    }
}
