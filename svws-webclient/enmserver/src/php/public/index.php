<?php
/**
 * Diese Datei funktioniert als Fallback, wenn der Server ohne mod_rewrite
 * betrieben wird. In diesem Fall wird auf die API-Dateien der entsprechenden
 * Unterverzeichnisse weitergeleitet. Dabei kommt explizit eine Whiteliste zum Tragen,
 * um Angriffe durch directory traversal zu verhinden.
 */
require_once dirname(__DIR__, 1).'/autoload.php';

// REQUEST_URI parsen
$requestUri = $_SERVER['REQUEST_URI'] ?? '/';
$path = parse_url($requestUri, PHP_URL_PATH);

$scriptName = $_SERVER['SCRIPT_NAME'] ?? '/index.php';
$baseDir = str_replace('\\', '/', dirname($scriptName));
if ($baseDir !== '/') {
    $path = str_replace($baseDir, '', $path);
}

$path = preg_replace('/[^a-zA-Z0-9\/\-_]/', '', $path);
$path = ltrim($path, '/');

// Führe einen Whitelist-Check durch, um Directory Traversal zu vermeiden
$allowedDirs = ['api', 'api/secure', 'oauth'];
$pathParts = explode('/', $path);
$fileName = array_pop($pathParts);
$currentDir = implode('/', $pathParts);
$isValidStructure = in_array($currentDir, $allowedDirs) && !empty($fileName);

// Wenn der Pfad gültig ist, dann prüfe, ob die PHP-Datei dort vorhanden ist
if ($isValidStructure) {
    $targetFile = __DIR__.'/'.$currentDir.'/'.$fileName.'.php';
    $realProjectRoot = realpath(__DIR__);
    $realTargetFile = realpath($targetFile);

    // Wenn die Datei existiert und exakt im erlaubten Unterverzeichnis liegt, dann lade die PHP-Datei mit require_once
    if ($realTargetFile && strpos($realTargetFile, $realProjectRoot) === 0 && file_exists($realTargetFile)) {
        http_response_code(200);
        require_once $realTargetFile;
        exit;
    }
}

// Wurde nichts gefunden, so gib ein NOT_FOUND - 404 zurück
header('Content-Type: application/json; charset=utf-8', true, 404);
echo json_encode(['error' => 'Not found', 'path' => $path]);
exit;
