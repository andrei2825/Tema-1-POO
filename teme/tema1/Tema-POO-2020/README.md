# README Tema 1 POO

Nume: Porumb Andrei-Cornel

Grupa: 322CD

## Descriere workflow

1. Crearea entitatilor :
Am creat initial clasele pentru Actors, Movies, Shows si Users, clase cu ajutorul carora am creat obiecte in care am stocat datele din input, astfel putand sa le modific asa cum am avut nevoie.

2. Pentru implementarea solutiilor, am ales sa impart actiunile in mai multe clase principale, Solve care redirectioneaza actiunea catre o clasa specifica, Commands care rezolva actiunile de cip comanda, Queries care se ocupa de queries si recommendations care se ocupa de recomandari.
Pentru a pastra o lungime modesta a claselor, am decis sa impart functionalitatea clasei queries in 5 clase fiecare cu cate o metoda principala specifica acesteia.

3. Pentru introducerea in fisierele de out, am ales sa folosesc urmatoarea sintaxa in care am parcurs toate actiunile primite in input si am creeat cate un fisier de iesire care sa contina id-ul actiunii, criteriul si aoutputul rularii codului meu:

```java
for (ActionInputData action : actions) {
      JSONObject object =
          fileWriter.writeFile(
              action.getActionId(),
              action.getCriteria(),
              solve.solve(action, users, movies, shows, actors));
      arrayResult.add(object);
    }
```



## Detaliere rezolvare

Pentru a putea lucra liber cu datele din input, am ales sa le stochez intr-un "repository" asa cum a fost specificat si in enuntul temei, astfel ca am creat cate un obiect specific fiecarei entitati si am adaugat toate elementele din input in acestea.

Am inceput propriu zis tema prin crearea clasei Solve in care am creat metoda sol ve care imparte rezolvarea altor 3 metode din clase separate. Pentru a face asta am implementat un switch in care cazurile sunt tipul actiunii:

```java
return switch (action.getActionType()) {
    case "command" -> commands.command(action, users, movies, shows);
    case "query" -> queries.query(action, actors, movies, shows, users);
    case "recommendation" -> recommendations.recommendation(action, movies, shows, users);
    default -> action.getActionType();
};
```

I) Primul tip de actiune implementat este cel de comenzi realizat prin intermediul clasei Commands. Metoda dina ceasta clasa primeste o actiune si listele de elemente necesare (users, movies si shows) si in functie de tipul actiunii, realizeaza urmatoarele prelucrari:

1. Pentru tipul "favorite",  am parcurs istoricul fiecarui utilizator pentru a verifica daca acesta a vazut sau nu videoclipul specificat in comanda, iar daca acesta se regasea in istoric, am parcurs lista de filme favorite a utilizatorului. Pentru filmele care se regaseau in lista de favorite sau care nu se regaseau in istoric, am returnat cate o eroare corespunzatoare, iar pentru filmele care au fost vazute dar nu au fost adaugate la favorite, am returnat un mesaj de confirmare si am adaugat videoclipul pe lista de favorite a utilizatorului.

```java
if (viewed == 1) {
    for (String movie : user.getFavoriteMovies()) {
        if (movie.equals(action.getTitle())) {
            return "error -> " + action.getTitle() + " is already in favourite list";
        }
    }
    user.getFavoriteMovies().add(action.getTitle());
    return "success -> " + action.getTitle() + " was added as favourite";
} else {
    return "error -> " + action.getTitle() + " is not seen";
}
```

2. Pentru tipul "view", am decis sa adaug si cate un camp in plus in clasele Movie si Show in care am retinut numarul de vizionari. La fel ca si la tipul "favorite", am parcurs istoricul utilizatorului curent si daca filmul / serialul nu se afla in istoric, il adaugam si returnam un mesaj, iar daca acesta se regasea in istoric, incrementam numarul de vizionari ale acelui videoclip din istoric.

3. Personal, cred ca realizarea corecta a comenzii de rating a fost cea mai dificila parte a temei, nici pana la final neputand sa o realizez perfect pentru fiecare caz (large_test_no_4). Implementarea incercata de mine consta parcurgerea istoricului fiecarui utilizator si calcularea unei medii aritmetice ce reprezinta ratingul acelui videoclip. Pentru a retine utilizatorii care au dat deja rating unui film, am adaugat un arraylist in clasele Movie si Show in care am retinut pe fiecare pozitie (pozitie echivalenta acelui videoclip in istoricul utilizatorului), daca acel utilizator a dat sau nu rating videoclipului.

```java
if (user.getRated().get(user.getRatedIndex()) == 0) {
    if (action.getSeasonNumber() == 0) {
        for (Movie movie : movies) {
            if (movie.getName().equals(action.getTitle())) {
                user.getRated().set(index, 1);
                 movie.setRating(movie.getRating() + action.getGrade());
                movie.setNumRatings(movie.getNumRatings() + 1);
                user.setNumRatings(user.getNumRatings() + 1);
                return "success -> "
                    + action.getTitle()
                    + " was rated with "
                    + action.getGrade()
                    + " by "
                    + username;
```

II) Al doilea tip de actiune implementat este query. Rezolvarea acestei actiuni fiind mult mai ampla, am fost nevoit sa sectionez codul in mai multe clase, fiecare clasa continand metode care se ocupa cu cate unul dintre tipurile de query:

