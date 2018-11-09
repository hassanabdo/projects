#!/bin/bash

apt-key adv --keyserver keyserver.ubuntu.com --recv-keys E298A3A825C0D65DFD57CBB651716619E084DAB9

add-apt-repository 'deb [arch=amd64,i386] https://cran.rstudio.com/bin/linux/ubuntu xenial/'

apt-get update

apt-get install r-base

-i R

install.packages(c("rjson", "rnmf", "Matrix", "MASS"))

q()