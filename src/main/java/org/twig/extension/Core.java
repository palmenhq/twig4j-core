package org.twig.extension;

import org.twig.syntax.operator.*;

import java.util.LinkedHashMap;

public class Core implements Extension {
    @Override
    public LinkedHashMap<String, Operator> getUnaryOperators() {
        LinkedHashMap<String, Operator> operators = new LinkedHashMap<>();

        return operators;
    }

    @Override
    public LinkedHashMap<String, Operator> getBinaryOperators() {
        LinkedHashMap<String, Operator> operators = new LinkedHashMap<>();

        operators.put("or", new BinaryOr());
        operators.put("and", new BinaryAnd());
        operators.put("<=", new BinaryLessThanOrEqual());
        operators.put(">=", new BinaryGreaterThanOrEqual());
        operators.put("<", new BinaryLessThan());
        operators.put(">", new BinaryGreaterThan());
        operators.put("==", new BinaryEquals());
        operators.put("!=", new BinaryNotEquals());
        operators.put("starts with", new BinaryStartsWith());
        operators.put("ends with", new BinaryEndsWith());
        operators.put("matches", new BinaryMatches());
        operators.put("+", new BinaryAdd());
        operators.put("-", new BinarySubtract());
        operators.put("~", new BinaryConcat());
        operators.put("**", new BinaryPowerOf());
        operators.put("*", new BinaryMultiply());
        operators.put("//", new BinaryFloorDivide());
        operators.put("/", new BinaryDivide());
        operators.put("%", new BinaryMod());

        return operators;
    }

    @Override
    public String getName() {
        return "Twig Core";
    }
}
