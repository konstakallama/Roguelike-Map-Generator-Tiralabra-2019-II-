## Toteutusdokumentti

Ideana tehdä joukko erilaisia mapgeneratoreita roguelike-tyylisiin peleihin. Projektin rakenne on aika selkeä: jokainen algoritmi omana MapGenerator-luokkanaan omassa paketissaan, kaksi pakettia aputitorakenteille. Jokaisesta algoritmistä tarkemmin alla:


### Algoritmi 1

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


### Algoritmi 2

int maxW = kartan leveys

int maxH = kartan korkeus

int steps = kuinka monta kertaa algoritmi yrittää lisätä jotain

int minRoomW = huoneen minimileveys

int minRoomH = huoneen minimikorkeus

int maxRoomW = huoneen maksimileveys

int maxRoomH = huoneen maksimikorkeus

int minCorridorLen = käytävän minimipituus

int maxCorridorLen = käytävän maksimipituus

double roomChance = todnäk huoneen tekoon (vs käytävän tekoon)

int connectDistance = maksimipituus, jolla käytävää pidennetään, jos se yhdistäisi huoneeseen/käytävään

1. Luo random kokoinen huone random lokaatioon.
2.	1. Valikoi jokin ruutu, joka on seinää mutta huoneen/käytävän vieressä. Tämä tapahtuu tällä hetkellä tyhmästi kokeilemalla.
	2. Jos valitun ruudun takana on huone, lisää käytävä. Muuten lisää randomilla joko huone tai käytävä.
	3. Jos lisäät käytävää, arvo sen pituus ja tarkista, ovatko käytävän viereiset ruudut seinää. Jos eivät, älä piirrä mitään ja palaa kohdan 2 alkuun.
	4. Katso, voisiko käytävää pidentämällä yhdistää sen huoneeseen/käytävään. Jos voi, pidennä tarvittavan verran. Joka tapauksessa piirrä käytävä.
	5. Jos huone, arvo sen koko. Tarkisto, mahtuuko huone tähän eli onko sen alue ja 1 ruutu sen ympärillä seinää. Jos on, piirrä, muuten älä tee mitään ja palaa kohdan 2 alkuun.
3. Toista kohtaa 2 steps kertaa.
4. Käy kartta läpi ja poista rekursiivisesti umpikujiin päättyvät käytävät.

Tämä algoritmi tekee nätimpiä karttoja kuin ensimmäinen mutta on myös ajallisesti vaativampi. Suoritusaikaa dominoi käytännössä parametri steps, mutta testien perusteella sattumalla on myös suuri merkitys ja samoillakin syötteillä suoritusaika voi heittää pahimmillaan yli sata millisekuntia. Realistisen pienillä arvoilla suoritusaika pysyy kuitenkin erittäin hyvin halutulla <500ms alueella (joskin saattaa hyvin harvoin sen hieman ylittää; alle sekunnin mennään kaikilla järkevillä parametriarvoilla). Steps-arvo 100 000 toimii vielä yleensä hyvin, 1 000 000 huonolla tuurilla yli sekunnin ja 10 000 000 aina useita sekunteja. Jo hyvin pienillä steps-arvoilla (ie 100) saadaan pelikelpoisia, joskin yksinkertaisia karttoja. Laskevat rajahyödyt alkavat tulla vastaan 1000 jälkeen. Näin pienillä arvoilla suoritusajat ovat erinomaisia, yleensä alle 100ms. Korkeat steps-arvot myös luovat karttaan paljon käytäviä, jotka saattavat kiertää kehää; roomChance:n kasvattaminen steps:in kanssa on empiirisesti vaikuttanut hyvältä idealta.


### Algoritmi 3

Täysin erilainen toimintalogiikaltaan ja myös lopputulokseltaan; karttaa ei ole jaoteltu eksplisiittisesti huoneisiin ja käytäviin. Ideana käyttää [cellular automata](https://en.wikipedia.org/wiki/Cellular_automaton)-menetelmää.

Parametrit:

int w = leveys

int h = korkeus

double floorChance = tn lattialle alussa

double floorRatio = osuus, joka tulee olla lattiaa, jotta kartta hyväksytään

int wallAddIts = montako iteraatiota, jossa suuren avoimen lattian keskelle lisätään seinää

int noWallAddIts = montako iteraatiota ilman liäystä

1. Täytä kartta sattumanvaraisesti seinällä ja laittialla floorChance:n mukaisella tn:llä.

2. wallAddIts määrä iteraatiota seuraavilla säännöillä (jokaiselle kartan ruudulle, ensimmäisenä kirjoitetut säännöt ottavat prioriteetin):
	- Jos 3x3 neliö, jonka keskipisteenä tämä ruutu on, sisältää väh. 5 seinää, tämä ruutu on seinää
	- Jos 4x4 neliö sisältää max 2 seinää, tämä ruutu on seinää
	- Jos 3x3:ssa max 2 seinää, tämä ruutu on lattia
	
3. noWallAddIts määrä iteraatioita muuten samoilla säännöillä, mutta ei 4x4-tarkastelua.

4. Laske lattian suhteellinen osuus. Jos < floorRatio, aloita alusta, kasvattaen floorChance:a 0.01:llä (muuten joutuu infinite looppiin liian pienillä floorChanceilla). Muuten valmis.

Aikavaativuus O((wallAddIts + noWallAddIts) * w * h), joskin kertautuu huonoilla parametreilla kun joutuu hylkäämään karttoja (max 100 kertaa, kun floorChance = 1 menee varmasti läpi vaikka olisi tahallaan laittanut epätarkoituksenmukaisen korkean floorRatio:n). Käytännössä esim default floorChance:lla 0.65 joutuu joskus hylkäämään 1 tai 2 karttaa.
