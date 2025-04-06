package com.example.addressbook.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteContactDAO implements IContactDAO {
    private Connection connection;

    public SqliteContactDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS contacts ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "firstName VARCHAR NOT NULL,"
                            + "lastName VARCHAR NOT NULL,"
                            + "phone VARCHAR NOT NULL,"
                            + "email VARCHAR NOT NULL"
                            + ")"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void addContact(Contact contact) {
        try {
            PreparedStatement insertContact = connection.prepareStatement(
                    "INSERT INTO contacts (id, firstName, lastName, phone, email) VALUES (?, ?, ?, ?, ?)"
            );
            insertContact.setInt(1, contact.getId());
            insertContact.setString(2, contact.getFirstName());
            insertContact.setString(3, contact.getLastName());
            insertContact.setString(4, contact.getPhone());
            insertContact.setString(5, contact.getEmail());
            insertContact.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void updateContact(Contact contact) {
        try {
            PreparedStatement updateContact = connection.prepareStatement(
                    "UPDATE contacts SET firstName = ?, lastName = ?, phone = ?, email = ? WHERE id = ?"
            );
            updateContact.setString(1, contact.getFirstName());
            updateContact.setString(2, contact.getLastName());
            updateContact.setString(3, contact.getPhone());
            updateContact.setString(4, contact.getEmail());
            updateContact.setInt(5, contact.getId());
            updateContact.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public void deleteContact(Contact contact) {
        try {
            PreparedStatement deleteContact = connection.prepareStatement("DELETE FROM contacts WHERE id = ?");
            deleteContact.setInt(1, contact.getId());
            deleteContact.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    @Override
    public Contact getContact(int id) {
        try {
            PreparedStatement getContact = connection.prepareStatement("SELECT * FROM contacts WHERE id = ?");
            getContact.setInt(1, id);
            ResultSet rs = getContact.executeQuery();
            if (rs.next()) {
                Contact contact = new Contact(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                contact.setId(id);
                return contact;
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return null;
    }

    @Override
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT * FROM contacts");
            while (rs.next()) {
                Contact contact = new Contact(
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                contact.setId(rs.getInt("id"));
                contacts.add(contact);
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return contacts;
    }
}
