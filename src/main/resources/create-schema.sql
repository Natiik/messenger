CREATE TABLE IF NOT EXISTS user_accounts(
    id            UUID,
    first_name    VARCHAR(255) NOT NULL,
    second_name   VARCHAR(255),
    nick_name     VARCHAR(255),
    phone_number  VARCHAR(20) NOT NULL,
    subscription  SMALLINT NOT NULL,
    image         BYTEA,
    image_type    VARCHAR(255),
    description   VARCHAR(1024),
    add_info      VARCHAR(2056),

    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS user_accounts_first_name_index ON user_accounts (first_name);
CREATE INDEX IF NOT EXISTS user_accounts_second_name_index ON user_accounts (second_name);
CREATE INDEX IF NOT EXISTS user_accounts_nick_name_index ON user_accounts (nick_name);
CREATE INDEX IF NOT EXISTS user_accounts_phone_number_index ON user_accounts (phone_number);
CREATE INDEX IF NOT EXISTS user_accounts_subscription_index ON user_accounts (subscription);

