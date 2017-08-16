#!/bin/expect
set storeName [lindex $argv 0]
set password [lindex $argv 1]
set caName [lindex $argv 2]
spawn keytool -import -file $caName.crt -keystore $storeName.jks
expect "输入密钥库口令:"
send "$password\r"
expect "再次输入新口令:"
send "$password\r"
expect "是否信任此证书"
send "y\n"
interact
