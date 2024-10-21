create table if not exists expense
(
    id                  uuid default RANDOM_UUID()   primary key,
    created_by          varchar(16)                  not null,
    created_at          timestamp without time zone  not null,
    last_updated_by     varchar(16)                  not null,
    last_updated_at     timestamp without time zone  not null,
    employee_id         uuid references employee     not null,
    date                timestamp without time zone  not null,
    category            varchar(32)                  not null,
    amount              decimal                      not null,
    description         text,
    status              varchar(10)                  not null
        check (status in ('PENDING', 'APPROVED', 'REJECTED'))
);

create index if not exists expense_employee_idx on expense (employee_id);
