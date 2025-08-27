ALTER TABLE ride
    CHANGE COLUMN pick_up pick_up_address VARCHAR(255) NOT NULL,
    CHANGE COLUMN drop_off drop_off_address VARCHAR(255) NOT NULL,
    ADD COLUMN pick_up_latitude DOUBLE PRECISION NOT NULL,
    ADD COLUMN pick_up_longitude DOUBLE PRECISION NOT NULL,
    ADD COLUMN drop_off_latitude DOUBLE PRECISION NOT NULL,
    ADD COLUMN drop_off_longitude DOUBLE PRECISION NOT NULL;

CREATE SPATIAL INDEX idx_ride_pickup ON ride(pick_up_latitude, pick_up_longitude);