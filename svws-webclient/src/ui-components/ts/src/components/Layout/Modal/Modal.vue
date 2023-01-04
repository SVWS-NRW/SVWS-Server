<script setup lang='ts'>
//TODO: props weiterleiten über :open. Das mit den refs ist Blödsinn
import { Dialog, DialogTitle, DialogDescription } from "@headlessui/vue";
import { Size } from "~/types";

const { size = 'small' } = defineProps<{ size: Extract<Size, 'small' | 'medium' | 'big'>; }>();

const isOpen = ref(false);

function closeModal() {
	isOpen.value = false;
}

function openModal() {
	isOpen.value = true;
}

defineExpose({
	openModal,
	closeModal,
	isOpen
});
</script>

<template>
	<Dialog :open="isOpen" class="modal--wrapper" @close="closeModal">
		<div class="modal--pageWrapper">
			<Overlay @click="closeModal" />
			<div
class="modal" :class="{
				'modal--sm': size === 'small',
				'modal--md': size === 'medium',
				'modal--lg': size === 'big'
			}">
				<div class="modal--titlebar">
					<DialogTitle class="modal--title">
						<slot name="modalTitle" />
					</DialogTitle>
					<Button type="icon" @click="closeModal">
						<Icon class="modal--closeIcon">
							<i-ri-close-line />
						</Icon>
					</Button>
				</div>
				<DialogDescription v-if="$slots.modalDescription" class="modal--description">
					<slot name="modalDescription" />
				</DialogDescription>

				<div v-if="$slots.modalContent" class="modal--content">
					<slot name="modalContent" />
				</div>

				<div class="modal--actions">
					<slot name="modalActions" />
				</div>
			</div>
		</div>
	</Dialog>
</template>

<style>
.modal--pageWrapper {
	@apply flex items-center justify-center;
	@apply min-h-screen;
	@apply p-8;
}

.modal--titlebar {
	@apply flex flex-row items-center justify-between;
	@apply w-full;
}

.modal {
	@apply bg-white;
	@apply flex flex-col items-center;
	@apply mx-auto;
	@apply relative z-50;
	@apply rounded-lg;
	@apply shadow-xl shadow-dark-20;
}

.modal--sm {
	@apply w-full max-w-modal-sm;
}

.modal--md {
	@apply w-full max-w-modal-md;
}

.modal--lg {
	@apply w-full max-w-modal-lg;
}

.modal--titlebar {
	@apply p-2;
}

.modal--title {
	@apply flex-grow px-2;
	@apply text-sm-bold uppercase;
}

.modal--description {
	@apply p-4 text-base;
}

.modal--wrapper {
	@apply fixed inset-0 z-50;
	@apply overflow-y-auto;
}

.modal--content {
	@apply w-full text-base p-4 text-center;
}

.modal--actions {
	@apply flex items-center justify-center p-4;
	@apply space-x-2;
}

.modal--description + .modal--content {
	@apply pt-0;
}

.modal--description + .modal--actions,
.modal--content + .modal--actions {
	@apply pt-2;
}
</style>
