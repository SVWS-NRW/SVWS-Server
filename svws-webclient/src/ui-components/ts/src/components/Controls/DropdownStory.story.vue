<script setup lang="ts">
import { hstEvent } from 'histoire/client';

import SvwsUiDropdown from "./Dropdown.vue";
import SvwsUiDropdownItem from "./DropdownItem.vue";

function onClick(event: Event) {
  hstEvent('click', event);
}
</script>

<template>
  <Story title="SVWS UI/Controls/Dropdown">
    <Variant title="Primary">
      <svws-ui-dropdown variant="primary" @click="onClick">
        <template #dropdownButton>More options</template>
        <template #dropdownItems>
          <svws-ui-dropdown-item icon="home" @click="onClick">Option 1</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 2</svws-ui-dropdown-item>
          <svws-ui-dropdown-item :disabled="true" @click="onClick">Option 3</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 4</svws-ui-dropdown-item>
        </template>
      </svws-ui-dropdown>
    </Variant>

    <Variant title="Secondary">
      <svws-ui-dropdown variant="secondary" @click="onClick">
        <template #dropdownButton>More options</template>
        <template #dropdownItems>
          <svws-ui-dropdown-item icon="home" @click="onClick">Option 1</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 2</svws-ui-dropdown-item>
          <svws-ui-dropdown-item :disabled="true" @click="onClick">Option 3</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 4</svws-ui-dropdown-item>
        </template>
      </svws-ui-dropdown>
    </Variant>

    <Variant title="Danger">
      <svws-ui-dropdown variant="danger" @click="onClick">
        <template #dropdownButton>More options</template>
        <template #dropdownItems>
          <svws-ui-dropdown-item icon="home" @click="onClick">Option 1</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 2</svws-ui-dropdown-item>
          <svws-ui-dropdown-item :disabled="true" @click="onClick">Option 3</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 4</svws-ui-dropdown-item>
        </template>
      </svws-ui-dropdown>
    </Variant>

    <Variant title="Disabled">
      <svws-ui-dropdown variant="Primary" disabled @click="onClick">
        <template #dropdownButton>More options</template>
        <template #dropdownItems>
          <svws-ui-dropdown-item icon="home" @click="onClick">Option 1</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 2</svws-ui-dropdown-item>
          <svws-ui-dropdown-item :disabled="true" @click="onClick">Option 3</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 4</svws-ui-dropdown-item>
        </template>
      </svws-ui-dropdown>
    </Variant>

    <Variant title="Text/Icon only">
      <svws-ui-dropdown variant="icon" @click="onClick">
        <template #dropdownButton>More options</template>
        <template #dropdownItems>
          <svws-ui-dropdown-item icon="home" @click="onClick">Option 1</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 2</svws-ui-dropdown-item>
          <svws-ui-dropdown-item :disabled="true" @click="onClick">Option 3</svws-ui-dropdown-item>
          <svws-ui-dropdown-item @click="onClick">Option 4</svws-ui-dropdown-item>
        </template>
      </svws-ui-dropdown>
    </Variant>
  </Story>
</template>
