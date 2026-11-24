# MedScan Backend — instalação local no Linux

Guia para Ubuntu ou Debian, usando o backend.zip, Java 17 e MariaDB do XAMPP.

## 1. Instale o JDK 17 e o unzip

```bash
sudo apt update
sudo apt install openjdk-17-jdk unzip
java -version
javac -version
```

O projeto inclui o Maven Wrapper (`mvnw`). Na primeira execução, ele baixa o Maven e as dependências pela internet.

## 2. Instale o XAMPP e inicie o MariaDB

Baixe o instalador Linux x64 no [site oficial do Apache Friends](https://www.apachefriends.org/download.html). Na pasta do download, substitua `NOME_DO_INSTALADOR.run` pelo nome completo do arquivo.

```bash
chmod +x ./NOME_DO_INSTALADOR.run
sudo ./NOME_DO_INSTALADOR.run
```

Se o XAMPP já estiver instalado, comece por aqui:

```bash
sudo /opt/lampp/lampp startmysql
sudo /opt/lampp/lampp status
```

## 3. Prepare o banco

Se você já tem um banco MedScan, use o nome e as credenciais dele no passo 5. Para criar um banco local novo, abra o cliente MariaDB:

```bash
sudo /opt/lampp/bin/mysql -u root -p
```

Execute o SQL abaixo no cliente, substituindo `SUA_SENHA_LOCAL` pela senha que você quer usar.

```sql
CREATE DATABASE IF NOT EXISTS medscan CHARACTER SET utf8mb4;
CREATE USER 'medscan_app'@'127.0.0.1' IDENTIFIED BY 'SUA_SENHA_LOCAL';
GRANT ALL PRIVILEGES ON medscan.* TO 'medscan_app'@'127.0.0.1';
EXIT;
```

O Hibernate cria as tabelas necessárias em um banco vazio. O ZIP não inclui dados iniciais de login.

## 4. Extraia o backend

Na pasta do ZIP:

```bash
unzip backend.zip
cd backend
chmod +x mvnw
```

Se já clonou o repositório, entre na pasta `backend/` que contém o `pom.xml` e o `mvnw`.

## 5. Configure o ambiente e inicie

Na pasta do backend, execute o bloco abaixo. Ajuste a porta, o nome do banco e o usuário conforme a sua instalação.

```bash
export SPRING_DATASOURCE_URL='jdbc:mariadb://127.0.0.1:3306/medscan'
export SPRING_DATASOURCE_USERNAME='medscan_app'
read -rsp 'Senha do MariaDB: ' SPRING_DATASOURCE_PASSWORD
printf '\n'
export SPRING_DATASOURCE_PASSWORD

export MEDSCAN_UPLOAD_DIR="$HOME/medscan/uploads"
mkdir -p "$MEDSCAN_UPLOAD_DIR"

./mvnw clean spring-boot:run
```

Aguarde `Started BackendApplication` no console. O endereço-base é `http://localhost:8080/MedScan`.

## 6. Rode pelo IntelliJ, se preferir

1. Abra a pasta do projeto e importe `backend/pom.xml` como projeto Maven.
2. Em **File → Project Structure**, selecione o JDK 17.
3. Em **Run → Edit Configurations…**, selecione `BackendApplication`.
4. Em **Environment variables**, preencha `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` e `MEDSCAN_UPLOAD_DIR`. Para uploads, use um caminho absoluto gravável, como `/home/breno/medscan/uploads`.
5. Clique em **Run**.

## Erros comuns

| Erro | O que conferir |
|---|---|
| O driver recebe `${SPRING_DATASOURCE_URL}` literalmente | Configure a variável na sessão ou no IntelliJ, conforme a forma de execução. |
| `Connection refused` | Confira o serviço MariaDB e a porta na URL. |
| `Access denied for user` | Confira o usuário, a senha e a permissão para conectar por `127.0.0.1`. |
| `Unknown database` | Confira o nome do banco no final da URL ou crie o banco. |
| `AccessDeniedException: /var/medscan` | Defina `MEDSCAN_UPLOAD_DIR` com um caminho gravável. |
| Porta `8080` ocupada | Libere a porta ou configure `SERVER_PORT=8081`. |
