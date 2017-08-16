#!/bin/expect
set validity [lindex $argv 0]
set keyName [lindex $argv 1]
set caName [lindex $argv 2]
set country [lindex $argv 3]
set province [lindex $argv 4]
set city [lindex $argv 5]
set company [lindex $argv 6]
set unit [lindex $argv 7]
set common [lindex $argv 8]
set configPath [lindex $argv 9]
set keyPass [lindex $argv 10]
spawn openssl req -new -x509 -days $validity -sha256 -key $keyName.key -out $caName.crt -subj /C=$country/ST=$province/L=$city/O=$company/OU=$unit/CN=$common -config $configPath
expect "Enter pass phrase for $keyName.key:"
send "$keyPass\r"
interact
