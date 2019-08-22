resource "kubernetes_deployment" "resume" {
  metadata {
    name      = "resume"
    namespace = "resume"
    labels {
      app = "resume"
    }
  }
  spec {
    replicas = 3
    selector {
      match_labels {
        app = "resume"
      }
    }
    template {
      metadata {
        labels {
          app = "resume"
        }
      }
      spec {
        volume {
          name = "resume-pvc"
          persistent_volume_claim {
            claim_name = "resume-pvc"
          }
        }
        container {
          name  = "resume"
          image = "sharifabdulcoder/app:v2"
          port {
            name           = "resume"
            container_port = 8080
          }
          volume_mount {
            name       = "resume-pvc"
            mount_path = "/mnt/resume"
          }
        }
        restart_policy = "Always"
        node_selector {
          you.are = "wrong"
        }
      }
    }
    strategy {
      type = "RollingUpdate"
      rolling_update {
        max_unavailable = "25%"
        max_surge       = "1"
      }
    }
  }
}

