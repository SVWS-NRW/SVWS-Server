<script setup lang="ts">
	import { logEvent } from "histoire/client";
	import { ref, reactive } from "vue";

	const items = reactive([
		{ id: 1, text: "item1" },
		{ id: 2, text: "item2" },
		{ id: 3, text: "item3" },
		{ id: 3, text: "item4" },
		{ id: 3, text: "item5" },
		{ id: 3, text: "item6" }
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
	<Story title="SVWS UI/Controls/ComboBox">
		<Variant title="Simple">
			<div class="demo-wrapper">
				v-model value: {{ modelValue1 }}
				<svws-ui-multi-select v-model="modelValue1"
					title="Simple"
					:item-text="item => item?.text || ''"
					:items="items"
					@input="onInput"
					:removable="true" />
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

		<Variant title="Simple Text">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue3"
					title="Simple"
					:items="items"
					:item-text="(item: any) => item?.id"
					:item-sort="(a: any, b: any) => a.text + b.text"
					@input="onInput" />
			</div>
		</Variant>

		<Variant title="Simple Autocomplete">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue4"
					autocomplete
					title="SimpleAutocomplete"
					:items="items"
					:item-text="item => item?.text || ''"
					:item-filter="(items: any, search: string) => items.filter((i: any) => i.text.includes(search))"
					@input="onInput" />
			</div>
		</Variant>

		<Variant title="Tags">
			<div class="demo-wrapper">
				<a href="#">Pre</a>
				<svws-ui-multi-select v-model="modelValue5"
					tags
					title="Tags"
					:item-text="item => item?.text || ''"
					:items="items"
					@input="onInput" />
				<a href="#">Post</a>
			</div>
		</Variant>

		<Variant title="TagsAutocomplete">
			<div class="demo-wrapper">
				<a href="#">Pre</a>
				<svws-ui-multi-select v-model="modelValue6"
					:item-text="item => item?.text || ''"
					tags
					autocomplete
					title="TagsAutocomplete"
					placeholder="Filters"
					:items="items"
					@input="onInput" />
				<a href="#">Post</a>
			</div>
		</Variant>

		<Variant title="SimpleHeadless">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue7"
					:item-text="item => item?.text || ''"
					headless
					title="SimpleHeadless"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>

		<Variant title="TagsHeadless">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue8"
					:item-text="item => item?.text || ''"
					tags
					autocomplete
					headless
					title="TagsHeadless"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>
		<Variant title="Statistics Mode">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue8"
					:item-text="item => item?.text || ''"
					tags
					autocomplete
					statistics
					title="TagsHeadless"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>
	</Story>
</template>

<style>
	.demo-wrapper {
		padding: 14px;
		max-width: 500px;
		min-height: 600px;
	}
</style>
