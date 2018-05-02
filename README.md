# ee
Aplikacie pre podporu kurzu Java EE

Na úspešné spustenie aplikácií je potrebné postupovať podľa nasledujúcich krokov.
1.	Stiahnuť najnovšiu verziu systému z  https://github.com/EE-DAI-FMPH-UNIBA-SK/ee
2.	Otvoriť všetky 3 aplikácie v Netbeans
3.	Ak nemáte nainštalovaný GlassFish je potrebné ho nainštalovať. V časti Services pravým tlačidlom kliknúť na možnosť Servers, vybrať možnosť Add Server
4.	Spraviť deploy na aplikáciu PrepocetWS
5.	V projekte Domacnost prejsť do časti Web Service References, pravým tlačidlo kliknúť na Prepocet a spraviť naň Refresh
6.	Spustiť GlassFish ak ste ho náhodou medzičasom vypli
7.	Spustiť príkazový riadok a dostať sa do priečinka kde je nainštalovaný GlassFish
8.	Vojsť do priečinka bin
9.	Spustiť postupne príkazy:
	a.	asadmin
	b.	create-jms-resource --host localhost --port 4848 --restype javax.jms.TopicConnectionFactory --property  Name=MyConnectionFactory jms/topicfactory
	c.	create-jms-resource --host localhost --port 4848 --restype javax.jms.Topic --property Name=MyTopic jms/topic
10.	Spraviť deploy na projekty Domacnost a KalendarDomacnost
11.	Môžete aplikácie spustiť a na prihlásenie použiť meno admin a heslo admin


