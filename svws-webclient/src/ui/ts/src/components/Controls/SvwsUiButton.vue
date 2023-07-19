<script lang="ts" setup>
	import type { ButtonType } from '../../types';
	import { Size } from '../../types';

	const props = withDefaults(defineProps<{
		type?: ButtonType;
		disabled?: boolean;
		// Typfehler beim Extract...?
		//Extract<Size, 'small' | 'normal' | 'big'>;
		size?: 'small' | 'normal' | 'big'
	}>(),{
		type: 'primary',
		disabled: false,
		size: 'normal'
	});

	const emit = defineEmits<{
		(e: 'click', event: MouseEvent): void;
	}>();

	function onClick(event: MouseEvent) {
		if (!props.disabled) {
			emit("click", event);
		}
	}
</script>

<template>
	<button class="button" :class="{
		'button--primary': type === 'primary' || !type,
		'button--secondary': type === 'secondary',
		'button--danger': type === 'error' || type === 'danger',
		'button--transparent': type === 'transparent',
		'button--icon': type === 'icon',
		'button--trash': type === 'trash',
		'button--small': size === 'small',
		'button--big': size === 'big',
	}" :disabled="disabled" @click="onClick">
		<slot v-if="type !== 'trash'" />
		<svws-ui-icon v-if="type === 'trash'" class="button--trash-icon">
			<i-ri-delete-bin-line class="icon--line" />
			<i-ri-delete-bin-fill class="icon--fill" />
		</svws-ui-icon>
		<span v-if="$slots.badge" class="button--badge">
			<slot name="badge" />
		</span>
	</button>
</template>

<style lang="postcss">
.button,
.cv-wrapper .cv-header button {
	@apply rounded-md border;
	@apply select-none relative;
	@apply text-button font-bold;
	@apply flex items-center;
	gap: 0.25em;
	padding: 0.45em 0.75em;

	svg {
		margin-top: -0.1em;
		margin-bottom: -0.1em;
	}

	&:hover {
		@apply brightness-110;
	}

	&:focus {
		@apply outline-none;
	}

	&:focus-visible {
		@apply ring;
	}

	&:active {
		@apply ring-0 brightness-100;
	}
}

.cv-wrapper .cv-header button {
	@apply inline-flex;
}

.button--primary {
	@apply bg-svws text-white border-svws;

	.page--statistik & {
		@apply bg-violet-500 border-violet-500;
	}

	&:focus-visible {
		@apply ring-svws/50;

		.page--statistik & {
			@apply ring-violet-500/50;
		}
	}
}

.button--secondary {
	@apply bg-transparent dark:bg-transparent text-black dark:text-white border-black/90 dark:border-white/90;

	.notification--error & {
		@apply text-white dark:text-black border-white/25 dark:border-black/25;
	}

	&:hover {
		@apply border-svws text-svws;

		.page--statistik & {
			@apply border-violet-500 text-violet-500;
		}

		.notification--error & {
			@apply border-white dark:border-black text-white dark:text-black brightness-100;
		}
	}

	&:focus-visible {
		@apply ring-svws/25 border-svws;

		.page--statistik & {
			@apply ring-violet-500/25 border-violet-500;
		}

		.notification--error & {
			@apply ring-white/25 dark:ring-black/25 border-white dark:border-black;
		}
	}

	&:active {
		@apply bg-svws/5 brightness-100;

		.notification--error & {
			@apply bg-white/10 dark:bg-black/10;
		}
	}
}

.button--transparent,
.cv-wrapper .cv-header button {
	@apply bg-transparent border-transparent dark:bg-transparent dark:border-transparent;

	&:hover {
		@apply bg-black/10 dark:bg-white/5 brightness-95;
	}

	&:focus-visible {
		@apply bg-light dark:bg-white/5 ring-black/25 dark:ring-white/25;
	}
}

.button--danger {
	@apply bg-transparent dark:bg-transparent text-error border-error;

	&:hover,
	&:focus {
		@apply bg-error text-white;
	}

	&:focus-visible {
		@apply ring-error/50;
	}
}

.button--trash {
	@apply bg-transparent rounded relative;
	@apply py-0 px-2;
	@apply text-error;
	border: 0 !important;
	padding: 0.2em !important;
	width: 1.6em;
	height: 1.6em;

	.icon--fill {
		@apply hidden;
	}

	&:hover {
		@apply bg-error text-white;

		.icon--line {
			@apply hidden;
		}

		.icon--fill {
			@apply inline-block;
		}
	}

	&:focus {
		@apply bg-error;
		@apply text-white;
	}

	&:focus-visible {
		@apply ring-error/25;
	}
}

.button--trash,
.button--icon {
	&.button--small {
		@apply text-sm font-medium h-6 w-6;
		padding: 0.3em !important;

		svg {
			width: 1.2em;
			height: 1.2em;
		}
	}

	.data-table__tfoot-actions &.button {
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

	&:hover, &:focus {
		@apply bg-black/10 dark:bg-white/5 rounded;
	}

	&:focus-visible {
		@apply ring-black/25 dark:ring-white/25;
	}
}

.button:disabled,
.cv-wrapper .cv-header button:disabled {
	&,
	&:hover,
	&:focus,
	&:focus-visible {
		@apply bg-black/25 border-black/50 text-black dark:bg-white/25 dark:border-white/50 dark:text-white;
		@apply opacity-25;
		@apply cursor-not-allowed pointer-events-none;
	}
}

.hover--danger:hover {
	@apply text-error bg-error/10;
}

.button--small,
.content-card--header .button,
.data-table__tbody .button,
.router-tab-bar--subnav .button,
.router-tab-bar--subnav .radio--label,
.cv-wrapper .cv-header button {
	@apply text-sm font-medium;
}

.button--small,
.data-table__tbody .button,
.content-card--header .button,
.router-tab-bar--subnav .button,
.cv-wrapper .cv-header button {
	padding: 0.3em 0.75em;
}

.data-table__tbody .button {
	@apply -my-2 h-full self-center rounded;
	padding: 0.1em 0.5em;
}

.data-table__tbody .button--icon.button--small {
	@apply w-5 h-5 -m-0.5;
	@apply p-0 !important;
}

.button--big {
	padding-top: 0.64em;
	padding-bottom: 0.64em;
}

.button--badge {
	@apply absolute top-0 left-[100%];
	@apply font-bold text-white;
	@apply bg-error rounded-full shadow;
	@apply flex items-center justify-center;
	@apply pointer-events-none;
	@apply -mt-2 -ml-3;
	@apply px-1.5;
	@apply h-5;
	font-size: 0.8rem;

	.data-table__tbody .button &,
	.content-card--header .button &,
	.router-tab-bar--subnav .button &,
	.cv-wrapper .cv-header button & {
		@apply -mt-1 h-4 -ml-1.5;
		font-size: 0.7rem;
	}

	.router-tab-bar--subnav .button & {
		@apply -ml-3;
	}
}
</style>
