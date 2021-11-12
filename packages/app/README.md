# App

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 12.2.5.

## Local Development

Run `ng serve --watch` to compile your project and run the application locally. Navigate to `http://localhost:4200/`. The app will automatically reload when source files are changed by using the `--watch` flag.

Note: To properly run and use the application locally, a running ElasticSearch instance as well as a running `osaft-api` is needed. Quickest way to do that is by starting up a `docker-compose` stack, then stopping the `osahft-app` with `docker stop osahft-app` and then, starting up the application using `ng serve --watch`. See also the [Docker-Compose-Documentation](https://github.com/osahft/OSAHFT/blob/1.0.0_release_preperation/packages/docker-compose/README.md) on how to run OSAHFT with Docker.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`. `<component-name>` may be prepended by a path of your choosing, i.e. `ng g c src/components/my-component`. 

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.

## Adding https-support

To run the app with https the following properties have to be added to `angular.json` 

```
"ssl": true
"sslKey": "ssl/server.key"
"sslCert": "ssl/server.crt"
```
The key and crt file have to be generated and the file paths have to be adjusted accordingly. 