1. Prima clasa implementata este clasa QueryActors care se ocupa cu aplicarea queri-urilor pentru actori. Codul din aceasta clasa este impartit in mai multe metode, o metoda principala pe post de punct de conectare si alte 3 metode care se ocupa de query-urile de tip average, awards si filter_description. 

    i. In metoda aveargeActors am parcurs lista de actori si pentru fiecare actor, am calculat media ratingului tuturor filmelor in care joaca. Pentru a face acest lucru, am parcurs toata filmografia acestuia, iar pentru filmele si serialele care aveau un rating diferit de 0, le adunam ratingul la o suma, pe care o imparteam dupa parcurgere la size-ul filmografiei: 
    
    ```java
    for (Movie movie : movies) {
        if (movie.getName().equals(actor.getFilmography().get(i))) {
            if (movie.getRating() != 0) {
                num += 1;
                if (movie.getNumRatings() != 0) {
                    avg += (movie.getRating() / movie.getNumRatings());
                    -
                    -
                    -
    if (num == 0) {
        actor.setAvgRating(0);
    } else {
        actor.setAvgRating(avg / num);
    }
    ```

    Dupa aflarea ratingului fiecarui actor, am sortat lista de actori mai intai alfabetic, apoi dupa rating.
    
    ii. Pentru implementarea metodei awardsActors, am parcurs lista de elemente din filtrul cu awards si in timp ce parcurg actorii, daca gasesc in lista de premii a unui actor unul dintre premiile din filtru, incrementez o variabila, iar dupa parcurgerea listei de premii, daca acea variabila este egala cu numarul de premii din filtru, voi adauga actorul impreuna cu numarul total de premii intr-un hash-map pe care il ordonez ascendent / descendent.

2.  Urmatoarele 2 clase implementate sunt QueryMovies si QueryShows care sunt asemanatoare in proportie de 90%, insa care au mici diferente acolo unde cerinta pentru show difera de cerinta pentru movie. Acestea sunt impartite inc ate o metoda principala si 4 metode ce rezolva fiecare cerinta.


III) Al treilea si ultimul tip de actiune este cel de recomandari pe care l-am realizat implemenmtand o clasa "Recommendations". In aceasta clasa am creat 6 metode, una principala de tip hub si restul care se ocupa de prelucrarea informatiei. Metodele standardMethod si bestUnseenMethod se aplica pentru orice tip de utilizator, astfel nu a fost nevoie realizarea unei verificari a tipului utilizatorului.

1. Pentru metoda standard, a fost nevoie doar de parcurgerea listei de videoclipuri si returnarea primului videoclip care nu se gasea in istoricul utilizatorului.

2. La implementarea metodei bestUnseen am intampinat o problema si anume ordinea in care sunt stocate datele intr-un HashMap, astfel ca pentru a mentine ordinea de adaugare in memorie, am decis sa folosesc un LinkedHashMap. Pentru rezolvarea propriuzisa a cerintei, am adaugat videoclipurile si ratingul lor intr-un LinkedHashMap pe care l-am ordonat descrescator si am returnat primul rezultat care nu se afla in istoricul utilizatorului.

3. Pentru implementarea metodei popular a fost nevoie de o conditie in plus pentru a verifica daca utilizatorul este eligibil sau nu pentru aceasta functie: 

```java
 for (User user : users) {
      if (user.getUsername().equals(action.getUsername())) {
        if (user.getSubscriptionType().equals("BASIC")) {
          return "PopularRecommendation cannot be applied!";
        } else if (user.getSubscriptionType().equals("PREMIUM")) {
```

Pentru a rezolva cerinta, am creat un LinkedHashMap in care am adaugat toate genurile filmelor si serialelor, ca dupa sa ordonez acestea in functie de numarul de vizionari a filmelor din acele genuri.

4. Metoda pentru faorite functioneaza asemanator cu cea pentru best, dpar ca aceasta cauta prin listele de favorite ale tuturor utilizatorilor. Pentru a rezolva taskul, am parcurs aceste liste si am retinut intr-un linkedHashMap numele si numarul de aparitii in listele de favorite a videoclipului, dupa care l-am sortat descrescator si am retrurnat primul rezultat.

5. La fel ca pentru metodele precedente pentru a incepe metoda search, am realizat verificarea tipului de user. Pentru rezolvarea propriuzisa, am decis sa parcurg listele de genuri ale fiecarui videoclip si sa adaug intr-un map doar filmele / serialele care contin in lista de genuri, genul cautat. Pentru return, am ordonat acest map in ordinea ratingului si alfabetic dupa numele videoclipului.

## Parere personala

Consider ca tema a fost una de dificultate medie. Cele mai grele parti au fost inceperea propriu zisa, care as putea spune ca mi-a luat cam o zi pentru a-mi da seama de unde sa incep si metoda de rating care consider ca ar fi putut avea un test initial mai complex, astfel find mai usor de evitat erorile. Acest ultim fapt cred ca se poate aplica in cazul mai multor teste, acestea fiind usor de rezolvat, deseori avand un rezultat gol care nu descrie perfect functionalitatea metodei. 

Overall, cred ca a fost o tema interesanta si sper ca si urmatoarele sa fie la fel :')