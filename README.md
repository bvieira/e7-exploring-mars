e7-exploring-mars
======================

# Technologies
List of technologies that I chose to work:
* java - required for coding test
* solr - required for coding test
* logback - async logging library
* resteasy - lightweight framework for REST APIs
* guice - lightweight dependency injection framework
* gson - simple serialization/deserialization json library
* assertj, junit, mockito - unit test with mocks and asserts
* jetty - HTTP (Web) server and Java Servlet container
* docker/docker-compose - container, helps to guarantees the environment creation and isolates the development

# Notes
* 


# Build
```sh
$ docker run -it --rm -v "$PWD":/usr/src/project -w /usr/src/project maven mvn clean install
```

# Run
```sh
$ docker-compose up --build -d
```

# Stop
```sh
$ docker-compose stop
```

# Logs
```sh
$ docker-compose logs -f
```

# API
- [Process mission](#process-mission)

## Error handling
if something went wrong on request, the application should return http code different from 2xx and on body the [Error response](#error-response)

## Process mission
process a mission and calculates the final point for each rover

### Request:
`POST` /mission/:name


| param   | description           |
|-------------------|-----------------------|
| `:name`             | `name` for mission  |

#### Body:
- [Mission Request Text](#mission-request-text)


### Response:
| code   | description           | body content |
|-------------------|-----------------------|-------|
| 200             | success  | [Mission Result Response Text](#mission-result-response-text) |
| 400             | invalid request  | [Error response](#error-response) |

### Example:
```sh
$ curl -v -X POST -H "Content-Type: text/plain" localhost:8080/mission/abc -X POST -d $'5 5\n1 2 N\nLMLMLMLMM\n3 3 E\nMMRMMRMRRM'
> POST /mission/abc HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.43.0
> Accept: */*
> Content-Type: text/plain
> Content-Length: 36
>
< HTTP/1.1 200 OK
< Date: Sun, 08 Jan 2017 21:03:28 GMT
< Content-Type: text/plain
< Transfer-Encoding: chunked
< Server: Jetty(9.4.0.v20161208)
<
1 3 N
5 1 E

```

## Search missions
search missions according with query and sort options

### Request:
`GET` /mission?q=:query&sort=:sort


| param   | required | description           |
|-------------------|--|-----------------------|
| `:query`            | yes |  `query` for searching, it uses q parameter from solr, more info for syntax on https://wiki.apache.org/solr/SolrQuerySyntax  |
| `:sort`             | no |  `sort` for searching, format: "field:order". Use 'asc' or 'desc' for order and any field from [Mission Search Response](#mission-search-response)  |


### Response:
| code   | description           | body content |
|-------------------|-----------------------|-------|
| 200             | success  | [Mission Result Response Text](#mission-result-response-text) |
| 400             | invalid request  | [Error response](#error-response) |
| 500             | error accessing solr  | [Error response](#error-response) |

### Example:
```sh
$ curl -v "localhost:8080/mission?q=name:hey"
> GET /mission?q=name:hey HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.43.0
> Accept: */*
>
< HTTP/1.1 200 OK
< Date: Sun, 08 Jan 2017 21:47:49 GMT
< Content-Type: application/json
< Content-Length: 154
< Server: Jetty(9.4.0.v20161208)
<
[{"name":"hey","created":"Jan 7, 2017 1:52:43 AM","width":5,"height":5,"qty_rover":3,"start_points":["1,2","3,3","1,4"],"end_points":["1,3","5,1","3,4"]}]

```

```sh
$ curl -v "http://localhost:8080/mission?q=created:%5B2017-01-07T23:59:59.999Z%20TO%20*%5D&sort=created:desc"
> GET /mission?q=created:%5B2017-01-07T23:59:59.999Z%20TO%20*%5D&sort=created:desc HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.43.0
> Accept: */*
>
< HTTP/1.1 200 OK
< Date: Sun, 08 Jan 2017 21:56:57 GMT
< Content-Type: application/json
< Content-Length: 271
< Server: Jetty(9.4.0.v20161208)
<
[{"name":"def","created":"Jan 8, 2017 7:52:47 PM","width":4,"height":4,"qty_rover":1,"start_points":["1,1"],"end_points":["0,2"]},{"name":"abc","created":"Jan 8, 2017 7:03:28 PM","width":5,"height":5,"qty_rover":2,"start_points":["1,2","3,3"],"end_points":["1,3","5,1"]}]

```

# Schema
## Mission Request Text

| header   | value           |
|-------------------|-----------------------|
| `Content-Type`             | plain/text  |

	number number //surface size
	number number direction //rover position, use N, E, S and W for directions
	action action action... //rover actions, use L, R and M for actions
	//repeat rovers info
	
eg.

	5 5
	1 2 N
	LMLMLMLMM
	3 3 E
	MMRMMRMRRM

## Mission Result Response Text

| header   | value           |
|-------------------|-----------------------|
| `Content-Type`             | plain/text  |

	number number direction //rover position
	//repeat rover positon for each rover
	
eg.

	1 3 N
	5 1 E

## Mission Search Response

| header   | value           |
|-------------------|-----------------------|
| `Content-Type`             | application/json  |

	[
		{
			"name": string,
			"created": string,
			"width": number,
			"height": number,
			"qty_rover": number,
			"start_points": string[],
			"end_points": string[]
		}
	
	]
	
eg.

	[
		{
			"name": "mission1",
			"created": "Jan 7, 2017 1:52:43 AM",
			"width": 5,
			"height": 5,
			"qty_rover": 3,
			"start_points": ["1,2","3,3","1,4"],
			"end_points": ["1,3","5,1","3,4"]
		}
	]


## Error response

| header   | value           |
|-------------------|-----------------------|
| `Content-Type`             | application/json  |

	{
		"message": string
	}
	
eg.

	{
		"message":"RESTEASY003210: Could not find resource for full path: http://localhost:8080/invalid"
	}

