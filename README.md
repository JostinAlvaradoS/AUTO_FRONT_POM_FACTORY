docker ps
# AUTO_FRONT_POM_FACTORY — Resumen rápido

Automatización UI (POM + Factory) con Serenity BDD + Cucumber (JUnit 5). El SUT completo (backend, frontend, infra) está en el submódulo `shared-specs/`.

## Quick Start

1) Clonar e inicializar submódulos

```bash
git clone https://github.com/JostinAlvaradoS/AUTO_API_SCREENPLAY.git
cd AUTO_API_SCREENPLAY
git submodule update --init --recursive
```

2) Alinear submódulo a `main` (evita `detached HEAD`)

```bash
cd shared-specs
git checkout main
git pull origin main
cd ..
```

3) Levantar infra (backend)

```bash
cd shared-specs/infra
docker compose up -d --build
```

4) Levantar frontend

```bash
cd ../frontend
pnpm install # o npm install
pnpm dev     # o npm run dev
cd ../../
```

5) Ejecutar tests

```bash
gradle clean test aggregate
```

## Estructura esencial

AUTO_FRONT_POM_FACTORY/
├── src/
├── build.gradle
├── shared-specs/
│   ├── infra/
│   └── frontend/
└── README.md

## Troubleshooting rápido

- Submódulo en `detached HEAD`: `cd shared-specs && git checkout main && git pull`
- Backend no responde: `cd shared-specs/infra && docker compose logs -f` → `docker compose down && up -d --build`
- Frontend falla: `cd shared-specs/frontend && pnpm install && pnpm dev` (usar `nvm` si hace falta)
- ChromeDriver mismatch: emparejar versiones Chrome/ChromeDriver o usar imagen CI compatible

## Requisitos mínimos

- Java JDK (17+), Git, Docker, Docker Compose, Node.js (LTS), pnpm/npm
