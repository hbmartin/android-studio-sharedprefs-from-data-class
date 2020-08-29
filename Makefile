clear:
	./gradlew clean

build:
	./gradlew buildPlugin

publish:
	./gradlew publishPlugin

lint:
	./gradlew clean ktlintCheck

tag:
	git diff-index --quiet HEAD --  # checks for unstaged/uncomitted files
	PLUGIN_VERSION=`grep "^version" build.gradle.kts  | cut -f2 -d'=' | tr -d "\" "`
	@echo "tagging $PLUGIN_VERSION"
	git tag "v$PLUGIN_VERSION"
	git push --tags

check-master:
	if [[ `git rev-parse --abbrev-ref HEAD` != "master" ]]; then exit 1; fi

pull:
	git pull

release: check-master pull lint tag publish
