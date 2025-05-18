package se.ifmo.client.ui.locale;

import lombok.Getter;

import java.util.*;

public class LocaleManager {
    @Getter
    private static final List<Locale> availableLocales = List.of(
            Locale.UK,
            Locale.of("ru", "RU"),
            Locale.of("sk", "SK"),
            Locale.of("lv", "LV")
    );

    private static final Map<Locale, Map<String, String>> RESOURCES = new HashMap<>();
    private static final List<Runnable> listeners = new ArrayList<>();
    private static Locale currentLocale = Locale.UK;

    static {
        Map<String, String> enResources = new HashMap<>();
        Map<String, String> ruResources = new HashMap<>();
        Map<String, String> skResources = new HashMap<>();
        Map<String, String> lvResources = new HashMap<>();

        //collection
        enResources.put("id", "ID");
        enResources.put("ownerId", "Owner ID");
        enResources.put("name", "Name");
        enResources.put("coordinateX", "Coordinate X");
        enResources.put("coordinateY", "Coordinate Y");
        enResources.put("creationDate", "Creation date");
        enResources.put("enginePower", "Engine power");
        enResources.put("capacity", "Capacity");
        enResources.put("distanceTravelled", "Distance travelled");
        enResources.put("fuelType", "Fuel type");

        //buttons
        enResources.put("login", "Login");
        enResources.put("register", "Register");
        enResources.put("username", "Username");
        enResources.put("password", "Password");
        enResources.put("signup", "Sign up");
        enResources.put("field.empty", "Please fill up all fields!");

        enResources.put("profile", "Profile");

        enResources.put("filter", "Filter");
        enResources.put("filter.apply", "Apply filter");
        enResources.put("filter.reset", "Reset");

        enResources.put("add", "Add");
        enResources.put("add.new", "Add new");
        enResources.put("add.random", "Add random");
        enResources.put("add.header", "Add an element");
        enResources.put("amount", "Amount");
        enResources.put("cancel", "Cancel");
        enResources.put("add.random.description", "Input amount of elements to be added");
        enResources.put("add.dialog.empty", "Please fill up all fields!");
        enResources.put("add.dialog.amount.positive", "Amount must be positive.");
        enResources.put("add.dialog.amount.max", "You can add 10_000 of elements maximum at once.");
        enResources.put("add.dialog.amount.invalid", "You've input an invalid number.");
        enResources.put("add.dialog.title", "Add random elements");
        enResources.put("add.dialog.header", "Input amount of elements to be added.");
        enResources.put("add.dialog.content", "Input here.");

        enResources.put("clear", "Clear collection");
        enResources.put("visualize", "Visualize");
        enResources.put("visualize.title", "Visualize elements");

        enResources.put("collection", "Collection");

        enResources.put("edit", "Edit");
        enResources.put("edit.header", "Edit element");
        enResources.put("delete", "Delete");

        // collection
        ruResources.put("id", "ID");
        ruResources.put("ownerId", "ID владельца");
        ruResources.put("name", "Имя");
        ruResources.put("coordinateX", "Координата X");
        ruResources.put("coordinateY", "Координата Y");
        ruResources.put("creationDate", "Дата создания");
        ruResources.put("enginePower", "Мощность двигателя");
        ruResources.put("capacity", "Вместимость");
        ruResources.put("distanceTravelled", "Пройденное расстояние");
        ruResources.put("fuelType", "Тип топлива");

        // buttons
        ruResources.put("login", "Войти");
        ruResources.put("register", "Регистрация");
        ruResources.put("username", "Имя пользователя");
        ruResources.put("password", "Пароль");
        ruResources.put("signup", "Зарегистрироваться");
        ruResources.put("field.empty", "Пожалуйста, заполните все поля!");

        ruResources.put("profile", "Профиль");
        ruResources.put("size", "Размер");
        ruResources.put("type", "Тип");
        ruResources.put("element.first", "Первый элемент");

        ruResources.put("filter", "Фильтр");
        ruResources.put("filter.apply", "Применить фильтр");
        ruResources.put("filter.reset", "Сбросить");

        ruResources.put("add", "Добавить");
        ruResources.put("add.new", "Добавить новый");
        ruResources.put("add.random", "Добавить случайный");
        ruResources.put("add.header", "Добавить элемент");
        ruResources.put("amount", "Количество");
        ruResources.put("cancel", "Отмена");
        ruResources.put("add.random.description", "Введите количество элементов для добавления");
        ruResources.put("add.dialog.empty", "Пожалуйста, заполните все поля!");
        ruResources.put("add.dialog.amount.positive", "Количество должно быть положительным.");
        ruResources.put("add.dialog.amount.max", "Вы можете добавить максимум 10 000 элементов за раз.");
        ruResources.put("add.dialog.amount.invalid", "Вы ввели недопустимое число.");
        ruResources.put("add.dialog.title", "Добавить случайные элементы");
        ruResources.put("add.dialog.header", "Введите количество элементов для добавления.");
        ruResources.put("add.dialog.content", "Введите здесь.");

        ruResources.put("clear", "Очистить коллекцию");
        ruResources.put("visualize", "Визуализировать");
        ruResources.put("visualize.title", "Визуализация элементов");

        ruResources.put("collection", "Коллекция");

        ruResources.put("edit", "Редактировать");
        ruResources.put("edit.header", "Редактировать элемент");
        ruResources.put("delete", "Удалить");

        // collection
        skResources.put("id", "ID");
        skResources.put("ownerId", "ID vlastníka");
        skResources.put("name", "Názov");
        skResources.put("coordinateX", "Súradnica X");
        skResources.put("coordinateY", "Súradnica Y");
        skResources.put("creationDate", "Dátum vytvorenia");
        skResources.put("enginePower", "Výkon motora");
        skResources.put("capacity", "Kapacita");
        skResources.put("distanceTravelled", "Prejdená vzdialenosť");
        skResources.put("fuelType", "Typ paliva");

        // buttons
        skResources.put("login", "Prihlásiť sa");
        skResources.put("register", "Registrovať sa");
        skResources.put("username", "Používateľské meno");
        skResources.put("password", "Heslo");
        skResources.put("signup", "Registrácia");
        skResources.put("field.empty", "Vyplňte prosím všetky polia!");

        skResources.put("profile", "Profil");
        skResources.put("size", "Veľkosť");
        skResources.put("type", "Typ");
        skResources.put("element.first", "Prvý prvok");

        skResources.put("filter", "Filter");
        skResources.put("filter.apply", "Použiť filter");
        skResources.put("filter.reset", "Resetovať");

        skResources.put("add", "Pridať");
        skResources.put("add.new", "Pridať nový");
        skResources.put("add.random", "Pridať náhodný");
        skResources.put("add.header", "Pridanie prvku");
        skResources.put("amount", "Množstvo");
        skResources.put("cancel", "Zrušiť");
        skResources.put("add.random.description", "Zadajte počet prvkov na pridanie");
        skResources.put("add.dialog.empty", "Vyplňte prosím všetky polia!");
        skResources.put("add.dialog.amount.positive", "Množstvo musí byť kladné.");
        skResources.put("add.dialog.amount.max", "Môžete pridať maximálne 10 000 prvkov naraz.");
        skResources.put("add.dialog.amount.invalid", "Zadali ste neplatné číslo.");
        skResources.put("add.dialog.title", "Pridať náhodné prvky");
        skResources.put("add.dialog.header", "Zadajte počet prvkov na pridanie.");
        skResources.put("add.dialog.content", "Zadajte sem.");

        skResources.put("clear", "Vyčistiť kolekciu");
        skResources.put("visualize", "Vizualizovať");
        skResources.put("visualize.title", "Vizualizácia prvkov");

        skResources.put("collection", "Kolekcia");

        skResources.put("edit", "Upraviť");
        skResources.put("edit.header", "Upraviť prvok");
        skResources.put("delete", "Vymazať");

        // collection
        lvResources.put("id", "ID");
        lvResources.put("ownerId", "Īpašnieka ID");
        lvResources.put("name", "Nosaukums");
        lvResources.put("coordinateX", "Koordināte X");
        lvResources.put("coordinateY", "Koordināte Y");
        lvResources.put("creationDate", "Izveides datums");
        lvResources.put("enginePower", "Dzinēja jauda");
        lvResources.put("capacity", "Ietilpība");
        lvResources.put("distanceTravelled", "Nobrauktais attālums");
        lvResources.put("fuelType", "Degvielas veids");

        // buttons
        lvResources.put("login", "Pieteikties");
        lvResources.put("register", "Reģistrēties");
        lvResources.put("username", "Lietotājvārds");
        lvResources.put("password", "Parole");
        lvResources.put("signup", "Reģistrācija");
        lvResources.put("field.empty", "Lūdzu, aizpildiet visus laukus!");

        lvResources.put("profile", "Profils");
        lvResources.put("size", "Izmērs");
        lvResources.put("type", "Tips");
        lvResources.put("element.first", "Pirmais elements");

        lvResources.put("filter", "Filtrs");
        lvResources.put("filter.apply", "Piemērot filtru");
        lvResources.put("filter.reset", "Atiestatīt");

        lvResources.put("add", "Pievienot");
        lvResources.put("add.new", "Pievienot jaunu");
        lvResources.put("add.random", "Pievienot nejaušu");
        lvResources.put("add.header", "Pievienot elementu");
        lvResources.put("amount", "Daudzums");
        lvResources.put("cancel", "Atcelt");
        lvResources.put("add.random.description", "Ievadiet pievienojamo elementu skaitu");
        lvResources.put("add.dialog.empty", "Lūdzu, aizpildiet visus laukus!");
        lvResources.put("add.dialog.amount.positive", "Daudzumam jābūt pozitīvam.");
        lvResources.put("add.dialog.amount.max", "Vienlaikus var pievienot ne vairāk kā 10 000 elementu.");
        lvResources.put("add.dialog.amount.invalid", "Jūs ievadījāt nederīgu skaitli.");
        lvResources.put("add.dialog.title", "Pievienot nejaušus elementus");
        lvResources.put("add.dialog.header", "Ievadiet pievienojamo elementu skaitu.");
        lvResources.put("add.dialog.content", "Ievadiet šeit.");

        lvResources.put("clear", "Notīrīt kolekciju");
        lvResources.put("visualize", "Vizualizēt");
        lvResources.put("visualize.title", "Elementu vizualizācija");

        lvResources.put("collection", "Kolekcija");

        lvResources.put("edit", "Rediģēt");
        lvResources.put("edit.header", "Rediģēt elementu");
        lvResources.put("delete", "Dzēst");

        RESOURCES.put(Locale.UK, enResources);
        RESOURCES.put(Locale.of("ru", "RU"), ruResources);
        RESOURCES.put(Locale.of("sk", "SK"), skResources);
        RESOURCES.put(Locale.of("lv", "LV"), lvResources);
    }

    public static void addLocaleChangeListener(Runnable listener) {
        listeners.add(listener);
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        listeners.forEach(Runnable::run);
    }

    public static String getString(String key) {
        return RESOURCES.get(currentLocale).get(key);
    }
}
