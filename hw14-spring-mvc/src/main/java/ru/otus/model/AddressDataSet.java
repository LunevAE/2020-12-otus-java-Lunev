package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("address_data_set")
public class AddressDataSet {

    @Id
    private final Long id;

    @Column("street")
    private final String street;

    @Column("client_id")
    private final Long clientId;

    @PersistenceConstructor
    private AddressDataSet(Long id, String street, Long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    public AddressDataSet(String street) {
        this(null, street, null);
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public long getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
