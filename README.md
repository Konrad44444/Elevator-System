# Elevator System
System zarządzający windami w budyndku. System może obsłużyć do 16 wind za pomocą wspólnego panelu do ich przywoływania.
System przechowuje informacje o wszystkich windach oraz o tym, która winda została przydzielona do użytkownika.

## Działanie systemu
System działa w trybie turowym. W każdej turze można wygenerować użytkownika z losowym piętrem początkowym oraz końcowym, przywołać windę oraz wpisać piętra ręcznie, uzyskać dane o wybranej windzie oraz wykonać krok symulacji - turę. Użytkownik przywołuje windę przyciskiem "góra" lub "dół". Do systemu wysyłana jest informacja o piętrze wystąpienia i kierunku, w któym chce pojechać użytkownik. System następnie przydziela odpowiednią windę. 

#### Algorytm przydziału windy
Po otrzymaniu zgłoszenia przywołania windy, system wybiera odpowiednią windę na podstawie kierunku oraz piętra przywołania. System rozpatruje 4 przypadki:
  
  1. Na piętrze, z którego wystąpiło przywołanie znajduje się nieaktywna winda - ona zostanie przywołana (jeśli na piętrze jest więcej takich wind wybrana zostanie pierwsza).
  2. Na piętrze nie ma wolnych wind. System szuka wtedy wind, które poruszają się w kierunku podanym przez użytkownika i mają jego piętro na swojej trasie. Zostaje przydzielona winda, która znajduje się najbliżej piętra przywołania (może być także na piętrze użytkownika).
  3. Windy poruszające się w odpowiednim kierunku minęły piętro, na którym wystąpiło przywołanie. System przydziela windę, która kończy swój obieg najbliżej piętra użytkownika.
  4. Żadna z wind nie porusza się w kierunku podanym przez użytkownika. System przydziela windę, której obieg kończy się najbliżej piętra użytkownika.

Po wyborze windy system zwraca informację o tym, która winda została przydzielona i dodaje do trasy windy piętro, na którym czeka użytkownik. Kiedy winda przyjedzie użytkownik do niej "wsiada" i wybiera na panelu windy piętro docelowe.

#### Działanie windy
Winda przechowuje informacje o swoim numerze ID (numery ID zaczynają się od 1), obecnym piętrze, aktualnym piętrze docelowym, kierunku w jakim się porusza, limicie osób, ilości pięter w budynku oraz o trasie, którą się porusza. Aby lepiej zasymulować działanie systemu, każda winda przy tworzeniu otrzymuje losowe wartości piętra obecnego oraz docelowego, dzięki czemu system nie jest statyczny. 

Kiedy winda zostanie wezwana (przydzielona) do jej trasy zostaje dodane piętro, z którego nastąpiło wezwanie. W zależności od tego czy winda porusza się w górę czy w dół trasa (numery pięter) zostaje posortowana (odpowiednio do kierunku) rosnąco lub malejąco. Jeśli aktualne piętro windy pokrywa się z piętrem w trasie winda zatrzymuje się na jedną turę. W momencie przyjazdu na piętro winda wypusza ludzi (jeśli to ich docelowe piętro), w kolejnej turze wsiadają ludzie z piętra (jeśli to ich piętro wezwania, po wejściu osoby te wybierają piętro, na które chcą dojechać i trasa znowu jest odpoweidnio sortowana) i w kolejnej następuje ruch windy.

## Interfejs systemu wind
```js
ElevatorSystem {
  Elevator getElevatorById(Integer elevatorID);
  String getElevatorStatusStringByID(Integer elevatorID);
  List<List<Integer>> getAllElevatorsStatus();
  Elevator callAnElevator(Direction direction, Integer userFloor);
  void addPersonToQueue(Elevator elevator, Person person);
  void checkWaitingPeople();
  void makeSimulationStep();
}
```

## Interfejs windy
```js
Elevator {
  void addStop(Integer floor);
  void updateStatus();
}
```

Mój pomysł nie zezwala na aktualizowanie stanu pojedynczej windy z poziomu systemu - w każdym kroku symulacji (`ElevatorSystem { void makeSimulationStep() }`) system sprawdza, czy ktoś ma wsiąść do windy (`ElevatorSystem { void checkWaitingPeople() }`) oraz aktualizowane są wszystkie windy (`Elevator { void updateStatus() }`). Możliwe jest oczywiście wezwanie windy (`ElevatorSystem { Elevator callAnElevator(Direction direction, Integer userFloor) }`). Po wezwaniu windy zaktualizowana jej trasa o piętro wezwania (`Elevator { void addStop(Integer floor) }`) oraz kolejka osób oczekujących (`ElevatorSystem { void addPersonToQueue(Elevator elevator, Person person) }`). Jeśli osoba wsiadła do windy także dodaje piętro do trasy (`Elevator { void addStop(Integer floor) }`). 

Dostępna jest także funkcja zwracająca informacje o systemie (`ElevatorSystem { List<List<Integer>> getAllElevatorsStatus() }`) - zwraca listę wind z informacjami o numerze ID windy, jej aktualnym piętrze, docelowym piętrze oraz ilości osób w windzie. 

Można także uzyskać informacje o pojedynczej windzie (funkcje `ElevatorSystem { Elevator getElevatorById(Integer elevatorID) }` oraz `ElevatorSystem { String getElevatorStatusStringByID(Integer elevatorID) }`).

## Uruchomienie programu
Program został napisany w środowisku Visual Studio Code. Po zainstalowaniu dodatku Java Extension Pack można uruchomić program z poziomu środowiska. Istnieje także możliwość uruchomienia programu w terminalu. Należy znajdować się w katalogu Program i wpsiać komendę: `java src.Main`
