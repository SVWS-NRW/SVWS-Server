<script setup lang="ts">
import { hstEvent } from 'histoire/client';

import SvwsUiSidebarMenuHeader from "./MenuHeader.vue";

function onClick(event: Event) {
  hstEvent("click", event);
}
</script>

<template>
  <Story title="SVWS UI/Layout/Sidebar/Menu Header">
    <svws-ui-sidebar-menu-header @click="onClick">Menu Header</svws-ui-sidebar-menu-header>
  </Story>
</template>