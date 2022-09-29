CREATE TABLE IF NOT EXISTS users
(
    id
    INT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    email VARCHAR
(
    512
) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY
(
    id
),
    CONSTRAINT UQ_USER_EMAIL UNIQUE
(
    email
)
    );

CREATE TABLE IF NOT EXISTS requests
(
    id
    INT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    description
    VARCHAR
    NOT
    NULL,
    requester_id
    INT
    NOT
    NULL,
    created
    TIMESTAMP
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    CONSTRAINT
    pk_request
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT fk_requests_to_users FOREIGN KEY
(
    requester_id
) REFERENCES users
(
    id
)
    );

CREATE TABLE IF NOT EXISTS items
(
    id
    INT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    name
    VARCHAR
(
    255
) NOT NULL,
    description VARCHAR NOT NULL,
    available BOOLEAN NOT NULL,
    owner_id INT,
    request_id INT,
    CONSTRAINT pk_item PRIMARY KEY
(
    id
),
    CONSTRAINT fk_items_to_users FOREIGN KEY
(
    owner_id
) REFERENCES users
(
    id
),
    CONSTRAINT fk_items_to_requests FOREIGN KEY
(
    request_id
) REFERENCES requests
(
    id
)
    );

CREATE TABLE IF NOT EXISTS bookings
(
    id
    INT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    item_id
    INT
    NOT
    NULL,
    start_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    end_date
    TIMESTAMP
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    booker_id
    INT
    NOT
    NULL,
    status
    VARCHAR
(
    255
) NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY
(
    id
),
    CONSTRAINT fk_bookings_to_items FOREIGN KEY
(
    item_id
) REFERENCES items
(
    id
),
    CONSTRAINT fk_bookings_to_users FOREIGN KEY
(
    booker_id
) REFERENCES users
(
    id
)
    );

CREATE TABLE IF NOT EXISTS comments
(
    id
    INT
    GENERATED
    BY
    DEFAULT AS
    IDENTITY
    NOT
    NULL,
    item_id
    INT
    NOT
    NULL,
    author_id
    INT
    NOT
    NULL,
    text
    VARCHAR
    NOT
    NULL,
    created
    TIMESTAMP
    WITHOUT
    TIME
    ZONE
    NOT
    NULL,
    CONSTRAINT
    pk_comments
    PRIMARY
    KEY
(
    id
),
    CONSTRAINT fk_comments_to_items FOREIGN KEY
(
    item_id
) REFERENCES items
(
    id
),
    CONSTRAINT fk_comments_to_users FOREIGN KEY
(
    author_id
) REFERENCES users
(
    id
)
    );