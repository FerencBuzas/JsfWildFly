#!/bin/bash

# Please adjust these values
# {

wildflyZip="/home/buzas/Downloads/wildfly-11.0.0.Final.zip"
ulyZip="/home/buzas/Downloads/uly.zip"
mysqlJar="/home/buzas/Downloads/mysql-connector-java-5.1.33.jar"
instDir="/home/buzas/work"       # wildly, uly will be installed here
mysqlUser="feri"
wildflyPort="8080"

# }
# --------------------------------------------------

# Do not modify these values
wildflyName="$(basename $wildflyZip .zip)"
export JBOSS_HOME="$instDir/$wildflyName"
ulyRoot="$instDir/uly"
ulyName="$(basename $ulyZip .zip)"
utilDir="$ulyRoot/util"
createSql="$utilDir/create.sql"
importSql="$utilDir/import.sql"
scDir="$JBOSS_HOME/standalone/configuration"
debugLevel=0              # 0: INFO  1: DEBUG  2: TRACE

# ----------- General purpose functions -------------

function INFO() {
    echo "INFO- $@"
}

function DEBUG() {
    [ $debugLevel -ge 1 ] && echo "DEBUG- $@"
}

function TRACE() {
    [ $debugLevel -ge 2 ] && echo "TRACE- $@"
}

function error_exit() {
    echo "ERROR: $@"
    exit 1
}

function pause() {
    read -p "$@ - continue? [Y/n] " yn
    ([ "$yn" == "y" ] || [ "$yn" == "" ]) && return
    error_exit "Aborted"
}

function checkFileExists() {
    TRACE "checkFileExists: $1"
    [ -f "$1" ] || error_exit "No such file: $1"
}

function checkDirExists() {
    TRACE "checkDirExists: $1"
    [ -d "$1" ] || error_exit "No such dir: $1"
}

function copyFile() {
    DEBUG "copying $1 to $2"
    checkFileExists "$1"
    cp "$1" "$2"
    checkFileExists "$2"
}

function checkPort() {
    INFO "Check if port $1 is free"
    sudo netstat -tulpn | grep "$1"
    pause "If it seems to be used, press 'n' and kill it."
}

# ----------- Functions for Uly -------------

function checkEnv() {
    INFO "Checking environment"

    checkPort $wildflyPort

    which unzip || error "Program 'unzip' not found"
    which mysql || error "Program 'mysql' not found"
    which mvn   || error "Program 'mvn' not found"

    checkFileExists "$wildflyZip"
    checkFileExists "$ulyZip"
    checkFileExists "$mysqlJar"
 
    [ ! -d "$instDir" ] && mkdir -p "$instDir"
    [ ! -d "$instDir" ] && error_exit "Could not create $instDir"
    [ -x "$instDir/*" ] && pause "Deleting files in $instDir"
    rm -rf $instDir/*

    INFO "Input files are OK."
    INFO "---------------------------------"    
}

function extract() {

    INFO "Extracting $wildflyZip to $instDir"
    cd "$instDir"
    unzip -q "$wildflyZip" || error_exit "Error unzipping $wildflyZip to $instDir"
    checkDirExists "$wildflyName"

    INFO "Extracting $ulyZip to $instDir"
    unzip -q "$ulyZip" || error_exit "Error unzipping $ulyZip to $instDir"
    checkDirExists "$ulyName"
    
    INFO "Directories created under $instDir:"
    ls "$instDir"
    
    #INFO "Removing directory $JBOSS_HOME/domain" # this is a bad idea
    #rm -rf "$JBOSS_HOME/domain"
    
    # Manually added ulyDomain to original standalone.xml (it is in uly.zip)
    copyFile "$scDir/standalone.xml" "$scDir/standalone_ori.xml"  # save the original
    copyFile "$utilDir/standalone.xml" "$scDir/standalone.xml"

    INFO "---------------------------------"    
}

function startWildfly() {

    INFO "Starting Wildfly from under JBOSS_HOME=$JBOSS_HOME"
    cd "$JBOSS_HOME"
    checkFileExists "$JBOSS_HOME/README.txt"
    bin/standalone.sh &
    sleep 5s
    pause "Please check if Wildfly is running"
}

function addMysqlDriver() {

    INFO "Adding a mysql driver with jboss-cli"
    copyFile "$scDir/standalone.xml" "$scDir/tmp.xml"

    cd "$JBOSS_HOME"
    checkFileExists bin/jboss-cli.sh
    bin/jboss-cli.sh -c --echo-command --command="module add --name=com.mysql.driver  --dependencies=javax.api,javax.transaction.api --resources=$mysqlJar"
    pause "after jboss-cli.sh, mysql"
    
    DEBUG "Comparing standalone.xml before and after adding driver >>>"
    diff "$scDir/tmp.xml" "$scDir/standalone.xml"
    DEBUG "<<<"    

    INFO "---------------------------------"
}

function enableRoleBasedAuth() {

    INFO "Enabling role base authorization"
    bin/jboss-cli.sh -c --echo-command --command="/core-service=management/access=authorization:write-attribute(name=provider,value=rbac)"
    pause "after jboss-cli.sh, enable rbac"
    
    INFO "Restarting WildFly" 
    bin/jboss-cli.sh --connect command=:reload
    sleep 5s
    pause "Please check restarted Wilddfly"
    
    INFO "---------------------------------"
}

function addUsersToWildfly() {

    INFO "Adding users to Wildfly"
    cd "$JBOSS_HOME"/bin
    ./add-user.sh -u feri -p feri
    ./add-user.sh -u bea -p bea
    ./add-user.sh -u marci -p marci
    ./add-user.sh -u admin2 -p admin2
    ./add-user.sh -u admin3 -p admin3
    ./add-user.sh -u guest -p guest -a

    pause "Users hopefully added"
    INFO "---------------------------------"      
}

function createDatabase() {

    INFO "Creating database, user=$mysqlUser"
    checkFileExists "$createSql"
    checkFileExists "$importSql"
    mysql -u "$mysqlUser" -p -e "source $createSql; source $importSql;" || error_exit "mysql source $createSql"
      
    INFO "---------------------------------"      
}

function makeDeploy() {

    INFO "Building, deploying from under $ulyRoot"
    cd $ulyRoot
    checkFileExists "$ulyRoot/pom.xml"

    mvn clean package wildfly:deploy
}

function usage() {
    echo "Usage: $0 [args]"
    echo "  -d:     set debuglevel=1"
    echo "  -h:     help (this text)"
    exit 1
}

# --------- 'main()' ------------------------

while getopts "dht" opt; do
    case "${opt}" in
        d) debugLevel=1;;
        h) usage;;
        t) debugLevel=2;;
        *) usage;;
    esac
done

checkEnv
extract
createDatabase
startWildfly
addUsersToWildfly
addMysqlDriver
enableRoleBasedAuth
makeDeploy
