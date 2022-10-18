package com.example.srot.data.model;

import com.example.srot.data.enums.KYCStatus;
import com.example.srot.data.enums.Role;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Profile extends BaseEntity implements UserDetails {

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "EMAIL_CONFIRMED")
    private boolean isEmailConfirmed;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @OneToOne
    private BankKYC bankKYC;

    @Column(name = "AADHAR_NUMBER")
    private String aadharNumber;

    @Column(name = "AADHAR_KYC_STATUS")
    @Enumerated(value = EnumType.STRING)
    private KYCStatus aadharKYCStatus;

    @Column(name = "PAN_NUMBER")
    private String panNumber;

    @Column(name = "PAN_KYC_STATUS")
    @Enumerated(value = EnumType.STRING)
    private KYCStatus panKYCStatus;

    @Column(name = "JOINING_DATE")
    private Date joiningDate;

    @Column(name = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @Column(name = "BANK_ACCOUNT_NAME")
    private String bankAccountName;

    @Column(name = "BANK_ACCOUNT_NUMBER")
    private String bankAccountNumber;

    @Column(name = "BANK_IFSC")
    private String bankIfsc;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "AUTHORITIES")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ACCOUNT_NOT_EXPIRED")
    private boolean isAccountNonExpired;

    @Column(name = "ACCOUNT_NOT_LOCKED")
    private boolean isAccountNonLocked;

    @Column(name = "CREDENTIAL_NOT_EXPIRED")
    private boolean isCredentialNonExpired;

    @Column(name = "ENABLED")
    private boolean isEnabled;

    @Column(name = "NATIONALITY")
    private String nationality;

    @Column(name="TFA")
    private Boolean twoFactorAuthentication;

    @OneToOne
    private RazorpayContact razorpayContact;

    @OneToOne
    private RazorpayFundAccount razorpayFundAccount;

    @OneToOne(cascade = CascadeType.ALL)
    private Wallet wallet;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Sets.newHashSet(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
