package com.example.inventory.model;
import java.util.Date;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "agent_credential", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String mobileNumber;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date joinedOn;

    @PrePersist
    @Column(name = "joined_on")

    protected void onCreate() {
        if (this.joinedOn == null) {
            this.joinedOn = new Date();
        }
    }
}
