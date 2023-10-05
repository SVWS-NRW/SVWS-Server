<template>
	<div class="svws-ui-button-select" ref="button">
		<svws-ui-button ref="inputEl" :type="type">
			<slot name="default" />
		</svws-ui-button>
		<button class="svws-toggle button" :class="`button--${type}`" @click="dropdownOpen = !dropdownOpen" :disabled="!dropdownActions">
			<i-ri-arrow-down-s-line v-if="!dropdownOpen" />
			<i-ri-arrow-up-s-line v-else />
		</button>
		<Teleport to="body">
			<svws-ui-dropdown-list v-if="dropdownOpen" ref="refList" :strategy="strategy" :floating-left="floatingLeft" :floating-top="floatingTop">
				<template #items>
					<ul class="svws-ui-dropdown-list--items" role="listbox">
						<button v-for="(action, index) in dropdownActions" :key="index" class="svws-ui-dropdown-list--item" role="button" @click="action.action">
							{{ action.text }}
						</button>
					</ul>
				</template>
			</svws-ui-dropdown-list>
		</Teleport>
	</div>
</template>

<script lang="ts" setup>
	import type {ComponentExposed} from "vue-component-type-helpers";
	import type { MaybeElement} from "@floating-ui/vue";
	import type { ButtonType } from '../types';
	import type { Ref} from 'vue';
	import {autoUpdate, flip, offset, shift, size, useFloating} from "@floating-ui/vue";
	import {onClickOutside} from "@vueuse/core";
	import {ref, computed} from 'vue';
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	const props = withDefaults(defineProps<{
		type?: ButtonType;
		disabled?: boolean;
		dropdownActions: {
			text: string;
			action: () => void;
		}[];
	}>(),{
		type: 'primary',
		disabled: false,
	});

	const button = ref(null);
	const dropdownOpen = ref(false);
	const inputEl = ref(null);
	const refList = ref<ComponentExposed<typeof SvwsUiDropdownList> | null>(null);

	const {x, y, strategy} = useFloating(
		inputEl,
		refList as Readonly<Ref<MaybeElement<HTMLElement>>>,
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
