<script lang="ts" setup>
	import type { ButtonType } from '../../types';
	import { Size } from '../../types';

	const props = withDefaults(defineProps<{
		type?: ButtonType;
		disabled?: boolean;
		dropdownAction?: boolean;
		// Typfehler beim Extract...?
		//Extract<Size, 'small' | 'normal' | 'big'>;
		size?: 'small' | 'normal' | 'big'
	}>(),{
		type: 'primary',
		disabled: false,
		dropdownAction: false,
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
		'button--dropdown-action': dropdownAction === true,
	}" :disabled="disabled" @click="onClick">
		<slot v-if="type !== 'trash'" />
		<svws-ui-icon v-if="type === 'trash'" class="button--trash-icon">
			<i-ri-delete-bin-line class="icon--line" />
			<i-ri-delete-bin-fill class="icon--fill" />
		</svws-ui-icon>
	</button>
</template>

<style lang="postcss">
.button,
.cv-wrapper .cv-header button {
	@apply rounded-md border;
	@apply select-none;
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
	@apply bg-primary text-white border-primary;

	&:focus-visible {
		@apply ring-primary ring-opacity-50;
	}
}

.button--secondary {
	@apply bg-transparent text-black border-dark;

	&:hover {
		@apply border-primary text-primary;
	}

	&:focus-visible {
		@apply ring-primary ring-opacity-25 border-primary;
	}

	&:active {
		@apply bg-primary bg-opacity-5 brightness-100;
	}
}

.button--transparent,
.cv-wrapper .cv-header button {
	@apply bg-transparent border-transparent;

	&:hover {
		@apply bg-light brightness-95;
	}

	&:focus-visible {
		@apply bg-light ring-dark-20 ring-opacity-75;
	}
}

.button--danger {
	@apply bg-transparent text-error border-error;

	&:hover,
	&:focus {
		@apply bg-error text-white;
	}

	&:focus-visible {
		@apply ring-error ring-opacity-50;
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
		@apply ring-error ring-opacity-25;
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
		padding: 0.3em !important;

		svg {
			width: 1.2em;
			height: 1.2em;
		}
	}
}

.button--icon {
	@apply p-2 justify-center border-0 items-center;
	@apply w-8 h-8;

	svg {
		width: 1.2rem;
		height: 1.2rem;
	}

	&:hover, &:focus {
		@apply bg-dark-20 bg-opacity-50 rounded;
	}

	&:focus-visible {
		@apply ring-dark ring-opacity-50;
	}
}

.button:disabled,
.cv-wrapper .cv-header button:disabled {
	&,
	&:hover,
	&:focus,
	&:focus-visible {
		@apply bg-black bg-opacity-25 border-black border-opacity-50 text-black;
		@apply opacity-20;
		@apply cursor-not-allowed pointer-events-none;
	}
}

.button--dropdown-action {
	@apply relative z-20;
	@apply rounded-r-none;
	padding-right: 0.5em;
}

.hover--danger:hover {
	@apply text-error bg-error/10;
}

.button--small,
.data-table__tbody .button,
.router-tab-bar--subnav .button,
.router-tab-bar--subnav .radio--label,
.cv-wrapper .cv-header button {
	@apply text-sm font-medium;
}

.button--small,
.data-table__tbody .button,
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
</style>
