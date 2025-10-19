# Project Configuration
project_id = "invest-xpert-465905"
region = "europe-west1"
service_name = "search-service"

# Database Configuration  
database_url = "jdbc:postgresql://aws-0-eu-central-1.pooler.supabase.com:5432/stockmarket?currentSchema=stockmarket_fmp&sslmode=require"
database_username = "postgres.himndihwewpcruwpczgb"
database_password = "agridag7823493!"

# Keycloak Configuration (from deployed keycloak-service)
keycloak_realm = "stock-market"
keycloak_client_id = "backend-client"
keycloak_client_secret = "jxfSG7LwEbl7aARC0gLE9qHELb48MJPl"
keycloak_auth_server_url = "https://keycloak-service-hbwhb26vqq-ew.a.run.app"
keycloak_issuer_uri = "https://keycloak-service-hbwhb26vqq-ew.a.run.app/realms/stock-market"
keycloak_jwk_set_uri = "https://keycloak-service-hbwhb26vqq-ew.a.run.app/realms/stock-market/protocol/openid-connect/certs"

# Security Configuration
cors_allowed_origins = "https://investxpert-app.vercel.app"

# Application Configuration
spring_profiles_active = "prod"
logging_level_root = "INFO"
logging_level_com_investxpert = "DEBUG"
