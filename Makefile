clear:
	./gradlew clean

build:
	./gradlew buildPlugin

publish:
	./gradlew publishPlugin

lint:
	./gradlew clean ktlintCheck detekt

tag:
	git diff-index --quiet HEAD --  # checks for unstaged/uncomitted files
	git tag "v`grep '^version' build.gradle.kts  | cut -f2 -d'=' | tr -d '" '`"
	git push --tags

check-master:
	if [[ `git rev-parse --abbrev-ref HEAD` != "master" ]]; then exit 1; fi

pull:
	git pull

release: check-master pull lint tag publish
