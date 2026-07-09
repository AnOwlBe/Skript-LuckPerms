#!/bin/bash

# Replaces INSERT VERSION with the given version.
# May be useful in future..?
find src/main/java -type f -exec sed -i "s/INSERT VERSION/$1/g" {} \;