<script setup lang="ts">
import { hstEvent } from 'histoire/client';

import SvwsUiButton from "./Button.vue";

const propsVariants = {
  Primary: { type: "primary" },
  Secondary: { type: "secondary" },
  Danger: { type: "danger" },
  Transparent: { type: "transparent" },
  'Dropdown Action': { type: "primary", dropdownAction: true },
  Disabled: { type: "primary", disabled: true },
  Normal: { type: "primary", size: 'normal' },
  Small: { type: "primary", size: 'small' },
};

function onClick(event: Event) {
  hstEvent('Click', event);
}
</script>

<template>
  <Story title="SVWS UI/Controls/Button">
    <Variant
      v-for="(props, title) of propsVariants"
      :key="title"
      :title="title"
    >
      <SvwsUiButton v-bind="props" @click="onClick">Button</SvwsUiButton>
    </Variant>
  </Story>
</template>