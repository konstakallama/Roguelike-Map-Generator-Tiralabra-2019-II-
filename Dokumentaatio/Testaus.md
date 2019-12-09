## Testausdokumentti

Testit ajetaan Mapgenerator-kansiosta komennolla "mvn test jacoco:report". Kattavuus tulee kansioon target/site/jacoco.

Projektin testit ovat käytännössä mallia aja generaattori läpi erilaisilla parametreillä -> katso vastaako saatu kartta vaatimuksia (kaikki ruudut saavutettavissa, ei umpikujia, käytetty aika < 500 ms). Monet karttojen "paremmuuden" vertailuun käytettävät ominaisuudet ovat sikäli laadullia, että niiden testaaminen on melko vaikeaa ja vaatisi melkeinpä oman projektinsa ([ks esim](http://www.roguebasin.com/index.php?title=Creating_Measurably_%22Fun%22_Maps)).

Main-luokan gui:n testaaminen on uniteila aika vaikeaa, sitä on testattu käsin (eikä se muutenkaan ole algoritmin toiminnan kannalta olennaista). Tähän liittyen Map-luokan terrainHistory ja siihen liittyvät metodit ovat siinä mielessä "ylimääräisiä", että niitä käytetään ainoastaan Main-luokan visuaaliseen presentaatioon eikä niillä ole muuta vaikutusta ohjelman toimintaan. Jos kuluneessa ajassa/muistitilassa on ongelmia, saattaa johtua näistä.

Location- ja Direction-luokissa saattaa olla jonkin verran ns legacy-koodia otm-roguelikestä jota ei todellisuudessa käytetä tässä projektissa ja joka siksi ei näy testikattavuudessa.