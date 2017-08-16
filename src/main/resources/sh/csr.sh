#!/bin/expect
set config [lindex $argv 0]
set keyName [lindex $argv 1]
set csrName [lindex $argv 2]
set country [lindex $argv 3]
set province [lindex $argv 4]
set city [lindex $argv 5]
set company [lindex $argv 6]
set unit [lindex $argv 7]
set common [lindex $argv 8]
set keyPass [lindex $argv 9]
spawn openssl req -config $config -new -sha256 -key $keyName.key -out $csrName.csr -subj /C=$country/ST=$province/L=$city/O=$company/OU=$unit/CN=$common
expect "Enter pass phrase for $keyName.key:"
send "$keyPass\r"
interact