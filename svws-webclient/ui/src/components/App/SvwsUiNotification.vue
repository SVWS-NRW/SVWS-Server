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
					<span v-if="icon || type" class="notification--icon">
						<span class="icon i-ri-lock-2-line" v-if="icon === 'login'" />
						<span class="icon i-ri-alert-fill" v-else-if="icon === 'error' || type === 'error'" />
						<span class="icon i-ri-bug-fill" v-else-if="icon === 'bug' || type === 'bug'" />
						<span class="icon i-ri-check-line" v-else-if="icon === 'success' || type === 'success'" />
						<span class="icon i-ri-information-line" v-else-if="icon === 'info' || type === 'info'" />
						<span class="icon i-ri-error-warning-line" v-else-if="icon === 'warning' || type === 'warning'" />
					</span>
					<slot name="header" />
				</div>
				<div class="notification--text">
					<slot />
				</div>
				<div class="mt-4 flex flex-wrap gap-1 w-full" v-if="$slots.stack || type === 'bug'">
					<svws-ui-button v-if="type === 'bug'" type="secondary" class="notification--send-button">
						Fehler melden
						<span class="icon i-ri-send-plane-fill" />
					</svws-ui-button>
					<button @click="copyToClipboard" v-if="toCopy !== undefined" class="notification--copy-button">
						<span>
							<span v-if="copied === null || copied === false">Meldung kopieren</span>
							<span v-else>Meldung kopiert</span>
						</span>
						<span class="icon i-ri-file-copy-line" v-if="copied === null" />
						<span class="icon i-ri-error-warning-fill" v-else-if="copied === false" />
						<span class="icon i-ri-check-line icon-success" v-else />
					</button>
					<button @click="toggleStackOpen" v-if="$slots.stack" class="notification--details-button">
						<span>Details</span>
						<span class="icon i-ri-arrow-up-s-line -ml-1" v-if="stackOpen" />
						<span class="icon i-ri-arrow-down-s-line -ml-1" v-else />
					</button>
				</div>
				<div v-if="copied === false" class="p-2 bg-ui border rounded-md text-ui-danger mt-2">{{ "Es gab einen Fehler beim Kopieren in die Zwischenablage. Ist die Zwischenablage gesperrt?" }}</div>
				<div class="notification--stack" v-if="$slots.stack && stackOpen">
					<slot name="stack" />
				</div>
			</div>
			<div class="absolute top-0 right-0 p-1">
				<svws-ui-button type="icon" @click="close" tabindex="-1" class="notification--close-button">
					<span class="icon i-ri-close-line" />
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>

<script setup lang='ts'>

	import { ref } from "vue";

	const props = withDefaults(defineProps<{
		type?: 'info' | 'error' | 'success' | 'warning' | 'bug';
		icon?: 'error' | 'login' | 'success' | 'warning' | 'info' | 'bug';
		id?: number;
		toCopy?: string;
	}>(), {
		type: 'info',
		icon: undefined,
		id: 0,
		toCopy: undefined,
	});

	const emit = defineEmits<{
		click: [id: number];
	}>()

	const isOpen = ref(true);
	const stackOpen = ref(false);
	const copied = ref<boolean|null>(null);

	function setIsOpen(value: boolean) {
		isOpen.value = value;
	}

	function toggleStackOpen() {
		stackOpen.value = !stackOpen.value;
	}

	async function copyToClipboard() {
		if (props.toCopy === undefined)
			return;
		try {
			await navigator.clipboard.writeText(props.toCopy);
		} catch(e) {
			copied.value = false;
		}
		copied.value = true;
	}

	function close() {
		isOpen.value = false;
		if (props.id > 0)
			emit('click', props.id);
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
		@apply bg-ui text-ui border border-ui-neutral;
		@apply flex flex-col flex-shrink-0;
		@apply w-full;
		@apply relative z-40;
		@apply rounded-lg overflow-hidden;
		@apply shadow-xl;
		@apply text-base pointer-events-auto;
		@apply font-bold;
		transition: transform 0.2s ease-out;

		.notification--icon {
			@apply opacity-75;

			span {
				@apply inline-block -my-1;
			}
		}

		.button:not(.button--secondary), .button--icon {
			@apply rounded-md;

			&:hover {
				@apply ring-0 bg-ui;
			}
		}

		&--info {
			@apply dark:bg-ui-neutral-weak;
		}

		&--success,
		&--error,
		.dark &--info,
		.htw-dark &--info {
			span.icon {
				-webkit-filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
				filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
			}
			/* TODO: COLORS icon */
		}

		&--error {
			@apply bg-ui-danger text-ui-ondanger border-ui-danger;

			.notification--icon {
				@apply animate-pulse opacity-100;
			}
		}

		&--success {
			@apply bg-ui-success text-ui-onsuccess border-ui-success;
		}

		&--warning {
			@apply bg-ui-warning text-ui-onwarning border-ui-warning;

			.dark & span.icon,
			.htw-dark & span.icon {
				-webkit-filter: none;
				filter: none;
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
		@apply flex-grow flex flex-wrap gap-x-1;
		@apply px-3 py-2 overflow-hidden;

		.notification--icon {
			@apply inline-block text-base leading-none;
		}

		.notification--text {
			@apply text-base font-bold;
		};

		&--has-header {
			.notification--icon {
				@apply text-headline-sm;
			}

			.notification--header {
				@apply w-auto text-headline-sm font-bold mb-0.5;
			}

			.notification--text {
				@apply w-full font-medium break-words;
			}
		}

		.notification--stack {
			@apply whitespace-pre-wrap bg-ui-black mt-2 -mb-2 -mx-3 p-3 font-mono overflow-auto min-w-full rounded-b-md;
		}
	}

	.notification .notification--content-wrapper .notification--close-button {
		@apply w-6 h-6 inline-flex items-center justify-center p-0 opacity-75;

		&:hover,
		&:focus-visible {
			@apply bg-ui opacity-100;

			span.icon {
				-webkit-filter: none;
				filter: none;

				.dark &,
				.htw-dark & {
					-webkit-filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
					filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
				}
			}
		}
	}

	.notification--info .notification--content-wrapper .notification--close-button {
		@apply border border-transparent;

		&:hover,
		&:focus-visible {
			@apply bg-ui-hover border-ui-neutral-hover;

			span.icon {
				-webkit-filter: none;
				filter: none;

				.dark &,
				.htw-dark & {
					-webkit-filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
					filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
				}
			}
		}
	}

	.notification--send-button,
	.notification--details-button,
	.notification--copy-button {
		&:hover,
		&:focus-visible {
			@apply !bg-ui !text-ui;

			.dark &,
			.htw-dark & {
				@apply !border-ui-black;
			}

			span.icon {
				-webkit-filter: none;
				filter: none;

				.dark &,
				.htw-dark & {
					-webkit-filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
					filter: invert(95%) sepia(100%) saturate(14%) hue-rotate(213deg) brightness(104%) contrast(104%);
				}
			}
		}
	}

	.notification--details-button,
	.notification--copy-button {
		@apply flex items-center gap-1 text-sm font-bold px-2 rounded-md;

		span.icon {
			@apply w-5 h-5 inline-block -mr-1;
		}
	}

	.notification--details-button {
		@apply ml-auto;
	}

</style>
