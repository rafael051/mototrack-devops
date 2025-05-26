# 🛵 MotoTrack - Sistema Inteligente de Gestão e Mapeamento de Motos

## 📄 Descrição do Projeto

O **MotoTrack** é um sistema inteligente para mapeamento, monitoramento e gestão das motocicletas nos pátios das filiais da **Mottu**, utilizando tecnologias como **API RESTful** e **banco de dados relacional**.

Projeto desenvolvido no contexto do **Challenge 2025 - 1º Semestre**, promovido pela **FIAP** em parceria com a empresa **Mottu**.

## 👨‍💻 Integrantes

- Rafael Rodrigues de Almeida - RM: 557837
- Lucas Kenji Miyahira - RM: 555368

---

## ✅ Passo a Passo: Publicação da Imagem no Docker Hub

### 1) Faça login no Docker Hub

```bash
docker login
```

Informe:

- Username: rafael051
- Password: sua senha ou token de acesso.

Se aparecer:

```bash
Login Succeeded
```

### 2) Faça o push da imagem para o Docker Hub

```bash
docker push rafael051/mototrack-1.0
```

O Docker enviará todas as camadas da imagem para o seu repositório.

Exemplo de saída:

```bash
The push refers to repository [docker.io/rafael051/mototrack-1.0]
8fc2ab47dc9b: Pushed
...
latest: digest: sha256:...
```

### 3) Confirme no Docker Hub

Acesse:

https://hub.docker.com/repositories

Repositório: `rafael051/mototrack-java21`

Sua imagem deve aparecer com a tag: `latest`

### 4) Como outras pessoas podem usar sua imagem

Para baixar e rodar sua imagem:

```bash
docker pull rafael051/mototrack-java21
docker run -d -p 8080:80 --name mototrack rafael051/mototrack-1.0
```

---

## ✅ Passo a Passo: Criação e Configuração da VM com Docker no Azure

### 1) Criar o Resource Group

```bash
az group create -l eastus -n rg-vm-challenge
```

### 2) Criar a Máquina Virtual

```bash
az vm create --resource-group rg-vm-challenge --name vm-challenge --image Canonical:ubuntu-24_04-lts:minimal:24.04.202505020 --size Standard_B2s --admin-username admin_fiap --admin-password admin_fiap@123
```

### 3) Criar regra de firewall para liberar a porta 8080

```bash
az network nsg rule create --resource-group rg-vm-challenge --nsg-name vm-challengeNSG --name port_8080 --protocol tcp --priority 1010 --destination-port-range 8080
```

### 4) Criar regra de firewall para liberar a porta 80

```bash
az network nsg rule create  --resource-group rg-vm-challenge --nsg-name vm-challengeNSG--name port_80 --protocol tcp  --priority 1020  --destination-port-range 80
```

### 5) Conectar via SSH e Instalar o Docker

#### a) Obter o IP público da VM

```bash
az vm show -d -g rg-vm-challenge -n vm-challenge --query publicIps -o tsv
```

Copie o IP retornado.

#### b) Conectar via SSH

```bash
ssh admin_fiap@<IP_DA_VM>
```

Substitua `<IP_DA_VM>` pelo IP copiado.

#### c) Instalar o Docker

```bash
sudo apt update && sudo apt install -y docker.io
```

#### d) Permitir usar Docker sem sudo

```bash
sudo usermod -aG docker $USER && newgrp docker
```

### 6) Rodar o Container em Background

```bash
docker run -d -p 8080:80 rafael051/mototrack-1.0
```

### 7) (Opcional) Remover o Resource Group

```bash
az group delete --name rg-vm-challenge --yes --no-wait
```

---

✅ Pronto! Sua aplicação MotoTrack está publicada, disponível no Azure e acessível via o IP público da VM:

```
http://<IP_DA_VM>:8080/swagger-ui.html
```

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.5
- Docker
- Azure CLI
