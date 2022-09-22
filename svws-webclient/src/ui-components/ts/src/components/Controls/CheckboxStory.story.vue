<script setup lang="ts">
import { hstEvent } from 'histoire/client';
import { ref } from 'vue';

import SvwsUiCheckbox from "./Checkbox.vue";

const modelValue1 = ref(true);
const modelValue2 = ref(false);
const modelValue3 = ref(false);
const modelValue4 = ref([]);
</script>

<template>
  <Story title="SVWS UI/Controls/Checkbox">
    <Variant title="Checked">
      <SvwsUiCheckbox v-model="modelValue1" :value="true">Checkbox</SvwsUiCheckbox>
    </Variant>

    <Variant title="Unchecked">
      <SvwsUiCheckbox :value="false" :model-value="modelValue2" @input="hstEvent('Update', $event)">
        Checkbox
      </SvwsUiCheckbox>
    </Variant>

    <Variant title="Statistics">
      <SvwsUiCheckbox :value="false" statistics :model-value="modelValue3" @input="hstEvent('Update', $event)">
        Checkbox
      </SvwsUiCheckbox>
    </Variant>

    <Variant title="Disabled">
      <SvwsUiCheckbox :value="false" disabled :model-value="modelValue3" @input="hstEvent('Update', $event)">
        Checkbox
      </SvwsUiCheckbox>
    </Variant>

    <Variant title="Multi selection">
      <SvwsUiCheckbox v-model="modelValue4" value="1">
        Checkbox 1
      </SvwsUiCheckbox>
      <SvwsUiCheckbox v-model="modelValue4" value="2">
        Checkbox 2
      </SvwsUiCheckbox>
      <SvwsUiCheckbox v-model="modelValue4" value="3">
        Checkbox 3
      </SvwsUiCheckbox>
      <SvwsUiCheckbox v-model="modelValue4" value="4">
        Checkbox 4
      </SvwsUiCheckbox>
      <br/>
      Result: {{modelValue4}}
    </Variant>
  </Story>
</template>