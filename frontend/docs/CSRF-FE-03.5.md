# FE-03.5 — Integrar a estratégia CSRF do backend

**Status: bloqueado pela ausência de proteção CSRF habilitada no backend recebido.**

Critério: operações autenticadas que alteram dados funcionam com a proteção CSRF habilitada.

## Levantamento

O backend foi disponibilizado em `MedScan-main/backend/`. Sua configuração `src/main/java/com/medscan/backend/Configuration/SecurityConfig.java`, linha 46, desabilita explicitamente CSRF:

```java
.csrf(AbstractHttpConfigurer::disable)
```

A busca no código Java não encontrou endpoint ou implementação de fornecimento de token CSRF. Portanto, ainda não há estratégia ativa para o frontend integrar.

O cliente usa credenciais de sessão e centraliza o transporte em `src/services/api.js`. Enviar cookies não substitui enviar o token CSRF exigido pelo servidor.

## Informações necessárias

Definir e habilitar a estratégia no backend antes da integração. Confirmar:

- repositório e handler CSRF utilizados;
- obtenção do token: endpoint e formato da resposta, ou cookie legível pelo JavaScript;
- nome exato do header e cookie, quando aplicável;
- necessidade de buscar um novo token após login/logout;
- operações protegidas e tratamento de token expirado.

O header `X-CSRF-TOKEN` citado no documento de CORS é apenas exemplo, não um contrato confirmado.

## Integração prevista após confirmação

Implementar aquisição e envio do token na camada HTTP, respeitando a estratégia real. Validar no navegador a aquisição, uma operação autenticada, a rejeição de token ausente/inválido e a renovação após mudanças de sessão.

Não foram adicionados endpoints ou nomes de token presumidos. A proteção do backend deve permanecer habilitada. FE-03.5 não está concluído.



