<template>
	<TransitionRoot appear :show>
		<Dialog class="modal--wrapper" @close="autoCloseModal" @keyup.esc="autoCloseModal">
			<div class="modal--pageWrapper"
				:class="{ 'modal--pageWrapper--help': size === 'help', }">
				<TransitionChild v-if="size !== 'help'" as="div"
					enter="ease-out duration-200" enter-from="opacity-0" enter-to="opacity-100"
					leave="ease-in duration-100" leave-from="opacity-100" leave-to="opacity-0">
					<div class="modal--overlay" @click="autoCloseModal" />
				</TransitionChild>
				<TransitionChild as="div"
					enter="ease-out duration-200" enter-from="opacity-0 scale-95" enter-to="opacity-100 scale-100"
					leave="ease-in duration-100" leave-from="opacity-100 scale-100" leave-to="opacity-0 scale-95"
					class="modal"
					:class="{
						'modal--sm': size === 'small',
						'modal--md': size === 'medium',
						'modal--lg': size === 'big',
						'modal--help': size === 'help',
						'modal--danger': type === 'danger',
					}">
					<div class="modal--titlebar">
						<DialogTitle class="modal--title inline-flex items-center gap-1">
							<span class="icon i-ri-alert-fill icon-ui-danger inline-block" v-if="type === 'danger'" />
							<slot name="modalTitle" />
						</DialogTitle>
						<svws-ui-tooltip v-if="$slots.hilfe" autosize>
							<svws-ui-button type="secondary" @click.stop>
								<span class="icon i-ri-question-line" />
								<span>Hilfe</span>
							</svws-ui-button>
							<template #content><slot name="hilfe" /></template>
						</svws-ui-tooltip>
						<svws-ui-button v-if="closeInTitle" type="icon" @click="closeModal">
							<span class="icon i-ri-close-line" />
						</svws-ui-button>
					</div>
					<div class="modal--content-wrapper" :class="{ 'modal--content-noscroll': noScroll }">
						<DialogDescription v-if="$slots.modalDescription" class="modal--description">
							<slot name="modalDescription" />
						</DialogDescription>

						<div v-if="$slots.modalContent" class="modal--content" :class="{ 'modal--content-noscroll': noScroll }">
							<slot name="modalContent" />
						</div>

						<div class="modal--actions">
							<slot name="modalActions" />
						</div>

						<div class="modal--logs" v-if="$slots.modalLogs">
							<slot name="modalLogs" />
						</div>
					</div>
				</TransitionChild>
			</div>
		</Dialog>
	</TransitionRoot>
</template>

<script setup lang='ts'>

	import { Dialog, DialogTitle, DialogDescription, TransitionRoot, TransitionChild } from "@headlessui/vue";
	import type { Size } from "../../types";

	const props = withDefaults(defineProps<{
		show: boolean;
		size?: Extract<Size, 'small' | 'medium' | 'big'> | 'help';
		type?: 'default' | 'danger';
		autoClose?: boolean;
		closeInTitle?: boolean;
		noScroll?: boolean;
	}>(), {
		size: 'small',
		type: 'default',
		autoClose: true,
		closeInTitle: true,
		noScroll: false,
	});

	defineSlots();

	const emit = defineEmits<{
		"update:show": [show: boolean];
	}>();

	function autoCloseModal() {
		if (props.autoClose)
			closeModal();
	}

	function closeModal() {
		emit('update:show', false);
	}

</script>
