<script setup lang="ts">
import { hstEvent } from 'histoire/client';

import SvwsUiTextareaInput from "../../components/Controls/TextareaInput.vue";

function onInput(event: Event) {
  hstEvent('input', event);
}
</script>

<template>
  <Story title="SVWS UI/Controls/Textarea Input">
    <Variant title="Empty">
      <svws-ui-textarea-input value="" valid placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
    </Variant>

    <Variant title="Filled">
      <svws-ui-textarea-input value="Textarea Input Value" valid placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
    </Variant>

    <Variant title="Invalid">
      <svws-ui-textarea-input value="" :valid="false" placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
    </Variant>

    <Variant title="Disabled">
      <svws-ui-textarea-input value="" valid disabled placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
    </Variant>

    <Variant title="Statistics">
      <svws-ui-textarea-input value="" valid statistics placeholder="Textarea Input Placeholder" resizeable="both" @input="onInput" />
    </Variant>
  </Story>
</template>