def httpserver
node('master') {
  properties([parameters([booleanParam(defaultValue: false, description: 'terraform apply', name: 'terraform-apply'), booleanParam(defaultValue: false, description: 'terraform destroy', name: 'terraform-destroy')])])

    stage('Clone repo') {
      git url: "https://github.com/SharifAbdulcoder/Resume_app.git"
    }

    stage('Check terraform') {
       try {
         // Trying to run terraform command
         env.terraform  = sh returnStdout: true, script: 'terraform --version'
         echo """
         echo AWS CLI is already installed version ${env.terraform}
         """
         } catch(er) {
               // if terraform does not installed in system stage will install the terraform
                stage('Installing Terraform') {
                  sh """
                  sudo yum install wget unzip -y
                  sudo wget https://releases.hashicorp.com/terraform/0.12.6/terraform_0.12.6_linux_amd64.zip
                  sudo unzip terraform_0.12.6_linux_amd64.zip
                  sudo mv terraform /bin/
                  """
                }
             }
           }


    stage('Check docker') {
        try {
          // Trying to run terraform command
          env.docker  = sh returnStdout: true, script: 'docker --version'
          echo """
          echo Docker is already installed version ${env.docker}
          """
          } catch(er) {
                // if terraform does not installed in system stage will install the terraform
                 stage('Installing Docker') {
                   sh """
                   sudo yum install docker -y
                   """
                 }
              }
            }

    stage('Build') {
        httpserver = docker.build("sharifabdulcoder/httpserver")
      }


    stage('Push image') {
       // Push docker image to the Docker hub
        docker.withRegistry('', 'abdul_dockerhub') {
            httpserver.push("0.1")
            httpserver.push("latest")
        }
      }

    stage("Terraform plan"){
          dir("${WORKSPACE}/Containerized-Python_app/") {
          sh "terraform init"
          sh "terraform plan"
          }
        }

        stage("Terraform apply"){
          if (!params.terraform-destroy) {
            if (params.terraform-apply) {
              dir("${WORKSPACE}/Containerized-Python_app/") {
                echo "#### Terraform Applying the Changes #####"
                sh "terraform apply --auto-approve"
              }
            }
          }
        }

        stage('Terraform Destroy') {
              if (!params.terraform-apply) {
                if (params.terraform-destroy) {
                  dir("${WORKSPACE}/Containerized-Python_app/") {
                    echo "##### Terraform Destroying ####"
                    sh "terraform destroy --auto-approve"
                  }
                }
              }
            }
              if (params.terraformDestroy) {
                if (params.terraformApply) {
                  println("""
                  Sorry you can not destroy and apply at the same time
                  """)
               }
           }
      }
