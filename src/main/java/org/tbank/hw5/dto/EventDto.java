package org.tbank.hw5.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventDto {
    private final String title;
    private final String price;

    public double extractSumFromPrice() {
        if (price == null || price.isEmpty()) {
            return 500;
        }

        StringBuilder numberBuilder = new StringBuilder();

        for (int i = 0; i < price.length(); i++) {
            char c = price.charAt(i);

            if (Character.isDigit(c) || c == '.' || c == ',') {
                if (c == ',') {
                    c = '.';
                }
                numberBuilder.append(c);
            } else if (!numberBuilder.isEmpty()) {
                break;
            }
        }

        if (!numberBuilder.isEmpty()) {
            try {
                return Double.parseDouble(numberBuilder.toString());
            } catch (NumberFormatException e) {
                return 500;
            }
        }

        return 500;
    }
}
