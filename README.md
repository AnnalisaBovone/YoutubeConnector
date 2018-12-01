# se-project-21

Per avviare il file .war è necessario scaricare:

## MONGODB Versione 3.1.1 ubuntu
**Prerequisito**: versione stabile di UBUNTU 18.4, 16.04.5

Guida: https://www.howtoforge.com/tutorial/install-mongodb-on-ubuntu/

sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2930ADAE8CAF5059EE73BB4B58712A2291FA4AD5

echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/3.6 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-3.6.list

sudo apt-get update

sudo apt-get install -y mongodb-org

systemctl star mongod ; systemctl enable mongod ; systemctl status mongod

mongo –host 127.0.0.1:27017


## MONGODB Versione 3.1.1 windows
link al download: https://www.mongodb.com/download-center?jmp=nav#enterprise 

guida per l’installazione e configurazione: https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/ 

Se si vuole visualizzare il database tramite interfaccia grafica usare MongoDB Compass (Versione 1.14.6) link al download: https://www.mongodb.com/products/compass 

## TOMCAT versione 8.5
**Pre requisito**: aver installato java jdk consigliata 1.8. Da terminale eseguire il comando java –version per controllare quale versione è installata sulla propria macchina.

Scaricare questo zip: https://archive.apache.org/dist/tomcat/tomcat-8/v8.5.32/bin/apache-tomcat-8.5.32.zip

Estrarre i file in una cartella a piacere. 

Aprire il file tomcat-user.xml nella cartella conf e aggiungere, all’interno del tag \<tomcat-users\>, un nuovo user come descritto di seguito:

  \<role rolename="manager-gui"/\>
  
  \<user username="YOUR-USERNAME" password="YOUR-PASSWORD" roles="manager-gui"/\>
  
Salvare il file.

Aprire il terminale navigare fino alla cartella dove avete estratto i file e poi entrare nella cartella apache-tomcat-8.5.32\apache-tomcat-8.5.32\bin.

Per windows: eseguire il commando startup. 

Per ubuntu: eseguire i comandi 

$ sudo chmod +x startup.sh

$ sudo chmod +x catalina.sh

$ sudo chmod +x shutdown.sh

$ ./startup.sh


## Configurazione e avvio
A questo punto viene avviato il server sull’indirizzo localhost alla porta 8080 (localhost:8080).

Per testare il corretto funzionamento del server andare alla pagina http://localhost:8080/manager/status accedere con le credenziali inserite precedentemente e verificare che il server sia up and running correttamente.

Navigare in http://localhost:8080/manager/html andare alla sezione Deploy > WAR file to deploy. 

Scegliere il file .war allegato al read me e schiacciare il tasto Deploy. 

Attendere il caricamento della pagina e verificare il corretto avvio dell’applicazione. 

Stoppare l’applicazione in quanto dopo il deploy viene avviata automaticamente. 

A questo punto bisogna impostare i dati di properties dell’applicazione YoutubeConnector. 

Andare nella cartella di tomcat e aprire la cartella webapps, cercare la cartella YoutubeApp dell’applicazione appena caricata, al suo interno WEB-INF > classes aprire il file application.properties e impostare il file di configurazione. 

**comandi per le impostazioni di comunicazione tra l’applicazione e mongodb**

spring.data.mongodb.host=localhost

spring.data.mongodb.port=27017

**comando per dare un nome al database**

spring.data.mongodb.database = __YOUR-DATABASE-NAME__

**per aggiungere video da analizzare inserire gli url dei video di YT come di seguito separati da una virgola (,)**

config.url = https://www.youtube.com/watch?v=VIDEO-ID-1, https://www.youtube.com/watch?v=VIDEO-ID-2, https://www.youtube.com/watch?v=VIDEO-ID-3

**per configurare l’orario di avvio estrazione dati immettere l'orario al posto di hh:mm
N.B.: dato che il processo di estrazione dati richiede un tempo non trascurabile, si consiglia di non impostare un orario d'inizio troppo vicino alla mezzanotte del giorno dopo perchè il tempo di completamento del task potrebbe superare la mezzanotte e quindi si potrebbero avere alcuni record (numero di like, dislike, visualizzazioni) assegnati al giorno richiesto e altri al giorno successivo.**

config.time = __hh:mm__

**per la configurazione delle chiavi di youtube e del sentiment engine**

config.youtubeKey = __YOUR-YOUTUBE-API-KEY__ (vedi sotto come ottenere una chiave)

config.sentimentKey = MTgzMTIzNTctMDdERi00NzRDLThFNzEtNDFCQzMzNTlFNDc1 (questa è la chiave)


Una volta che il file di properties è stato modificato, salvarlo e andare su http://localhost:8080/manager/html e schiacciare il tasto Start per avviare l’applicazione. 

Navigare ora su http://localhost:8080/YoutubeConnector e attendere che il processo di estrazione dati arrivi all’orario prestabilito e inizi a salvare i dati sul database.

**Per visualizzare i dati, browser consigliato: Google Chrome Versione 68.0.3440.106**

**download qui: https://www.google.it/intl/it/chrome/**



Per interrompere l’esecuzione del server:

Per windows: eseguire il comando shutdown

Per ubuntu: 

$ ./shutdown.sh

### Per ottenere una chiave di youtube
**pre requisiti** avere un account google
consultare il seguente link: 

https://console.developers.google.com/apis/library/youtube.googleapis.com?id=125bab65-cfb6-4f25-9826-4dcc309bc508&supportedpurview=project 

premere su “abilita”, seguire le istruzioni fino al raggiungimento della chiave.


## Import in Eclipse
Per visualizzare il codice su eclipse: installare “Spring Tools (aka Spring IDE and Spring Tool Suite) 3.9.5” da EclipseMarketPlace, andare sulla dashboard e importare il progetto.


## Test
Se si vogliono provare i test, importare il progetto in Eclipse con installato Spring sts (scaricarlo dal marketplace). 
Per provare i test strutturali e funzionali è necessario avviare la spring boot application.
Per i test strutturali: avviare la testSuite
Per i test funzionali: assicurarsi di avere installato google chrome. Se si utilizza il sistema operativo Windows, i driver per l’apertura del browser sono già impostati; se si utilizza Linux: modificare in tutti i test funzionali nei metodi createAndStartService il path 

new File("lib/windows/chromedriver.exe")  con   new File(“lib/linux/chromedriver”)

A questo punto aprire il terminale nella cartella YoutubeApp/lib/linux e dare i permessi di esecuzione al file chromedriver con il comando: 

$sudo chmod +x chromedriver. Infine avviare la testSuite. 


I test strutturali coprono il progetto al 90% circa. La mancata copertura totale è dovuta al fatto che non è stato possibile testare il metodo run() nella classe YoutubeConnector che scarica i dati da Youtube. Questo metodo rimane sempre in esecuzione perché controlla di essere nel range temporale corretto per avviare il processo di estrazione dati. In questo metodo, attraverso diverse funzioni che sono state tutte testate, vengono create e lanciate le richieste da sottomettere al servizio API di Youtube in base all’avanzamento del processo e vengono salvati i dati sul database. Dato che le richieste vengono fatte ad una terza parte esterna, Youtube appunto, non c’è modo, in questo particolare metodo, di modificare le risposte in modo da finire nei casi particolari di errore. I casi di errore vengono comunque gestiti, ma sono difficilmente replicabili (server di Youtube in down, mancata connessione, modifica delle API).

