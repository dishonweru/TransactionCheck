-- Table: transaction

-- DROP TABLE transaction;

CREATE TABLE transaction
(
  tran_id character varying(20),
  tran_ref character varying(20),
  tran_value character varying(20),
  tran_origin character varying(20),
  tran_status character varying(3)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE transaction
  OWNER TO postgres;
