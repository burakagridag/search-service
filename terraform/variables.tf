variable "project_id" {
  description = "The GCP project ID"
  type        = string
  default     = "invest-xpert-465905"
}

variable "region" {
  description = "The GCP region"
  type        = string
  default     = "europe-west1"
}

variable "service_name" {
  description = "Name of the Cloud Run service"
  type        = string
  default     = "search-service"
}

variable "container_image" {
  description = "Container image URL"
  type        = string
}

variable "min_instances" {
  description = "Minimum number of instances"
  type        = number
  default     = 0
}

variable "max_instances" {
  description = "Maximum number of instances"
  type        = number
  default     = 10
}

variable "cpu_limit" {
  description = "CPU limit for the container"
  type        = string
  default     = "2000m"
}

variable "memory_limit" {
  description = "Memory limit for the container"
  type        = string
  default     = "4Gi"
}

variable "environment" {
  description = "Environment (dev/prod)"
  type        = string
  default     = "dev"
}

# Database Configuration - Using shared secrets, no variables needed

# Keycloak Configuration - Using shared secrets, no variables needed
