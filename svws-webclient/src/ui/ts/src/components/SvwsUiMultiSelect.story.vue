<script setup lang="ts">
	import { logEvent } from "histoire/client";
	import { ref, reactive } from "vue";

	const items = reactive([
		{ id: 1, text: "Item 1" },
		{ id: 2, text: "Item 2 with a very, very, very, really long title that's way too big for the row. If this happens, the UI should be prepared" },
		{ id: 3, text: "Item 3" },
		{ id: 4, text: "Item 4" },
		{ id: 5, text: "Item 5" },
		{ id: 6, text: "Item 6" }
	]);

	const modelValue1 = ref(items[0]);
	const modelValue2 = ref();
	const modelValue3 = ref();
	const modelValue4 = ref();
	const modelValue5 = ref([items[0]]);
	const modelValue6 = ref();
	const modelValue7 = ref();
	const modelValue8 = ref([items[0]]);

	function onInput(event: Event) {
		logEvent("input", event);
	}
</script>

<template>
	<Story title="Multiselect" :layout="{type: 'grid', width: '45%'}" icon="ri:expand-up-down-fill">
		<Variant title="Default">
			<div class="demo-wrapper flex flex-col gap-3">
				<svws-ui-multi-select v-model="modelValue1"
					title="Simple"
					:item-text="item => item?.text || ''"
					:items="items"
					@input="onInput"
					:removable="true" />
				<pre>v-model value: {{ modelValue1 }}</pre>
			</div>
		</Variant>

		<Variant title="Simple Sort">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue2"
					title="Simple"
					:items="items"
					:item-text="item => item?.text || ''"
					:item-sort="(a: any, b: any) => a.text + b.text"
					@input="onInput" />
			</div>
		</Variant>

		<Variant title="Autocomplete">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue4"
					autocomplete
					title="Autocomplete"
					:items="items"
					:item-text="item => item?.text || ''"
					:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
					@input="onInput" />
			</div>
		</Variant>

		<Variant title="Tags">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue5"
					tags
					title="Tags"
					:item-text="item => item?.text || ''"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>

		<Variant title="Tags with Autocomplete">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue6"
					:item-text="item => item?.text || ''"
					tags
					autocomplete
					title="Tags with Autocomplete"
					placeholder="Filters"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>

		<Variant title="Headless">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue7"
					:item-text="item => item?.text || ''"
					headless
					title="Headless"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>

		<Variant title="Tags Headless">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue8"
					:item-text="item => item?.text || ''"
					tags
					autocomplete
					headless
					title="Tags Headless"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>
		<Variant title="Statistics">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue8"
					:item-text="item => item?.text || ''"
					tags
					autocomplete
					statistics
					title="Statistics"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>
	</Story>
</template>

<style>
.demo-wrapper {
	@apply py-4;
}
</style>
