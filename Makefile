publish:
	./gradlew publishPlugin

lint:
	./gradlew clean ktlintCheck detekt

tag:
	git tag "v`grep '^version' build.gradle.kts  | cut -f2 -d'=' | tr -d '" '`"
	git push --tags

check-master:
	if [[ `git rev-parse --abbrev-ref HEAD` != "master" ]]; then exit 1; fi
	git diff-index --quiet HEAD --  # checks for unstaged/uncomitted files

pull:
	git pull

checkchangelog:
	grep -q `grep '^version' build.gradle.kts  | cut -f2 -d'=' | tr -d '" '` CHANGELOG.html

release: check-master pull checkchangelog lint tag publish
