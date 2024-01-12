Git Repository Link:
[https://github.com/pkkp86nitd/spring-data-mesh/tree/master]
PG Docker Image URL:
[ https://hub.docker.com/r/pkkp86nitd/postgres ]
Spring App Image URL:
[https://hub.docker.com/r/pkkp86nitd/spring_data_mesh]
Swagger URL:
[http://localhost:8080/swagger-ui/index.html]
Commands to Run the Images:


1.docker pull pkkp86nitd/postgres:latest

2.docker run -d -p 5432:5432 -e POSTGRES_PASSWORD=admin -e POSTGRES_USER=admin -e POSTGRES_DB=albaniro_db 
-v  <path to postgres_container unzip file shared >:/var/lib/postgresql/data 
--name=postgres_pulled pkkp86nitd/postgres:latest

3.docker pull pkkp86nitd/spring_data_mesh:swagger

4.docker run -d -p 8080:8080 --name spring_data_mesh_con \
  -e DB_HOST=172.17.0.2 \
  -e DB_PORT=5432 \
  -e DB_USERNAME=admin \
  -e DB_PASSWORD=admin \
 pkkp86nitd/spring_data_mesh:swagger




 Note -> For Postgres , you can pull official image and create creds (username  = admin, pswd= admin) , create albaniro_db , create customer table 
     CREATE TABLE public.customer (
    customer_id serial PRIMARY KEY,
    name character varying(255),
    email character varying(255),
    phone character varying(20),
    address character varying(255),
    company_name character varying(255),
    industry_type character varying(255),
    customer_status smallint,
    account_manager_id bigint,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

