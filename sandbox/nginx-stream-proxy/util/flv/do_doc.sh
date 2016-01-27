#!/bin/sh

RST2HTML=rst2html.py

for rstfile in doc/*.rst
do
  rst2html.py -v "$rstfile" "${rstfile/.rst/.html}"
done

