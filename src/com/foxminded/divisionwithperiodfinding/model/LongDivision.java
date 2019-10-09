package com.foxminded.divisionwithperiodfinding.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LongDivision {

    private int searchPeriod;

    public void setSearchPeriod(int searchPeriod) {
        this.searchPeriod = searchPeriod;
    }

    boolean isIntNumber(String str) {
        if (str.length() == 0) {
            return false;
        }
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if ((i != 0 && c == '-') || (!Character.isDigit(c) && c != '-') || c == '-' && chars.length == 1) {
                return false;
            }
        }
        return true;
    }

    char[] receiveDividendsDigits(int dividend) {
        return String.valueOf(Math.abs(dividend)).toCharArray();
    }

    int calculateMinuend(int dividend, int diviser) {
        int minuend = 0;
        char dividendsDigits[] = receiveDividendsDigits(Math.abs(dividend));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dividendsDigits.length; i++) {
            if (minuend < Math.abs(diviser)) {
                sb.append(String.valueOf(dividendsDigits[i]));
                minuend = Integer.parseInt(sb.toString());
            }
        }
        return minuend;
    }

    List<Integer> computeSubtrahends(int dividend, int diviser) {
        char quotientDigits[] = String.valueOf(Math.abs(dividend/diviser)).toCharArray();
        List<Integer> subtrahendsList = new ArrayList<Integer>();
        for (int i = 0; i < quotientDigits.length; i++) {
            if (!String.valueOf(quotientDigits[i]).equals("0")) {
                subtrahendsList.add(Integer.parseInt(String.valueOf(quotientDigits[i])) * Math.abs(diviser));
            }
        }
        return subtrahendsList;
    }

    String calculateIntermediateDividend(int dividend, int diviser) {
        String stringDividend = new String(receiveDividendsDigits(dividend));
        String minuend = String.valueOf(calculateMinuend(dividend, diviser));
        String newDividendsValue = stringDividend.replaceFirst(minuend, "");
        if (newDividendsValue.equals("")) {
            return "0";
        }
        return newDividendsValue;
    }

    String composeSecondLineIndent(int number, int subtrahend) {
        int counter = String.valueOf(Math.abs(number)).length() - String.valueOf(Math.abs(subtrahend)).length();
        StringBuilder indentComposer = new StringBuilder();
        if (counter > 0) {
            for (int i = 0; i < counter; i++) {
                indentComposer.append(" ");
            }
        }
        return indentComposer.toString() + "|";
    }

    String receiveQuotient(int dividend, int diviser) {
        String result = "";
        if ((dividend < 0) ^ (diviser < 0)) {
            result += "-";
        }
        int integerQuotient = Math.abs(dividend) / Math.abs(diviser);
        result += String.valueOf(integerQuotient);
        int modulo = (Math.abs(dividend) % Math.abs(diviser)) * 10;
        if (modulo == 0) {
            return result;
        }
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        result += ".";
        String finish = "";
        StringBuilder resultBuilder = new StringBuilder(result);
        while (finish.length() < searchPeriod) {
            if (map.containsKey(modulo)) {
                int beg = map.get(modulo);
                String firstPartOfResult = resultBuilder.toString().substring(0, beg);
                String secondPartOfResult = resultBuilder.toString().substring(beg, resultBuilder.toString().length());
                if (!"0".equals(secondPartOfResult)) {
                    result = firstPartOfResult + "(" + secondPartOfResult + ")";
                } else {
                    result = firstPartOfResult;
                }
                return result;
            }
            map.put(modulo, resultBuilder.toString().length());
            integerQuotient = modulo / Math.abs(diviser);
            resultBuilder.append(String.valueOf(integerQuotient));
            modulo = (modulo % diviser) * 10;
            int dotIndex = resultBuilder.toString().indexOf(".");
            finish = resultBuilder.toString().substring(dotIndex + 1);
        }
        return resultBuilder.toString();
    }

    public boolean printResult(String dividendNumber, String diviserNumber) {
        boolean isExit = false;
        if ((isIntNumber(dividendNumber) && isIntNumber(diviserNumber)) && (!"0".equals(diviserNumber))
                && (!"exit".equals(dividendNumber) && !"exit".equals(diviserNumber))) {
            int dividend = Integer.parseInt(dividendNumber), diviser = Integer.parseInt(diviserNumber);
            int modulo = dividend - (dividend/diviser * diviser);
            int initialDividend = dividend;
            if (Math.abs(dividend) >= Math.abs(diviser)) {
                System.out.println("_" + Math.abs(dividend) + "|" + Math.abs(diviser));
                if (Math.abs(dividend) == Math.abs(diviser)) {
                    System.out.println(" " + dividend + "|" + "1");
                    int linesLength = String.valueOf(dividend).length();
                    StringBuilder indent = new StringBuilder();
                    for (int j = 0; j < linesLength; j++) {
                        indent.append(" ");
                    }
                    System.out.println(indent.toString() + "0");
                } else {
                    if (modulo == 0) {
                        composeLongDivisionView(dividend, diviser);
                    } else {
                        composeLongDivisionView(dividend, diviser, initialDividend);
                    }
                }
            } else {
                if (dividend == 0) {
                    System.out.println("_" + Math.abs(dividend) + "|" + Math.abs(diviser));
                    System.out.println(" " + Math.abs(dividend) + "|" + "0");
                } else {
                    while(Math.abs(dividend) < Math.abs(diviser)) {
                        dividend *= 10;
                    }
                    StringBuilder lessIndent = new StringBuilder();
                    for (int i = 0; i < String.valueOf(dividend).length() - String.valueOf(initialDividend).length(); i++) {
                        lessIndent.append(" ");
                    }
                    System.out.println("_" + Math.abs(initialDividend) + lessIndent.toString() + "|" + Math.abs(diviser));
                    composeModuloLongDivisionView(dividend, diviser, initialDividend);
                }
            }
        } else {
            if ("exit".equals(dividendNumber) || "exit".equals(diviserNumber)) {
                isExit = true;
            } else {
                if (!isIntNumber(dividendNumber)) {
                    System.out.println("Dividend has invalid value! Try again, please!");
                } else {
                    System.out.println("Diviser has invalid value! Try again, please!");
                }
            }
        }
        return isExit;
    }

    List<Integer> computeAfterPointSubtrahends(int dividend, int diviser) {
        String quotient = receiveQuotient(dividend, diviser);
        int dotIndex = quotient.indexOf(".");
        String afterPointQutient = quotient.substring(dotIndex + 1);
        char afterPointQuotientDigits[] = afterPointQutient.toCharArray();
        List<Integer> subtrahendsList = new ArrayList<Integer>();
        for (int i = 0; i < afterPointQuotientDigits.length; i++) {
            char c = afterPointQuotientDigits[i];
            if (!"0".equals(String.valueOf(c)) && Character.isDigit(c)) {
                subtrahendsList.add(Integer.parseInt(String.valueOf(c)) * Math.abs(diviser));
            }
        }
        return subtrahendsList;
    }

    int calculateAfterPointMinuend(int dividend, int diviser) {
        int minuend = Math.abs(dividend);
        StringBuilder minuendBuilder = new StringBuilder(String.valueOf(minuend));
        while(minuend < Math.abs(diviser)) {
            minuendBuilder.append("0");
            minuend = Integer.parseInt(minuendBuilder.toString());
        }
        return minuend;
    }

    String calculateAfterPointIntermediateDividend(int dividend, int diviser) {
        String stringDividend = new String(receiveDividendsDigits(dividend));
        String minuend = String.valueOf(calculateAfterPointMinuend(dividend, diviser));
        String newDividendsValue = stringDividend.replaceFirst(minuend, "");
        if (newDividendsValue.equals("")) {
            return "0";
        }
        return newDividendsValue;
    }

    private void composeLongDivisionView(int dividend, int diviser) {
        int modulo = Math.abs(dividend - (dividend/diviser * diviser));
        List<Integer> subtrahendsList = computeSubtrahends(dividend, diviser);
        String currentDivident = "";
        int minuend = 0, delta = 0;
        System.out.println(" " + Math.abs(subtrahendsList.get(0)) + composeSecondLineIndent(dividend, subtrahendsList.get(0)) + dividend/diviser);
        String lastLine = "";
        StringBuilder indent = new StringBuilder();
        if (subtrahendsList.size() > 1) {
            for (int i = 0; i < subtrahendsList.size(); i++) {
                minuend = calculateMinuend(dividend, diviser);
                delta = minuend - Math.abs(subtrahendsList.get(i));
                if (delta != 0) {
                    currentDivident = String.valueOf(delta)
                            .concat(calculateIntermediateDividend(dividend, diviser));
                } else {
                    currentDivident = calculateIntermediateDividend(dividend, diviser);
                }
                dividend = Integer.parseInt(currentDivident);
                if (i != 0) {
                    indent.append(" ");
                    System.out.println(indent.toString() + "_" + minuend);
                    lastLine = indent.toString() + " " + Math.abs(subtrahendsList.get(i));
                    System.out.println(indent.toString() + " " + Math.abs(subtrahendsList.get(i)));
                }
            }
            int lastLineLength = lastLine.length() - String.valueOf(delta).length();
            StringBuilder lastLineIdents = new StringBuilder();
            for (int k = 0; k < lastLineLength; k++) {
                lastLineIdents.append(" ");
            }
            System.out.println(lastLineIdents.toString() + modulo);
        } else {
            StringBuilder moduloIdents = new StringBuilder();
            for (int k = 0; k < String.valueOf(Math.abs(dividend)).length(); k++) {
                moduloIdents.append(" ");
            }
            System.out.println(moduloIdents.toString() + modulo);
        }
    }

    private void composeLongDivisionView(int dividend, int diviser, int initialDividend) {
        int modulo = Math.abs(dividend - (dividend/diviser * diviser));
        List<Integer> subtrahendsList = computeSubtrahends(dividend, diviser);
        String currentDivident = "";
        int minuend = 0, delta = 0;
        System.out.println(" " + Math.abs(subtrahendsList.get(0)) + composeSecondLineIndent(dividend, subtrahendsList.get(0)) +
                receiveQuotient(initialDividend, diviser));
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < subtrahendsList.size(); i++) {
            minuend = calculateMinuend(dividend, diviser);
            delta = minuend - Math.abs(subtrahendsList.get(i));
            if (delta != 0) {
                currentDivident = String.valueOf(delta)
                        .concat(calculateIntermediateDividend(dividend, diviser));
            } else {
                currentDivident = calculateIntermediateDividend(dividend, diviser);
            }
            dividend = Integer.parseInt(currentDivident);
            if (i != 0) {
                indent.append(" ");
                System.out.println(indent.toString() + "_" + minuend);
                System.out.println(indent.toString() + " " + Math.abs(subtrahendsList.get(i)));
            }
        }
        while (modulo < Math.abs(diviser)){
            modulo *= 10;
        }
        composeLongDivisionView(initialDividend, modulo, diviser, indent.toString().length());
    }

    private void composeLongDivisionView(int dividend, int moduloDividend, int diviser, int previousLineLength) {
        List<Integer> subtrahendsList = computeAfterPointSubtrahends(dividend, diviser);
        String currentDivident = "";
        int minuend = 0, delta = 0;
        String lastLine = "";
        StringBuilder previousIndent = new StringBuilder();
        for (int i = 0; i < previousLineLength; i++) {
            previousIndent.append(" ");
        }
        StringBuilder indent = new StringBuilder(previousIndent);
        for (int i = 0; i < subtrahendsList.size(); i++) {
            minuend = calculateAfterPointMinuend(moduloDividend, diviser);
            delta = minuend - Math.abs(subtrahendsList.get(i));
            if (delta != 0) {
                while (delta < Math.abs(diviser)){
                    delta *= 10;
                }
                moduloDividend = delta;
            } else {
                currentDivident = calculateAfterPointIntermediateDividend(moduloDividend, diviser);
                moduloDividend = Integer.parseInt(currentDivident);
            }
            indent.append(" ");
            System.out.println(indent.toString()+ "_" + minuend);
            lastLine = indent.toString()+ " " + Math.abs(subtrahendsList.get(i));
            System.out.println(lastLine);
        }
        int lengthLastLine = lastLine.length() - String.valueOf(delta).length();
        StringBuilder lastLineIdents = new StringBuilder();
        for (int k = 0; k < (lengthLastLine + String.valueOf(moduloDividend).length() - 1); k++) {
            lastLineIdents.append(" ");
        }
        System.out.println(lastLineIdents.toString() + delta / 10);
    }

    private void composeModuloLongDivisionView(int dividend, int diviser, int initialDividend) {
        int modulo = (Math.abs(dividend) % Math.abs(diviser)) * 10;
        List<Integer> subtrahendsList = computeSubtrahends(dividend, diviser);
        String currentDivident = "";
        int minuend = 0, delta = 0;
        System.out.println(" " + Math.abs(subtrahendsList.get(0)) + composeSecondLineIndent(dividend, subtrahendsList.get(0)) +
                receiveQuotient(initialDividend, diviser));
        subtrahendsList = computeAfterPointSubtrahends(initialDividend, diviser);
        String lastLine = "";
        StringBuilder indent = new StringBuilder();
        for (int i = 0, j = 1; i < subtrahendsList.size() - 1; i++, j++) {
            minuend = calculateAfterPointMinuend(modulo, diviser);
            delta = minuend - Math.abs(subtrahendsList.get(j));
            if (delta != 0) {
                while (delta < Math.abs(diviser)){
                    delta *= 10;
                }
                modulo = delta;
            } else {
                currentDivident = calculateAfterPointIntermediateDividend(modulo, diviser);
                modulo = Integer.parseInt(currentDivident);
            }
            indent.append(" ");
            System.out.println(indent.toString() + "_" + minuend);
            lastLine = indent.toString()+ " " + Math.abs(subtrahendsList.get(j));
            System.out.println(lastLine);
        }
        int lengthLastLine = lastLine.length() - String.valueOf(delta).length();
        StringBuilder lastLineIdents = new StringBuilder();
        for (int k = 0; k < (lengthLastLine + String.valueOf(modulo).length() - 1); k++) {
            lastLineIdents.append(" ");
        }
        System.out.println(lastLineIdents.toString() + delta / 10);
    }
}
