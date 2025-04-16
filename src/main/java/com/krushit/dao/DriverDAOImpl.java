package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.DBConfig;
import com.krushit.common.config.JPAConfig;
import com.krushit.common.enums.DriverDocumentVerificationStatus;
import com.krushit.common.enums.Role;
import com.krushit.common.exception.DBException;
import com.krushit.dto.PendingDriverDTO;
import com.krushit.entity.Driver;
import com.krushit.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements IDriverDAO {
    private static final String IS_DOCUMENT_UNDER_REVIEW = "SELECT verification_status FROM drivers WHERE driver_id = :driverId";
    private static final String UPDATE_DRIVER_DETAILS = "UPDATE Driver SET licenceNumber = ?, licencePhoto = ?WHERE user.userId = ?";
    private static final String GET_PENDING_VERIFICATION_DRIVERS = "SELECT d.driverId, d.user.userId, d.licenceNumber, d.licencePhoto, " +
            "d.isDocumentVerified, d.verificationStatus, d.comment, " +
            "u.emailId, u.firstName, u.lastName, u.displayId " +
            "FROM Driver d " +
            "JOIN User u ON d.user.userId = u.userId " +
            "WHERE d.isDocumentVerified = FALSE";
    private static final String UPDATE_DRIVER_VERIFICATION_STATUS = "UPDATE drivers SET verification_status = ?, comment = ?, is_document_verified = ?, updated_by = ? WHERE driver_id = ?";
    private static final String CHECK_DRIVER_EXISTENCE = "SELECT 1 FROM drivers WHERE driver_id = ?";
    private static final String CHECK_DRIVER_DOCUMENTS = "SELECT licence_number FROM drivers WHERE driver_id = ?";
    private static final String GET_ALL_DRIVERS = "SELECT d.*, " +
            "u.first_name, u.last_name, u.email_id, u.display_id " +
            "FROM Drivers d " +
            "JOIN Users u ON d.user_id = u.user_id";
    private static final String GET_DRIVER_ID_FROM_USERID = "SELECT driver_id FROM Drivers WHERE user_id = ?";
    private static final String IS_DOCUMENT_VERIFIED = "SELECT is_document_verified FROM Drivers WHERE driver_id = ?";
    private static final String IS_LICENCE_EXIST = "SELECT 1 FROM drivers WHERE licence_number = ?";
    private static final String UPDATE_DRIVER_AVAILABILITY = "UPDATE Drivers SET is_available = TRUE WHERE driver_id = ?";
    private static final UserDAOImpl userDAO = new UserDAOImpl();

    @Override
    public void insertDriverDetails(Driver driver) throws DBException {
        EntityTransaction tx = null;
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.createQuery(UPDATE_DRIVER_DETAILS)
                    .setParameter("driverId", driver.getUser().getUserId())
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new DBException(Message.Driver.ERROR_WHILE_INSERT_DRIVER_DETAILS, e);
        }
    }

    @Override
    public List<PendingDriverDTO> getDriversWithPendingVerification() throws DBException {
        List<PendingDriverDTO> pendingDrivers = new ArrayList<>();
        EntityTransaction tx = null;
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            @SuppressWarnings("unchecked")
            List<Object[]> rows = em.createQuery(GET_PENDING_VERIFICATION_DRIVERS).getResultList();
            for (Object[] row : rows) {
                PendingDriverDTO dto = new PendingDriverDTO.PendingDriverDTOBuilder()
                        .setDriverId((Integer) row[0])
                        .setUserId((Integer) row[1])
                        .setLicenceNumber((String) row[2])
                        .setLicencePhoto((String) row[3])
                        .setDocumentVerified((Boolean) row[4])
                        .setVerificationStatus((String) row[5])
                        .setComment((String) row[6])
                        .setEmailId((String) row[7])
                        .setFirstName((String) row[8])
                        .setLastName((String) row[9])
                        .setDisplayId((String) row[10])
                        .build();
                pendingDrivers.add(dto);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new DBException(Message.Driver.ERROR_WHILE_GETTING_PENDING_VERIFICATION_DRIVER, e);
        }
        return pendingDrivers;
    }

    @Override
    public boolean isDriverExist(int driverId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_DRIVER_EXISTENCE)) {
            preparedStatement.setInt(1, driverId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_CHECKING_DRIVER_EXISTENCE, e);
        }
    }

    @Override
    public void updateDriveVerificationDetail(int driverId, boolean isVerified, String rejectionMessage) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DRIVER_VERIFICATION_STATUS)) {
            preparedStatement.setString(1, isVerified ? DriverDocumentVerificationStatus.ACCEPTED.getStatus() : DriverDocumentVerificationStatus.REJECTED.getStatus());
            preparedStatement.setString(2, rejectionMessage);
            preparedStatement.setBoolean(3, isVerified);
            preparedStatement.setInt(4, driverId);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_VERIFYING_DRIVER, e);
        }
    }

    @Override
    public List<Driver> getAllDrivers() throws DBException {
        List<Driver> drivers = new ArrayList<>();
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_DRIVERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Driver driver = (Driver) new Driver.DriverBuilder()
                        .setDriverId(resultSet.getInt("driver_id"))
                        .setLicenceNumber(resultSet.getString("licence_number"))
                        .setDocumentVerified(resultSet.getBoolean("is_document_verified"))
                        .setAvailable(resultSet.getBoolean("is_available"))
                        .setVerificationStatus(resultSet.getString("verification_status"))
                        .setComment(resultSet.getString("comment"))
                        .setUser(new User.UserBuilder().setUserId(resultSet.getInt("user_id")).setFirstName(resultSet.getString("first_name")).setLastName(resultSet.getString("last_name"))
                                .setEmailId(resultSet.getString("email_id")).build())
                        .build();
                drivers.add(driver);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_FETCHING_ALL_DRIVERS, e);
        }
        return drivers;
    }

    @Override
    public int getDriverId(int userId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_DRIVER_ID_FROM_USERID)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("driver_id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_FOR_GETTING_DRIVER_ID_FROM_USER_ID, e);
        }
        return 0;
    }

    @Override
    public boolean isDocumentVerified(int driverId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_DOCUMENT_VERIFIED)) {
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_document_verified");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_FOR_CHECKING_DRIVER_DOCUMENT_VERIFIED, e);
        }
        return false;
    }

    @Override
    public boolean isDocumentExist(int driverId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(CHECK_DRIVER_DOCUMENTS)) {
            stmt.setInt(1, driverId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String licenceNumber = resultSet.getString("licence_number");
                    return licenceNumber != null && !licenceNumber.trim().isEmpty();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_FOR_CHECKING_DRIVER_DOCUMENT_UPLOADED, e);
        }
        return false;
    }

    @Override
    public boolean isLicenseNumberExist(String licenseNumber) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_LICENCE_EXIST)) {
            stmt.setString(1, licenseNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_CHECKING_LICENCE_NUMBER, e);
        }
    }

    @Override
    public DriverDocumentVerificationStatus isDocumentUnderReview(int driverId) throws DBException {
        try (Connection connection = DBConfig.INSTANCE.getConnection();
             PreparedStatement stmt = connection.prepareStatement(IS_DOCUMENT_UNDER_REVIEW)) {
            stmt.setInt(1, driverId);
            try (ResultSet rs = stmt.executeQuery()) {
                return DriverDocumentVerificationStatus.getType(rs.getString("verification_status"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_CHECKING_LICENCE_NUMBER, e);
        }
    }

    @Override
    public void updateDriverAvailability(int driverId) throws DBException {
        try (Connection conn = DBConfig.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_DRIVER_AVAILABILITY)) {
            ps.setInt(1, driverId);
            ps.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new DBException(Message.Driver.ERROR_WHILE_UPDATING_DRIVER_AVAILABILITY, e);
        }
    }
}