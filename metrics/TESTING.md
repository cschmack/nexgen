
### run the server
ant run

: 267,7 ; curl -v -X POST -H "content-type: application/json" http://localhost:8088/metrics/v1/metrics --data @-<<EOF
[{"timestamp":"2012-04-04T12:33:59.173","service":"biz.neustar.nis","customer":"windstream","resource":"http://www.foo.com","values":{"method":"testCreation"}, "foo":"boo"}]


### test a simple metric query, expect 204 - No Content
curl -v -X POST -H "content-type: application/json" http://localhost:8088/metrics/v1/metrics --data @-<<EOF
[{"timestamp":"2012-04-04T12:33:59.173","from":"biz.neustar.nis","host":"example.com","requestor":"windstream","resource":"http://www.foo.com","values":{"method":"testCreation"}}]
EOF

### test a failing custom context, expect 400 bad request
curl -v -X POST -H "content-type: application/json" http://localhost:8088/metrics/v1/metrics --data @-<<EOF
[{"timestamp":"2012-04-04T12:33:59.173","from":"biz.neustar.nis","host":"example.com","requestor":"windstream","resource":"http://www.foo.com","values":{"method":"testCreation"}, "foo": "dummy"}]
EOF

### send a default config, expect 204 - No Content
curl -v -X POST -H "content-type: application/json" http://localhost:8088/metrics/v1/config --data @-<<EOF
{"name":"biz.neustar.nis","description": "default", "contexts":["foo"]}
EOF

### resend, expect 204 - No Content
curl -v -X POST -H "content-type: application/json" http://localhost:8088/metrics/v1/metrics --data @-<<EOF
[{"timestamp":"2012-04-04T12:33:59.173","from":"biz.neustar.nis","host":"example.com","requestor":"windstream","resource":"http://www.foo.com","values":{"method":"testCreation"}, "foo": "dummy"}]
EOF
