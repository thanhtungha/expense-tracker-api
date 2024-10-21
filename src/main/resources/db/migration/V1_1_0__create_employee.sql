create table if not exists employee
(
    id                  uuid default RANDOM_UUID()   primary key,
    created_by          varchar(16)                  not null,
    created_at          timestamp without time zone  not null,
    last_updated_by     varchar(16)                  not null,
    last_updated_at     timestamp without time zone  not null,
    name                varchar(255)                 not null
);