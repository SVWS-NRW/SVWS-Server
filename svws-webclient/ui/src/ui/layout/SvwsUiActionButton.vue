<template>
	<div :class="{'svws-active': isActive}" class="svws-ui-action-button">
		<button role="button" class="svws-ui-action-button--button" @click="e => emit('click', e)">
			<div class="svws-icon">
				<slot name="icon">
					<span class="icon" :class="[icon]" />
				</slot>
			</div>
			<div class="flex flex-col overflow-x-hidden overflow-y-hidden">
				<div class="svws-title" :class="{'my-auto': !description}">{{ title }}</div>
				<div v-if="description" class="svws-description">{{ description }}</div>
			</div>
		</button>
		<div v-if="isActive" class="p-4 svws-ui-action-button--content" :class="{'pt-5': $slots.default}">
			<slot />
			<svws-ui-button v-if="actionFunction !== undefined" :disabled="actionDisabled || isLoading" title="Aktion ausführen" @click="actionFunction" :is-loading="isLoading" :class="{'mt-8': $slots.default}">
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
	import type { ButtonType } from '../../types';

	const props = withDefaults(defineProps<{
		title?: string;
		description?: string;
		icon?: string;
		actionLabel?: string;
		actionFunction?: (() => void) | (() => Promise<void>) | undefined;
		actionDisabled?: boolean;
		isLoading?: boolean;
		isActive?: boolean;
		type?: ButtonType;
		size?: 'small' | 'normal' | 'big';
	}>(), {
		title: '',
		description: '',
		icon: 'i-ri-settings-2-line',
		actionLabel: '',
		actionFunction: undefined,
		actionDisabled: false,
		isLoading: false,
		isActive: false,
		type: 'primary',
		size: 'normal',
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
		@apply bg-ui text-ui;

		.svws-ui-action-button--button {
			@apply border border-ui-neutral;
			@apply py-4 px-3 text-balance flex gap-3 text-left w-full rounded-lg;

			.icon {
				@apply block h-[1.05em] w-[1.05em];
			}

			.svws-title {
				@apply font-bold text-headline-md;
			}

			.svws-description {
				@apply text-ui-secondary;
				@apply leading-tight;
			}

			.svws-icon {
				@apply text-headline-xl w-12 text-center;
			}

			&:hover,
			&:focus-visible {
				@apply border-ui-brand text-ui-hover;
				@apply outline-none;

				.icon {
					@apply icon-ui-brand;

					.dark & {
						@apply icon-ui-brand--dark;
					}
				}

				.svws-description {
					@apply text-ui-onneutral-hover;
				}
			}

			&:focus-visible {
				@apply ring-4 ring-ui-brand;
			}
		}

		&.svws-active {
			.svws-ui-action-button--content {
				@apply border border-ui;
				@apply rounded-b-lg border-t-0;
			}

			.svws-ui-action-button--button {
				@apply border-ui bg-ui-neutral text-ui-onneutral;
				@apply rounded-b-none;

				&:hover,
				&:focus-visible {
					@apply text-ui-onneutral-hover;
				}
			}
		}

		&:focus .svws-ui-action-button--button {
			@apply outline-none;
		}
	}
</style>
