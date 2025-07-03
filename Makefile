# Variables
SRC_DIR = src
TEST_DIR = test
BUILD_DIR = build
CLASSES_DIR = $(BUILD_DIR)/cls
DOC_DIR = doc
UML_DIR = $(DOC_DIR)/uml
CLASSPATH = $(CLASSES_DIR):lib/*

# Find all source and test Java files
SRC_FILES = $(shell find $(SRC_DIR) -name "*.java")
UNIT_TEST_FILES = $(shell find $(TEST_DIR)/unit -name "*.java")
FUNCTIONAL_TEST_FILES = $(shell find $(TEST_DIR)/functional -name "*.java")
ALL_TEST_FILES = $(UNIT_TEST_FILES) $(FUNCTIONAL_TEST_FILES)

# Targets
.PHONY: all clean compile doc clean-doc tests

all: compile

# Clean the build directory
clean:
	rm -rf $(BUILD_DIR)

# Compile all source and test files
compile: clean
	mkdir -p $(BUILD_DIR)
	mkdir -p $(CLASSES_DIR)
	javac -d $(CLASSES_DIR) -cp $(CLASSPATH) $(SRC_FILES) $(ALL_TEST_FILES)

# Run all tests
tests: func-tests unit-tests

unit-tests: compile
	@echo "Running all unit tests..."
	@for test in $(UNIT_TEST_FILES:.java=); do \
		test_name=$$(basename $$test); \
		test_class=$$(echo $$test | sed 's/test\/unit\///' | sed 's/\//./g'); \
		echo "Running JUnit test $$test_name... $$test_class"; \
		java -cp $(CLASSPATH):lib/junit-4.13.2.jar:lib/hamcrest-2.2.jar:. \
			org.junit.runner.JUnitCore $$test_class || exit 1; \
	done

func-tests: compile
	@echo "Running functional tests..."
	@for test in $(FUNCTIONAL_TEST_FILES:.java=); do \
		test_name=$$(basename $$test); \
		test_class=$$(echo $$test_name | sed 's/\.java//'); \
		echo "Running functional test $$test_class..."; \
		java -cp $(CLASSPATH):lib/junit-4.13.2.jar:lib/hamcrest-2.2.jar:. \
			org.junit.runner.JUnitCore $$test_class || exit 1; \
	done
	@echo "All tests completed."

# run the renderer
run: compile
	java -cp $(CLASSPATH) renderer.gui.GUIApp

# Generate Javadoc
doc: clean-doc
	mkdir -p ${DOC_DIR}
	javadoc -d ${DOC_DIR} -sourcepath ${SRC_DIR} ${SRC_FILES}

# clean the doc directory
clean-doc:
	rm -rf ${DOC_DIR}

# create plant UML file
createUML: compile
	java -jar lib/plantuml-dep-cli-1.4.0.jar -b src/ -dp "^renderer.(model|gui|controller).*" -o $(UML_DIR)/ClassDiagramWithoutMembers.puml
	java -cp $(CLASSPATH) renderer.doc.CreateClassDiagram -dp "^renderer.(model|gui|controller).*"

# draw plant UML File 
drawUML:
	export PLANTUML_LIMIT_SIZE=8192
	java -jar lib/plantuml-1.2025.3.jar net.sourceforge.plantuml.ant.PlantUmlTask $(UML_DIR)

create-drawUML:createUML drawUML