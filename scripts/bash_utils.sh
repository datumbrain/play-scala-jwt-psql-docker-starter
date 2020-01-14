#!/usr/bin/env bash

echo_info() {
  echo -e "\033[32;1m$1\033[0m"
}

echo_box_info() {
  length=$(expr ${#1} + 4)
  pattern=$(printf "%-${length}s" "*")
  echo_info "${pattern// /*}"
  echo_info "* $1 *"
  echo_info "${pattern// /*}\n"
}

echo_error() {
  echo -e "\033[31;4m$1\033[0m"
}

echo_box_error() {
  length=$(expr ${#1} + 4)
  pattern=$(printf "%-${length}s" "*")
  echo_error "${pattern// /*}"
  echo_error "* $1 *"
  echo_error "${pattern// /*}"
}