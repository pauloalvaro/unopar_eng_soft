#!/usr/bin/env sh\n\nAPP_DIR=$(dirname "$(readlink -f "$0")")\n\nexec "$APP_DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
