<template>
	<Story title="Checkbox" id="svws-ui-checkbox" icon="ri:checkbox-line" :layout="{ type: 'single', iframe: false }" auto-props-disabled responsive-disabled>
		<Variant title="Default" id="Default" :source>
			<div class="p-4">
				<div class="flex flex-col items-start gap-2 mt-10">
					<input type="checkbox" checked>
					<input type="checkbox" checked class="svws-statistik">
					<input type="checkbox" checked color="error">
					<input type="checkbox" checked color="success">
					<input type="checkbox" checked color="warning">
					<input type="checkbox" checked class="svws-bw">
				</div>

				<div class="flex flex-col items-start gap-2 mt-10">
					<svws-ui-checkbox v-model="modelValue1" :color="state.color" :bw="state.bw" :statistics="state.statistics" :disabled="state.disabled" :headless="state.headless" :type="state.toggle ? 'toggle' : 'checkbox'">{{ state.label }}</svws-ui-checkbox>
					<svws-ui-checkbox v-model="modelValue1" color="error">Error</svws-ui-checkbox>
					<svws-ui-checkbox v-model="modelValue1" color="success">Success</svws-ui-checkbox>
					<svws-ui-checkbox v-model="modelValue1" color="warning">Warning</svws-ui-checkbox>
				</div>

				<div class="flex flex-col items-start gap-2 mt-10">
					<div :class="{'text-ui-brand': modelValue2}">
						<svws-ui-checkbox v-model="modelValue2">Beispiel mit Label Farbe wenn aktiv</svws-ui-checkbox>
					</div>
					<svws-ui-checkbox v-model="modelValue1" headless>Headless</svws-ui-checkbox>
					<div class="pointer-events-none leading-none">
						<svws-ui-checkbox v-model="modelValue" @update:model-value="v=>modelValue6=v" :indeterminate="true">Indeterminate Status</svws-ui-checkbox>
					</div>
					<svws-ui-checkbox v-model="modelValue3" bw>BW (black and white)</svws-ui-checkbox>
					<svws-ui-checkbox v-model="modelValue4" statistics>Wichtige Checkbox für die Statistik</svws-ui-checkbox>
					<svws-ui-checkbox v-model="modelValue5" disabled>Deaktivierte Checkbox</svws-ui-checkbox>
					<svws-ui-checkbox v-model="modelValue7" class="svws-loading">Value wird gesetzt…</svws-ui-checkbox>
				</div>

				<div class="flex flex-col items-start gap-2 mt-10">
					<svws-ui-checkbox v-model="modelValue2" type="toggle">Toggle</svws-ui-checkbox>
					<div class="pointer-events-none leading-none">
						<svws-ui-checkbox v-model="modelValue" :indeterminate="true" @update:model-value="v=>modelValue6=v" type="toggle">Indeterminate Status</svws-ui-checkbox>
					</div>
					<svws-ui-checkbox v-model="modelValue3" bw type="toggle">BW (black and white)</svws-ui-checkbox>
					<svws-ui-checkbox v-model="modelValue4" statistics type="toggle">Wichtige Checkbox für die Statistik</svws-ui-checkbox>
					<svws-ui-checkbox v-model="modelValue5" disabled type="toggle">Deaktivierte Checkbox</svws-ui-checkbox>
					<svws-ui-checkbox v-model="modelValue7" class="svws-loading" type="toggle">Value wird gesetzt…</svws-ui-checkbox>
				</div>
			</div>

			<template #controls>
				<HstText v-model="state.label" title="Checkbox Label" />
				<HstCheckbox v-model="state.toggle" title="Type: Toggle" />
				<HstText v-model="state.title" title="title" />
				<HstText v-model="state.color" title="color" />
				<HstCheckbox v-model="state.bw" title="bw" />
				<HstCheckbox v-model="state.statistics" title="statistics" />
				<HstCheckbox v-model="state.disabled" title="disabled" />
				<HstCheckbox v-model="state.headless" title="headless" />
			</template>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { ref, reactive } from 'vue';

	const state = reactive({
		value: false,
		label: 'Checkbox Label',
		title: '',
		bw: false,
		statistics: false,
		disabled: false,
		toggle: false,
		headless: false,
		color: undefined,
	})

	const modelValue = ref(true);
	const modelValue1 = ref(true);
	const modelValue2 = ref(true);
	const modelValue3 = ref(true);
	const modelValue4 = ref(false);
	const modelValue5 = ref(false);
	const modelValue6 = ref();
	const modelValue7 = ref(true);

	const source = `<svws-ui-checkbox v-model="..." [title | bw | statistics | disabled | type="toggle" | headless]>
	Label
</svws-ui-checkbox> `;
</script>
