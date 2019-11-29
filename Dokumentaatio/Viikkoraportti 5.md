## Viikoraportti 5


Tällä viikolla oli aika vähän aikaa tehdä mitään, aika paljon kaikenlaista päällä.

Päätin toteuttaa kolmantena algoritminä cellular automatoihin perustuvan algoritmin. Se tuottaa hyvin erilaisia karttoja kuin 2 edellistä. Algoritmi on myös erittäin haavoittuvainen parametrien muutoksille; varsinkin floorChancen laskeminen näyttäisi rikkovan sen täysin.

Löysin mg1:stä bugin, mikä jättää sen loputtomaan rekursioon epätavallisen mallisilla huoneilla. En ehtinyt vielä korjata.

Testien randomeista: en tiedä miten tätä ongelmaa saisi fiksusti korjattua, kun algoritmit itsessään käyttävät niin paljon rng:tä. Lisätä kaikkiin parametri, jolla testeissä randomit seedataan?

Nuo algoritmi#.md:t on tarkoitettu vain avaamaan tarkemmin kunkin algoritmin toimintaa, varmaan vähän samaa mitä toteutusdokkarissa pitäisi olla?

Ensi viikolla korjaan mg1:n, kirjoitan toteutus/testausdokkareita ja aloitan tekemään graafista käyttöliittymää demoa varten (otm-rogueliken käyttöliittymä pohjana).
