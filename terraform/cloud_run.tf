# Cloud Run service for Search Service
resource "google_cloud_run_v2_service" "search_app" {
  project  = var.project_id
  name     = var.service_name
  location = var.region

  template {
    service_account = data.google_service_account.search_cloud_run_sa.email

    scaling {
      min_instance_count = var.min_instances
      max_instance_count = var.max_instances
    }

    containers {
      image = var.container_image

      ports {
        container_port = 8084
      }

      resources {
        limits = {
          cpu    = var.cpu_limit
          memory = var.memory_limit
        }
      }

      # Database Configuration - Using shared secrets
      env {
        name = "SPRING_DATASOURCE_URL"
        value_source {
          secret_key_ref {
            secret  = "database_url"
            version = "latest"
          }
        }
      }

      env {
        name = "SPRING_DATASOURCE_USERNAME"
        value_source {
          secret_key_ref {
            secret  = "database_username"
            version = "latest"
          }
        }
      }

      env {
        name = "SPRING_DATASOURCE_PASSWORD"
        value_source {
          secret_key_ref {
            secret  = "database_password"
            version = "latest"
          }
        }
      }

      # Keycloak Configuration - Using shared secrets
      env {
        name = "SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI"
        value_source {
          secret_key_ref {
            secret  = "keycloak_issuer_uri"
            version = "latest"
          }
        }
      }

      env {
        name = "KEYCLOAK_ISSUER_URI"
        value_source {
          secret_key_ref {
            secret  = "keycloak_issuer_uri"
            version = "latest"
          }
        }
      }

      env {
        name = "SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI"
        value_source {
          secret_key_ref {
            secret  = "keycloak_jwk_set_uri"
            version = "latest"
          }
        }
      }

      # Hardcoded Keycloak values (not sensitive)
      env {
        name  = "KEYCLOAK_AUTH_SERVER_URL"
        value = "https://keycloak-service-262998085762.europe-west1.run.app"
      }

      env {
        name  = "KEYCLOAK_REALM"
        value = "stock-market"
      }

      env {
        name  = "KEYCLOAK_CLIENT_ID"
        value = "backend-client"
      }

      env {
        name  = "KEYCLOAK_CLIENT_SECRET"
        value = "jxfSG7LwEbl7aARC0gLE9qHELb48MJPl"
      }

      # Application Configuration
      env {
        name  = "SPRING_PROFILES_ACTIVE"
        value = "production"
      }

      env {
        name  = "CORS_ALLOWED_ORIGINS"
        value = "https://investxpert-app.vercel.app,https://*.vercel.app,http://localhost:3000,http://localhost:3001,https://localhost:3000,https://localhost:3001"
      }

      env {
        name  = "GCP_PROJECT_ID"
        value = var.project_id
      }

      # Health check - More lenient startup probe for Spring Boot
      startup_probe {
        http_get {
          path = "/api/actuator/health"
        }
        initial_delay_seconds = 90
        timeout_seconds       = 10
        period_seconds        = 15
        failure_threshold     = 8
      }

      liveness_probe {
        http_get {
          path = "/api/actuator/health"
        }
        initial_delay_seconds = 120
        timeout_seconds       = 5
        period_seconds        = 30
        failure_threshold     = 3
      }
    }
  }

  traffic {
    type    = "TRAFFIC_TARGET_ALLOCATION_TYPE_LATEST"
    percent = 100
  }

  depends_on = [
    data.google_service_account.search_cloud_run_sa
  ]
}

# Allow public access to the Search service
resource "google_cloud_run_service_iam_member" "search_public_access" {
  location = var.region
  project  = var.project_id
  service  = google_cloud_run_v2_service.search_app.name
  role     = "roles/run.invoker"
  member   = "allUsers"
}