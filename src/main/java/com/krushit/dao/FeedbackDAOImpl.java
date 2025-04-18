package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.JPAConfig;
import com.krushit.common.enums.RoleType;
import com.krushit.common.exception.DBException;
import com.krushit.entity.Feedback;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class FeedbackDAOImpl implements IFeedbackDAO {
    private static final String IS_FEEDBACK_GIVEN =
            "SELECT 1 FROM Feedback f WHERE f.fromUser.userId = :fromUserId AND f.toUser.userId = :toUserId AND f.ride.rideId = :rideId";
    private final IUserDAO userDAO = new UserDAOImpl();

    @Override
    public void saveFeedback(Feedback feedback) throws DBException {
        EntityTransaction tx = null;
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            em.persist(feedback);
            userDAO.updateUserRating(feedback.getToUserId().getUserId(), feedback.getRating(), em);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new DBException(Message.FeedBack.ERROR_WHILE_ADDING_FEEDBACK, e);
        }
    }

    @Override
    public int getRecipientUserIdByRideId(int rideId, RoleType roleType) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            String query = (RoleType.ROLE_CUSTOMER == roleType)
                    ? "SELECT r.driver.id FROM Ride r WHERE r.rideId = :rideId"
                    : "SELECT r.customer.id FROM Ride r WHERE r.rideId = :rideId";
            return em.createQuery(query, Integer.class)
                    .setParameter("rideId", rideId).executeUpdate();
        } catch (Exception e) {
            throw new DBException(Message.FeedBack.ERROR_WHILE_FETCHING_TO_FEEDBACK, e);
        }
    }

    @Override
    public boolean isFeedbackGiven(int fromUserId, int toUserId, int rideId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            Integer result = em.createQuery(IS_FEEDBACK_GIVEN, Integer.class)
                    .setParameter("fromUserId", fromUserId)
                    .setParameter("toUserId", toUserId)
                    .setParameter("rideId", rideId)
                    .setMaxResults(1)
                    .getSingleResult();
            return result != null;
        } catch (Exception e) {
            throw new DBException(Message.FeedBack.ERROR_WHILE_CHECKING_IS_FEEDBACK_GIVEN, e);
        }
    }
}
