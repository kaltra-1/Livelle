package dao;

import model.Prona;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PronaDAO {
    private Connection connection;

    public PronaDAO() {
        connection = DBConnection.getConnection();
    }

    public void addProna(Prona prona) {
        try {
            String query = "INSERT INTO prona (emri, adresa, cmimi, statusi, foto, pershkrimi, nr_dhoma, siperfaqe) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, prona.getEmri());
            ps.setString(2, prona.getAdresa());
            ps.setDouble(3, prona.getCmimi());
            ps.setString(4, prona.getStatusi());
            ps.setBytes(5, prona.getFoto());
            ps.setString(6, prona.getPershkrimi());
            ps.setInt(7, prona.getNrDhoma());
            ps.setDouble(8, prona.getSiperfaqe());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProna(Prona prona) {
        try {
            String query = "UPDATE prona SET emri=?, adresa=?, cmimi=?, statusi=?, foto=?, pershkrimi=?, nr_dhoma=?, siperfaqe=? WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, prona.getEmri());
            ps.setString(2, prona.getAdresa());
            ps.setDouble(3, prona.getCmimi());
            ps.setString(4, prona.getStatusi());
            ps.setBytes(5, prona.getFoto());
            ps.setString(6, prona.getPershkrimi());
            ps.setInt(7, prona.getNrDhoma());
            ps.setDouble(8, prona.getSiperfaqe());
            ps.setInt(9, prona.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProna(int id) {
        try {
            // Fillimisht fshij të gjitha vizitat që lidhen me këtë pronë
            String deleteViziteQuery = "DELETE FROM vizite WHERE prona_id=?";
            try (PreparedStatement psVizite = connection.prepareStatement(deleteViziteQuery)) {
                psVizite.setInt(1, id);
                psVizite.executeUpdate();
            }

            // Pastaj fshi pronën
            String deletePronaQuery = "DELETE FROM prona WHERE id=?";
            try (PreparedStatement psProna = connection.prepareStatement(deletePronaQuery)) {
                psProna.setInt(1, id);
                int rowsAffected = psProna.executeUpdate();
                
                if (rowsAffected == 0) {
                    System.out.println("Nuk u gjet pronë me ID: " + id);
                } else {
                    System.out.println("Prona u fshi me sukses. ID: " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë fshirjes së pronës: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Gabim gjatë fshirjes së pronës", e);
        }
    }

    public List<Prona> getAllPronat() {
        List<Prona> pronat = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM prona");
            while (rs.next()) {
                Prona prona = new Prona();
                prona.setId(rs.getInt("id"));
                prona.setEmri(rs.getString("emri"));
                prona.setAdresa(rs.getString("adresa"));
                prona.setCmimi(rs.getDouble("cmimi"));
                prona.setStatusi(rs.getString("statusi"));
                prona.setFoto(rs.getBytes("foto"));
                prona.setPershkrimi(rs.getString("pershkrimi"));
                prona.setNrDhoma(rs.getInt("nr_dhoma"));
                prona.setSiperfaqe(rs.getDouble("siperfaqe"));
                pronat.add(prona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pronat;
    }

    public Prona getPronaById(int id) {
        Prona prona = new Prona();
        try {
            String query = "SELECT * FROM prona WHERE id=?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prona.setId(rs.getInt("id"));
                prona.setEmri(rs.getString("emri"));
                prona.setAdresa(rs.getString("adresa"));
                prona.setCmimi(rs.getDouble("cmimi"));
                prona.setStatusi(rs.getString("statusi"));
                prona.setFoto(rs.getBytes("foto"));
                prona.setPershkrimi(rs.getString("pershkrimi"));
                prona.setNrDhoma(rs.getInt("nr_dhoma"));
                prona.setSiperfaqe(rs.getDouble("siperfaqe"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prona;
    }

    public List<Prona> searchProna(String keyword) {
        List<Prona> pronat = new ArrayList<>();
        try {
            String query = "SELECT * FROM prona WHERE emri LIKE ? OR adresa LIKE ? OR statusi LIKE ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Prona prona = new Prona();
                prona.setId(rs.getInt("id"));
                prona.setEmri(rs.getString("emri"));
                prona.setAdresa(rs.getString("adresa"));
                prona.setCmimi(rs.getDouble("cmimi"));
                prona.setStatusi(rs.getString("statusi"));
                prona.setFoto(rs.getBytes("foto"));
                prona.setPershkrimi(rs.getString("pershkrimi"));
                prona.setNrDhoma(rs.getInt("nr_dhoma"));
                prona.setSiperfaqe(rs.getDouble("siperfaqe"));
                pronat.add(prona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pronat;
    }
}