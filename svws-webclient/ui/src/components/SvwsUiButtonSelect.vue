<template>
	<div class="svws-ui-button-select" ref="button">
		<svws-ui-button ref="inputEl" :type="type">
			<span v-if="defaultItem" @click="action(defaultItem)">{{ defaultItem.text }}</span>
			<i-ri-menu-line v-else />
		</svws-ui-button>
		<button class="svws-toggle button" :class="`button--${type}`" @click.stop="dropdownOpen = !dropdownOpen" :disabled="listEmpty">
			<i-ri-arrow-down-s-line v-if="!dropdownOpen" />
			<i-ri-arrow-up-s-line v-else />
		</button>
		<Teleport to="body">
			<svws-ui-dropdown-list v-if="dropdownOpen && dropdownActions" ref="refList" :strategy="strategy" :floating-left="floatingLeft" :floating-top="floatingTop">
				<template #items>
					<ul class="svws-ui-dropdown-list--items" role="listbox">
						<button v-for="(item, index) in dropdownActions" :key="index" class="svws-ui-dropdown-list--item" role="button" @click="action(item)">
							{{ item.text }}
						</button>
					</ul>
				</template>
			</svws-ui-dropdown-list>
		</Teleport>
	</div>
</template>

<script lang="ts" setup>
	import type { ButtonType } from '../types';
	import {autoUpdate, flip, offset, shift, size, useFloating} from "@floating-ui/vue";
	import {onClickOutside} from "@vueuse/core";
	import {ref, computed} from 'vue';
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	type Item = {
		text: string;
		default?: boolean;
		action: () => void;
	};

	const props = withDefaults(defineProps<{
		type?: ButtonType;
		disabled?: boolean;
		dropdownActions: Iterable<Item>;
	}>(),{
		type: 'primary',
		disabled: false,
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

	const listEmpty = computed(()=> {
		for (const _ of props.dropdownActions)
			return false;
		return true;
	})


	function action(item: Item) {
		defaultItem.value = item;
		item.action();
	}

	const {x, y, strategy} = useFloating(
		inputEl,
		refList,
		{
			placement: 'bottom-start',
			middleware: [flip(), shift(), offset(2), size({
				apply({rects, elements}) {
					Object.assign(elements.floating.style, {
						width: `${rects.reference.width}px`
					});
				}
			})],
			whileElementsMounted: autoUpdate,
		}
	);

	const floatingTop = computed(() => `${y.value ?? 0}px`);
	const floatingLeft = computed(() => `${x.value ?? 0}px`);

	onClickOutside(button, ()=> dropdownOpen.value = false);
</script>

<style lang="postcss">
.svws-ui-button-select {
  @apply flex gap-px;

  .svws-toggle.button {
    @apply rounded-l-none rounded-r-md px-0.5;
  }
}
</style>

<style lang="postcss" scoped>
.button {
  @apply rounded-r-none z-10 relative;
}

.svws-ui-dropdown-list {
  @apply min-w-fit;
}

.svws-ui-dropdown-list--item {
  @apply text-button;
}
</style>
