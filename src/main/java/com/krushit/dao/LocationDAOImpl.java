package com.krushit.dao;

import com.krushit.common.Message;
import com.krushit.common.config.JPAConfig;
import com.krushit.common.exception.DBException;
import com.krushit.entity.Location;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.List;

public class LocationDAOImpl implements ILocationDAO {

    @Override
    public void addLocation(String location) throws DBException {
        EntityTransaction tx = null;
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();

            Location newLocation = new Location();
            newLocation.setName(location);
            em.persist(newLocation);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new DBException(Message.Location.ERROR_WHILE_ADDING_LOCATION, e);
        }
    }

    @Override
    public String getLocationName(int locationId) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            Location location = em.find(Location.class, locationId);
            return location != null ? location.getName() : null;
        } catch (Exception e) {
            throw new DBException(Message.Location.ERROR_WHILE_GETTING_LOCATION_BY_NAME, e);
        }
    }

    @Override
    public List<Location> getAllLocations() throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            String jpql = "SELECT l FROM Location l";
            return em.createQuery(jpql, Location.class).getResultList();
        } catch (Exception e) {
            throw new DBException(Message.Location.ERROR_WHILE_GETTING_ALL_LOCATION, e);
        }
    }

    @Override
    public void deleteLocation(int locationId) throws DBException {
        EntityTransaction tx = null;
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();

            Location location = em.find(Location.class, locationId);
            if (location != null) {
                em.remove(location);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new DBException(Message.Location.ERROR_WHILE_DELETING_LOCATION, e);
        }
    }

    @Override
    public BigDecimal getCommissionByDistance(double distance) throws DBException {
        try (EntityManager em = JPAConfig.getEntityManagerFactory().createEntityManager()) {
            String jpql = "SELECT c.commissionPercentage FROM CommissionSlab c WHERE :distance BETWEEN c.fromKm AND c.toKm";
            return em.createQuery(jpql, BigDecimal.class)
                    .setParameter("distance", distance)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DBException(Message.Location.ERROR_WHILE_GET_COMMISSION_BY_DISTANCE, e);
        }
    }
}
