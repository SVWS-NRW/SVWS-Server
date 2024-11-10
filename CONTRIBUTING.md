## Betrifft Gitlab Instanz
Die Branches `master` und `dev` sind für `git push` gesperrt.
Neue Commits bitte über einen Merge Request in das Repository laden mit der folgenden Regel zur Namensgebung: `benutzername/feature`, also z.B. `hmt/laufbahnplanung_bugfixes`. Es können nur Merge Requests in den `dev`-Branch geholt werden, die ein Rebase mitbringen. Manche Branches können direkt in Gitlab mit einem Rebase versorgt werden, treten Konflikte auf, bitte lokal ein Rebase durchführen und erneut hochladen. Damit soll verhindert werden, dass der `dev`-Branch mit zusätzlichen Merge-Commits überflutet wird. Weniger Merge-Commits machen es deutlich einfacher, den Verlauf nachzuverfolgen und ggf. Fehler zu finden.

In der Regel werden Merge-Requests per Squash in den `dev`-Branch geholt. Wenn das nicht der Fall sein soll, z.B. weil mehrere daran gearbeitet haben oder die Commits erhalten bleiben sollen, dann bitte selber die entsprechenden Squashes vornehmen oder den Branch entsprechend für ein Merge vorbereiten.

Sieh dir bitte auch die Coding-Vorgaben und die Code-Styles an:

https://doku.svws-nrw.de/admin/Entwicklungsumgebungen/Coding-Guidlines/

https://doku.svws-nrw.de/admin/Entwicklungsumgebungen/Code-Styles/
