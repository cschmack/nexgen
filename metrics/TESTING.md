
### run the server
ant run

### test a simple metric query, expect 204 - No Content
curl -v -X POST -H "content-type: application/json" http://localhost:8088/metrics/v1/metrics --data @-<<EOF
[{"timestamp":"2012-04-04T12:33:59.173","source":"biz.neustar.nis","host":"example.com","from":"windstream","resource":"http://www.foo.com","values":{"range":"1.5"}}]
EOF

### test a failing custom context, expect 400 - bad request
curl -v -X POST -H "content-type: application/json" http://localhost:8088/metrics/v1/metrics --data @-<<EOF
[{"timestamp":"2012-04-04T12:33:59.173","source":"biz.neustar.nis","host":"example.com","from":"windstream","resource":"http://www.foo.com","values":{"range":"1.5"}, "foo": "dummy"}]
EOF

### send a default config, expect 204 - No Content
curl -v -X POST -H "content-type: application/json" http://localhost:8088/metrics/v1/config --data @-<<EOF
{"name":"biz.neustar.nis","description": "default", "contexts":["foo"]}
EOF

### resend, expect 204 - No Content
curl -v -X POST -H "content-type: application/json" http://localhost:8088/metrics/v1/metrics --data @-<<EOF
[{"timestamp":"2012-04-04T12:33:59.173","source":"biz.neustar.nis","host":"example.com","from":"windstream","resource":"http://www.foo.com","values":{"range":"1.5"}, "foo": "dummy"}]
EOF

### query
curl -v -H "content-type: application/json" -X GET "http://localhost:8088/metrics/v1/query?contexts=source\{biz\},host\{home\}&ts=-12h&te=10h"
