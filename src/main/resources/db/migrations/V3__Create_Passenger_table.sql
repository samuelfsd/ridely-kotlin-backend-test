CREATE TABLE passenger (
    id              int            NOT NULL AUTO_INCREMENT,
    name            VARCHAR(255)   NOT NULL,
    email           VARCHAR(255)   NOT NULL UNIQUE,
    in_traveling    BOOLEAN        NOT NULL,
    activation_date TIMESTAMP      NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB