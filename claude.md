# TotalMed - Backend (Spring Boot)

API REST do TCC TotalMed: **pre-agendamento (ficha digital)** para unidades de saude. Nao substitui a triagem da enfermeira.
Este arquivo resume o estado atual do codigo. Comportamentos nao testados rodando estao marcados como "a testar".

## Stack
Java 21, Spring Boot 4.0.6 (Maven), JPA/Hibernate, SQL Server (runtime), Bean Validation, Lombok,
`spring-security-crypto` (so BCrypt, sem Spring Security completo). H2 so nos testes.

## Como rodar
1. SQL Server em `localhost:1433`, banco `totalmed` criado, usuario `sa`.
2. Senha: variavel de ambiente `DATABASE_PASSWORD` **ou** troque o valor padrao em `application.properties` (`${DATABASE_PASSWORD:troque_aqui}`).
3. Origem do front liberada no CORS: `app.cors.allowed-origin` (padrao `http://localhost:5173`).
4. `ddl-auto=update`: ao mudar tipos de coluna (ex.: enums de ordinal para STRING) apague as tabelas afetadas em dev.
5. Testes usam H2 em memoria (`src/test/resources/application.properties`).

## Fluxo do sistema
1. Paciente cria a ficha (`POST /fichas/pacientes/{id}`): unidade, data preferencial, tipo (NORMAL/PREFERENCIAL/SUPERPREFERENCIAL), motivo opcional. Nasce `PENDENTE`.
2. Recepcao (admin) **aceita** (gera `codigo` unico de 8 caracteres, status `ACEITO`) ou **recusa** (justificativa obrigatoria, status `RECUSADO`).
3. Na chegada, a recepcao valida o codigo (`POST /fichas/validar/{codigo}`): ficha `VALIDADO` e o paciente entra na fila.
4. A triagem e feita pela enfermeira fora do sistema. A recepcao marca `atendido` na fila.
5. Paciente pode cancelar ficha `PENDENTE`/`ACEITO`. `PUT /fichas/expirar` marca como `EXPIRADO` as vencidas.

Regras: unidade precisa estar ativa; so uma ficha `PENDENTE`/`ACEITO` por paciente por unidade; `COM_MOTIVO` exige descricao, `SEM_MOTIVO` proibe.

## Endpoints
| Base | Rotas |
|---|---|
| `POST /login` | `{email, password}` -> `{id, nome, role}` (401 se invalido) |
| `/admin` | CRUD de admin, `GET /admin/pacientes`, `GET /admin/relatorio` (pacientes + fichas pendentes/aceitas/recusadas) |
| `/pacientes` | CRUD, `GET /cpf/{cpf}`, `GET /nome/{nome}` (lista, busca parcial) |
| `/clinicas` | `POST`, `GET`, `GET /{id}` (nasce ativa) |
| `/fichas` | `POST /pacientes/{id}`, `GET /pacientes/{id}`, `GET ?status=`, `GET /{id}`, `PUT /{id}/aceitar`, `PUT /{id}/recusar` (`{justificativa}`), `PUT /{id}/cancelar`, `POST /validar/{codigo}`, `PUT /expirar` |
| `/fila` | `GET` (nao atendidos, por horario de entrada), `PUT /{id}/atendido` |

## Modelo
- `Usuario` abstrato, tabela unica; subtipos `Admin` e `Paciente`. Coluna `tipo_usuario` e o discriminador (`ADMIN`/`PACIENTE`); `role` le a mesma coluna (somente leitura).
- `FichaInicial`: status, tipo, data preferencial, motivo, `codigo`, `justificativaRecusa`, paciente, clinica, fila. Enums gravados como STRING.
- `FilaAtendimento`: paciente, `ordemChegada` (tipo da ficha), `horarioEntrada`, `atendido`.
- `Clinica`: nome, telefone, `ativa`, endereco embutido.
- Consulta, Exame e Medico **foram removidos** (a pasta `Descart` foi apagada). `Role.MEDICO` ficou no enum, sem uso.

## Erros (`ApiError`)
404 nao encontrado, 409 conflito (duplicado, registro em uso/chave estrangeira), 422 regra de negocio, 400 validacao (`detalhes` = "campo: mensagem") ou JSON invalido, 401 credencial invalida. `RuntimeException` crua cai em 400 (fallback).

## Seguranca - estado atual
- Senha com BCrypt; `password` nao sai em nenhuma resposta (`@JsonIgnore`).
- **Ainda sem token/JWT e sem controle de acesso por role**: qualquer cliente chama qualquer endpoint. Limitacao conhecida para citar no TCC.

## Pontos de atencao
- Usuarios antigos (senha em texto puro ou `tipo_usuario` com "Admin"/"Paciente") precisam ser recriados em dev.
- `Clinica` e recebida direto como entidade no POST (sem DTO).
- Nada foi compilado/rodado no momento da geracao: a testar subida, `POST /clinicas`, o fluxo completo da ficha e o `contextLoads` com H2.
