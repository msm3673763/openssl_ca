#!/bin/expect
set validity [lindex $argv 0]
set config [lindex $argv 1]
set keyName [lindex $argv 2]
set caName [lindex $argv 3]
set csrName [lindex $argv 4]
set crtName [lindex $argv 5]
set keyPass [lindex $argv 6]
spawn openssl ca -days $validity -md sha256 -config $config -keyfile $keyName.key -cert $caName.crt -in $csrName.csr -out $crtName.crt
expect "Enter pass phrase for $keyName.key:"
send "$keyPass\r"
expect {Sign the certificate? [y/n]:}
send "y\n"
expect {1 out of 1 certificate requests certified, commit? [y/n]}
send "y\n"
interact
