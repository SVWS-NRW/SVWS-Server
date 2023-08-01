---
group: 'top'
title: 'README'
id: 'readme'
icon: 'ri:code-s-slash-line'
---

# SVWS UI Guide

In diesem Guide werden die wichtigsten Informationen und Best Practices für die Entwicklung von UI-Komponenten für den
SVWS Client zusammengefasst.

* [Farben](/story/farben)
* [Performance](#performance)
  * [Komponente oder nur ein `div`?](#komponente-oder-nur-ein-div)
  * [Optimierungen](#optimierungen)
	* [Verwendung von `v-once`](#verwendung-von-v-once)
	* [Verwendung von `v-if` und `v-show`](#verwendung-von-v-if-und-v-show)
* [CSS Classes](#css-classes)
  * [`.svws-ui-component`](#svws-ui-component)
  * [`.svws-attribute`](#svws-attribute)
  * [Verwendung beider Kategorien](#verwendung-beider-kategorien)

## Performance

Für den Client werden viele und teils komplexe Komponenten verwendet, die oft mehrfach gerendert werden. Um den
Speicherverbrauch und die Ladezeiten zu optimieren, können schon einfache Änderungen große Auswirkungen haben.

### Komponente oder nur ein `div`?

Eine Komponente kostet mehr Performance als einfache DOM-Nodes. Besonders verschachtelte Komponenten können schnell zu
einem Problem werden. Daher sollte immer überlegt werden, ob eine Komponente wirklich notwendig ist.

Beispiel bei einer Tabelle: Zur Übersichtlichkeit und einfachen Verwendung an anderen Stellen außerhalb der
Table-Komponente wird darin eine Zeile mit einer Row-Komponente gerendert. Jede Row hat mehrere Cells und davon braucht
eine Cell eine Checkbox-Komponente.
Wenn es jetzt 100 Zeilen gibt, werden in diesem Beispiel 100 Row-Komponenten gerendert, jede mit 5 Cells und einer
Checkbox-Komponente. Das sind 700 Komponenten, die gerendert werden müssen.

Wenn die Komponenten durch einfaches HTML Markup ersetzt werden, müssen nur noch 100 Komponenten gerendert werden. Das
ist ein großer Unterschied.

Damit trotzdem eine einheitliche und einfache Verwendung möglich ist, sollen alle Komponenten hier dokumentiert werden.

_Bitte beachte dazu die [Guidelines zu CSS Classes](#css-classes)._

### Optimierungen

#### Verwendung von `v-once`

Damit wird das Element und alle darin enthaltenen Elemente nur einmal gerendert. Das ist besonders nützlich für
Elemente, die sich nicht ändern, wie z.B. Überschriften.

_Weitere Informationen dazu: [Vue.js Guide](https://vuejs.org/guide/best-practices/performance.html#v-once)_

#### Verwendung von `v-if` und `v-show`

`v-if` ist performanter und sollte in den meisten Fällen verwendet werden, da `v-show` immer gerendert wird, aber nur
mit CSS versteckt wird. `v-if` wird nur gerendert, wenn die Bedingung erfüllt ist.

`v-show` benötigt beim initialen Render mehr Speicher, da die Komponente nur mit CSS versteckt wird. Dafür wird die
Komponente nicht neu gerendert, wenn die Bedingung erfüllt ist.

## CSS Classes

Um die [Performance](#komponente-oder-nur-ein-div) zu verbessern, reduzieren wir die Anzahl der Komponenten und
verwenden stattdessen einfache HTML Elemente mit einfachen und eindeutigen CSS Classes. Aber auch in den Komponenten
selbst sollten die CSS Classes einheitlich sein.

Alle wiederverwendbare CSS Classes — auch, wenn sie voraussichtlich erstmal nicht an anderen Stellen verwendet werden —
werden hier dokumentiert.

Für bessere Übersichtlichkeit im Code und einfacher Verwendung werden die CSS Classes in zwei Kategorien unterteilt:

* **.svws-ui-component**: eindeutige, übergeordnete Class Names, damit "attributes" Styles nicht mit anderen Komponenten
  kollidieren.
* **.svws-attribute**: Styles für ein Attribut, das mehrfach in verschiedenen Kontexten verwendet werden kann.

### .svws-ui-component

Prefix mit `svws-ui-`

| Schema                                    | Verwendung                                                                                                             |
|-------------------------------------------|------------------------------------------------------------------------------------------------------------------------|
| `.svws-ui-name`                           | Class für eine Komponente                                                                                              |
| `.svws-ui-name--element`                  | Eindeutige Class zur Differenzierung untergeordneter Elemente in einer Komponente. Beispiel `--header` und `--content` |
| `.svws-ui-name--element--subelement`      | Weitere Ebene. Beispiel `--header--title` und `--header--subtitle`                                                     |
| `.svws-ui-name--element--subelement--...` | Wenn möglich vermeiden. Verschachtelungen sollten nicht zu komplex werden.                                             |

Eine einzelne, eindeutige Komponenten-Class kann auch für untergeordnete Elemente einer Komponente verwendet werden, um
komplex verschachtelte Namen zu vermeiden.

Beispiel: ~~`.svws-ui-table--thead--tr--th`~~ &rarr; einzelne Classes:

* `.svws-ui-table`
* `.svws-ui-thead`
* `.svws-ui-tbody`
* `.svws-ui-tfoot`
* `.svws-ui-tr`
* `.svws-ui-td`
* `.svws-ui-th`

```scss
.svws-ui-tr {
  @apply h-6;

  .svws-ui-thead & {
    @apply h-8;
  }
}
```

### .svws-attribute

Prefix mit `svws-`

Beispiel: `.svws-active`, `.svws-disabled`

### Verwendung beider Kategorien

```vue
<!-- SvwsUiTable.vue -->
<style lang="postcss">
.svws-ui-thead {
  @apply text-sm;

  &.svws-active {
    @apply bg-svws;
  }
}

.svws-ui-data-table {
  .svws-ui-thead {
    @apply font-bold;
  }
}
</style>
```

```vue
<!-- SvwsUiComponentA.vue -->
<style lang="postcss">
.svws-ui-component-a {
  .svws-ui-thead {
    @apply text-svws;
  }

  .svws-ui-button {
    &.svws-active {
      @apply bg-error;
    }
  }
}
</style>
```

```vue
<!-- SvwsUiComponentB.vue -->
<style lang="postcss">
.svws-ui-component-b {
  .svws-ui-thead {
    @apply uppercase;
  }
}
</style>
```
