class TraceReader:
    
    public TraceReader(String file) :
        Analyse un fichier ligne par ligne en prenant en parametre le chemin vers ce fichier.
        initialise une liste "trace" d'octet a chaque nouvelle offset "0000" rencontrer et ajoute cette liste a la liste "alltrace" 
        qui a pour role de contenir la totaliter des traces .
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
        construit en protocole avec en variable d'instance toute les traces dans une liste
        ce protocole correspond a la racine de notre arbe.
    
    Protocol(List<String> trace):
        construit un protocole avec en variable d'instance une liste contenan une seule trace et une racine
        faisant referance a lui meme.
        ce protocole correspond a un noeud dans notre arbe.

    protocol(List<String> octet,Protocol racine,List<String> trace):
        construit un protocole pour variable une liste d'octet correspondant octet de ce protocole.
        une racine qui correpond au protocole noeud dont ce protocole est issue.
        ces protocole correspondent au feuille de notre arbre  et contiennent le text resultant de l'analyse effectuer a l'appel de ce constructeur.
        les appel a ETHERNET ,Ip, UDP, DNS,DHCP passe par ce constructeur.

    public void run() throws Exception:
        initie l'analyse des protocole par la creation d'un protocole Ethernet sur chcune des trace.
        une fois l'anlyse du protocole Ethenet effectuer les protocole suivant IP ainsi de suite sont initialiser (toujour au extremiter de notre arbre)

    public int octToDec(String oct) throws Exception  
            prend des octet sous format String en parametre et retourne leur valeur en decimal sous forme d'int.
    
    private boolean octtobin(String oct):
        prend des octet sous format String en parametre et retourne leur valeur binaire sous forme de string.
    
    public String octToAscii(int begin, int end):
         prend des octet sous format String en parametre et retourne les caracter correspondant selon la table ASCII sous forme de string.

    public String toString():
        construit une chaine de caractere correspondant a l'analyse de nos trace en recuperant les chaine de caractere obtenu de chaque protocole instancier,
        par un parcours prefixe de notre arbre.