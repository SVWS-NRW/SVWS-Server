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

	defineSlots();
	const emit = defineEmits<{
		'click': [value: MouseEvent];
	}>();


</script>
