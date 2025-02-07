<template>
	<div class="flex gap-px" ref="button">
		<svws-ui-button ref="inputEl" :type="type">
			<span v-if="defaultAction !== undefined" @click="noDefault ? dropdownOpen = !dropdownOpen : (defaultAction && action(defaultAction))" class="flex gap-1">
				<slot name="icon" />{{ defaultAction.text }}
			</span>
			<span v-else-if="defaultItem" @click="defaultItem && action(defaultItem)" class="flex gap-1">
				<slot name="icon" />{{ defaultItem.text }}
			</span>
			<span v-else class="icon i-ri-menu-line" />
		</svws-ui-button>
		<button class="rounded-l-none rounded-r-md px-0.5 z-10 relative border border-ui" :class="`button--${type}`" @click.stop="dropdownOpen = !dropdownOpen" :disabled="listEmpty">
			<span class="icon mt-1 i-ri-arrow-down-s-line" v-if="!dropdownOpen" />
			<span class="icon mt-1 i-ri-arrow-up-s-line" v-else />
		</button>
		<Teleport to="body">
			<svws-ui-dropdown-list v-if="dropdownOpen && dropdownActions" ref="refList" :strategy :floating-left :floating-top :filtered-list="dropdownActions">
				<template #item="{ item }">
					<hr v-if="item.separator === true" class="w-full mx-auto my-1 text-ui-neutral">
					<button v-else class="svws-ui-dropdown-list--item" role="button" @click="action(item)">
						{{ item.text }}
					</button>
				</template>
			</svws-ui-dropdown-list>
		</Teleport>
	</div>
</template>

<script lang="ts" setup>

	import type { ButtonType } from '../../types';
	import { autoUpdate, flip, offset, shift, size, useFloating } from "@floating-ui/vue";
	import { onClickOutside } from "@vueuse/core";
	import { ref, computed } from 'vue';
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	type Item = {
		text: string;
		action: () => void | Promise<any>;
		default?: boolean;
		separator?: boolean;
	}

	const props = withDefaults(defineProps<{
		type?: ButtonType;
		disabled?: boolean;
		dropdownActions: Iterable<Item>;
		defaultAction?: Item | undefined;
		noDefault?: boolean;
	}>(),{
		type: 'primary',
		disabled: false,
		defaultAction: undefined,
		noDefault: false,
	});

	const button = ref(null);
	const inputEl = ref(null);
	const refList = ref(null);
	const dropdownOpen = ref(false);
	const defaultItem = ref<Item | undefined>(undefined);

	for (const item of props.dropdownActions) {
		if (defaultItem.value === undefined)
			defaultItem.value = item;
		if (item.default === true) {
			defaultItem.value = item;
			break
		}
	}

	const listEmpty = computed(() => {
		for (const _ of props.dropdownActions)
			return false;
		return true;
	})

	function action(item: Item) {
		defaultItem.value = item;
		void item.action();
	}

	const { x, y, strategy } = useFloating( inputEl, refList, {
		placement: 'bottom-start',
		middleware: [flip(), shift(), offset(2), size({
			apply({rects, elements}) {
				Object.assign(elements.floating.style, {
					width: `${rects.reference.width}px`,
				})}})],
		whileElementsMounted: autoUpdate,
	});

	const floatingTop = computed(() => `${y.value}px`);
	const floatingLeft = computed(() => `${x.value}px`);

	onClickOutside(button, () => dropdownOpen.value = false);

</script>

<style lang="postcss" scoped>

	@reference "../../assets/styles/index.css";

	.button {
		position: relative;
		border-top-right-radius: 0;
		border-bottom-right-radius: 0;
		z-index: 10;
	}

	.svws-ui-dropdown-list {
		min-width: fit-content;
	}

</style>