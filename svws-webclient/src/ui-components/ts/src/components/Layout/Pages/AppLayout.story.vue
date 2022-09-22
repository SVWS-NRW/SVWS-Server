<script setup lang="ts">
import { ref } from "vue";
import { hstEvent } from 'histoire/client';

import SvwsUiAppLayout from "./AppLayout.vue";
import SvwsUiSecondaryMenu from "../Sidebar/SecondaryMenu.vue";
import SvwsUiSidebarMenu from "../Sidebar/Menu.vue";
import SvwsUiSidebarMenuHeader from "../Sidebar/MenuHeader.vue";
import SvwsUiSidebarMenuItem from "../Sidebar/MenuItem.vue";

const isCollapsed = ref(false);

function onToggle(value: boolean) {
  isCollapsed.value = value;

  hstEvent("toggle", value);
}
</script>

<template>
  <Story title="SVWS UI/Layout/Pages/App">
    <svws-ui-app-layout :collapsed="isCollapsed">
      <template #sidebar>

        <svws-ui-sidebar-menu :collapsed="isCollapsed" @toggle="onToggle">
          <template #header>
            <svws-ui-sidebar-menu-header :collapsed="isCollapsed">SVWS-NRW</svws-ui-sidebar-menu-header>
          </template>
          <template #default>
            <svws-ui-sidebar-menu-item icon="team" active :collapsed="isCollapsed">Schüler
            </svws-ui-sidebar-menu-item>
            <svws-ui-sidebar-menu-item icon="building" :collapsed="isCollapsed">Schule</svws-ui-sidebar-menu-item>
            <svws-ui-sidebar-menu-item icon="book-open" :collapsed="isCollapsed">Kataloge</svws-ui-sidebar-menu-item>
            <svws-ui-sidebar-menu-item icon="donut-chart" :collapsed="isCollapsed">Extras</svws-ui-sidebar-menu-item>
          </template>
          <template #footer>
            <svws-ui-sidebar-menu-item icon="settings-2" :collapsed="isCollapsed">Einstellungen
            </svws-ui-sidebar-menu-item>
            <svws-ui-sidebar-menu-item icon="logout-box" subline="Schule XYZ" :collapsed="isCollapsed">Administrator
            </svws-ui-sidebar-menu-item>
          </template>
        </svws-ui-sidebar-menu>

      </template>

      <template #secondaryMenu>

        <svws-ui-secondary-menu>
          <template #header>Schülerverwaltung</template>
          <template #content>
            <div>
              <p>
                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut
                labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et
                ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum
                dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
                magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet
                clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
              </p>
              <p>
                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut
                labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et
                ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum
                dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
                magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet
                clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
              </p>
              <p>
                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut
                labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et
                ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum
                dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
                magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet
                clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
              </p>
              <p>
                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut
                labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et
                ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum
                dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
                magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet
                clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
              </p>
              <p>
                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut
                labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et
                ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum
                dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore
                magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet
                clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.
              </p>
            </div>
          </template>
        </svws-ui-secondary-menu>

      </template>

      <template #main>
      </template>
    </svws-ui-app-layout>
  </Story>
</template>