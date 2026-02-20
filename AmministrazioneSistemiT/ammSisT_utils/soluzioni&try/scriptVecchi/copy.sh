#!/bin/bash

while true; do
  echo "Starting backup at $(date)..."

  source="save.list"
  echo "Source: $source"

  date=$(date -Iseconds | tr -d '+:')
  fname="bck.$date.tgz"
  tar -czf "$fname" $(cat "$source" | tr '\n' ' ') 2> /dev/null

  echo "$fname created. It contains:"
  cat -n "$source"

  sleep 4h
done

