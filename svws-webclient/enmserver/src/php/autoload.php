<?php

if (!defined('PHPWENOM_VERSION')) {
    // Definiere eine Konstante, um prüfen können, ob die Autoload-Funktion weiter unten schon registriert wurde
    define('PHPWENOM_VERSION', '0.0.1');

    // Definiert den Namespace WENOM für die automatisch zu ladenden Klassen
    $mapNamespaces = [ 'wenom' => __DIR__ . '/app/', ];

    // Registriert die Autoload-Closure, welche hier als Parameter von spl_autoload_register übergeben wird.
    // $mapNamespaces wird dabei innerhalb der Closure zur Verfügung gestellt.
    spl_autoload_register(function(string $classname) use ($mapNamespaces) {

        // Trenne den Klassennamen anhand des Backspace-Zeichens in seine Teile auf
        $parts = explode('\\', $classname);

        // Der erste Teil ist der Namespace. Der Pfad muss in der Map bekannt sein, da sonst der Autoload nicht funktionieren kann.
        $namespace = array_shift($parts);
        if (!array_key_exists($namespace, $mapNamespaces)) {
            return;
        }

        // Der letzte Teil beinhaltet den Klassennamen, die anderen Teile die einzelnen Verzeichnisse. Bestimme daraus den Dateinamen.
        $classfile = array_pop($parts).'.php';
        $path = implode(DIRECTORY_SEPARATOR, $parts);
        $file = $mapNamespaces[$namespace].$path.DIRECTORY_SEPARATOR.$classfile;

        // Wenn die Datei nicht existiert und ein andere autoload-Funktion über class_exists die Klasse finden kann, dann kann sie nicht geladen werden
        if (!file_exists($file) && !class_exists($classname)) {
            return;
        }

        // Führe den autoload mit require_once aus.
        require_once $file;
    });
}
