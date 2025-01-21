# Variables
SRC_DIR = src
TEST_DIR = test
BUILD_DIR = build
CLASSPATH = $(BUILD_DIR):lib/*

# Find all source and test Java files
SRC_FILES = $(shell find $(SRC_DIR) -name "*.java")
UNIT_TEST_FILES = $(shell find $(TEST_DIR)/unit -name "*.java")
FUNCTIONAL_TEST_FILES = $(shell find $(TEST_DIR)/functional -name "*.java")
ALL_TEST_FILES = $(UNIT_TEST_FILES) $(FUNCTIONAL_TEST_FILES)

# Targets
.PHONY: all clean compile test

all: compile

# Clean the build directory
clean:
	rm -rf $(BUILD_DIR)

# Compile all source and test files
compile: clean
	mkdir -p $(BUILD_DIR)
	javac -d $(BUILD_DIR) -cp $(CLASSPATH) $(SRC_FILES) $(ALL_TEST_FILES)

# Run all tests
test: compile
	@echo "Running all unit and functional tests..."
	@for test in $(UNIT_TEST_FILES:.java=) $(FUNCTIONAL_TEST_FILES:.java=); do \
	    test_name=$$(basename $$test); \
	    test_class=$$(echo $$test_name | sed 's/\.java//'); \
	    echo "Running $$test_class..."; \
	    java -cp $(CLASSPATH) $$test_class || exit 1; \
	done
	@echo "All tests completed."

# run the renderer
run:
	java -cp $(CLASSPATH) Renderer ${SCENE}