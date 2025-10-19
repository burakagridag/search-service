# Create Artifact Registry repository for Docker images
resource "google_artifact_registry_repository" "search_docker_repo" {
  project       = var.project_id
  location      = var.region
  repository_id = var.service_name
  description   = "Docker repository for ${var.service_name}"
  format        = "DOCKER"
  
  # Make sure APIs are enabled first
  depends_on = []
}

# Note: GitHub Actions service account already has project-level
# artifactregistry.writer permission from iam.tf, which provides
# access to all repositories in the project
