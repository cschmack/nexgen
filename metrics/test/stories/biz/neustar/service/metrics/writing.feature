Feature: Write Metrics
  Scenario: Write numeric metric
    Given a metric interface
    When I send key value pair metric data that is numeric
    Then the metric is stored with a timestamp for later retrieval 
    
  Scenario: Write non-numeric metric
    Given a metric interface
    When I send key value pair metric data that is non-numeric
    Then the metric is rejected as invalid
    