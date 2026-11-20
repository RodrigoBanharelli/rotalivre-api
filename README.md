# Rotalivre — API Java

API REST do Rotalivre, mapa colaborativo de acessibilidade urbana.
Global Solution 2026/2 CitizenTech — Domain Driven Design Using Java — FIAP 1TDSPW.

## Integrantes

| Nome | RM |
| --- | --- |
| Tayna Jimenes Silva | 569337 |
| Gabriel Augusto | 573120 |
| Rodrigo Banharelli de Souza | 570539 |
| Nycolas Melo Escobar | 573052 |
| Davi Trindade Ribeiro | 571336 |

## Links

- API no Render: https://rotalivre-gs.onrender.com (ex.: https://rotalivre-gs.onrender.com/locais)
- Vídeo: https://youtu.be/dCKFwULuO4w

## Como rodar

1. Execute o script `sql/rotalivre_banco.sql` no Oracle.
2. Configure as variáveis de ambiente do banco:

   ```
   DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
   DB_USER=seu_usuario
   DB_PASSWORD=sua_senha
   ```

3. Rode a aplicação (Java 25):

   ```
   ./mvnw spring-boot:run
   ```

   Ou com Docker:

   ```
   docker build -t rotalivre .
   docker run -p 8080:8080 -e DB_URL=... -e DB_USER=... -e DB_PASSWORD=... rotalivre
   ```

4. A API responde em `http://localhost:8080` (ex.: `/usuarios`, `/locais`, `/relatos`).
