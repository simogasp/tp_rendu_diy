JAVAC=javac
JAVA=java
SRC=$(wildcard **/*.java) $(wildcard *.java)
CLASS=$(SRC:%.java=build/%.class)
TESTS=$(wildcard test/Test*.java)
TEST_CLASSES=$(TESTS:%.java=build/%.class)

all: $(CLASS)

build/%.class: %.java
	@mkdir -p $(dir $@)
	$(JAVAC) -d build $<

run: all
	$(JAVA) -cp build Renderer data/example0.scene

test: $(TEST_CLASSES)
	for test in $(patsubst test/%.java,%,$(TESTS)); do \
		$(JAVA) -cp build $$test; \
	done

clean:
	rm -rf build