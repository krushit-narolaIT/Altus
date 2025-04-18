package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.JPAConfig;
import com.krushit.common.enums.DocumentVerificationStatus;
import com.krushit.common.exception.DBException;
import com.krushit.dto.PendingDriverDTO;
import com.krushit.entity.Driver;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.ArrayList;
import java.util.List;

public class DriverDAOImpl implements IDriverDAO {
    private static final String IS_DOCUMENT_UNDER_REVIEW = "SELECT d.verificationStatus FROM Driver d WHERE d.driverId = :driverId";
    private static final String UPDATE_DRIVER_DETAILS = "UPDATE Driver SET licenceNumber = ?, licencePhoto = ?WHERE user.userId = ?";
    private static final String GET_PENDING_VERIFICATION_DRIVERS = "SELECT d.driverId, d.user.userId, d.licenceNumber, d.licencePhoto, " +
            "d.isDocumentVerified, d.verificationStatus, d.comment, " +
            "u.emailId, u.firstName, u.lastName, u.displayId " +
            "FROM Driver d " +
            "JOIN User u ON d.user.userId = u.userId " +
            "WHERE d.isDocumentVerified = FALSE";
    private static final String UPDATE_DRIVER_VERIFICATION_STATUS = "UPDATE Driver SET verificationStatus = :verificationStatus, comment = :comment, isDocumentVerified = :isDocumentVerified WHERE driverId = :driverId";
    private static final String CHECK_DRIVER_DOCUMENTS = "SELECT verificationStatus FROM Driver WHERE driverId = :driverId";
    private static final String GET_ALL_DRIVERS = "SELECT d FROM Driver d JOIN FETCH d.user";
    private static final String GET_DRIVER_ID_FROM_USERID = "SELECT driverId FROM Driver WHERE Driver.user.userId = :userId";
    private static final String IS_DOCUMENT_VERIFIED = "SELECT isDocumentVerified FROM Driver WHERE driverId = :driverId";
    private static final String IS_LICENCE_EXIST = "SELECT 1 FROM Driver WHERE licenceNumber = :licenceNumber";
    private static final String UPDATE_DRIVER_AVAILABILITY = "UPDATE Driver d SET d.isAvailable = true WHERE d.driverId = :driverId";

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
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            if(em.getReference(Driver.class, driverId) != null){
                return true;
            }
        } catch (Exception e) {
            throw new DBException(Message.Driver.ERROR_WHILE_INSERT_DRIVER_DETAILS, e);
        }
        return false;
    }

    @Override
    public void updateDriveVerificationDetail(int driverId, boolean isVerified, String rejectionMessage) throws DBException {
        EntityTransaction tx = null;
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.createQuery(UPDATE_DRIVER_VERIFICATION_STATUS)
                    .setParameter("verificationStatus", isVerified ? DocumentVerificationStatus.VERIFIED.getStatus() : DocumentVerificationStatus.REJECTED.getStatus())
                    .setParameter("comment", rejectionMessage)
                    .setParameter("isDocumentVerified", isVerified)
                    .setParameter("driverId", driverId)
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
    public List<Driver> getAllDrivers() throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(GET_ALL_DRIVERS, Driver.class).getResultList();
        } catch (Exception e) {
            throw new DBException(Message.Driver.ERROR_WHILE_FETCHING_ALL_DRIVERS, e);
        }
    }

    @Override
    public int getDriverId(int userId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            Integer driverId = em.createQuery(
                            GET_DRIVER_ID_FROM_USERID, Integer.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return driverId != null ? driverId : 0;
        } catch (Exception e) {
            throw new DBException(Message.Driver.ERROR_FOR_GETTING_DRIVER_ID_FROM_USER_ID, e);
        }
    }

    @Override
    public boolean isDocumentVerified(int driverId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            Boolean verified = em.createQuery(
                            IS_DOCUMENT_VERIFIED, Boolean.class)
                    .setParameter("driverId", driverId)
                    .getSingleResult();
            return Boolean.TRUE.equals(verified);
        } catch (Exception e) {
            throw new DBException(Message.Driver.ERROR_FOR_CHECKING_DRIVER_DOCUMENT_VERIFIED, e);
        }
    }

    @Override
    public boolean isDocumentExist(int driverId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            String status = em.createQuery(
                            CHECK_DRIVER_DOCUMENTS, String.class)
                    .setParameter("driverId", driverId)
                    .getSingleResult();
            return !status.equalsIgnoreCase(DocumentVerificationStatus.INCOMPLETE.getStatus());
        } catch (Exception e) {
            throw new DBException(Message.Driver.ERROR_FOR_CHECKING_DRIVER_DOCUMENT_UPLOADED, e);
        }
    }

    @Override
    public boolean isLicenseNumberExist(String licenseNumber) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            int count = em.createQuery(
                            IS_LICENCE_EXIST, Integer.class)
                    .setParameter("licenceNumber", licenseNumber)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            throw new DBException(Message.Driver.ERROR_WHILE_CHECKING_LICENCE_NUMBER, e);
        }
    }

    @Override
    public DocumentVerificationStatus isDocumentUnderReview(int driverId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            String status = em.createQuery(
                            IS_DOCUMENT_UNDER_REVIEW, String.class)
                    .setParameter("driverId", driverId)
                    .getSingleResult();
            return DocumentVerificationStatus.getType(status);
        } catch (Exception e) {
            throw new DBException(Message.Driver.ERROR_WHILE_CHECKING_LICENCE_NUMBER, e);
        }
    }

    @Override
    public void updateDriverAvailability(int driverId) throws DBException {
        EntityTransaction tx = null;
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.createQuery(UPDATE_DRIVER_AVAILABILITY)
                    .setParameter("driverId", driverId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DBException(Message.Driver.ERROR_WHILE_UPDATING_DRIVER_AVAILABILITY, e);
        }
    }
}