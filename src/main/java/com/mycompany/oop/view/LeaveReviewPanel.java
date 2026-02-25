/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.mycompany.oop.service.LeaveService;
import com.mycompany.oop.model.Leave;

public class LeaveReviewPanel extends JPanel {

    private LeaveService service;
    private JTable table;

    public LeaveReviewPanel(){

        service = new LeaveService();

        setLayout(new BorderLayout());
        setBackground(UITheme.MAIN_GRAY);

        add(UITheme.createTitleBar("Leave Approval Center"), BorderLayout.NORTH);

        JPanel content = UITheme.createInsetPanel();
        content.setLayout(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        content.add(createTablePanel(), BorderLayout.CENTER);
        content.add(createButtonPanel(), BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    // ================= TABLE =================
    private JScrollPane createTablePanel(){

        table = new JTable();
        table.setRowHeight(26);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        table.setSelectionBackground(new Color(0,0,128));
        table.setSelectionForeground(Color.WHITE);

        refreshTable();

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLoweredBevelBorder());

        return scroll;
    }

    private void refreshTable(){

        List<Leave> leaves = service.getAllLeaves();

        String[] cols = {
                "Leave ID",
                "Employee ID",
                "Type",
                "Start Date",
                "End Date",
                "Reason",
                "Status"
        };

        Object[][] data = new Object[leaves.size()][7];

        for(int i=0;i<leaves.size();i++){

            Leave l = leaves.get(i);

            data[i][0] = l.getLeaveId();
            data[i][1] = l.getEmployeeId();
            data[i][2] = l.getLeaveType();
            data[i][3] = l.getStartDate();
            data[i][4] = l.getEndDate();
            data[i][5] = l.getReason();
            data[i][6] = l.getStatus();
        }

        DefaultTableModel model = new DefaultTableModel(data, cols){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };

        table.setModel(model);
    }

    // ================= BUTTONS =================
    private JPanel createButtonPanel(){

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        panel.setBackground(UITheme.MAIN_GRAY);

        JButton approveBtn = UITheme.createButton("Approve");
        JButton rejectBtn = UITheme.createAccentButton("Reject");

        approveBtn.setPreferredSize(new Dimension(100,32));
        rejectBtn.setPreferredSize(new Dimension(100,32));

        approveBtn.addActionListener(e -> updateStatus("APPROVED"));
        rejectBtn.addActionListener(e -> updateStatus("REJECTED"));

        panel.add(approveBtn);
        panel.add(rejectBtn);

        return panel;
    }

    private void updateStatus(String status){

        int row = table.getSelectedRow();
        if(row == -1) return;

        int leaveId = Integer.parseInt(
                table.getValueAt(row,0).toString());

        if(status.equals("APPROVED")){
            service.approveLeave(leaveId);
        } else {
            service.rejectLeave(leaveId);
        }

        refreshTable();
    }
}