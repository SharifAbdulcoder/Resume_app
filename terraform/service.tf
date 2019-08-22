resource "kubernetes_service" "resume" {
  metadata {
    name      = "resume"
    namespace = "resume"
    labels {
      app = "resume"
    }
  }
  spec {
    port {
      port        = 80
      target_port = "8080"
    }
    selector {
      app = "resume"
    }
    type = "LoadBalancer"
  }
}

