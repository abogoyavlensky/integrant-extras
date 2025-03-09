# integrant-extras

[![Clojars Project](https://img.shields.io/clojars/v/io.github.abogoyavlensky/integrant-extras.svg)](https://clojars.org/io.github.abogoyavlensky/integrant-extras)
[![cljdoc badge](https://cljdoc.org/badge/io.github.abogoyavlensky/integrant-extras)](https://cljdoc.org/jump/release/io.github.abogoyavlensky/integrant-extras)
[![CI](https://github.com/abogoyavlensky/integrant-extras/actions/workflows/snapshot.yaml/badge.svg?branch=master)](https://github.com/abogoyavlensky/integrant-extras/actions/workflows/snapshot.yaml)

Additional utilities for Integrant system and config.

## Overview

The library provides additional utilities and helpers for working with [Integrant](https://github.com/weavejester/integrant), a popular Clojure dependency injection and configuration system. Key features include:

- Enhanced configuration management
- Utility functions for common Integrant operations
- Helper macros for cleaner system definitions


## Development

### Requirements
Install Java, Clojure and Babashka manually or via [mise](https://mise.jdx.dev/):

```shell
mise install
```

*Note: Check versions in `.mise.toml` file.*

### Manage project

All management tasks:

```shell
bb tasks
The following tasks are available:

deps             Install all deps
fmt-check        Check code formatting
fmt              Fix code formatting
lint-init        Import linting configs
lint             Linting project's code
test             Run tests
outdated-check   Check outdated Clojure deps versions
outdated         Upgrade outdated Clojure deps versions
check            Run all code checks and tests
install-snapshot Install snapshot version locally
install          Install version locally
deploy-snapshot  Deploy snapshot version to Clojars
deploy-release   Deploy release version to Clojars
release          Create and push git tag for release
```

## Build and publish

### Install locally

```shell
bb install
```

### Deploy to Clojars from local machine

**Note:** Publishing to Clojars requires `CLOJARS_USERNAME` and `CLOJARS_PASSWORD` environment variables.

Deploy snapshot version:

```shell
bb deploy-snapshot
```

Deploy release version:

```shell
bb deploy-release
```

### Deploy to Clojars from Github Actions

Set up following secrets for Actions:

- `CLOJARS_USERNAME`
- `CLOJARS_PASSWORD`

Then you will be able to push to master branch to deploy snapshot version automatically.

Once you decide to publish release you just need to bump version at deps.edn:

`:aliases -> :build -> :exec-args -> :version -> "0.1.1`

and create a git tag with this version. There is a shortcut command for this:

```shell
bb release
```

This command will create a git tag with the latest version from deps.edn and push it to git repository.
Github Actions will automatically deploy a release version to Clojars.

## License
MIT License
Copyright (c) 2025 Andrey Bogoyavlenskiy
