<template>
	<button class="button" :class="{
		'button--primary': type === 'primary',
		'button--secondary': type === 'secondary',
		'button--danger': (type === 'error') || (type === 'danger'),
		'button--transparent': type === 'transparent',
		'button--icon': type === 'icon',
		'button--trash': type === 'trash',
		'button--small': size === 'small',
		'button--big': size === 'big',
		'filterFocusField': filterButton,
	}" :disabled="disabled" ref="addButton">
		<slot v-if="type !== 'trash'" />
		<span v-if="type === 'trash'" class="button--trash-icon">
			<span class="inline-block icon i-ri-delete-bin-line icon--line" />
			<span class="inline-block icon i-ri-delete-bin-fill icon--fill" />
		</span>
		<span v-if="$slots.badge" class="button--badge">
			<slot name="badge" />
		</span>
	</button>
</template>

<script lang="ts" setup>

	import type { ButtonType } from '../../types';
	import { onMounted, ref } from "vue";

	const addButton = ref<HTMLButtonElement|null>(null);

	const props = withDefaults(defineProps<{
		type?: ButtonType;
		disabled?: boolean;
		size?: 'small' | 'normal' | 'big';
		autofocus?: boolean;
		filterButton?: boolean;
	}>(),{
		type: 'primary',
		disabled: false,
		size: 'normal',
		autofocus: false,
		filterButton: false,
	});

	onMounted(() => setAutofocus());

	function setAutofocus() {
		if(props.autofocus && (addButton.value !== null))
			addButton.value.focus();
	}

</script>

