package at.htlleonding.hexcalc.controller;

import java.util.Stack;

public class Calculator {
    public static String calculate(String expression) throws DivisionByZeroException {
        String postfix = infixToPostfix(expression);
        double result = evaluatePostfix(postfix);

        String value = HexConverter.ToHex(result);
        return value;
    }


    private static String infixToPostfix(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        boolean lastWasOperator = true; // Track if the last character was an operator

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If the character is a digit or a dot, add it to the result
            if ("0123456789abcdefABCDEF.".contains(c + "")) {
                result.append(c);
                lastWasOperator = false;
            } else if (c == '(') {
                stack.push(c);
                lastWasOperator = true;
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    result.append(' ').append(stack.pop());
                }
                stack.pop();
                lastWasOperator = false;
            } else if (isOperator(c)) {
                if (c == '-' && lastWasOperator) {
                    // Handle negative numbers
                    result.append(c);
                } else {
                    result.append(' ');
                    while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                        result.append(stack.pop()).append(' ');
                    }
                    stack.push(c);
                    lastWasOperator = true;
                }
            }
        }

        // Pop all the operators from the stack
        while (!stack.isEmpty()) {
            result.append(' ').append(stack.pop());
        }

        return result.toString();
    }

    private static double evaluatePostfix(String postfix) throws DivisionByZeroException {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split(" ");
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            if (isOperator(token.charAt(0)) && token.length() == 1) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+':
                        stack.push(a + b);
                        break;
                    case '-':
                        stack.push(a - b);
                        break;
                    case '*':
                        stack.push(a * b);
                        break;
                    case '/':
                        if (b == 0)
                            throw new DivisionByZeroException();

                        stack.push(a / b);
                        break;
                    case '^':
                        stack.push(Math.pow(a, b));
                }
            } else {
                stack.push(HexConverter.FromHex(token));
            }
        }
        return stack.pop();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }
}