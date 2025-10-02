# 🚀 Passo a Passo: Deploy da Aplicação MotoTrack (Java + Docker) no Azure App Service

Este guia descreve como publicar a aplicação **MotoTrack** (Java 21, Spring Boot, Docker) no **Azure App Service**, com **Azure SQL Database** como banco de dados.  
Todos os comandos estão prontos para serem executados via **Azure CLI** (no PowerShell ou no Bash).

---

## 📦 1) Build e Push da Imagem no Docker Hub

### a) Build da imagem local
```bash
docker build -t rafael051/mototrack-1.0:latest .
```

### b) Login no Docker Hub
```bash
docker login
```

Informe:
- **Username:** rafael051
- **Password:** sua senha ou token de acesso

Se aparecer:
```bash
Login Succeeded
```

### c) Push da imagem para o Docker Hub
```bash
docker push rafael051/mototrack-1.0:latest
```

Confirme em: [https://hub.docker.com/repositories](https://hub.docker.com/repositories)

---

## 🔑 2) Login no Azure

```bash
az login
```

Autentique-se no navegador.  
Opcional: selecione a subscription correta:

```bash
az account set --subscription "<ID_DA_SUBSCRIPTION>"
```

---

## 🖥️ 3) Criar Resource Group

```bash
az group create -l eastus -n rg-mototrack-appsvc
```

Cria um grupo de recursos na região **eastus**.

---

## 🖥️ 4) Criar App Service Plan (Linux)

```bash
az appservice plan create   --name plan-mototrack-b1   --resource-group rg-mototrack-appsvc   --location eastus   --is-linux   --sku B1
```

Cria um **App Service Plan Linux** no SKU **B1** (plano básico).

---

## 🖥️ 5) Criar Web App (Docker)

```bash
az webapp create   --name app-mototrack-rm557837   --resource-group rg-mototrack-appsvc   --plan plan-mototrack-b1   --deployment-container-image-name rafael051/mototrack-1.0:latest
```

> ⚠️ Aviso depreciação de `--deployment-container-image-name` pode aparecer, mas ainda funciona.

---

## ⚙️ 6) Configurar Variáveis Básicas

```bash
az webapp config appsettings set   --name app-mototrack-rm557837   --resource-group rg-mototrack-appsvc   --settings WEBSITES_PORT=80 SERVER_PORT=80 SPRING_PROFILES_ACTIVE=prod
```

---

## 🗄️ 7) Criar Azure SQL Server (em outra região)

A região **eastus** não aceita novos SQL Servers.  
Use **eastus2** ou **brazilsouth**:

```bash
az sql server create   --name sql-mototrack-rm557837   --resource-group rg-mototrack-appsvc   --location eastus2   --admin-user sqladmin   --admin-password TroqueEstaSenha!2025
```

---

## 🔓 8) Liberar Firewall do SQL

```bash
az sql server firewall-rule create   --resource-group rg-mototrack-appsvc   --server sql-mototrack-rm557837   --name AllowAzureServices   --start-ip-address 0.0.0.0   --end-ip-address 0.0.0.0
```

---

## 🗄️ 9) Criar Database

```bash
az sql db create   --resource-group rg-mototrack-appsvc   --server sql-mototrack-rm557837   --name db_mototrack   --service-objective S0
```

---

## ⚙️ 10) Configurar Variáveis do Spring Datasource

```bash
az webapp config appsettings set   --name app-mototrack-rm557837   --resource-group rg-mototrack-appsvc   --settings     SPRING_DATASOURCE_URL="jdbc:sqlserver://sql-mototrack-rm557837.database.windows.net:1433;database=db_mototrack;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"     SPRING_DATASOURCE_USERNAME=sqladmin     SPRING_DATASOURCE_PASSWORD=TroqueEstaSenha!2025     SPRING_DATASOURCE_DRIVER_CLASS_NAME=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

---

## 🔄 11) Reiniciar App Service

```bash
az webapp restart --name app-mototrack-rm557837 --resource-group rg-mototrack-appsvc
```

---

## 🔎 12) Obter a URL da Aplicação

```bash
az webapp show -g rg-mototrack-appsvc -n app-mototrack-rm557837 --query defaultHostName -o tsv
```

Exemplo de saída:

```
app-mototrack-rm557837.azurewebsites.net
```

Acesse em:

```
https://app-mototrack-rm557837.azurewebsites.net
```

---

## 🧹 13) Remover todos os recursos (opcional)

```bash
az group delete --name rg-mototrack-appsvc --yes --no-wait
```

Remove o grupo de recursos e todos os recursos associados.

---

✅ **Pronto!** Sua aplicação está publicada no **Azure App Service** e integrada ao **Azure SQL Database**.