<style lang="postcss">

	.button {
		@apply rounded-md border;
		@apply select-none relative;
		@apply text-button font-bold;
		@apply flex items-center;
		gap: 0.25em;
		padding: 0.45em 0.75em;

		.svws-ui-table-filter & {
			min-height: 2.25rem;
		}

		svg {
			margin-top: -0.1em;
			margin-bottom: -0.1em;
		}

		&:focus {
			@apply outline-none;
			@apply ring;
		}

		&:focus-visible {
			@apply ring ring-ui-brand;

			.page--statistik & {
				@apply ring-ui-statistic;
			}
		}

		&:active {
			@apply ring-0 brightness-110;
		}

		.svws-ui-tfoot & {
			@apply h-7 min-h-[unset];
		}

		span.icon {
			@apply -my-1;
		}
	}

	.button--primary {
		@apply bg-ui-brand text-ui-onbrand border-ui-brand;

		&:hover,
		&:focus-visible {
			@apply bg-ui-brand-hover text-ui-onbrand-hover border-ui-brand-hover;
		}

		.icon {
			@apply icon-ui-onbrand;

			.dark & {
				@apply icon-ui-onbrand--dark;
			}
		}

		.page--statistik & {
			@apply bg-ui-statistic text-ui-onstatistic border-ui-statistic;

			.icon {
				@apply icon-ui-onstatistic;

				.dark & {
					@apply icon-ui-onstatistic--dark;
				}
			}

			&:hover,
			&:focus-visible {
				@apply bg-ui-statistic-hover text-ui-onstatistic-hover border-ui-statistic-hover;
			}
		}
	}

	.button--secondary {
		@apply bg-transparent text-ui border-ui;

		.notification--error &,
		.svws-ui-stundenplan--unterricht--warning & {
			@apply text-ui-ondanger border-ui-ondanger;
		}

		&:hover,
		&:focus-visible {
			@apply border-ui-brand text-ui-brand;

			.icon {
				@apply icon-ui-brand;

				.dark & {
					@apply icon-ui-brand--dark;
				}
			}

			.page--statistik & {
				@apply border-ui-statistic text-ui-statistic;

				.icon {
					@apply icon-ui-statistic;

					.dark & {
						@apply icon-ui-statistic--dark;
					}
				}
			}

			.notification--error &,
			.svws-ui-stundenplan--unterricht--warning & {
				@apply text-ui-ondanger-hover border-ui-ondanger-hover;
			}
		}

		&:focus-visible {
			.notification--error &,
			.svws-ui-stundenplan--unterricht--warning & {
				@apply ring-ui-neutral;
			}
		}
	}

	.button--transparent,
	.button--icon {
		@apply bg-transparent border-transparent;

		&:hover,
		&:focus-visible {
			@apply bg-ui-hover text-ui-onneutral-hover;
		}

		&:active {
			@apply brightness-95;
		}
	}

	.button--danger,
	.button--trash {
		@apply bg-transparent text-ui-danger border-ui-danger;

		.icon {
			@apply icon-ui-danger;

			.dark & {
				@apply icon-ui-danger--dark;
			}
		}

		&:hover,
		&:focus-visible {
			@apply bg-ui-danger text-ui-ondanger;

			.icon {
				@apply icon-ui-ondanger;

				.dark & {
					@apply icon-ui-ondanger--dark;
				}
			}
		}

		&:focus-visible {
			@apply ring-ui-danger;
		}
	}

	.button--trash {
		@apply rounded relative;
		@apply py-0 px-2;
		border: 0 !important;
		padding: 0.2em !important;
		width: 1.6em;
		height: 1.6em;

		.icon--fill {
			@apply hidden;
		}

		&:hover,
		&:focus-visible {
			.icon--line {
				@apply hidden;
			}

			.icon--fill {
				@apply inline-block;
			}
		}
	}

	.button--trash,
	.button--icon {
		&.button--small,
		.svws-ui-tbody & {
			@apply text-sm font-medium h-6 w-6;
			padding: 0.3em !important;

			svg {
				width: 1.2em;
				height: 1.2em;
			}
		}

		.svws-ui-tfoot &.button {
			@apply text-button h-7 w-7;
			padding: 0.25em !important;

			svg {
				width: 1.3em;
				height: 1.3em;
			}
		}
	}

	.button--icon {
		@apply p-1.5 justify-center border-0 items-center;
		@apply w-9 h-9;

		svg {
			width: 1.3rem;
			height: 1.3rem;
		}
	}

	.button:disabled {
		&,
		&:hover,
		&:focus,
		&:focus-visible {
			@apply bg-ui-disabled text-ui-ondisabled border-ui-disabled;
			@apply cursor-not-allowed pointer-events-none;
		}
	}

	.button--small,
	.content-card--header .button,
	.svws-ui-tbody .button,
	.router-tab-bar--subnav .button,
	.router-tab-bar--subnav .radio--label {
		@apply text-sm font-bold;
	}

	.button--small,
	.svws-ui-tbody .button,
	.content-card--header .button,
	.router-tab-bar--subnav .button {
		padding: 0.3rem 0.65rem;
	}

	.svws-ui-tbody .button {
		@apply -my-2 h-full self-center rounded;
		padding: 0.1rem 0.5rem;

		&.button--transparent {
			@apply px-1.5;
		}
	}

	.svws-ui-tbody .button--icon.button--small {
		@apply w-5 h-5 -m-0.5;
		@apply p-0 !important;
	}

	.button--big {
		padding-top: 0.64em;
		padding-bottom: 0.64em;
	}

	.button--badge {
		@apply bg-ui-brand text-ui-onbrand;
		@apply absolute top-0 left-[100%];
		@apply font-bold rounded-full shadow;
		@apply flex items-center justify-center;
		@apply pointer-events-none;
		@apply -mt-3 -ml-3;
		@apply px-1.5;
		@apply h-5;
		font-size: 0.8rem;

		.svws-ui-tfoot .button &,
		.content-card--header .button &,
		.router-tab-bar--subnav .button & {
			@apply -mt-0 h-4 -ml-1.5;
			font-size: 0.75rem;
		}

		.router-tab-bar--subnav .button & {
			@apply -ml-3;
		}
	}

</style>
