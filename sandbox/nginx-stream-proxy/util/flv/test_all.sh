#!/bin/sh

TESTDIR=../tests
LOGFILE=test_log.out
OLDLOG=test_log.old

./tool.py --parse-amf "$TESTDIR"/* 2>&1 1>/dev/null |tee "$LOGFILE"

echo "Tests done: $LOGFILE"
[[ -f "$OLDLOG" ]] && diff -U 0 "$OLDLOG" "$LOGFILE" && echo -e "\nNo differences from $OLDLOG"


