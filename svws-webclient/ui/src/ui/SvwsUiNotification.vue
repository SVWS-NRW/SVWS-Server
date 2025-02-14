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
					<div v-if="icon || type" class="notification--icon">
						<span class="icon i-ri-lock-2-line" v-if="icon === 'login'" />
						<span class="icon i-ri-alert-fill" v-else-if="icon === 'error' || type === 'error'" />
						<span class="icon i-ri-bug-fill" v-else-if="icon === 'bug' || type === 'bug'" />
						<span class="icon i-ri-check-line" v-else-if="icon === 'success' || type === 'success'" />
						<span class="icon i-ri-information-line" v-else-if="icon === 'info' || type === 'info'" />
						<span class="icon i-ri-error-warning-line" v-else-if="icon === 'warning' || type === 'warning'" />
					</div>
					<slot name="header" />
				</div>
				<div class="notification--text">
					<slot />
				</div>
				<div class="mt-4 flex flex-wrap gap-1 w-full select-none" v-if="$slots.stack || type === 'bug'">
					<div v-if="type === 'bug'" class="notification--send-button">
						Fehler melden
						<span class="icon i-ri-send-plane-fill" />
					</div>
					<div @click="copyToClipboard" v-if="toCopy !== undefined" class="notification--copy-button">
						<span>
							<span v-if="copied === null || copied === false">Meldung kopieren</span>
							<span v-else>Meldung kopiert</span>
						</span>
						<span class="icon i-ri-file-copy-line" v-if="copied === null" />
						<span class="icon i-ri-error-warning-fill" v-else-if="copied === false" />
						<span class="icon i-ri-check-line" v-else />
					</div>
					<div @click="toggleStackOpen" v-if="$slots.stack" class="notification--details-button">
						Details
						<span class="icon i-ri-arrow-up-s-line" v-if="stackOpen" />
						<span class="icon i-ri-arrow-down-s-line" v-else />
					</div>
				</div>
				<div v-if="copied === false" class="p-2 bg-ui border rounded-md text-ui-danger mt-2">{{ "Es gab einen Fehler beim Kopieren in die Zwischenablage. Ist die Zwischenablage gesperrt?" }}</div>
				<div class="notification--stack" v-if="$slots.stack && stackOpen">
					<slot name="stack" />
				</div>
			</div>
			<div class="absolute top-0 right-0 p-1">
				<div @click="close" tabindex="-1" class="notification--close-button cursor-pointer opacity-70 hover:opacity-100">
					<span class="icon i-ri-close-line" />
				</div>
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
