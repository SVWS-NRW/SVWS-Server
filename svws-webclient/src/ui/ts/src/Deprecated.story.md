---
group: 'design-system'
title: 'Deprecated'
iconColor: '#ccc'
id: 'deprecated'
icon: 'carbon:bookmark'
---

# Veraltete Komponenten

Hier werden Komponenten aufgelistet, die nicht mehr verwendet werden und entfernt wurden.

## ab 0.7.1-SNAPSHOT
### **Checkbox**

`circle`, `span` und `headless` wurden entfernt.

### **Toggle**

Verfügbar als `type="toggle"` in der [Checkbox](/story/svws-ui-checkbox) Komponente.

## ab 0.6.18-SNAPSHOT
### **Drag/Drop**

Drag und Drop wird direkt in den Komponenten mit JS-Boardmitteln umgesetzt. Ist sauberer und schneller.

  * `<svws-ui-drag/>`
  * `<svws-ui-drop/>`

## ab 0.6.12-SNAPSHOT

### **Icon**

Icons können in den meisten Fällen direkt mit `<i-ri-icon-name/>` eingesetzt werden.
Dazu wird [unplugin-icons](https://github.com/antfu/unplugin-icons) verwendet, eine Bibliothek, die bei Bedarf Icons aus verschiedenen Projekten nachlädt.

Falls benötigt kann ein Icon mit einem `<span class="icon">…</span>` umschlossen werden.

  * `<svws-ui-icon/>` entfernt

### **Dropdown**

Empfohlene Alternative: [Multiselect](/story/src-components-svwsuimultiselect-story-vue)

  * `<svws-ui-dropdown/>` entfernt
  * `<svws-ui-dropdownItem/>` entfernt
  * `<svws-ui-dropdownWithAction/>` entfernt


### **Select Input**

Empfohlene Alternative: [Multiselect](/story/src-components-svwsuimultiselect-story-vue)

  * `<svws-ui-select-input/>` entfernt


### **Tab Bar**

Ersetzt durch [Router Tab Bar](/story/src-components-app-svwsuiroutertabbar-story-vue)

  * `<svws-ui-tab-bar/>` entfernt
  * `<svws-ui-tab-button/>` entfernt
  * `<svws-ui-tab-panel/>` entfernt

