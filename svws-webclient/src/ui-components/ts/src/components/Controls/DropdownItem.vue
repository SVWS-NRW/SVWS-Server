<script setup lang='ts'>
	import { MenuItem as MenuItemHeadless } from "@headlessui/vue";

	const { disabled = false } = defineProps<{ disabled?: boolean; }>();
</script>
	
<template>
	<menu-item-headless v-slot="{ active }" :disabled="disabled">
		<button
			class="dropdown--item"
			:class="{
				'dropdown--item--active': active === true
			}"
		>
			<Icon class="dropdown--item--icon"
				><slot name="icon"></slot
			></Icon>
			<slot />
		</button>
	</menu-item-headless>
</template>

<style>
	.dropdown--item {
		@apply text-left text-button font-bold text-black;
		@apply flex items-start;
		@apply w-full;
		@apply px-5 py-2;
		@apply border-t-2 border-b-2 border-transparent;
	}

	.dropdown--item:disabled {
		@apply text-disabled-dark;
		@apply bg-transparent;
		@apply cursor-not-allowed;
	}

	.dropdown--item:focus {
		@apply outline-none ring-2;
	}

	.dropdown--item--active {
		@apply bg-dark-20 bg-opacity-50;
	}

	.dropdown--items--primary .dropdown--item:focus {
		@apply ring-primary ring-opacity-50;
		@apply border-primary;
	}

	.dropdown--items--secondary .dropdown--item:focus {
		@apply ring-primary ring-opacity-50;
		@apply border-black;
	}

	.dropdown--items--danger .dropdown--item:focus {
		@apply ring-error ring-opacity-50;
		@apply border-error;
	}

	.dropdown--item--icon {
		@apply mr-2;
	}
</style>
