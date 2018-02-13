Megjegyzések
===============

Az alábbi leírásban ROOT-nak hívom, ahová a ZIP-et kicsomagolod  

MySQL adatbázis
------------------

DDL és DML
    $ROOT/uly-ear/src/main/application/META-INF/uly-ds.xml  # adatbázis user neve ('feri')
    $ROOT/work/create.sql   # minden jogot megad 'feri'-nek, táblákat létrehoz  
    $ROOT/work/import.sql   # tesztadatok a táblákba (3+3 user is)
    
  Használat
    cd $ROOT/work
    mysql -u root -p
    source create.sql  
    source import.sql

Autentikáció és autorizáció
-----------------------------

A Database authorization nem sikerült korrektül, a WildFly adminisztrálásához nem nagyon értek.
Pedig látszik a logban, hogy használja a principalsQuery-t és a rolesQuery-t,
és ha elrontok egy mezőnevet az utóbbiban, akkor sikít.
Tehát megtörténik a rolesQuery beolvasása, mégsem lesz request.isUserInRole() igaz, csak "**"-ra.

Kínomban magam is beolvasom a Myuser, Myrole táblákat, és aszerint engedélyezem a menüpontokat.
Akinek van 'Admin' role-ja, az engedélyezhet, más ('User') csak saját kérelmet írhat.

Mindenesetre:
A <wildfly>/standalone/configuration/standalone.xml-t bemásoltam $ROOT/work/-be.
(bevallom, nem tudom, hogyan szokás azokat a beállításokat deploy-olni).
Fő változtatások az eredetihez képest:

  - mysql driver megadása (h2 alá):
  - autentikációs adatok: ld. "FORM" illetve "ulyDomain" 
       
Futtatás
----------------------
  # MySQL táblák létrehozása: ld. fentebb, a "DDL és DMS" cím alatt
  $ cd <wildfly>
  $ cp $ROOT/work/standalone.xml standalone/configuration/standalone.xml
  $ bin/standalone.sh & 
  $ cd $ROOT
  $ mvn clean package wildfly:deploy
  # browser: localhost:8080/uly-web
  
Teendők (TODO)
===============

- konkurrens használat tesztelése (igyekeztem úgy csinálni)
- a Database autorizáció nem megy (request.isUserInRole() sem), ezért van a web.xml-ben DUMMY
- jelszavak hash-elt tárolása (jelenleg plain). Generálni így lehet:
    cd $JBOSS_HOME/modules/system/layers/base/org/picketbox/main/
    java -cp picketbox-5.0.2.Final.jar org.jboss.security.Base64Encoder "$login_name" 'SHA-256'
- tesztek: unit és end to end
- pagination: láttam rá támogatást pl. PrimeFaces-ben, de ezt most nem függ attól
