package com.foxminded.divisionwithperiodfinding;

import com.foxminded.divisionwithperiodfinding.model.LongDivision;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean isExit = false;
        LongDivision division = new LongDivision();
        division.setSearchPeriod(10);

        while (!isExit) {
            String dividend = "", diviser = "";
            try {
                dividend = reader.readLine();
                diviser = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isExit = division.printResult(dividend, diviser);
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}