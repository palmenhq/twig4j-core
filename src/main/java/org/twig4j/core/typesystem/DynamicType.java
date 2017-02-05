package org.twig4j.core.typesystem;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DynamicType implements Comparable<DynamicType> {
    protected Object value;

    public DynamicType(Object value) {
        this.value = value;
    }

    /**
     * Add this dynamic value with another dynamic value.
     *
     * @param value The value to add
     *
     * @return Integer or double, i.e. 1 or 4.2
     *
     * @throws TypeErrorException If any of the values aren't numbers
     */
    public Number add(DynamicType value) {
        return normalizeNumber(
            BigDecimal.valueOf(this.getValueAsNumber().doubleValue())
                .add(BigDecimal.valueOf(value.getValueAsNumber().doubleValue()))
        );
    }

    /**
     * Subtract another dynamic value from this value.
     *
     * @param value The value to subtract
     *
     * @return Integer or double, i.e. 1 or 4.2
     *
     * @throws TypeErrorException If any of the values aren't numbers
     */
    public Number subtract(DynamicType value) {
        return normalizeNumber(
            BigDecimal.valueOf(this.getValueAsNumber().doubleValue())
                .subtract(BigDecimal.valueOf(value.getValueAsNumber().doubleValue()))
        );
    }

    /**
     * Multiply this dynamic value with another dynamic value.
     *
     * @param value The value to multiply with
     *
     * @return Integer or double, i.e. 1 or 4.2
     *
     * @throws TypeErrorException If any of the values aren't numbers
     */
    public Number multiply(DynamicType value) {
        return normalizeNumber(
            BigDecimal.valueOf(this.getValueAsNumber().doubleValue())
                .multiply(BigDecimal.valueOf(value.getValueAsNumber().doubleValue()))
        );
    }

    /**
     * Divide this dynamic value with another dynamic value.
     *
     * @param value The value to divide by
     *
     * @return Integer or double, i.e. 1 or 4.2
     *
     * @throws TypeErrorException If any of the values aren't numbers
     */
    public Number divide(DynamicType value) {
        return normalizeNumber(
            BigDecimal.valueOf(this.getValueAsNumber().doubleValue())
                .divide(BigDecimal.valueOf(value.getValueAsNumber().doubleValue()))
        );
    }

    /**
     * Divide this dynamic value with another dynamic value and floor round it.
     *
     * @param value The value to divide by
     *
     * @return Integer or double, i.e. 1 or 4.2
     *
     * @throws TypeErrorException If any of the values aren't numbers
     */
    public Number floorDivide(DynamicType value) {
        return (int)
            Math.floor(
                BigDecimal.valueOf(this.getValueAsNumber().doubleValue())
                    .divide(BigDecimal.valueOf(value.getValueAsNumber().doubleValue()))
                   .doubleValue()
            );
    }

    /**
     * Set this dynamic value to the power of provided dynamic value, which will always be an Integer
     *
     * @param value The value to multiply with
     *
     * @return Integer or double, i.e. 1 or 4.2
     *
     * @throws TypeErrorException If any of the values aren't numbers
     */
    public Number pow(DynamicType value) {
        return normalizeNumber(
            BigDecimal.valueOf(this.getValueAsNumber().doubleValue())
                .pow(getValueAsNumber().intValue())
        );
    }

    /**
     * Modulo this dynamic value from another dynamic value.
     *
     * @param value The value to do modulo with
     *
     * @return Integer or double, i.e. 1 or 4.2
     *
     * @throws TypeErrorException If any of the values aren't numbers
     */
    public Number mod(DynamicType value) {
        return normalizeNumber(
            BigDecimal.valueOf(this.getValueAsNumber().doubleValue())
                .remainder(BigDecimal.valueOf(value.getValueAsNumber().doubleValue()))
        );
    }

    /**
     * Convert a double to an int if it's even or return it as double if it's still a double
     *
     * @param number The number to normalize
     * @return The normalized number
     */
    protected Number normalizeNumber(BigDecimal number) {
        if (number.setScale(0, RoundingMode.FLOOR).subtract(number).doubleValue() == 0.0 && number.setScale(0, RoundingMode.CEILING).subtract(number).doubleValue() == 0.0) {
            return number.intValue();
        } else {
            return number.doubleValue();
        }
    }

    /**
     * Get this object's value
     *
     * @return Value
     * @throws TypeErrorException Never thrown in this method
     */
    public Object getValue() throws TypeErrorException {
        return value;
    }

    /**
     * Get this object's value as a double, if possible to get it as a double
     *
     * @return A double of this value
     *
     * @throws TypeErrorException If not possible to get value as double
     */
    public Number getValueAsNumber() throws TypeErrorException {
        if (getValue() instanceof Number) {
            return (Number) getValue();
        } else if (getValue() instanceof String && ((String) getValue()).matches("^\\d+(\\.\\d+)?$")) {
            // Is a string that represents a number
            return Double.valueOf((String) getValue());
        } else {
            throw TypeErrorException.thingAsType(String.valueOf(getValue()), "number");
        }
    }

    @Override
    public int compareTo(@NotNull DynamicType o) {
        if (getValueAsNumber().doubleValue() == o.getValueAsNumber().doubleValue()) {
            return 0;
        } else if (getValueAsNumber().doubleValue() < o.getValueAsNumber().doubleValue()) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DynamicType) {
            // This takes care of many cases, i.e. booleans, nulls etc.
            if (obj.toString().equals(this.toString())) {
                return true;
            } else if (((DynamicType) obj).getValue() instanceof Number) {
                return ((DynamicType) obj).getValueAsNumber().doubleValue() == getValueAsNumber().doubleValue();
            } else {
                return getValue().equals(((DynamicType) obj).getValue());
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
