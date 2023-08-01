---
group: 'design-system'
title: 'Installation'
id: 'install'
iconColor: '#ccc'
icon: 'carbon:bookmark'
---

# Vue3 UI-Komponenten für das SVWS-NRW Projekt


Um die UI-Komponenten in ein vue3-Projekt einzubinden:

```shell
npm i @svws-nrw/svws-ui
```

Komponenten können anschließend einzeln importiert werden:

```vue
<script setup lang="ts">
  import { SvwsUiAvatar } from '@svws-nrw/svws-ui'
</script>

<template>
  <SvwsUiAvatar/>
</template>
```

Stories werden mit Hilfe von Histoire erstellt:

```shell
npm run story:dev
```

```shell
npm run story:build
```
