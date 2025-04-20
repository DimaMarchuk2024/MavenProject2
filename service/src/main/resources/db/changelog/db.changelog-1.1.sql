--liquibase formatted sql

--changeset KamoUser:1
ALTER TABLE pizza
ALTER COLUMN image TYPE VARCHAR(64);
--rollback ALTER TABLE pizza ALTER COLUMN image TYPE bytea;
