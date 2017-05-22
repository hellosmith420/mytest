#!/bin/bash
# search all allocated ports by rabbitmq nodes.
#tested in rhel6 only.
find /path/to/nodes/home -type f \( -name 'rabbitmq.config' -o -name 'rabbitmq-env.conf' \) | while read line;do grep -o '{tcp_listeners.*\|{ssl_listeners.*\|{listener,.*\|RABBITMQ_DIST_PORT.*' $line;done
