## Algoritmi 2

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