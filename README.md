# Overview

Toy example (for reference for another project) how to sign and verify messages on the JVM.

Generate keys (and convert them) with

    openssl genrsa -out key.private.pem 2048
    openssl pkcs8 -topk8 -inform PEM -outform DER -in key.private.pem -out key.private.der -nocrypt
    openssl rsa -in key.private.pem -pubout -outform DER -out key.public.der

