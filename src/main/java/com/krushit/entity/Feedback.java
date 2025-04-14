package com.krushit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private int feedBackId;

    @Column(name = "from_user_id")
    private int fromUserId;

    @Column(name = "to_user_id")
    private int toUserId;

    @Column(name = "ride_id")
    private int rideId;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment")
    private String comment;

    private Feedback(FeedbackBuilder builder) {
        this.fromUserId = builder.fromUserId;
        this.toUserId = builder.toUserId;
        this.rideId = builder.rideId;
        this.rating = builder.rating;
        this.comment = builder.comment;
    }

    public Feedback() {
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getFeedBackId() {
        return feedBackId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public int getRideId() {
        return rideId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "feedBackId=" + feedBackId +
                ", fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", rideId=" + rideId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                '}';
    }

    public static class FeedbackBuilder {
        private int fromUserId;
        private int toUserId;
        private int rideId;
        private int rating;
        private String comment;

        public FeedbackBuilder setFromUserId(int fromUserId) {
            this.fromUserId = fromUserId;
            return this;
        }

        public FeedbackBuilder setToUserId(int toUserId) {
            this.toUserId = toUserId;
            return this;
        }

        public FeedbackBuilder setRideId(int rideId) {
            this.rideId = rideId;
            return this;
        }

        public FeedbackBuilder setRating(int rating) {
            this.rating = rating;
            return this;
        }

        public FeedbackBuilder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Feedback build() {
            return new Feedback(this);
        }
    }
}
