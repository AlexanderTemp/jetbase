#!/usr/bin/env bash
# =============================================================================
# Turns this template into a new project.
#
#   ./scripts/rename.sh com.mycompany.myapp MyApp
#
# Changes: Kotlin package (+ folders), namespace, applicationId, Gradle
# project name, visible app name and the "JetBase" identifiers in the
# code (JetBaseTheme -> MyAppTheme, etc.).
# Run it ONCE, right after cloning, with a clean git tree.
# =============================================================================
set -euo pipefail

if [[ $# -ne 2 ]]; then
  echo "Usage: $0 <new.package> <AppName>   (e.g. $0 com.acme.notes Notes)" >&2
  exit 1
fi

NEW_PKG="$1"
NEW_NAME="$2"
OLD_PKG="com.jetbase"
OLD_NAME="JetBase"
OLD_LOWER="jetbase"
NEW_LOWER="$(echo "$NEW_NAME" | tr '[:upper:]' '[:lower:]')"

if [[ ! "$NEW_PKG" =~ ^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)+$ ]]; then
  echo "Invalid package: $NEW_PKG" >&2; exit 1
fi
if [[ ! "$NEW_NAME" =~ ^[A-Z][A-Za-z0-9]*$ ]]; then
  echo "Invalid name (PascalCase, no spaces): $NEW_NAME" >&2; exit 1
fi

ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

OLD_PATH="${OLD_PKG//.//}"
NEW_PATH="${NEW_PKG//.//}"

echo "1/5 Moving package folders: $OLD_PATH -> $NEW_PATH"
find . -type d -path "*/src/*/kotlin/$OLD_PATH" -not -path "*/build/*" | while read -r dir; do
  base="${dir%/$OLD_PATH}"
  mkdir -p "$base/$NEW_PATH"
  # Move the CONTENT (there can be sub-packages core/, feature/...).
  find "$dir" -mindepth 1 -maxdepth 1 -exec mv {} "$base/$NEW_PATH/" \;
  # Delete the empty folders left over from the old package.
  find "$base" -type d -empty -delete
done

echo "2/5 Replacing text in code, Gradle, XML and scripts"
FILES=$(grep -rlE "$OLD_PKG|$OLD_NAME|$OLD_LOWER" \
  --include='*.kt' --include='*.kts' --include='*.xml' --include='*.toml' \
  --include='*.pro' --include='*.properties' --include='*.md' --include='*.yml' \
  --exclude-dir=build --exclude-dir=.gradle --exclude-dir=.git --exclude-dir=scripts . || true)
for f in $FILES; do
  sed -i \
    -e "s/${OLD_PKG//./\\.}/$NEW_PKG/g" \
    -e "s/$OLD_NAME/$NEW_NAME/g" \
    -e "s/$OLD_LOWER/$NEW_LOWER/g" \
    "$f"
done

echo "3/5 Renaming files that contain '$OLD_NAME'"
find . -type f -name "*$OLD_NAME*" -not -path "*/build/*" -not -path "./.git/*" | while read -r f; do
  mv "$f" "${f//$OLD_NAME/$NEW_NAME}"
done

echo "4/5 Deleting the template's Room schemas (the new project starts at version 1)"
rm -rf core/database/schemas

echo "5/5 Reformatting (the new names change the length of some lines)"
./gradlew ktfmtFormat -q

echo "Listo. Siguiente: ./gradlew assembleDebug testDebugUnitTest"
