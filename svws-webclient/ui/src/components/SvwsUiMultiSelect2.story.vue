<template>
	<Story title="Multiselect 2" id="svws-ui-multi-select2" :layout="{type: 'grid', width: '45%'}" icon="ri:expand-up-down-fill">
		<Variant title="Tags" id="Tags">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue5"
					tags
					title="Tags"
					:item-text="item => item.text"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>

		<Variant title="Tags with Autocomplete" id="Tags with Autocomplete">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue6"
					:item-text="item => item.text"
					tags
					autocomplete
					title="Tags with Autocomplete"
					placeholder="Filters"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>


		<Variant title="Tags Headless" id="Tags Headless">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue8"
					:item-text="item => item.text"
					tags
					autocomplete
					headless
					title="Tags Headless"
					:items="items"
					@input="onInput" />
			</div>
		</Variant>
		<Variant title="Statistics" id="Statistics">
			<div class="demo-wrapper">
				<svws-ui-multi-select v-model="modelValue8"
					:item-text="item => item.text"
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

<script setup lang="ts">
	import { logEvent } from "histoire/client";
	import { ref, reactive } from "vue";

	const items = reactive<{id: number, text: string}[]>([
		{ id: 1, text: "Item 1" },
		{ id: 2, text: "Item 2 with a very, very, very, really long title that's way too big for the row. If this happens, the UI should be prepared" },
		{ id: 3, text: "Item 3" },
		{ id: 4, text: "Item 4" },
		{ id: 5, text: "Item 5" },
		{ id: 6, text: "Item 6" }
	]);

	const modelValue5 = ref([items[0]]);
	const modelValue6 = ref();
	const modelValue8 = ref([items[0]]);

	function onInput(event: Event) {
		logEvent("input", event);
	}
</script>

<style>
.demo-wrapper {
	@apply py-4;
}
</style>
