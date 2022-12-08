In diesen Ordner können Shell-Scripts abgelegt werden, die vor dem Start des SVWSs im Container ausgeführt werden, z.B. Testdatenimporte etc.

Zur Aktivierung dieser Scripts muss dieser Ordner als Volume-Mount in den Container eingebunden werden (Beispiel siehe docker-compose.yml).

WICHTIG: Zum Ausführen des Beispielscripts 'import-test-db.sh' muss noch eine Access-Datenbank (z.B. GymAbi.mdb) in dieses Verzeichnis kopiert werden.