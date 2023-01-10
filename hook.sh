#!/bin/bash

cd /home/andreas/git/poppy

git --git-dir=../poppy/.git --work-tree=../poppy pull origin main

/opt/gradle/gradle-7.6/bin/gradle build

cp build/libs/poppy.jar /srv/minecraft/plugins/
