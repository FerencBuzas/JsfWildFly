#/bin/bash

#
# This script has been used to generate passwords for the test users.
# Not necessary to run it, since the scrambled words have been hardcoded into the create.sql .
# Moreover, I cannot configure WildFly correctly with scrambled passwords.
#   So I use plain text.
#

function error_exit() {
  echo "ERROR: $@"
  exit 1
}

cd $JBOSS_HOME/modules/system/layers/base/org/picketbox/main/
ls -l

function genOne() {
  id="$1"
  login_name="$2"
  name="$3"
  role="$4"
  gen=$(java -cp picketbox-5.0.2.Final.jar org.jboss.security.Base64Encoder "$login_name" 'SHA-256')

  echo "INSERT INTO Myuser(id, roleName, login_name, password, roles) VALUES"
  echo "  ($id, '$roleName', '$login_name', '$gen', '$roles');"
  echo ""
}

genOne 1 'feri'   'Buzas Feri'   'Admin'
genOne 2 'admin2' 'Admin 2'      'Admin'  
genOne 3 'admin3' 'Admin 3'      'Admin' 
genOne 4 'bea'    'Buzas Bea'    'Admin' 
genOne 5 'marci'  'Buzas Marci'  'Admin'
genOne 6 'guest'  'Guest user'   'Admin'

