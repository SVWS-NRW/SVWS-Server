<script setup lang="ts">
import { hstEvent } from 'histoire/client';
import { ref } from 'vue';

import SvwsUiCheckbox from "./Checkbox.vue";

const modelValue1 = ref(true);
const modelValue1b = ref(true);
const modelValue2 = ref(false);
const modelValue3 = ref(false);
const modelValueC = ref(true);
const modelValue4 = ref([]);
</script>

<template>
  <Story title="SVWS UI/Controls/Checkbox">
    <Variant title="Checked">
      <SvwsUiCheckbox v-model="modelValue1" :value="true">Checkbox</SvwsUiCheckbox>
		<br/>
		<div :class="{'text-primary': modelValue1b}">
			<SvwsUiCheckbox v-model="modelValue1b" :value="true">Example: Blue highlight in table</SvwsUiCheckbox>
		</div>
    </Variant>

    <Variant title="Unchecked">
		<SvwsUiCheckbox v-model="modelValue2" :value="false">Checkbox</SvwsUiCheckbox>
    </Variant>

    <Variant title="Without Label">
		<SvwsUiCheckbox v-model="modelValue2" :value="true"/>
    </Variant>

    <Variant title="Circle Style">
		<SvwsUiCheckbox v-model="modelValueC" :value="true" circle>Same function, different appearance!</SvwsUiCheckbox>
    </Variant>

    <Variant title="Statistics">
		<SvwsUiCheckbox v-model="modelValue3" statistics :value="false">Checkbox</SvwsUiCheckbox>
    </Variant>

    <Variant title="Disabled">
		<SvwsUiCheckbox v-model="modelValue3" disabled :value="false">Checkbox</SvwsUiCheckbox>
    </Variant>

    <Variant title="Multi selection">
		<div class="checkbox--wrapper">
		  <SvwsUiCheckbox v-model="modelValue4" value="1">
			Checkbox 1
		  </SvwsUiCheckbox>
		  <SvwsUiCheckbox v-model="modelValue4" value="2">
			Checkbox 2 with long Title
		  </SvwsUiCheckbox>
		  <SvwsUiCheckbox v-model="modelValue4" value="3">
			Checkbox 3
		  </SvwsUiCheckbox>
		  <SvwsUiCheckbox v-model="modelValue4" value="4">
			Checkbox 4
		  </SvwsUiCheckbox>
		</div>
      <br/>
      Result: {{modelValue4}}
    </Variant>
  </Story>
</template>
