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

            writer.write(formatRecord(record));
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

        return findAll().stream()
                .filter(record -> record.getEmployeeId() == employeeId)
                .collect(java.util.stream.Collectors.toList());
    }

    /*
        Check if cutoff already exists
    */
    @Override
    public boolean existsByCutoff(String cutoffPeriod) {

        return findAll().stream()
                .anyMatch(record -> record.getCutoffPeriod()
                        .equalsIgnoreCase(cutoffPeriod));
    }

    /*
        Delete all records of a specific cutoff
        Used for demo reset or overwrite logic
    */
    @Override
    public void deleteByCutoff(String cutoffPeriod) {

        List<PayrollHistoryRecord> remaining = findAll().stream()
                .filter(record -> !record.getCutoffPeriod()
                        .equalsIgnoreCase(cutoffPeriod))
                .collect(java.util.stream.Collectors.toList());

        try (PrintWriter writer =
                     new PrintWriter(new FileWriter(FILE_PATH))) {

            for (PayrollHistoryRecord record : remaining) {
                writer.println(formatRecord(record));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatRecord(PayrollHistoryRecord record) {

        return record.getEmployeeId() + "," +
                record.getCutoffPeriod() + "," +
                record.getBasicComponent() + "," +
                record.getAllowanceComponent() + "," +
                record.getGross() + "," +
                record.getSss() + "," +
                record.getPhilhealth() + "," +
                record.getPagibig() + "," +
                record.getTax() + "," +
                record.getTotalDeductions() + "," +
                record.getNet();
    }
}

