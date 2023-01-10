![alt text](https://user-images.githubusercontent.com/34127980/92908354-71004880-f426-11ea-99ca-9ce758d20c7b.png)

# Vue3 UI-Komponenten für das SVWS-NRW Projekt


Um die UI-Komponenten in ein vue3-Projekt einzubinden:

    npm i @svws-nrw/svws-ui

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

```
npm run story:dev

# oder

npm run story:build
````
