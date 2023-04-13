<script setup lang='ts'>
	import { Menu as MenuHeadless, MenuButton, MenuItems } from "@headlessui/vue";
	import type { DropdownType } from "../../types";

	const props = withDefaults(defineProps<{
		type?: DropdownType;
		dropdownDisabled?: boolean;
	}>(), {
		type: 'primary',
		dropdownDisabled: false,
	});
</script>

<template>
	<MenuHeadless as="div" class="dropdown-with-action--wrapper">
		<slot name="actionButton" />
		<menu-button v-slot="{ open }" class="dropdown-with-action--button" :class="{
			'dropdown-with-action--button--primary':
				type === 'primary',
			'dropdown-with-action--button--secondary':
				type === 'secondary',
			'dropdown-with-action--button--error':
				type === 'error'
		}" :disabled="dropdownDisabled">
			<svws-ui-icon class="dropdown-with-action--icon">
				<i-ri-arrow-up-s-line v-if="open" />
				<i-ri-arrow-down-s-line v-else />
			</svws-ui-icon>
		</menu-button>
		<menu-items as="div" class="dropdown--items" :class="{
			'dropdown--items--primary': type === 'primary',
			'dropdown--items--secondary': type === 'secondary',
			'dropdown--items--error': type === 'error'
		}">
			<slot name="dropdownItems" />
		</menu-items>
	</MenuHeadless>
</template>

<style>
.dropdown-with-action--wrapper {
	@apply inline-flex items-center;
	@apply relative;
}

.dropdown-with-action--button {
	@apply ml-1;
	@apply border-2;
	@apply flex items-center;
	@apply py-2 pl-2 pr-3;
	@apply relative z-10;
	@apply rounded-full rounded-l-none;
	@apply text-button font-bold;
}

.dropdown-with-action--button:focus {
	@apply outline-none ring;
}

.dropdown-with-action--button--primary {
	@apply bg-primary;
	@apply border-primary;
	@apply text-white;
}

.dropdown-with-action--button--primary:focus {
	@apply ring-primary ring-opacity-50;
}

.dropdown-with-action--button--secondary {
	@apply bg-transparent;
	@apply border-black;
	@apply text-black;
}

.dropdown-with-action--button--secondary:focus {
	@apply ring-primary ring-opacity-50;
}

.dropdown-with-action--button--error {
	@apply bg-transparent;
	@apply border-error;
	@apply text-error;
}

.dropdown-with-action--button--error:focus {
	@apply bg-error;
	@apply ring-error ring-opacity-50;
	@apply text-white;
}

.dropdown-with-action--button:disabled {
	@apply bg-disabled;
	@apply border-disabled-medium;
	@apply cursor-not-allowed;
	@apply text-disabled-dark;
}

.dropdown-with-action--icon {
	@apply inline-block;
	@apply mb-px;
}

.dropdown--items {
	@apply absolute top-4 z-0;
	@apply bg-white;
	@apply border-2 border-t-0;
	@apply overflow-hidden;
	@apply pt-6 pb-2;
	@apply rounded-b-3xl;
	@apply w-full;
}

.dropdown--items:focus {
	@apply outline-none;
}

.dropdown--items--primary {
	@apply border-primary;
}

.dropdown--items--secondary {
	@apply border-black;
}

.dropdown--items--error {
	@apply border-error;
}
</style>
