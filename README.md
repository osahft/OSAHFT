# OSAHFT

![build](https://github.com/osahft/OSAHFT/actions/workflows/buildPushAndDeploy.yml/badge.svg)  | OSAHFT API: [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=osahft_monorepo_api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=osahft_monorepo_api)  | OSAHFT App: [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=osahft_monorepo_app&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=osahft_monorepo_app) \
\
OSAHFT is an open source ad hoc file transfer service that uses files services like [DRACOON](https://www.dracoon.com/) to share files via email. \
Currently [DRACOON](https://www.dracoon.com/) is the only supported file service.

## Project structure 

```
.
├── .github/                            github related stuff such as github actions
├── packages/                           components
│   ├── api/                            spring boot backend
│   ├── app/                            angular frontend
│   ├── docker-compose/                 docker compose
```
## Running OSAHFT
Follow the instructions in [docker-compose/README.md](packages/docker-compose/README.md) 

## Further documentation
The API, APP and Docker-Compose are documented under:  
[API-Documentation](packages/api/README.md)  
[APP-Documentation](packages/app/README.md)  
[Docker-Compose-Documentation](packages/docker-compose/README.md)

## Git Workflow

We are using the [Gitflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow).
