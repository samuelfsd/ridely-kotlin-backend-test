ALTER TABLE driver
ADD COLUMN latitude DOUBLE PRECISION DEFAULT 0.0,
ADD COLUMN longitude DOUBLE PRECISION DEFAULT 0.0;

CREATE SPATIAL INDEX idx_driver_location ON driver(latitude, longitude);