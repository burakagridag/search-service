output "service_url" {
  description = "URL of the deployed Search service"
  value       = google_cloud_run_v2_service.search_app.uri
}

output "service_name" {
  description = "Name of the Search service"
  value       = google_cloud_run_v2_service.search_app.name
}

output "cloud_run_service_account_email" {
  description = "Email of the Cloud Run service account"
  value       = data.google_service_account.search_cloud_run_sa.email
}

output "github_actions_service_account_email" {
  description = "Email of the GitHub Actions service account (shared)"
  value       = data.google_service_account.search_github_actions_sa.email
}

output "artifact_registry_repository" {
  description = "Artifact Registry repository name"
  value       = google_artifact_registry_repository.search_docker_repo.name
}
