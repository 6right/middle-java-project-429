.PHONY: install build start test lint

install:
	npm ci

build:
	./gradlew shadowJar

start:
	java -jar build/libs/app.jar

test:
	./gradlew test

lint:
	./gradlew spotlessCheck