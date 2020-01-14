#!/usr/bin/env bash

source ./scripts/bash_utils.sh

usage() {
  echo "Usage: $0 [-c|-l|-u] [ -p HTTP_PORT ]"
  exit 2
}

while getopts "lcup:?h" opt; do
  case $opt in
  l) SHOULD_CLEAN=true ;;
  c) SHOULD_COMPILE=true ;;
  u) SHOULD_UPDATE=true ;;
  p) HTTP_PORT=$OPTARG ;;
  h | ?) usage ;;
  esac
done

if [[ -n "$SHOULD_CLEAN" ]]; then
  echo_box_info "Cleaning..."
  sbt clean
fi

if [[ -n "$SHOULD_UPDATE" ]]; then
  echo_box_info "Updating..."
  sbt update
fi

if [[ -n "$SHOULD_COMPILE" ]]; then
  echo_box_info "Compiling..."
  sbt compile
fi

echo "Running server..."
if [[ -n "$HTTP_PORT" ]]; then
  sbt -Dhttp.port="$HTTP_PORT" run
else
  sbt -Dhttp.port=9000 run
fi
