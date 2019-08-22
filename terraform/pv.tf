resource "kubernetes_persistent_volume" "resume" {
  metadata {
    name = "resume"
    labels {
      app = "resume"
    }
  }
  spec {
    capacity {
      storage = "10Gi"
    }
    access_modes                     = ["ReadWriteOnce"]
    persistent_volume_reclaim_policy = "Recycle"
    storage_class_name               = "resume"
  }
}

