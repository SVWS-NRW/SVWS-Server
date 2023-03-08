<script setup lang="ts">
	import { hstEvent } from 'histoire/client';
	import { ref } from 'vue';

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
			<svws-ui-checkbox v-model="modelValue1" :value="true">Checkbox</svws-ui-checkbox>
			<br>
			<div :class="{'text-primary': modelValue1b}">
				<svws-ui-checkbox v-model="modelValue1b" :value="true">Example: Blue highlight in table</svws-ui-checkbox>
			</div>
		</Variant>

		<Variant title="Unchecked">
			<svws-ui-checkbox v-model="modelValue2" :value="false">Checkbox</svws-ui-checkbox>
		</Variant>

		<Variant title="Without Label">
			<svws-ui-checkbox v-model="modelValue2" :value="true" />
		</Variant>

		<Variant title="Circle Style">
			<svws-ui-checkbox v-model="modelValueC" :value="true" circle>Same function, different appearance!</svws-ui-checkbox>
		</Variant>

		<Variant title="Statistics">
			<svws-ui-checkbox v-model="modelValue3" statistics :value="false">Checkbox</svws-ui-checkbox>
		</Variant>

		<Variant title="Disabled">
			<svws-ui-checkbox v-model="modelValue3" disabled :value="false">Checkbox</svws-ui-checkbox>
		</Variant>

		<Variant title="Headless">
			<svws-ui-checkbox v-model="modelValue3" headless :value="false">Checkbox</svws-ui-checkbox>
		</Variant>

		<Variant title="Multi selection">
			<div class="checkbox--wrapper">
				<svws-ui-checkbox v-model="modelValue4" value="1">
					Checkbox 1
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="modelValue4" value="2">
					Checkbox 2 with long Title
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="modelValue4" value="3">
					Checkbox 3
				</svws-ui-checkbox>
				<svws-ui-checkbox v-model="modelValue4" value="4">
					Checkbox 4
				</svws-ui-checkbox>
			</div>
			<br>
			Result: {{ modelValue4 }}
		</Variant>
	</Story>
</template>
