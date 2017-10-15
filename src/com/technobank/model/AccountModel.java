/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.technobank.model;

import com.technobank.entity.Account;
import com.technobank.entity.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daolinh
 */
/**
 * Thực hiện các thao tác với database của lớp Account.
 */
public class AccountModel {
    
    private TransactionModel transactionModel = new TransactionModel();

    // Thêm mới dữ liệu.
    public boolean insert(Account obj) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("insert into");
        sqlBuilder.append(" ");
        sqlBuilder.append("account");
        sqlBuilder.append(" ");
        sqlBuilder.append("(number, account, password, balance, userIdentifyCard, createdTimeMLS, updatedTimeMLS, status)");
        sqlBuilder.append(" ");
        sqlBuilder.append("values");
        sqlBuilder.append(" ");
        sqlBuilder.append("(?, ?, ?, ?, ?, ?, ?, ?);");

        try {
            Connection con = DAO.getConnection();
            PreparedStatement preStt = con.prepareStatement(sqlBuilder.toString());
            preStt.setString(1, obj.getNumber());
            preStt.setString(2, obj.getAccount());
            preStt.setString(3, obj.getPassword());
            preStt.setLong(4, obj.getBalance());
            preStt.setString(5, obj.getUserIdentifyCard());
            preStt.setLong(6, obj.getCreatedTimeMLS());
            preStt.setLong(7, obj.getUpdatedTimeMLS());
            preStt.setInt(8, obj.getStatus());
            preStt.execute();
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean update(Account obj) {
        return false;
    }

    public boolean delete(String id) {
        return false;
    }

    public Account getById(String id) {
        Account obj = new Account();
        return obj;
    }

    public ArrayList<Account> getList() {
        ArrayList<Account> list = new ArrayList<>();
        return list;
    }

    // Thực hiện cộng tiền vào tài khoản, trả về là số dư mới.
    public long deposit(String targetAccountNumber, long amount) {
        Connection con = null;
        try {
            con = DAO.getConnection();
            con.setAutoCommit(false);
            
            // 1. Thực hiện truy vấn số dư của tài khoản.
            StringBuilder sqlSelectBuilder = new StringBuilder();
            sqlSelectBuilder.append("select balance from account where number = ?;");
            PreparedStatement queryBalance = con.prepareStatement(sqlSelectBuilder.toString());
            queryBalance.setString(1, targetAccountNumber);
            ResultSet resultBalance = queryBalance.executeQuery();
            // Số dư mặc định bằng 0.
            long balance = 0;
            if (resultBalance.next()) {
                balance = resultBalance.getLong("balance");
            }

            // 2. Tiến hành cộng tiền vào tài khoản.
            balance += amount;
            
            StringBuilder sqlUpdate = new StringBuilder();
            sqlUpdate.append("update account");
            sqlUpdate.append(" ");
            sqlUpdate.append("set balance = ?");
            sqlUpdate.append(" ");
            sqlUpdate.append("where");
            sqlUpdate.append(" ");
            sqlUpdate.append("number = ?;");
            PreparedStatement updateBalance = con.prepareStatement(sqlUpdate.toString());
            updateBalance.setLong(1, balance); // số dư mới của tài khoản.
            updateBalance.setString(2, targetAccountNumber); // tài khoản cộng tiền.
            updateBalance.executeUpdate();
            
            // Lưu lịch sử giao dịch.
            Transaction tx = new Transaction();
            tx.setSenderAccountNumber(targetAccountNumber);
            tx.setReceiverAccountNumber(targetAccountNumber);
            tx.setAmount(amount);
            tx.setMessage("Deposit money.");
            if(transactionModel.insert(tx)){
                con.commit(); // Lưu tất cả các thay đổi của các lệnh vừa thực thi vào db.
                return balance;
            } else{
                con.rollback();
            }                                    
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            if(con != null){
                try {
                    con.setAutoCommit(true);
                } catch (SQLException ex) {
                    Logger.getLogger(AccountModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return 0;
    }

    // Thực hiện rút tiền từ tài khoản, trả về số dư mới.
    public long withdraw(long amount) {
        return 0;
    }

    // Thực hiện chuyển tiền đến tài khoản đích, tham số thứ nhất là số lượng tiền
    // cần chuyển, tham số thứ hai là số tài khoản nhận tiền. Trả về số dư hiện tại
    // của tài khoản thực hiện.
    public long transfer(long amount, String targetAccountNumber) {
        return 0;
    }

    // Kiểm tra thông tin tài khoản với account và password. Trả về null trong 
    // trường hợp không tồn tại account và password hợp lệ.
    public Account checkAuthentication(String account, String password) {
        return null;
    }
}
