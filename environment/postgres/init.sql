
SELECT 'CREATE DATABASE bbs OWNER postgres ENCODING ''UTF8'' LC_COLLATE=''ru_RU.utf8'' LC_CTYPE=''ru_RU.utf8'' TEMPLATE=''template0'' CONNECTION LIMIT 10' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'library')\gexec
;
GRANT ALL PRIVILEGES ON DATABASE library TO postgres;
SELECT 'CREATE DATABASE keycloak OWNER postgres ENCODING ''UTF8'' LC_COLLATE=''ru_RU.utf8'' LC_CTYPE=''ru_RU.utf8'' TEMPLATE=''template0'' CONNECTION LIMIT 10' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'keycloak')\gexec
;
GRANT ALL PRIVILEGES ON DATABASE keycloak TO postgres;