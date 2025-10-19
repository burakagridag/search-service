# Use existing Cloud Run service account (created in bootstrap phase)
data "google_service_account" "search_cloud_run_sa" {
  project    = var.project_id
  account_id = "search-svc-cloud-run"
}

# Use existing shared GitHub Actions service account
# This service account is shared across all services
data "google_service_account" "search_github_actions_sa" {
  project    = var.project_id
  account_id = "github-actions-deploy"
}

# Cloud Run service account permissions
# Note: IAM roles are assigned manually due to permission limitations
# Required roles for search_cloud_run_sa:
# - roles/secretmanager.secretAccessor
# - roles/cloudsql.client

# GitHub Actions service account permissions  
# Note: IAM roles are assigned manually due to permission limitations
# Required roles for search_github_actions_sa:
# - roles/run.admin
# - roles/iam.serviceAccountUser  
# - roles/secretmanager.admin
# - roles/serviceusage.serviceUsageAdmin
# - roles/artifactregistry.writer

# Secret Manager IAM bindings for shared secrets are managed by data-integration-service
# Search service Cloud Run SA needs secretmanager.secretAccessor role for:
# - database_url, database_username, database_password
# - keycloak_issuer_uri, keycloak_jwk_set_uri

# Use existing service account key from data-integration-service
# No need to create a new key for the shared GitHub Actions service account
