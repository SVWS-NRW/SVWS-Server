<script setup lang="ts">
import SvwsUiContentCard from "./ContentCard.vue";
import SvwsUiTextInput from "../Controls/TextInput.vue";
import SvwsUiButton from "../Controls/Button.vue";
</script>

<template>
  <Story title="SVWS UI/Layout/Content Card">
    <svws-ui-content-card title="Basisdaten">
        <template #actions>
            <svws-ui-button>Test</svws-ui-button>
            <svws-ui-button>Test</svws-ui-button>
        </template>
        <div class="flex flex-row items-start space-x-4">
          <div class="flex-shrink-0">
            <img src="https://via.placeholder.com/256" class="w-32 h-32 rounded-full md:h-20 md:w-20 lg:h-24 lg:w-24">
          </div>
          <div class="flex-grow grid gap-4 grid-cols-1 md:grid-cols-2">
            <svws-ui-text-input type="text" placeholder="Vorname" />
            <svws-ui-text-input type="text" placeholder="Alle Vornamen" />
            <svws-ui-text-input type="text" placeholder="Nachname" />
            <svws-ui-text-input type="text" placeholder="Zusatz" />
            <svws-ui-text-input type="text" placeholder="Adresse" />
            <svws-ui-text-input type="text" placeholder="Wohnort" />
          </div>
        </div>
      </svws-ui-content-card>
  </Story>
</template>