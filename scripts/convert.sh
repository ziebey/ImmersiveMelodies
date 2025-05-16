#!/bin/bash

if [ $# -ne 1 ]; then
    echo "Usage: [input]"
    exit 1
fi

input_file="$1"

base_octave=4

if [ ! -f "$input_file" ]; then
    echo "Input file not found: $input_file"
    exit 1
fi

if ! command -v sox &>/dev/null; then
    echo "Please install 'sox' to use this script."
    exit 1
fi

input_filename_without_ext=$(basename -- "$input_file")
input_filename_without_ext="${input_filename_without_ext%.*}"

mkdir "${input_filename_without_ext}"

for (( octave = 1; octave <= 8; octave++ )); do
    output_file="${input_filename_without_ext}/c${octave}.ogg"


    # In theory, due to perceived loudness this should be one
    # In reality, its somewhere between 0 and 1 and non linear because of biological (ear) and mechanical (instrument) reasons
    # In practice, having two adjustments (here and ingame) is confusing, too static, requires modders to understand and use gain adjustments, ...
    gain_db_factor=0.0

    pitch_shift=$(( octave - base_octave ))
    speed_factor=$(bc -l <<< "2^($pitch_shift)")
    gain_db=$(bc -l <<< "$gain_db_factor * 20 * l(1 / sqrt($speed_factor)) / l(10)")

    sox "$input_file" "$output_file" speed "$speed_factor" gain "$gain_db"
    
    if [ $? -eq 0 ]; then
        echo "Generated $output_file"
        echo "Octave: $octave, Speed: $speed_factor, Gain: $gain_db dB"
    else
        echo "Error generating $output_file"
    fi
done
