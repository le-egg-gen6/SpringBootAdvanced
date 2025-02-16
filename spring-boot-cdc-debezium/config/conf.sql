-- Create a replication user
CREATE ROLE debezium WITH LOGIN REPLICATION PASSWORD 'dbz';

-- Grant replication privileges
ALTER ROLE debezium WITH REPLICATION;

