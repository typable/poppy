#!/bin/bash

path=$(pwd)

if [[ ! -z $1 ]]; then
	if [ $1 = "--path" ]; then
		if [[ -z $2 ]]; then
			echo "Invalid arguments '--path <directory>'!"
			exit 0
		fi
		path=$2
	fi
fi

if [ ! -d $path ]; then
	echo "Unable to find directory '$path'!"
	exit 0
fi

if [ ! -f "$(pwd)/plugin.yml" ]; then
	echo "Unable to find plugin.yml in '$(pwd)'!"
	exit 0
fi

jar cf "$path/MinecraftHub.jar" plugin.yml lib/* -C bin .
if [ $? = "0" ]; then
	echo "Successfully compile into $path/MinecraftHub.jar"
else
	echo "Build failed!"
fi