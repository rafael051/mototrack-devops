# 🚀 Passo a Passo: Publicação da Imagem no Docker Hub

## 1) Faça login no Docker Hub

Se ainda não fez na sessão atual:

```bash
docker login
```

Informe:

- **Username:** rafael051
- **Password:** sua senha ou, preferencialmente, um Token de Acesso.

Se aparecer:

```bash
Login Succeeded
```

---

## 2) Faça o push da imagem para o Docker Hub

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

---

## 3) Confirme no Docker Hub

- Acesse: [https://hub.docker.com/repositories](https://hub.docker.com/repositories)
- Clique no repositório: `rafael051/mototrack-java21`
- Sua imagem deve aparecer com a tag: `latest`

---

## 4) Como outras pessoas podem usar sua imagem

Para baixar e rodar sua imagem:

```bash
docker pull rafael051/mototrack-java21
docker run -d -p 8080:80 --name mototrack rafael051/mototrack-1.0
```

- Baixar a imagem do Docker Hub.
- Rodar o container no background (`-d`), expondo a porta `8080`.

---

---

# 🖥️ Passo a Passo: Criação e Configuração da VM com Docker no Azure

## 1) Criar o Resource Group

```bash
az group create -l eastus -n rg-vm-challenge
```

Cria um grupo de recursos na região `eastus` com o nome `rg-vm-challenge`.

---

## 2) Criar a Máquina Virtual

```bash
az vm create --resource-group rg-vm-challenge --name vm-challenge --image Canonical:ubuntu-24_04-lts:minimal:24.04.202505020 --size Standard_B2s --admin-username admin_fiap --admin-password admin_fiap@123
```

Após a criação da VM, imediatamente:

---

## 3) Criar Regra de Firewall para liberar a porta 8080

```bash
az network nsg rule create --resource-group rg-vm-challenge --nsg-name vm-challengeNSG --name port_8080 --protocol tcp --priority 1010 --destination-port-range 8080
```

Libera tráfego externo na porta `8080`, necessária para acessar a aplicação mapeada via Docker.

---

## 4) Criar Regra de Firewall para liberar a porta 80

```bash
az network nsg rule create  --resource-group rg-vm-challenge --nsg-name vm-challengeNSG--name port_80 --protocol tcp  --priority 1020  --destination-port-range 80
```

Libera tráfego externo na porta `80`, permitindo acesso padrão HTTP.

---

## 5) Conectar via SSH e Instalar o Docker

### a) Obter o IP público da VM

```bash
az vm show -d -g rg-vm-challenge -n vm-challenge --query publicIps -o tsv
```

Copie o IP retornado.

---

### b) Conectar via SSH

```bash
ssh admin_fiap@<IP_DA_VM>
```

Substitua `<IP_DA_VM>` pelo IP copiado.

---

### c) Instalar o Docker

```bash
sudo apt update && sudo apt install -y docker.io
```

Atualiza os pacotes e instala o Docker.

---

### d) Permitir usar Docker sem sudo

```bash
sudo usermod -aG docker $USER && newgrp docker
```

Adiciona o usuário ao grupo do Docker.

---

## 6) Rodar o Container em Background

```bash
docker run -d -p 8080:80 rafael051/mototrack-1.0
```

Executa a aplicação no modo **detached**, mapeando a porta `8080` para `80` dentro do container.

---

## 7) (Opcional) Remover o Resource Group

```bash
az group delete --name rg-vm-challenge --yes --no-wait
```

Remove o grupo de recursos e todos os recursos associados.

---

✅ **Pronto!** Sua aplicação está publicada e acessível via o IP público da VM.
