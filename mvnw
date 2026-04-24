#!/bin/sh

set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
WRAPPER_DIR="$SCRIPT_DIR/.mvn/wrapper"
PROPERTIES_FILE="$WRAPPER_DIR/maven-wrapper.properties"

MAVEN_VERSION=$(grep '^mavenVersion=' "$PROPERTIES_FILE" | cut -d'=' -f2-)
DISTRIBUTION_URL=$(grep '^distributionUrl=' "$PROPERTIES_FILE" | cut -d'=' -f2-)
MAVEN_HOME="$WRAPPER_DIR/apache-maven-$MAVEN_VERSION"

if [ ! -x "$MAVEN_HOME/bin/mvn" ]; then
  echo "Downloading Apache Maven $MAVEN_VERSION..."
  mkdir -p "$WRAPPER_DIR"
  ARCHIVE="$WRAPPER_DIR/apache-maven-$MAVEN_VERSION-bin.zip"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "$DISTRIBUTION_URL" -o "$ARCHIVE"
  elif command -v wget >/dev/null 2>&1; then
    wget -O "$ARCHIVE" "$DISTRIBUTION_URL"
  else
    echo "curl or wget is required to download Maven." >&2
    exit 1
  fi
  unzip -oq "$ARCHIVE" -d "$WRAPPER_DIR"
fi

exec "$MAVEN_HOME/bin/mvn" "$@"