ALTER SYSTEM SET wal_level = 'logical';
ALTER SYSTEM SET max_replication_slot = 10;
ALTER SYSTEM SET max_wal_senders = 10;

SELECT pg_reload_conf();

CREATE PUBLICATION cdc_publication FOR ALL TABLES;

SELECT * FROM pg_create_logical_replication_slot('cdc_slot_0', 'pgoutput');

