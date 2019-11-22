## Määrittelydokumentti

Toteutan luolastogeeneraattorin roguelike-peliin. Motivaationa on OTM-kurssilla tekemäni [roguelike](https://github.com/konstakallama/otm-harjoitustyo), johon ohjelman voisi yhdistää.

Tarkoitus generoida koko kerroksen kartta valmiiksi kerralla. Aikaa ei saisi kulua sellaista määrää, että pelaaja sen huomaa (-> mitataan millisekunneissa). Ajan riittäessä tarkoitus toteuttaa useita eri algoritmejä ja vertailla niiden toimivuutta. Eri vaihtoehtoja:

[OTM-kurssilla toteutetun alkeellsien algoritmin laajennus](https://github.com/konstakallama/Roguelike-Map-Generator-Tiralabra-2019-II-/blob/master/Dokumentaatio/algoritmi%201.md)


http://www.roguebasin.com/index.php?title=Dungeon-Building_Algorithm mukainen, [Tarkempi selostus syötteistä ym](https://github.com/konstakallama/Roguelike-Map-Generator-Tiralabra-2019-II-/blob/master/Dokumentaatio/algoritmi%202.md)


https://www.rockpapershotgun.com/2015/07/28/how-do-roguelikes-generate-levels/ mukainen, joskin todnäk yhksinkertaistettu, sillä OTM-pelissä ei ole kaikkia näitä hienouksia toteutettu


Ohjelma saa syötteenä jotain parametreja ja tuottaa niitä noudattelevan satunnaisen kartan. OTM-projektissa ollutta käyttöliittymää voi pienein muutoksin helposti käyttää karttojen demoamiseen.
Perusalgoritmin toimiessa näihin voi lisätä esim eri muotoisten huoneiden preferointia, itemien paikoitusta etc.
Arviointiperusteina:

Vastaako yleensä perusvaatimuksia, ie kaikki lokaatiot saavutettavissa, käytävät halutun levyisiä, huoneet/käytävät ei päällekkäin etc

Onnistuuko noudattamaan parametreina annettuja vaatimuksia, esim huoneiden/käytävien määrä tai koko, seinän suhteellinen osuus kartasta, minimimatka aloituspisteestä portaisiin

Lisäfeatureille määritellään omat onnistumiperusteet

Suoritusaika pysyy halutussa kokoluokassa


Kielenä Java.