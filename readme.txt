**Analyseur de protocole réseaux offline JAVA 
Logiciel de decodage de trame ethernet prenant en entrée un fichier trace d’octets capturés sur un réseau et renvoyant l’analyse des protocoles Ethernet - IP - UDP - DNS/DHCP .**

class TraceReader:
    
    public TraceReader(String file) :
        Analyse un fichier ligne par ligne en prenant en parametre le chemin vers ce fichier.
        initialise une liste "trace" d'octet a chaque nouvelle offset "0000" rencontre et ajoute cette liste a la liste "alltrace" 
        qui a pour role de contenir la totalite des traces.
        Si l'offset n'est pas valide le programme retourne une erreur.

    private boolean checkHexa(String oct):
        verifie si le string en parametre respecte le format hexadecimal


class UtileFile
    public static String chooseFile() :
        permet a l'utilaseur de choisir un fichier retourne le chemin de celui-ci.

    public static void writeFile (String res):
        cree un fichier "Analyse.txt" contenant la chaine de caractere en parametre.
        supprime le fichier "Analyse.txt" avant l'ecriture si celui ci existe deja.

class Protocole: Design pattern composit
    Protocol(TraceReader t ):
        construit un protocole avec en variable d'instance toute les traces dans une liste
        ce protocole correspond a la racine de notre arbe.
    
    Protocol(List<String> trace):
        construit un protocole avec en variable d'instance une liste contenant une seule trace et une racine
        faisant referance a lui meme.
        ce protocole correspond a un noeud dans notre arbe.

    protocol(List<String> octet,Protocol racine,List<String> trace):
        construit un protocole avec pour variable une liste d'octet contenant les octets du protocole.
        une racine correpondant au protocole noeud dont ce protocole est issue.
        ces protocoles sont les feuilles de notre arbre  et contiennent le texte resultant de l'analyse effectue a l'appel de ce constructeur.
        les appels a ETHERNET ,Ip, UDP, DNS,DHCP passe par ce constructeur.

    public void run() throws Exception:
        initie l'analyse des protocoles par la creation d'un protocole Ethernet sur chcune des traces.
        une fois l'anlyse du protocole Ethenet effectue les protocoles suivant IP sont initialise (toujour au extremiter de notre arbre)

    public int octToDec(String oct) throws Exception  
            prend des octets sous format String en parametre et retourne leur valeur en decimal sous forme d'int.
    
    private boolean octtobin(String oct):
        prend des octets sous format String en parametre et retourne leur valeur binaire sous forme de string.
    
    public String octToAscii(int begin, int end):
         prend des octets sous format String en parametre et retourne les caracteres correspondant selon la table ASCII sous forme de string.

    public String toString():
        construit une chaine de caractere correspondant a l'analyse de nos traces en recuperant les chaines de caractere obtenu de chaque protocole instancier,
        par un parcours prefixe de notre arbre.
        
 __________________________________________________________________________________________________________

PRESENTATION VIDEO :

https://youtu.be/kqDpEsjVv3o

___________________________________________________________________________________________________________
