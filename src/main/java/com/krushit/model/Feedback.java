package com.krushit.model;

public class Feedback {
    private int fromUserId;
    private int toUserId;
    private int rideId;
    private int rating;
    private String comment;

    private Feedback(FeedbackBuilder builder) {
        this.fromUserId = builder.fromUserId;
        this.toUserId = builder.toUserId;
        this.rideId = builder.rideId;
        this.rating = builder.rating;
        this.comment = builder.comment;
    }

    public int getFromUserId() {
        return fromUserId;
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
