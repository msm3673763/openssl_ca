#!/bin/expect
set crtName [lindex $argv 0]
set keyName [lindex $argv 1]
set caName [lindex $argv 2]
set p12Name [lindex $argv 3]
set password [lindex $argv 4]
spawn openssl pkcs12 -export -in $crtName.crt -inkey $keyName.key -certfile $caName.crt -out $p12Name.p12
expect "Enter Export Password:"
send "$password\r"
expect "Verifying - Enter Export Password:"
send "$password\r"
interact
