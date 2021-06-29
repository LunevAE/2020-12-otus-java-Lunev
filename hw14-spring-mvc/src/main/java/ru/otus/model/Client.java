package ru.otus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;

@Table("client")
public class Client {

    @Id
    private final Long id;

    @Column("name")
    private final String name;

    @Column("login")
    private final String login;

    @Column("password")
    private final String password;

    @MappedCollection(idColumn = "client_id")
    private final AddressDataSet address;

    @MappedCollection(idColumn = "client_id")
    private final Set<PhoneDataSet> phoneDataSet;

    @PersistenceConstructor
    public Client(Long id, String name, String login, String password, AddressDataSet address, Set<PhoneDataSet> phoneDataSet) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.address = address;
        this.phoneDataSet = phoneDataSet;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AddressDataSet getAddressDataSet() {
        return address;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public AddressDataSet getAddress() {
        return address;
    }

    public Set<PhoneDataSet> getPhoneDataSet() {
        return phoneDataSet;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phoneDataSet=" + phoneDataSet +
                '}';
    }
}
