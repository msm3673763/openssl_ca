#!/bin/sh
openssl ca -revoke $1 -keyfile $2 -cert $3 -config $4
openssl ca -gencrl -crldays $5 -keyfile $2 -cert $3 -out $6 -config $4