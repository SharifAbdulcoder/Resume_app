resource "kubernetes_persistent_volume_claim" "resume_pvc" {
  metadata {
    name      = "resume-pvc"
    namespace = "resume"
    labels {
      app = "resume"
    }
  }
  spec {
    access_modes = ["ReadWriteOnce"]
    resources {
      requests {
        storage = "2Gi"
      }
    }
    storage_class_name = "resume"
  }
}

