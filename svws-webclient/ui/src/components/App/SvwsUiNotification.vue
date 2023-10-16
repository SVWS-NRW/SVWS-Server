<template>
	<div v-if="isOpen" class="notification inline-flex transform overflow-hidden"
		:class="{
			'notification--info': type === 'info',
			'notification--success': type === 'success',
			'notification--error': type === 'error' || type === 'bug',
			'notification--warning': type === 'warning',
			'notification--details-open': stackOpen,
		}">
		<div class="notification--content-wrapper flex justify-between items-start">
			<div class="notification--content" :class="{'notification--content--has-header': $slots.header}">
				<div class="notification--header">
					<span v-if="icon || type" class="icon notification--icon">
						<i-ri-lock-2-line v-if="icon === 'login'" />
						<i-ri-alert-fill v-else-if="icon === 'error' || type === 'error'" />
						<i-ri-bug-fill v-else-if="icon === 'bug' || type === 'bug'" />
						<i-ri-check-line v-else-if="icon === 'success' || type === 'success'" />
						<i-ri-information-line v-else-if="icon === 'info' || type === 'info'" />
						<i-ri-error-warning-line v-else-if="icon === 'warning' || type === 'warning'" />
					</span>
					<slot name="header" />
				</div>
				<div class="notification--text">
					<slot />
				</div>
				<div class="mt-3 -mb-1 flex flex-wrap gap-1" v-if="$slots.stack || type === 'bug'">
					<svws-ui-button v-if="type === 'bug'" type="secondary">
						Fehler melden
						<i-ri-send-plane-fill />
					</svws-ui-button>
					<svws-ui-button type="transparent" @click="toggleStackOpen" v-if="$slots.stack">
						<span>Details</span>
						<i-ri-arrow-up-s-line v-if="stackOpen" class="-ml-1" />
						<i-ri-arrow-down-s-line v-else class="-ml-1" />
					</svws-ui-button>
				</div>
				<div class="notification--stack" v-if="$slots.stack && stackOpen">
					<slot name="stack" />
				</div>
			</div>
			<div class="absolute top-0 right-0 p-1">
				<svws-ui-button type="icon" @click="isOpen = false" tabindex="-1" class="notification--close-button">
					<i-ri-close-line />
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>

<script setup lang='ts'>

	import {ref} from "vue";

	const props = withDefaults(defineProps<{
		type?: 'info' | 'error' | 'success' | 'warning' | 'bug';
		icon?: 'error' | 'login' | 'success' | 'warning' | 'info' | 'bug';
	}>(), {
		type: 'info',
		icon: undefined,
	});

	const isOpen = ref(true)
	const stackOpen = ref(false)

	function setIsOpen(value: boolean) {
		isOpen.value = value
	}

	function toggleStackOpen () {
		stackOpen.value = !stackOpen.value
	}

	defineExpose({
		setIsOpen,
		toggleStackOpen,
		isOpen,
		stackOpen,
	});

</script>

<style lang="postcss">

	.notification {
		@apply flex flex-col flex-shrink-0;
		@apply w-full;
		@apply relative z-40;
		@apply rounded-lg overflow-hidden;
		@apply shadow-lg shadow-black/10;
		@apply text-base pointer-events-auto;
		@apply bg-primary text-white font-bold;
		transition: transform 0.2s ease-out;

		.button:not(.button--secondary), .button--icon {
			@apply rounded-md;

			&:hover {
				@apply ring-0 bg-white/25;
			}
		}

		&--error {
			@apply bg-error text-white;

			.notification--icon {
				@apply animate-pulse;
			}
		}

		&--success {
			@apply bg-success text-white;

			.button, .button--icon {
				&:hover,
				&:focus {
					@apply bg-black/10;
				}
			}
		}
	}

	.notification--content-wrapper {
		@apply h-full overflow-y-auto w-full;
		-webkit-overflow-scrolling: touch;
	}

	.notification--wrapper {
		@apply fixed inset-0 z-50;
		@apply overflow-y-auto;
	}

	.notification--content {
		@apply flex-grow flex flex-wrap;
		@apply px-4 py-2 overflow-hidden;

		.notification--icon {
			@apply inline-block mr-1 text-base leading-none -mb-1;
		}

		.notification--text {
			@apply text-base font-bold;
		};

		&--has-header {
			@apply py-3;

			.notification--icon {
				@apply text-headline-sm;
			}

			.notification--header {
				@apply w-auto text-headline-sm font-bold mb-1;
			}

			.notification--text {
				@apply w-full font-medium break-words;
			}
		}

		.notification--stack {
			@apply whitespace-pre-wrap bg-black mt-4 -mb-2 -mx-3 p-3 font-mono overflow-auto min-w-full rounded-md;
		}
	}

	.notification--close-button {
		@apply w-7 h-7;
	}

</style>
