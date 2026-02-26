/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.repository;

import com.mycompany.oop.model.PayrollHistoryRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
    CsvPayrollHistoryRepository

    Handles persistence of payroll history records
    using a CSV file as storage.

    Responsibilities:
    - Save payroll records
    - Load all records
    - Filter by employee
    - Check cutoff duplicates
    - Delete by cutoff (for demo reset)
*/

public class CsvPayrollHistoryRepository implements PayrollHistoryRepository {

    private static final String FILE_PATH = "payroll_history.csv";

    /*
        Save a payroll record (append mode)
    */
    @Override
    public void savePayrollRecord(PayrollHistoryRecord record) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(FILE_PATH, true))) {

        writer.write(
                record.getEmployeeId() + "," +
                record.getCutoffPeriod() + "," +

                record.getBasicComponent() + "," +
                record.getAllowanceComponent() + "," +

                record.getGross() + "," +
                record.getSss() + "," +
                record.getPhilhealth() + "," +
                record.getPagibig() + "," +
                record.getTax() + "," +
                record.getTotalDeductions() + "," +
                record.getNet()
        );
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    /*
        Load all payroll history records
    */
    @Override
    public List<PayrollHistoryRecord> findAll() {

        List<PayrollHistoryRecord> records = new ArrayList<>();

        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return records; // return empty if no file yet
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                PayrollHistoryRecord record =
                    new PayrollHistoryRecord(
                            Integer.parseInt(data[0].trim()),  // employeeId
                            data[1].trim(),                    // cutoffPeriod

                            Double.parseDouble(data[2].trim()), // basicComponent
                            Double.parseDouble(data[3].trim()), // allowanceComponent

                            Double.parseDouble(data[4].trim()), // gross
                            Double.parseDouble(data[5].trim()), // sss
                            Double.parseDouble(data[6].trim()), // philhealth
                            Double.parseDouble(data[7].trim()), // pagibig
                            Double.parseDouble(data[8].trim()), // tax
                            Double.parseDouble(data[9].trim()), // totalDeductions
                            Double.parseDouble(data[10].trim()) // net
                    );

                records.add(record);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return records;
    }

    /*
        Filter payroll history by employee
    */
    @Override
    public List<PayrollHistoryRecord> findByEmployeeId(int employeeId) {

        List<PayrollHistoryRecord> filtered = new ArrayList<>();

        for (PayrollHistoryRecord record : findAll()) {
            if (record.getEmployeeId() == employeeId) {
                filtered.add(record);
            }
        }

        return filtered;
    }

    /*
        Check if cutoff already exists
    */
    @Override
    public boolean existsByCutoff(String cutoffPeriod) {

        for (PayrollHistoryRecord record : findAll()) {
            if (record.getCutoffPeriod()
                    .equalsIgnoreCase(cutoffPeriod)) {
                return true;
            }
        }

        return false;
    }

    /*
        Delete all records of a specific cutoff
        Used for demo reset or overwrite logic
    */
    @Override
    public void deleteByCutoff(String cutoffPeriod) {

        List<PayrollHistoryRecord> updated = new ArrayList<>();

        for (PayrollHistoryRecord record : findAll()) {
            if (!record.getCutoffPeriod()
                    .equalsIgnoreCase(cutoffPeriod)) {
                updated.add(record);
            }
        }

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(FILE_PATH))) {

            for (PayrollHistoryRecord record : updated) {
                writer.println(
                        record.getEmployeeId() + "," +
                        record.getCutoffPeriod() + "," +
                        record.getGross() + "," +
                        record.getSss() + "," +
                        record.getPhilhealth() + "," +
                        record.getPagibig() + "," +
                        record.getTax() + "," +
                        record.getTotalDeductions() + "," +
                        record.getNet()
                );
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
CSV PAYROLL HISTORY REPOSITORY – IMPLEMENTATION UPDATE SUMMARY

Purpose:
Concrete implementation of PayrollHistoryRepository using CSV file storage.

Key Responsibilities:
Append payroll records
Read all payroll records
Filter by employee
Detect duplicate cutoffs
Delete cutoff records (demo overwrite)

Important Fixes:
Unified method names to match interface exactly
Added safe file existence check
Standardized CSV parsing logic
Removed duplicate or inconsistent methods
Centralized file path constant

Duplicate Prevention:
existsByCutoff() now scans all records and prevents re-processing
the same cutoff unless overwrite is enabled.

Demo Reset Support:
deleteByCutoff() rewrites the file without selected cutoff.

Design Pattern:
Implements Repository Pattern.
PayrollService depends on abstraction (interface),
not concrete CSV implementation.

Scalability:
Can be replaced with a database without touching PayrollService.
*/