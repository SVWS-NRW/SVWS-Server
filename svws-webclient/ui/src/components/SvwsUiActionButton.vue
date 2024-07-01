<template>
	<div :class="{'svws-active': isActive}" class="svws-ui-action-button">
		<button role="button" class="svws-ui-action-button--button" @click="e => $emit('click', e)">
			<div class="svws-icon">
				<slot name="icon">
					<span class="icon" :class="[icon]" />
				</slot>
			</div>
			<div class="flex flex-col overflow-x-hidden">
				<div class="svws-title" :class="{'my-auto': !description}">{{ title }}</div>
				<div v-if="description" class="svws-description">{{ description }}</div>
			</div>
		</button>
		<div v-if="isActive" class="p-4" :class="{'pt-5': $slots.default}">
			<slot />
			<svws-ui-button v-if="actionFunction" :disabled="actionDisabled || isLoading" title="Aktion ausführen" @click="actionFunction" :is-loading="isLoading" :class="{'mt-8': $slots.default}">
				<template v-if="isLoading">
					<svws-ui-spinner spinning />
				</template>
				<template v-else>
					<span class="icon i-ri-play-line" />
				</template>
				{{ actionLabel || 'Ausführen' }}
			</svws-ui-button>
		</div>
	</div>
</template>

<script setup lang="ts">

	const props = withDefaults(defineProps<{
		title?: string;
		description?: string;
		icon?: string;
		actionLabel?: string;
		actionFunction?: (() => void) | (() => Promise<void>) | undefined;
		actionDisabled?: boolean;
		isLoading?: boolean;
		isActive?: boolean;
	}>(), {
		title: '',
		description: '',
		icon: 'i-ri-settings-2-line',
		actionLabel: '',
		actionFunction: undefined,
		actionDisabled: false,
		isLoading: false,
		isActive: false,
	});

	const emit = defineEmits<{
		'click': [value: MouseEvent];
	}>();


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
				@apply block h-[1.05em] w-[1.05em];
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

		.svws-ui-action-button--button:hover,
		.svws-ui-action-button--button:focus-visible {
			@apply outline-none bg-black/10 border-black/10 opacity-100 border;
		}

		&.svws-active {
			@apply border-black/10 dark:border-white/10;

			.svws-ui-action-button--button {
				@apply rounded-b-none border-b-black/5 dark:border-white/5  bg-primary/10;
			}

			.svws-ui-action-button--button:hover,
			.svws-ui-action-button--button:focus-visible {
				@apply border-x-transparent border-t-transparent;
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
