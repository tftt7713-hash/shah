#!/usr/bin/env sh

# Android Gradle Wrapper Executable Script for Unix/Linux/GitHub Actions
# Do not modify this file.

# Resolve links
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done

SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOVER="`pwd`"
cd "$SAVED" >/dev/null

# Set APP_BASE_NAME
if [ "$OSTYPE" = "cygwin" ] || [ "$OSTYPE" = "msys" ] ; then
  APP_BASE_NAME=`basename "$PRG" .bat`
else
  APP_BASE_NAME=`basename "$PRG"`
fi

# Add default JVM options here.
DEFAULT_JVM_OPTS=""

# Find Java
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/bin/java" ] ; then
        JAVACMD="$JAVA_HOME/bin/java"
    fi
fi

if [ -z "$JAVACMD" ] ; then
    JAVACMD="java"
fi

if ! command -v "$JAVACMD" >/dev/null 2>&1; then
    echo "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH." >&2
    exit 1
fi

# Execute Gradle
exec "$JAVACMD" $DEFAULT_JVM_OPTS -jar "$APP_HOVER/gradle/wrapper/gradle-wrapper.jar" "$@"
