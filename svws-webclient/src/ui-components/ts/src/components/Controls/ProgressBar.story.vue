<script setup lang="ts">
import { hstEvent } from 'histoire/client';

import SvwsUiProgressBar from "./ProgressBar.vue";
</script>

<template>
  <Story title="SVWS UI/Controls/Progress Bar">
    <Variant title="Default">
      <svws-ui-progress-bar :progress="82" size="default">Progress Bar</svws-ui-progress-bar>
    </Variant>

    <Variant title="Small">
      <svws-ui-progress-bar :progress="41" size="small">Progress Bar</svws-ui-progress-bar>
    </Variant>
  </Story>
</template>