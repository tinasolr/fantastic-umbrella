/*
 * Copyright (C) 2018 Agustina y Nicolas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package swt.view;

/**
 * @author tinar
 */

import com.opencsv.bean.*;
import com.opencsv.exceptions.*;
import java.util.*;
import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.converters.*;
import org.apache.commons.lang3.*;

/**
 * @param <T> Type of the bean to be manipulated
 *
 */
public class ConvertSpanishToBoolean<T> extends AbstractBeanField<T> {

    protected static final String VERDADERO = "si";
    protected static final String FALSO = "no";

    /**
     * Silence code style checker by adding a useless constructor.
     */
    public ConvertSpanishToBoolean() {
    }

    /**
     * Converts Spanish text into a Boolean.
     * The comparisons are case-insensitive. The recognized pairs are
     * verdadero/falso, v/f, si/no, s/n, 1/0.
     * assumes empty string to be false
     *
     * @param value String that should represent a Boolean
     * @return Boolean
     * @throws CsvDataTypeMismatchException   If anything other than the
     *                                        explicitly translated pairs is found
     */
    @Override
    protected Object convert(String value)
            throws CsvDataTypeMismatchException {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        String[] trueStrings = {VERDADERO, "verdadero", "s", "1", "v", "sí"};
        String[] falseStrings = {FALSO, "sobre", "falso", "n", "0", "f"};

        Converter bc = new BooleanConverter(trueStrings, falseStrings);

        try {
            return bc.convert(Boolean.class, value.trim());
        } catch (ConversionException e) {
            CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(
                    value, field.getType(), ResourceBundle
                            .getBundle("convertSpanishToBoolean", errorLocale)
                            .getString("input.not.boolean"));
            csve.initCause(e);
            throw csve;
        }
    }

    /**
     * This method takes the current value of the field in question in the bean
     * passed in and converts it to a string.
     * This implementation returns true/false values in Spanish.
     *
     * @return "si" if true, "no" if false
     * @throws CsvDataTypeMismatchException If the field is not a {@code boolean}
     *   or {@link java.lang.Boolean}
     */
    @Override
    protected String convertToWrite(Object value)
            throws CsvDataTypeMismatchException {
        String result = "";
        try {
            if(value != null) {
                Boolean b = (Boolean) value;
                result = b?VERDADERO:FALSO;
            }
        }
        catch(ClassCastException e) {
            CsvDataTypeMismatchException csve =
                    new CsvDataTypeMismatchException(ResourceBundle
                            .getBundle("convertSpanishToBoolean", errorLocale)
                            .getString("field.not.boolean"));
            csve.initCause(e);
            throw csve;
        }
        return result;
    }

}