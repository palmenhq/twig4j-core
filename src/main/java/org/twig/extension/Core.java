package org.twig.extension;

import org.twig.syntax.operator.*;

import java.util.HashMap;

public class Core implements Extension {
    @Override
    public HashMap<String, Operator> getUnaryOperators() {
        HashMap<String, Operator> operators = new HashMap<>();

        return operators;
    }

    @Override
    public HashMap<String, Operator> getBinaryOperators() {
        HashMap<String, Operator> operators = new HashMap<>();

        operators.put("+", new BinaryAdd());

        return operators;
    }

    @Override
    public String getName() {
        return "Twig Core";
    }
}
