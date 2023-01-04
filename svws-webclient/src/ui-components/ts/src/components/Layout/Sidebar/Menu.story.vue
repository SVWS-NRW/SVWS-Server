<script setup lang="ts">
import { ref } from 'vue';
import { hstEvent } from 'histoire/client';

import SvwsUiSidebarMenu from "./Menu.vue";
import SvwsUiSidebarMenuHeader from "./MenuHeader.vue";
import SvwsUiSidebarMenuItem from "./MenuItem.vue";

const isCollapsed = ref(false);

function onToggle(event: Event) {
  isCollapsed.value = !isCollapsed.value;
  hstEvent("toggle", event);
}
</script>

<template>
  <Story title="SVWS UI/Layout/Sidebar/Menu">
    <svws-ui-sidebar-menu :collapsed="isCollapsed" @toggle="onToggle">
      <template #header>
        <svws-ui-sidebar-menu-header :collapsed="isCollapsed">SVWS-NRW</svws-ui-sidebar-menu-header>
      </template>
      <template #default>
        <svws-ui-sidebar-menu-item icon="team" :collapsed="isCollapsed" active>Schüler</svws-ui-sidebar-menu-item>
        <svws-ui-sidebar-menu-item icon="building" :collapsed="isCollapsed">Schule</svws-ui-sidebar-menu-item>
        <svws-ui-sidebar-menu-item icon="book-open" :collapsed="isCollapsed">Kataloge</svws-ui-sidebar-menu-item>
        <svws-ui-sidebar-menu-item icon="donut-chart" :collapsed="isCollapsed">Extras</svws-ui-sidebar-menu-item>
      </template>
      <template #footer>
        <svws-ui-sidebar-menu-item icon="settings-2" :collapsed="isCollapsed">Einstellungen</svws-ui-sidebar-menu-item>
        <svws-ui-sidebar-menu-item icon="logout-box" subline="Schule XYZ" :collapsed="isCollapsed">Administrator</svws-ui-sidebar-menu-item>
      </template>
    </svws-ui-sidebar-menu>
  </Story>
</template>