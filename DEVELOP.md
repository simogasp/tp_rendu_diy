# Development Guide

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
```
hide fields
```
in the `doc/uml/ClassDiagram.puml` file.

To hide methods you can add :
```
hide methods
```

to hide both, you can add :
```
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
```
hide fields
```
in the `doc/uml/Class Diagram.puml` file.

To hide methods you can add :
```
hide methods
```

to hide both, you can add :
```
hide members
```

and then you can run :
```sh
ant plantUML
```

