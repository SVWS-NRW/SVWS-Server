<script setup lang='ts'>
const {
	modelValue = false,
	statistics = false,
	headless = false,
} = defineProps<{
	modelValue?: boolean;
	statistics?: boolean;
	headless?: boolean;
}>();

const emit = defineEmits<{
	(e: 'update:modelValue', value: boolean): void;
}>();

const value = computed({
	get() {
		return modelValue;
	},
	set(value: boolean) {
		emit('update:modelValue', value);
	}
});
</script>

<template>
	<label
class="toggle" :class="{
		'toggle--statistics': statistics,
		'toggle--headless': headless
	}">
		<input v-model="value" class="toggle--control" type="checkbox" />
		<span class="toggle--indicator" />
		<span v-if="$slots.default || statistics" class="toggle--label">
			<slot />
			<i-ri-bar-chart-fill v-if="statistics" class="ml-2" />
		</span>
	</label>
</template>

<style>
.toggle {
	@apply cursor-pointer;
	@apply inline-flex;
	@apply select-none;
	@apply text-input;
}

.toggle--control {
	@apply hidden;
}

.toggle--indicator {
	@apply bg-white;
	@apply rounded-full border-2 border-black;
	@apply relative;
	@apply h-5 w-9;
}

.toggle--statistics {
	@apply text-purple;
}

.toggle--statistics .toggle--indicator {
	@apply border-purple;
}

.toggle--indicator:before {
	@apply absolute;
	@apply bg-black;
	@apply block;
	@apply rounded-full;
	@apply h-3 w-3;

	content: "";
	left: 0.125rem;
	top: 0.125rem;
}

.toggle--statistics .toggle--indicator:before {
	@apply bg-purple;
}

.toggle input:checked+.toggle--indicator {
	@apply bg-primary;
	@apply border-primary;
}

.toggle.toggle--statistics input:checked+.toggle--indicator {
	@apply bg-purple;
	@apply border-purple;
}

.toggle input:checked+.toggle--indicator:before {
	@apply bg-white;

	left: auto;
	right: 0.125rem;
}

.toggle--label {
	@apply flex items-center ml-3;
}

.toggle--headless {
	@apply h-full w-full;

	.toggle--indicator {
		@apply w-full rounded-none border-0;

		&:before {
			@apply relative bg-transparent w-auto h-auto text-center font-bold;
			content: "x";
		}
	}

	input:checked+.toggle--indicator {
		@apply text-primary bg-transparent;

		&:before {
			@apply bg-transparent left-auto right-auto;
			content: "✔";
		}
	}
}
</style>
