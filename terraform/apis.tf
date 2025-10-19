# Enable required Google Cloud APIs
resource "google_project_service" "search_apis" {
  for_each = toset([
    "cloudresourcemanager.googleapis.com",
    "iam.googleapis.com",
    "serviceusage.googleapis.com",
    "cloudbuild.googleapis.com",
    "run.googleapis.com",
    "secretmanager.googleapis.com",
    "containerregistry.googleapis.com",
    "artifactregistry.googleapis.com",
    "compute.googleapis.com"
  ])
  
  project = var.project_id
  service = each.value
  
  # Don't disable the service on destroy
  disable_on_destroy = false
}
