<script setup lang="ts">
import { ref } from 'vue';
import { hstEvent } from 'histoire/client';

import SvwsUiModal from "./Modal.vue";
import SvwsUiRadioGroup from "../../Controls/RadioGroup.vue";
import SvwsUiRadioOption from "../../Controls/RadioOption.vue";
import SvwsUiButton from "../../Controls/Button.vue";

const modal = ref(null);

function onClick(event: Event) {
  hstEvent('click', event);
}
</script>

<template>
  <Story title="SVWS UI/Layout/Modal">
    <svws-ui-button @click="modal.openModal()">Open Modal</svws-ui-button>

    <svws-ui-modal ref="modal" size="small">
      <template #modalTitle>Schüler hinzufügen</template>
      <template #modalDescription>Welches Schuljahr soll bei den neu aufzunehmenden Schülern gesetzt
        werden?</template>
      <template #modalContent>
        <svws-ui-radio-group>
          <svws-ui-radio-option
value="option3" name="radioInputExample" label="Vergangenes Schuljahr 2019/2020"
            disabled />
          <svws-ui-radio-option value="option1" name="radioInputExample" label="Aktuelles Schuljahr 2020/21" />
          <svws-ui-radio-option value="option2" name="radioInputExample" label="Kommendes Schuljahr 2021/22" checked />
        </svws-ui-radio-group>
      </template>

      <template #modalActions>
        <svws-ui-button type="secondary" @click="modal.closeModal()">Abbrechen</svws-ui-button>
        <svws-ui-button @click="onClick">Weiter</svws-ui-button>
      </template>
    </svws-ui-modal>
  </Story>
</template>