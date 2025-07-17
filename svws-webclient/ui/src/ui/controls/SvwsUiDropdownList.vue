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
					{{ noItemsText }}
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
							'svws-selected': selectedItemList.has(item),
						}"
						:aria-selected="selectedItemList.has(item) ? 'true' : 'false'"
						@mousedown.prevent
						@click="selectItem(item)">
						<span v-if="itemText(item).length === 0" class="opacity-25">—</span>
						<span v-else :class="{'font-bold': highlightItem === item}">{{ itemText(item) }}</span>
						<span class="icon i-ri-check-line w-5 shrink-0 -mr-1 -my-1 relative top-1.5" v-if="selectedItemList.has(item)" />
					</li>
				</slot>
			</template>
		</ul>
	</div>
</template>

<script setup lang="ts" generic="Item">

	import type { Strategy } from "@floating-ui/vue";
	import { ref, shallowRef, computed, useId } from "vue";

	const props = withDefaults(defineProps<{
		statistics?: boolean;
		selectedItemList?: Set<Item>;
		filteredList: Item[] | Iterable<Item>;
		itemText?: (item: Item) => string;
		selectItem?: (item: Item) => void;
		strategy?: Strategy;
		floatingLeft?: string;
		floatingTop?: string;
		searchText?: string;
		noItemsText?: string;
		highlightItem?: Item;
	}>(),{
		statistics: false,
		itemText: (item: Item) => "",
		selectItem: (item: Item) => undefined,
		searchText: "",
		selectedItemList: () => new Set<Item>(),
		highlightItem: undefined,
		strategy: undefined,
		floatingLeft: '',
		floatingTop: '',
		noItemsText: "Keine Einträge gefunden",
	});

	const floating = ref<HTMLDivElement | null>(null);

	const listIdPrefix = useId();

	const itemRefs = shallowRef<HTMLLIElement[]>([]);
	const activeItemIndex = ref(-1);

	const listEmpty = computed(() => {
		for (const _ of props.filteredList)
			return false;
		return true;
	})

	defineExpose({ activeItemIndex, floating, itemRefs });


</script>
