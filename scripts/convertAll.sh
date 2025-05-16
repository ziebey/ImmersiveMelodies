#!/bin/bash
script_dir="$(dirname "$(realpath "$0")")"

for file in *.ogg; do
    if [ -f "$file" ]; then
        echo "Processing file: $file"
        "$script_dir/convert.sh" "$file"
    fi
done
