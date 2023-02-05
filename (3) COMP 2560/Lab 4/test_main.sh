printf "\nTesting main...\n\n"

pass=0;fail=0

while IFS="," read -r -a line || [ -n "${line[1]}" ]; do
    result=$(./main <<< ${line[0]})
    echo -n "input: ${line[0]}, main: $result, correct: ${line[1]} ==> "
    if [[ $result == ${line[1]} ]]; then
        echo "Passed"
        let pass++
    else
        echo "Failed"
        let fail++
    fi

done < "./test_inputs.txt"

printf "\nTotal Passed: $pass\nTotal Failed: $fail\n\n"

read -p "Press Enter to Exit: "