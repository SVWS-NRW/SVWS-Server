<template>
	<Story title="Select" id="svws-ui-select" icon="ri:expand-up-down-fill" auto-props-disabled>
		<Variant title="Default">
			<svws-ui-content-card class="p-5">
				<svws-ui-input-wrapper :grid="2">
					<svws-ui-select v-model="modelValue"
						:label="state.label"
						:item-text="item => item.text"
						:item-sort="(a: any, b: any) => a.text + b.text"
						:items="items"
						@input="onInput"
						:autocomplete="state.autocomplete"
						:removable="state.removable"
						:headless="state.headless"
						:statistics="state.statistics"
						:disabled="state.disabled"
						class="col-span-full" />
					<svws-ui-spacing :size="2" />
					<svws-ui-select label="Relevant für die Statistik"
						:items="items"
						:item-text="item => item.text"
						v-model="modelValueStatistik"
						statistics />
					<svws-ui-select label="Disabled"
						:items="items"
						:item-text="item => item.text"
						v-model="modelValueDisabled" disabled />
					<svws-ui-spacing />
					<svws-ui-select label="Headless z. B. in der Tabelle"
						:items="items"
						:item-text="item => item.text"
						v-model="modelValueHeadlessUndefined" headless />
					<svws-ui-select label="Headless z. B. in der Tabelle"
						:items="items"
						:item-text="item => item.text"
						v-model="modelValueHeadless" headless removable />
				</svws-ui-input-wrapper>
			</svws-ui-content-card>
			<template #source>
				{{ `
<svws-ui-select label="Label"
                :items="items"
                :item-text="item => item.text"
                v-model="undefined" />
        ` }}
			</template>
			<template #controls>
				<HstText v-model="state.label" title="label" />
				<HstCheckbox v-model="state.autocomplete" title="autocomplete" />
				<HstCheckbox v-model="state.removable" title="removable" />
				<HstCheckbox v-model="state.headless" title="headless" />
				<HstCheckbox v-model="state.statistics" title="statistics" />
				<HstCheckbox v-model="state.disabled" title="disabled" />
			</template>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { logEvent } from "histoire/client";
	import { ref, reactive } from "vue";
	import SvwsUiContentCard from "./App/SvwsUiContentCard.vue";

	const state = reactive({
		label: 'Label für die Komponente',
		removable: true,
		headless: false,
		statistics: false,
		disabled: false,
		autocomplete: false
	});

	const items = reactive<{id: number, text: string}[]>([
		{ id: 1, text: "Item 1" },
		{ id: 2, text: "Item 2 hat einen sehr langen Titel, der je nach verfügbarer Breite gekürzt wird. Im Dropdown ist immer der gesamte Text lesbar." },
		{ id: 3, text: "Item 3" },
		{ id: 4, text: "Item 4" },
		{ id: 5, text: "Item 5" },
		{ id: 6, text: "Item 6" }
	]);

	const modelValue = ref(undefined);
	const modelValueStatistik = ref(undefined);
	const modelValueDisabled = ref(items[3]);
	const modelValueHeadless = ref(items[0]);
	const modelValueHeadlessUndefined = ref(undefined);
	const modelValue5 = ref([items[0]]);

	function onInput(event: Event) {
		logEvent("input", event);
	}
</script>

