package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.JPAConfig;
import com.krushit.common.enums.DocumentVerificationStatus;
import com.krushit.common.enums.RoleType;
import com.krushit.common.exception.DBException;
import com.krushit.entity.Driver;
import com.krushit.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements IUserDAO {
    private static final String GET_USER_BY_EMAIL_AND_PASSWORD =
            "SELECT u FROM User u WHERE u.emailId = :email AND u.password = :password";
    private static final String VALIDATE_USER =
            "SELECT COUNT(u) FROM User u WHERE u.emailId = :email AND u.password = :password";
    private static final String CHECK_USER_EXISTENCE_BY_EMAIL_OR_PHONE =
            "SELECT COUNT(u) FROM User u WHERE u.emailId = :email OR u.phoneNo = :phone";
    private static final String GET_ALL_CUSTOMERS =
            "SELECT u FROM User u WHERE u.role.role = :roleType";
    private static final String GET_DISPLAY_ID_BY_USER_ID =
            "SELECT u.displayId FROM User u WHERE u.userId = :userId";
    private static final String GET_USER_NAME_BY_USER_ID =
            "SELECT u.firstName, u.lastName FROM User u WHERE u.userId = :userId";
    private static final String GET_USER_BY_EMAIL =
            "SELECT u FROM User u WHERE u.emailId = :email";
    private static final String UPDATE_PASSWORD_BY_EMAIL =
            "UPDATE User u SET u.password = :pwd WHERE u.emailId = :email";
    private static final String UPDATE_USER_RATING =
            "UPDATE User u SET u.totalRatings = ((u.totalRatings * u.ratingCount) + :rating) / (u.ratingCount + 1), " +
                    "u.ratingCount = u.ratingCount + 1 WHERE u.userId = :userId";
    private static final String BLOCK_USER_BY_ID =
            "UPDATE User u SET u.isBlocked = true WHERE u.userId = :id";
    private static final String IS_USER_BLOCKED =
            "SELECT u.isBlocked FROM User u WHERE u.userId = :id";
    private static final String GET_USERS_BY_LOW_RATING =
            "SELECT u FROM User u WHERE u.totalRatings < :rating AND u.ratingCount < :reviews";
    private static final String GET_USERS_PAGINATED =
            "SELECT u FROM User u";

    @Override
    public void registerUser(User user) throws DBException {
        EntityTransaction tx = null;
        EntityManager em = null;
        try {
            em = JPAConfig.getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            em.persist(user);
            em.flush();
            String displayId = (user.getRole().equals(RoleType.ROLE_DRIVER))
                    ? generateDriverDisplayId(user.getUserId())
                    : generateCustomerDisplayId(user.getUserId());

            user.setDisplayId(displayId);
            em.merge(user);
            if (user.getRole().equals(RoleType.ROLE_DRIVER)) {
                Driver driver = new Driver.DriverBuilder()
                        .setUser(user)
                        .setVerificationStatus(DocumentVerificationStatus.INCOMPLETE)
                        .build();
                em.persist(driver);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new DBException(Message.User.ERROR_WHILE_REGISTERING_USER, e);
        } finally {
            if (em != null) em.close();
        }
    }

    @Override
    public User getUser(String emailId, String password) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            TypedQuery<User> query = em.createQuery(GET_USER_BY_EMAIL_AND_PASSWORD, User.class);
            query.setParameter("email", emailId);
            query.setParameter("password", password);
            return query.getResultStream().findFirst().orElse(null);
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_USER_LOGIN, e);
        }
    }

    @Override
    public boolean isValidUser(String emailID, String password) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            Long count = em.createQuery(VALIDATE_USER, Long.class)
                    .setParameter("email", emailID)
                    .setParameter("password", password)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_VALIDATING_USER, e);
        }
    }

    @Override
    public boolean isUserExist(String emailID, String phoneNo) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            Long count = em.createQuery(CHECK_USER_EXISTENCE_BY_EMAIL_OR_PHONE, Long.class)
                    .setParameter("email", emailID)
                    .setParameter("phone", phoneNo)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_CHECKING_USER_EXISTENCE, e);
        }
    }

    @Override
    public boolean isUserExist(int userId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return em.find(User.class, userId) != null;
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_CHECKING_USER_EXISTENCE, e);
        }
    }

    private String generateCustomerDisplayId(int userId) {
        String timestampPart = String.valueOf(System.currentTimeMillis() % 1000);
        String userIdPart = String.format("%04d", userId % 10000);
        return "US" + userIdPart + "R" + timestampPart;
    }

    private String generateDriverDisplayId(int userId) {
        String timestampPart = String.valueOf(System.currentTimeMillis() % 1000);
        String userIdPart = String.format("%04d", userId % 10000);
        return "DR" + userIdPart + "V" + timestampPart;
    }

    @Override
    public Optional<User> getUser(int userId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return Optional.ofNullable(em.find(User.class, userId));
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_GET_USER_DETAILS, e);
        }
    }

    @Override
    public List<User> getAllCustomers() throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(GET_ALL_CUSTOMERS, User.class)
                    .setParameter("roleType", RoleType.ROLE_CUSTOMER)
                    .getResultList();
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_ALL_CUSTOMERS, e);
        }
    }

    @Override
    public String getUserDisplayId(int userId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(GET_DISPLAY_ID_BY_USER_ID, String.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_DISPLAY_ID, e);
        }
    }

    @Override
    public String getUserFullName(int userId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            Object[] result = em.createQuery(GET_USER_NAME_BY_USER_ID, Object[].class)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return result[0] + " " + result[1];
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_USER_FULL_NAME, e);
        }
    }

    @Override
    public void updateUser(User user) throws DBException {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(user);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new DBException(Message.User.ERROR_WHILE_UPDATING_USER_DETAILS, e);
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(GET_USER_BY_EMAIL, User.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_GET_USER_DETAILS, e);
        }
    }

    @Override
    public void updatePassword(String email, String newPassword) throws DBException {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery(UPDATE_PASSWORD_BY_EMAIL)
                    .setParameter("pwd", newPassword)
                    .setParameter("email", email)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new DBException(Message.User.ERROR_WHILE_UPDATING_PASSWORD, e);
        } finally {
            em.close();
        }
    }

    @Override
    public void updateUserRating(int userId, int rating, EntityManager em) throws DBException {
        try {
            em.createQuery(UPDATE_USER_RATING)
                    .setParameter("rating", rating)
                    .setParameter("userId", userId)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_UPDATE_DRIVER_RATING, e);
        }
    }

    @Override
    public void blockUser(int userId) throws DBException {
        EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery(BLOCK_USER_BY_ID)
                    .setParameter("id", userId)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new DBException(Message.User.ERROR_WHILE_BLOCKING_USER, e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean isUserBlocked(int userId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(IS_USER_BLOCKED, Boolean.class)
                    .setParameter("id", userId)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_CHECK_IS_USER_BLOCKED, e);
        }
    }

    @Override
    public List<User> getUsersByLowRatingAndReviewCount(int ratingThreshold, int reviewCountThreshold) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(GET_USERS_BY_LOW_RATING, User.class)
                    .setParameter("rating", ratingThreshold)
                    .setParameter("reviews", reviewCountThreshold)
                    .getResultList();
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_CUSTOMERS_BY_RATING, e);
        }
    }

    @Override
    public List<User> getUsersByPagination(int offset, int limit) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(GET_USERS_PAGINATED, User.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            throw new DBException(Message.User.ERROR_WHILE_GETTING_CUSTOMERS_BY_RATING, e);
        }
    }

    @Override
    public void addFavouriteUser(int customerId, int driverId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                User customer = em.find(User.class, customerId);
                User driver = em.find(User.class, driverId);
                customer.getFavoriteUsers().add(driver);
                em.merge(customer);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw new DBException(Message.User.ERROR_WHILE_ADDING_FAVOURITE_DRIVER, e);
            }
        }
    }

    @Override
    public boolean isAlreadyFavourite(int customerId, int driverId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            try {
                User customer = em.find(User.class, customerId);
                User driver = em.find(User.class, driverId);
                return customer.getFavoriteUsers().contains(driver);
            } catch (Exception e) {
                throw new DBException(Message.User.ERROR_WHILE_VALIDATIONG_FAVOURITE_DRIVER, e);
            }
        }
    }

    @Override
    public void removeFavouriteUser(int customerId, int driverId) throws DBException{
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();
                User customer = em.find(User.class, customerId);
                User driver = em.find(User.class, driverId);
                customer.getFavoriteUsers().remove(driver);
                em.merge(customer);
                tx.commit();
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();
                throw new DBException(Message.User.ERROR_WHILE_DELETING_FAVOURITE_DRIVER, e);
            }
        }
    }
}
