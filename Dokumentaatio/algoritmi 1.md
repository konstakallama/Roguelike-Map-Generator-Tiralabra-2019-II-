## Algoritmi 1

int roomN = huoneiden määrä

int corridorN = sattumanvaraisten lähtevien käytävien määrä per huone

int w = kartan leveys

int h = kartan korkeus


1. Luo roomN huonetta, tämä tapahtuu arpomalla huoneen paikka ja koko ja tarkistamalla, onko paikka validi, toistaen tätä vakiomäärän kertoja (atm 100) tai kunnes validi paikka löytyy. Piirrä huoneet samalla karttaan. O(roomN * 100 * w/5 * h/5) ~ O(roomN * w * h), koska huoneen koon yläraja on w/5 x h/5, paikan validiuden tarkistus kestää huoneen koon verran ja tämä tulee pahimmillaan toistaa jokaiselle huoneelle yritysvakion (100) verran kertoja.
2. Aseta jokaisesta huoneesta lähtemään corridorN käytävää sattumanvaraiseen toiseen huoneeseen (ei kuitenkaan itseensä). O(roomN * corridorN)
3. Etsi dfs:llä ensimmäisestä huoneesta saavutettavissa olevat huoneet. Luo jokaisesta saavuttamattomasta huoneesta käytävä sattumanvaraiseen saavutettavissa olevaan huoneeseen. O(roomN + min(roomN * corridorN, roomN^2)), sillä roomN * corridorN on tässä vaiheessa yläraja käytävien (ie verkon kaarien) määrälle.
4. Piirrä käytävät karttaan. Tämä tapahtuu etsimällä lähtöhuoneen keskikohta ja liikkumalla siitä toisen huoneen suuntaan, kunnes tähän suuntaan liikkumalla ei enää tulla lähemmäksi, sitten liikkumalla toiseen tarvittavaan suuntaan kunnes käytävä on valmis (koska käytössä on neliögrid, tarvittavia suuntia on aina korkeitaan 2). O(roomN^2 * (w+h)), sillä roomN^2 on yläraja käytävien määrälle ja (w+h) yläraja käytävän pituudelle.

Koko algoritmin aikavaativuus on siis parametreista riippuen O(roomN * w * h) tai O(roomN^2 * (w+h)). Näistä useimmiten merkittävämpi on ensimmäinen, sillä tyypillisesti roomN < 10 ja w, h >= 50. Aikavaativuuksien tutkimisen hyödyllisyys on muutenkin ehkä hieman kyseenalaista, koska käyttötilnateissa kaikkien parametrien arvot tulevat joka tapauksessa olemaan pienehköjä; tärkeintä on, että todellinen käytetty aika on pieni (absoluuttinen yläraja tälle olisi 1s, tavoite <500ms).

Tällä hetkellä ainoa valmis tietorakenne, joka tuolta pitäisi omalla toteutuksella korvata on pino (ja senkin voisi periaatteessa tehdä laiskasti taulukolla kun yläraja lisättävien elementtien määrälle on tiedossa). Nimesin laiskan kokorajoitetun listani DynamicArray:ksi, jos joku sille tietää sopivamman nimen niin otan mielelläni vastaan.

Suurin ongelma tässä algoritmissä tällä hetkellä on, että se joskus luo käytäviä, jotka sivuavat huonetta (ja jotka siis loogisesti olisivat osa tuota huonetta) ja vierekkäisiä tuplakäytäviä. Saatan myöhemmin kurssilla parannella tätä näiltä osin, mutta seuraavana halunnen implementoida jonkun toisen algoritmin.