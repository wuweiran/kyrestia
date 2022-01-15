package clan.midnight.kyrestia.infra.el;

import clan.midnight.kyrestia.infra.el.conversion.DataConversion;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class SimpleProposition {
    private static final String[] SUPPORTING_OPERATORS = new String[]{"==", "!=", ">", "<", ">=", "<="};

    private SimpleProposition() {
    }

    public static boolean compileAndEvaluate(String expression, Map<String, Object> context) {
        String[] tokens = tokenize(expression);
        if (tokens.length == 0) {
            throw new ExpressionException("[EL] No supporting operators found", expression);
        }
        if (context == null) {
            context = Collections.emptyMap();
        }

        String[] literals = Arrays.copyOfRange(tokens, 1, tokens.length);
        Object[] operands = parse(literals, context);
        return evaluate(tokens[0], operands);
    }

    private static String[] tokenize(String expression) {
        String operator = "";
        int operatorIndex = -1;
        for (String op : SUPPORTING_OPERATORS) {
            if (operatorIndex < 0) {
                operatorIndex = expression.indexOf(op);
                operator = op;
            } else {
                break;
            }
        }
        if (operatorIndex < 0) {
            return new String[0];
        }

        String leftOperand = expression.substring(0, operatorIndex).trim();
        String rightOperand = expression.substring(operatorIndex + operator.length()).trim();

        return new String[]{operator, leftOperand, rightOperand};
    }

    private static Object[] parse(String[] literals, Map<String, Object> context) {
        Object[] operands = new Object[literals.length];
        for (int i = 0; i < literals.length; i++) {
            String operandLiteral = literals[i];
            if (Boolean.TRUE.toString().equals(operandLiteral)
                    || Boolean.FALSE.toString().equals(operandLiteral)) {
                operands[i] = Boolean.parseBoolean(operandLiteral);
            } else if (isStringLiteral(operandLiteral)) {
                operands[i] = operandLiteral.substring(1, operandLiteral.length() - 1);
            } else if (isIdentifier(operandLiteral)) {
                Object operand = context.get(operandLiteral);
                if (operand == null) {
                    throw new ExpressionException("[EL] Variable not in context", operandLiteral);
                }
                operands[i] = operand;
            } else {
                Number number = parseNumber(operandLiteral);
                if (number == null) {
                    throw new ExpressionException("[EL] Not a number", operandLiteral);
                }
                operands[i] = number;
            }
        }
        return operands;
    }

    private static boolean isStringLiteral(String literal) {
        return literal.length() >= 2
                && (literal.startsWith("'") && literal.endsWith("'")
                || literal.startsWith("\"") && literal.endsWith("\""));
    }

    private static boolean isIdentifier(String literal) {
        if (literal == null || literal.isEmpty()) {
            return false;
        }

        if (!Character.isJavaIdentifierStart(literal.charAt(0))) {
            return false;
        }

        for (int i = 0; i < literal.length(); i++) {
            if (!Character.isJavaIdentifierPart(literal.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static Number parseNumber(String literal) {
        try {
            return Integer.parseInt(literal);
        } catch (NumberFormatException e) {
            // do nothing
        }

        try {
            return Long.parseLong(literal);
        } catch (NumberFormatException e) {
            // do nothing
        }

        try {
            return Float.parseFloat(literal);
        } catch (NumberFormatException e) {
            // do nothing
        }

        try {
            return Double.parseDouble(literal);
        } catch (NumberFormatException e) {
            // do nothing
        }

        return null;
    }

    private static boolean evaluate(String operator, Object[] operands) {
        switch (operator) {
            case "==":
                // fall through
            case "!=":
                // fall through
            case ">=":
                // fall through
            case "<=":
                // fall through
            case ">":
                // fall through
            case "<":
                if (DataConversion.canConvert(operands[0].getClass(), operands[1].getClass())) {
                    operands[1] = DataConversion.convert(operands[1], operands[0].getClass());
                } else if (DataConversion.canConvert(operands[1].getClass(), operands[0].getClass())) {
                    operands[0] = DataConversion.convert(operands[0], operands[1].getClass());
                } else {
                    throw new ExpressionException("[EL] Unable to convert between " + operands[0].getClass().getName()
                            + " and " + operands[1].getClass().getName(), operator);
                }
                break;
            default: // do nothing
        }

        switch (operator) {
            case "==":
                return operands[0].equals(operands[1]);
            case "!=":
                return !operands[0].equals(operands[1]);
            case ">=":
            case "<=":
            case ">":
            case "<":
                if (!(operands[0] instanceof Comparable)) {
                    throw new ExpressionException("[EL] Incomparable type " + operands[0].getClass(), operator);
                }
                switch (operator) {
                    case ">=":
                        return ((Comparable) operands[0]).compareTo(operands[0]) >= 0;
                    case "<=":
                        return ((Comparable) operands[0]).compareTo(operands[0]) <= 0;
                    case ">":
                        return ((Comparable) operands[0]).compareTo(operands[0]) > 0;
                    case "<":
                        return ((Comparable) operands[0]).compareTo(operands[0]) < 0;
                    default: // do nothing
                }
                break;
            default:
                throw new ExpressionException("[EL] Unsupported operator", operator);
        }

        throw new ExpressionException("[EL] Unsupported operator", operator);
    }
}
