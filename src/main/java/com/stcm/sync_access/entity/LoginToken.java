/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stcm.sync_access.entity;

/**
 *
 * @author Isaac Vasconcelos
 */

/*
{
    "issuedAt": 1673628373,
    "issuer": "https://metrorrey.api-sfinx.com/",
    "username": "APIUSER",
    "exp": 1705164373,
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2NzM2MjgzNzMsImlzcyI6Imh0dHBzOi8vbWV0cm9ycmV5LmFwaS1zZmlueC5jb20vIiwic3ViIjoiQVBJVVNFUiIsImV4cCI6MTcwNTE2NDM3M30.hxIObQvD4cvKR9cBvqj-k1rWz8ZfSWc_kTIrFD1SMEQDW-DS8XLgG2a-PpaQXTkKsSO9WJ9_-SgOX35phm8zYA",
    "role": [
        "0000",
        "0001"
    ]
}
*/
public class LoginToken {
    private long issueAt;
    private String issuer;
    private String username;
    private long exp;
    private String token;
    private String [] role;

    public long getIssueAt() {
        return issueAt;
    }

    public void setIssueAt(long issueAt) {
        this.issueAt = issueAt;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String[] getRole() {
        return role;
    }

    public void setRole(String[] role) {
        this.role = role;
    }
    
    
}

