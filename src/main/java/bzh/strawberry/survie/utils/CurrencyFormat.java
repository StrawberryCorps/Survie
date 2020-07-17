package bzh.strawberry.survie.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/*
 * This file (CurrencyFormat) is part of a project Survie.
 * It was created on 10/07/2020 09:58 by Eclixal.
 * This file as the whole project shouldn't be modify by others without the express permission from Survie author(s).
 * Also this comment shouldn't get remove from the file. (see Licence.md)
 */
public class CurrencyFormat {

    public String format(double balance) {
        String[] theAmount = BigDecimal.valueOf(balance).toPlainString().split("\\.");
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setGroupingSeparator(' ');
        DecimalFormat decimalFormat = new DecimalFormat("###,###", unusualSymbols);

        String amount;
        try {
            amount = decimalFormat.format(Double.parseDouble(theAmount[0]));
        } catch (NumberFormatException var14) {
            amount = theAmount[0];
        }
        return amount;
    }

}
