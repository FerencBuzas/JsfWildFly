Gyakorlati feladat

Kötelezően használandó technológiák
=====================================

- JavaEE
- Wildfly
- Mysql
- JSF
- Maven
- JPA
- cdi használata
    junior: előny 
    senior: kötelező

Logikai entitások:
====================
- Felhasználó (név, loginnév, jelszó, típus (enum: kérelmező, jóváhagyó)
- Kérelmek (felhasználó, összeg, jóváhagyva)
- Események (dátum, felhasználó, esemény, sikeres / sikertelen)
- Bejelentkezés

Form authetikáció
=====================
o Junioroknak saját
o Senioroknak JAAS

Feladatok:
==============
Három menüpont:
o Kérelmek
o Ügyintézői
o Események

Kérelmek
-----------
o A bejelentkezett felhasználó saját kérelmeit láthatja, módosíthatja jóváhagyás előtt
az összeget, illetve bármikor vehet fel újat

Jóváhagyás
--------------
o Az összes jóváhagyott kérelem listázása readonly módban. Az összes jóváhagyásra
váró kérelem, melyet egy gomb nyomására jóváhagyhat.

Események (létrehozás, állapotváltozások)
------------------------------------------

o Az események megjelenítése readonly módban. Minden módosító eseményt menteni
kell:
 kérelem létrehozás és módosítás
 kérelem jóváhagyás és elutasítás
Sikertelen az esemény, ha exception történt
Admin felületek nem kötelezőek

Leadandó:
-----------
- ear forráskódja
- mysql sql telepítő script (DDL, DML)
- az egész zippelve
- tartalmazzon 3-3- usert
