#!/bin/expect
set name [lindex $argv 0]
set password [lindex $argv 1]
spawn openssl genrsa -des3 -out $name.key 2048
expect "Enter pass phrase for $name.key:"
send "$password\r"
expect "Verifying - Enter pass phrase for $name.key:"
send "$password\r"
interact
