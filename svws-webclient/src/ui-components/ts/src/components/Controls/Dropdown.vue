<script lang="ts" setup>
import { Menu as MenuHeadless, MenuButton, MenuItems } from "@headlessui/vue";

const {
	variant = "primary",
	disabled = false,
} = defineProps<{
	variant?: "primary" | "secondary" | "danger";
	disabled?: boolean;
}>();
</script>

<template>
	<MenuHeadless as="div" class="dropdown--wrapper">
		<MenuButton
v-slot="{ open }" class="dropdown--button" :class="{
			'dropdown--button--primary': variant === 'primary',
			'dropdown--button--secondary': variant === 'secondary',
			'dropdown--button--danger': variant === 'danger'
		}" :disabled="disabled">
			<slot name="dropdownButton" />
			<Icon class="dropdown--icon">
				<i-ri-arrow-up-s-line v-if="open" />
				<i-ri-arrow-down-s-line v-else />
			</Icon>
		</MenuButton>
		<MenuItems
as="div" class="dropdown--items" :class="{
			'dropdown--items--primary': variant === 'primary',
			'dropdown--items--secondary': variant === 'secondary',
			'dropdown--items--danger': variant === 'danger'
		}">
			<slot name="dropdownItems" />
		</MenuItems>
	</MenuHeadless>
</template>

<style>
.dropdown--wrapper {
	@apply inline-block;
	@apply relative;
}

.dropdown--button {
	@apply border-2;
	@apply flex items-center;
	@apply px-5 py-2;
	@apply relative z-10;
	@apply rounded-full;
	@apply text-button font-bold;
}

.dropdown--button:focus {
	@apply outline-none ring;
}

.dropdown--button--primary {
	@apply bg-primary;
	@apply border-primary;
	@apply text-white;
}

.dropdown--button--primary:focus {
	@apply ring-primary ring-opacity-50;
}

.dropdown--button--secondary {
	@apply bg-transparent;
	@apply border-black;
	@apply text-black;
}

.dropdown--button--secondary:focus {
	@apply ring-primary ring-opacity-50;
}

.dropdown--button--danger {
	@apply bg-transparent;
	@apply border-error;
	@apply text-error;
}

.dropdown--button--danger:focus {
	@apply bg-error;
	@apply ring-error ring-opacity-50;
	@apply text-white;
}

.dropdown--button:disabled {
	@apply bg-disabled;
	@apply border-disabled-medium;
	@apply cursor-not-allowed;
	@apply text-disabled-dark;
}

.dropdown--icon {
	@apply inline-block;
	@apply ml-1;
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

.dropdown--items--danger {
	@apply border-error;
}
</style>
