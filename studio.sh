#!/usr/bin/env bash
export GRADLE_HOME="/home/dewmal/applications/gradle-2.14"
export PATH="$PATH:$GRADLE_HOME/bin/"

gradle cleanOsgiRuntime
gradle createOsgiRuntime
mkdir "./build/osgi/load"
chmod +x "./build/osgi/run.sh"
sh "./build/osgi/run.sh"  "$@"