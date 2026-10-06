CREATE TABLE accounts
(
    id      UUID PRIMARY KEY,
    balance NUMERIC NOT NULL
);

CREATE TABLE transfers
(
    id              UUID PRIMARY KEY,
    from_account_id UUID        NOT NULL,
    to_account_id   UUID        NOT NULL,
    amount          NUMERIC     NOT NULL,
    status          VARCHAR(20) NOT NULL
);