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

import com.opencsv.bean.*;
import com.opencsv.exceptions.*;
import java.util.*;
import org.apache.commons.beanutils.*;
import org.apache.commons.lang3.*;

/**
 *
 * @author tinar
 */
public class ConvertOriginalToInt<T> extends AbstractBeanField<T> {

    protected static final int ORIGINAL = 1;
    protected static final int MIXTO = 2;
    protected static final int NOTORIG = 3;

    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        if (StringUtils.isEmpty(value)) {
            return NOTORIG;
        }
        String[] trueStrings = {"si", "verdadero", "s", "1", "v", "sí", "orignal"};
        String[] mixStrings = {"mixto", "mezcla"};
        String[] falseStrings = {"otro", "no", "falso", "n", "0", "f"};

        try {
            for(String t : trueStrings){
                if(value.equalsIgnoreCase(t)){
                    return ORIGINAL;
                }
            }
            for(String f : falseStrings){
                if(value.equalsIgnoreCase(f)){
                    return NOTORIG;
                }
            }
            for(String m : mixStrings){
                if(value.equalsIgnoreCase(m)){
                    return MIXTO;
                }
            }
            return NOTORIG;
        } catch (ConversionException e) {
            CsvDataTypeMismatchException csve = new CsvDataTypeMismatchException(
                    value, field.getType(), ResourceBundle
                            .getBundle("convertOriginalToInt", errorLocale)
                            .getString("input.not.boolean"));
            csve.initCause(e);
            throw csve;
        }
    }


    @Override
    protected String convertToWrite(Object value)
            throws CsvDataTypeMismatchException {
        String result = "";
        try {
            if(value != null) {
                Integer b = (Integer) value;
                result = (b==ORIGINAL)?"original":(b==MIXTO)?"mixto":"otro";
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
