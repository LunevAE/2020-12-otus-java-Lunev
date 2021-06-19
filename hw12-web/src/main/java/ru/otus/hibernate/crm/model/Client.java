package ru.otus.hibernate.crm.model;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private AddressDataSet address;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PhoneDataSet> phoneDataSet;

    public Client() {
    }

    public Client(String name, String login, String password) {
        this.id = null;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public Client(Long id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public Client clone() {
        var client = new Client(this.id, this.name, this.login, this.password);
        var copiedAddress = this.address.clone();
        copiedAddress.setClient(client);
        client.setAddressDataSet(copiedAddress);
        client.setPhoneDataSet(this.phoneDataSet.stream()
                .map(it -> {
                    var copiedPhone = it.clone();
                    copiedPhone.setClient(client);
                    return copiedPhone;
                }).collect(Collectors.toList())
        );
        return client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressDataSet getAddressDataSet() {
        return address;
    }

    public void setAddressDataSet(AddressDataSet addressDataSet) {
        this.address = addressDataSet;
    }

    public List<PhoneDataSet> getPhoneDataSet() {
        return phoneDataSet;
    }

    public void setPhoneDataSet(List<PhoneDataSet> phoneDataSet) {
        this.phoneDataSet = phoneDataSet;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
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
