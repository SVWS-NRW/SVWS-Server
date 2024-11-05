<template>
	<div class="svws-ui-dropdown-list" id="svws-ui-dropdown-list-id"
		:class="{'svws-statistik': statistics}"
		:style="{ position: strategy, top: floatingTop, left: floatingLeft }"
		ref="floating">
		<ul :id="listIdPrefix"
			class="svws-ui-dropdown-list--items"
			role="listbox"
			@mouseenter="activeItemIndex = -1">
			<li v-if="listEmpty" class="px-2 py-1.5 text-base text-ui-disabled inline-block">
				<template v-if="!searchText">
					Keine Einträge gefunden
				</template>
				<template v-else>
					Keine Ergebnisse für "{{ searchText }}"
				</template>
			</li>
			<template v-for="(item, index) in filteredList" :key="index">
				<slot name="item" :item="item" :index="index">
					<li :id="`${listIdPrefix}-${index}`"
						ref="itemRefs"
						role="option"
						class="svws-ui-dropdown-list--item"
						:class="{
							'svws-active': activeItemIndex === index,
							'svws-selected': selectedItemList.has(item)
						}"
						:aria-selected="selectedItemList.has(item) ? 'true' : 'false'"
						@mousedown.prevent
						@click="selectItem(item)">
						<span v-if="itemText(item).length === 0" class="opacity-25">—</span>
						<span v-else :class="{'font-bold': highlightItem === item}">{{ itemText(item) }}</span>
						<span class="icon i-ri-check-line w-5 flex-shrink-0 -mr-1 -my-1 relative top-1.5" v-if="selectedItemList.has(item)" />
					</li>
				</slot>
			</template>
		</ul>
	</div>
</template>

<script setup lang="ts" generic="Item">

	import type { Strategy } from "@floating-ui/vue";
	import type { Ref, ShallowRef } from "vue";
	import { ref, shallowRef, computed, useId } from "vue";

	const props = withDefaults(defineProps<{
		statistics?: boolean;
		selectedItemList?: Set<Item>;
		filteredList: Item[] | Iterable<Item>;
		itemText?: (item: Item) => string;
		selectItem?: (item: Item) => void;
		strategy: Strategy;
		floatingLeft: string;
		floatingTop: string;
		searchText?: string;
		highlightItem?: Item;
	}>(),{
		statistics: false,
		itemText: (item: Item) => "",
		selectItem: (item: Item) => undefined,
		searchText: "",
		selectedItemList: () => new Set<Item>(),
		highlightItem: undefined,
	});

	const floating = ref(null);

	const listIdPrefix = useId();

	const itemRefs = shallowRef<HTMLLIElement[]>([]);
	const activeItemIndex = ref(-1);

	const listEmpty = computed(() => {
		for (const _ of props.filteredList)
			return false;
		return true;
	})

	defineExpose<{
		activeItemIndex: Ref<number>;
		floating: Ref<HTMLElement|null>;
		itemRefs: ShallowRef<HTMLLIElement[]>;
	}>({
		activeItemIndex, floating, itemRefs
	});


</script>


<style lang="postcss">
.svws-ui-dropdown-list {
	@apply border bg-ui border-ui-secondary text-ui;
	@apply w-full z-50 min-w-[11rem] shadow-xl rounded-lg;
}

	.svws-ui-dropdown-list--items {
		@apply overflow-y-auto overflow-x-hidden flex flex-col gap-px pt-1 px-1 pb-1;
		max-height: 24rem;
	}

.svws-ui-dropdown-list--item {
	@apply border border-transparent;
  	@apply rounded px-2 py-1.5 inline-flex items-start justify-between gap-0.5 text-base font-medium cursor-pointer;

	&.svws-selected {
		@apply bg-ui-selected text-ui-onselected font-bold border-ui-selected;
	}

	&.svws-active,
	&:hover,
	&:focus-visible {
		@apply bg-ui-hover border-ui;

		&.svws-selected {
			@apply bg-ui-selected-hover text-ui-onselected-hover border-ui-selected;
		}
	}

	&.svws-active {
		@apply ring ring-ui border-ui-brand z-10;

		.svws-statistik & {
			@apply ring-ui-statistic border-ui-statistic;
		}
	}
}

/* TODO: COLORS icon (multi-select checkmark) */
</style>
