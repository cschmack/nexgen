
CREATE KEYSPACE metrics_service
  WITH strategy_class = 'NetworkTopologyStrategy';

  --AND strategy_options:us_east=1
  --AND strategy_options:us_west=1;

use metrics_service;

CREATE TABLE metrics_data (
    metric_time timestamp,
    source varchar,
    host varchar,
    contexts varchar, // encoded as json
    metric varchar,
    value double,
    PRIMARY KEY (metric_time, source, metric) 
);

-- doesnt need these..
--CREATE INDEX sourceIndex ON metrics_data (source); 
--CREATE INDEX metricIndex ON metrics_data (metric); 
-- testing
-- insert into metrics_data (metric_time, source, host, contexts, metric, value) values ('2012-03-01T00:00:00', 'biz.neustar.nis.cnam2nddip.lib.quova.ip2ll', '127.0.0.1', '{from: windstream}', 'responseTime', 300);
