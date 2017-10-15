/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.technobank.entity;

import java.util.Date;

/**
 *
 * @author daolinh
 */

// Chứa thông tin người dùng.
public class User {
    
    // Số chứng minh thư của bạn.
    private String identifyCard;
    private String fullName;
    private Date birthDay;
    private int gender;
    private String email;
    private String phone;
    private String address;
    
    private long createdTimeMLS;
    private long updatedTimeMLS;
    private int status;
}
