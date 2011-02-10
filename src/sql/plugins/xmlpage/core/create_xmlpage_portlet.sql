--
-- Structure for table xmlpage_portlet
--
DROP TABLE IF EXISTS xmlpage_portlet;
CREATE TABLE xmlpage_portlet (
  id_portlet int DEFAULT '0' NOT NULL,
  xmlpage_name varchar(100) DEFAULT NULL,
  xmlpage_style varchar(10) DEFAULT NULL,
  PRIMARY KEY  (id_portlet)
);