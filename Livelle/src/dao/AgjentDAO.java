package dao;

import model.Agjent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgjentDAO {
    private Connection connection;

    public AgjentDAO() {
        connection = DBConnection.getConnection();
    }

    public void addAgjent(Agjent agjent) {
        try {
            String query = "INSERT INTO agjent (emer, email, username, password, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, agjent.getEmer());
            ps.setString(2, agjent.getEmail());
            ps.setString(3, agjent.getUsername());
            ps.setString(4, agjent.getPassword());
            ps.setString(5, agjent.getRole());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAgjent(Agjent agjent) {
        try {
            String query = "UPDATE agjent SET emer=?, email=?, username=?, password=?, role=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, agjent.getEmer());
            ps.setString(2, agjent.getEmail());
            ps.setString(3, agjent.getUsername());
            ps.setString(4, agjent.getPassword());
            ps.setString(5, agjent.getRole());
            ps.setInt(6, agjent.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAgjent(int id) {
        try {
            String query = "DELETE FROM agjent WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Agjent> getAllAgjentet() {
        List<Agjent> agjentet = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM agjent");
            while (rs.next()) {
                Agjent agjent = new Agjent();
                agjent.setId(rs.getInt("id"));
                agjent.setEmer(rs.getString("emer"));
                agjent.setEmail(rs.getString("email"));
                agjent.setUsername(rs.getString("username"));
                agjent.setPassword(rs.getString("password"));
                agjent.setRole(rs.getString("role"));
                agjentet.add(agjent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agjentet;
    }

    public Agjent getAgjentById(int id) {
        Agjent agjent = new Agjent();
        try {
            String query = "SELECT * FROM agjent WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                agjent.setId(rs.getInt("id"));
                agjent.setEmer(rs.getString("emer"));
                agjent.setEmail(rs.getString("email"));
                agjent.setUsername(rs.getString("username"));
                agjent.setPassword(rs.getString("password"));
                agjent.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agjent;
    }

    public Agjent login(String username, String password) {
        Agjent agjent = null;
        try {
            String query = "SELECT * FROM agjent WHERE username=? AND password=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                agjent = new Agjent();
                agjent.setId(rs.getInt("id"));
                agjent.setEmer(rs.getString("emer"));
                agjent.setEmail(rs.getString("email"));
                agjent.setUsername(rs.getString("username"));
                agjent.setPassword(rs.getString("password"));
                agjent.setRole(rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return agjent;
    }
}