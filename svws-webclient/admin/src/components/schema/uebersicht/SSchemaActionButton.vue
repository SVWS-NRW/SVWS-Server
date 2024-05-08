<template>
	<div :class="{'svws-active': isActive}" class="svws-ui-action-button">
		<button role="button" class="svws-ui-action-button--button" @click="toggleActionButton">
			<div class="svws-icon">
				<slot name="icon">
					<span class="icon" :class="[icon]" />
				</slot>
			</div>
			<div class="flex flex-col">
				<div class="svws-title">{{ title }}</div>
				<div class="svws-description">{{ description }}</div>
			</div>
		</button>
		<div v-if="isActive" class="p-4">
			<slot />
		</div>
	</div>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const props = withDefaults(defineProps<{
		title?: string;
		description?: string;
		icon?: string;
	}>(), {
		title: '',
		description: '',
		icon: 'i-ri-archive-line',
	});

	const isActive = ref<boolean>(false);

	const toggleActionButton = () => {
		isActive.value = !isActive.value;
	};

</script>

<style lang="postcss">
	.svws-ui-action-button + .svws-ui-action-button {
		@apply mt-2;
	}

	.svws-ui-action-button.svws-active + .svws-ui-action-button {
		@apply mt-6;
	}

	.svws-ui-action-button {
		@apply rounded-lg border-light border;

		.svws-ui-action-button--button {
			@apply py-4 px-3 text-balance flex gap-3 text-left w-full rounded-lg border border-transparent;

			.icon {
				@apply block h-[1.1em] w-[1.1em];
			}

			.svws-title {
				@apply font-bold text-headline-md;
			}

			.svws-description {
				@apply opacity-50 leading-tight;
			}

			.svws-icon {
				@apply text-headline-xl w-12 text-center;
			}
		}

		&.svws-active {
			@apply border-black/10 dark:border-white/10;
		}

		&:not(.svws-active) .svws-ui-action-button--button:hover,
		&:not(.svws-active) .svws-ui-action-button--button:focus-visible {
			@apply outline-none bg-black/10 border-black/10 opacity-100 border;

			.svws-icon {
				@apply opacity-100;
			}
		}

		&:focus .svws-ui-action-button--button {
			@apply outline-none;
		}

		&:not(.svws-active) .svws-ui-action-button--button:focus-visible {
			@apply ring ring-primary/50 ring-offset-1;
		}
	}
</style>
