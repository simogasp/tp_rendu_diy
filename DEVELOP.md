# Development Guide

[![Java CI](https://github.com/simogasp/tp_rendu_diy/actions/workflows/java-ci.yml/badge.svg)](https://github.com/simogasp/tp_rendu_diy/actions/workflows/java-ci.yml)
[![CodeQL](https://github.com/simogasp/tp_rendu_diy/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/simogasp/tp_rendu_diy/actions/workflows/github-code-scanning/codeql)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/aa6822e55c934a7c946946abd20cbfc2)](https://app.codacy.com/gh/simogasp/tp_rendu_diy/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![codecov](https://codecov.io/gh/simogasp/tp_rendu_diy/graph/badge.svg?token=T2W3BQ9FG4)](https://codecov.io/gh/simogasp/tp_rendu_diy)

Please use the provided `checkstyle.xml` file to ensure code quality and consistency.
In Visual Studio Code, you can use the `Checkstyle for Java` extension to automatically format your code according to the rules defined in `checkstyle.xml`.
You can install it from the Marketplace or via command line:

```bash
code --install-extension shengchen.vscode-checkstyle
```

## Generate Class Diagram with Makefile

To generate the [PlantUML](https://plantuml.com/class-diagram) file only do **twice**:

```bash
make createUML
```

To generate the [PlantUML](https://plantuml.com/class-diagram) file and get the png of it **three times**:

```bash
make create-drawUML
```

The class diagram can be a little big, to hide fields you can add :

```none
hide fields
```

in the `doc/uml/ClassDiagram.puml` file.

To hide methods you can add :

```none
hide methods
```

to hide both, you can add :

```none
hide members
```

and then you can run :

```sh
make drawUML
```

### Generate ClassDiagram with Ant

To generate the [PlantUML](https://plantuml.com/class-diagram) file only do :

```bash
ant createUML
```

To generate the [PlantUML](https://plantuml.com/class-diagram) file and get the png of it :

```bash
export PLANTUML_LIMIT_SIZE=8192
ant create-plantUML
```

The class diagram can be a little big, to hide fields you can add :

```none
hide fields
```

in the `doc/uml/Class Diagram.puml` file.

To hide methods you can add :

```none
hide methods
```

to hide both, you can add :

```none
hide members
```

```none
hide members
```

and then you can run :

```sh
ant plantUML
```
