# OSAHFT

![build](https://github.com/osahft/OSAHFT/actions/workflows/buildPushAndDeploy.yml/badge.svg) \
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
## Git Workflow

We are using the [Gitflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow).
