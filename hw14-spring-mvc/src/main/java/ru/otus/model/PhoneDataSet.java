package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone_data_set")
public class PhoneDataSet {

    @Id
    private final Long id;

    @Column("number")
    private final String number;

    @Column("client_id")
    private final Long clientId;

    @PersistenceConstructor
    private PhoneDataSet(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    public PhoneDataSet(String number) {
        this(null, number, null);
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
