<script lang="ts" setup>
type ButtonType =
	"primary" |
	"secondary" |
	"danger" |
	"icon" |
	"transparent";
type ButtonSize =
	'small' |
	'normal';

const {
	type = 'primary',
	disabled = false,
	dropdownAction = false,
	size = 'normal'
} = defineProps<{
	type?: ButtonType;
	disabled?: boolean;
	dropdownAction?: boolean;
	size?: ButtonSize;
}>();

const emit = defineEmits<{
	(e: 'click', event: MouseEvent): void;
}>();

function onClick(event: MouseEvent) {
	if (!disabled) {
		emit("click", event);
	}
}
</script>

<template>
	<button
class="button" :class="{
		'button--primary': type === 'primary',
		'button--secondary': type === 'secondary',
		'button--danger': type === 'danger',
		'button--transparent': type === 'transparent',
		'button--icon': type === 'icon',
		'button--dropdown-action': dropdownAction === true,
		'button--normal': size === 'normal',
		'button--small': size === 'small',
	}" :disabled="disabled" @click="onClick">
		<slot />
	</button>
</template>

<style>
.button {
	@apply rounded-full border-2;
	@apply select-none;
	@apply text-button font-bold;
	@apply flex items-center;
}

.button:focus {
	@apply outline-none ring;
}

.button--primary {
	@apply bg-primary;
	@apply border-primary;
	@apply text-white;
}

.button--primary:hover {
	@apply bg-light text-primary;
}

.button--primary:focus {
	@apply ring-primary ring-opacity-50;
}

.button--secondary {
	@apply bg-transparent;
	@apply border-black;
	@apply text-black;
}

.button--secondary:hover {
	@apply bg-black text-white;
}

.button--secondary:focus {
	@apply ring-primary ring-opacity-50;
}

.button--danger {
	@apply bg-transparent;
	@apply border-error;
	@apply text-error;
}

.button--danger:hover {
	@apply bg-error text-white;
}

.button--danger:focus {
	@apply bg-error;
	@apply ring-error ring-opacity-50;
	@apply text-white;
}

.button--transparent {
	@apply bg-transparent;
	@apply border-transparent;
	@apply rounded;
}

.button--transparent:hover {
	@apply bg-dark-20 bg-opacity-50;
}

.button--transparent:focus {
	@apply ring-primary;
}

.button--normal {
	@apply px-5 py-2;
}

.button:disabled {
	@apply bg-disabled;
	@apply border-disabled-medium;
	@apply cursor-not-allowed;
	@apply text-disabled-dark;
}

.button--dropdown-action {
	@apply pr-3;
	@apply relative z-20;
	@apply rounded-r-none;
}

.button--transparent,
.button--icon {
	@apply rounded border-0 justify-center items-center;
	@apply p-2;

	&:hover, &:focus {
		@apply bg-dark-20 bg-opacity-50 rounded;
	}

	&:focus {
		@apply ring-dark ring-opacity-50;
	}
}

.button--icon {
	@apply w-8 h-8;

	svg {
		width: 1.2rem;
		height: 1.2rem;
	}
}

.hover--danger:hover {
	@apply text-error;
}

.button--small {
	border-width: thin;
	font-size: .833rem;
	padding: .1em .5em .15em;
}
</style>
