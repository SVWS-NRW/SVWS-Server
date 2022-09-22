<script setup lang='ts'>
//TODO: props weiterleiten über :open. Das mit den refs ist Blödsinn
import { Dialog, DialogTitle, DialogDescription } from "@headlessui/vue";

type Size = "small" | "medium" | "large";

const {
	size = 'small'
} = defineProps<{
	size: Size;
}>();

const isOpen = ref(false);

function closeModal() {
	isOpen.value = false;
}

function openModal() {
	isOpen.value = true;
}

defineExpose({
	openModal,
	closeModal
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
				'modal--lg': size === 'large'
			}">
				<div class="modal--titlebar">
					<DialogTitle class="modal--title">
						<slot name="modalTitle" />
					</DialogTitle>
					<Button class="modal--closeButton" @click="closeModal">
						<Icon class="modal--closeIcon">
							<i-ri-close-line />
						</Icon>
					</Button>
				</div>
				<DialogDescription class="modal--description">
					<slot name="modalDescription" />
				</DialogDescription>

				<div class="modal-content">
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
.modal--closeButton {
	@apply text-dark;
	@apply mr-3;
	@apply flex items-center justify-center;
	@apply rounded-full;
	@apply h-8 w-8;
}

.modal--closeButton:hover {
	@apply bg-light;
}

.modal--closeButton:focus {
	@apply bg-light;
	@apply outline-none ring ring-primary ring-opacity-50;
}

.modal--closeIcon {
	@apply inline-block;
	@apply ml-0;
}

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
	@apply px-6 py-4;
	@apply relative z-50;
	@apply rounded-md;
	@apply shadow-lg;
	@apply space-y-8;
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

.modal--title {
	@apply flex-grow;
	@apply text-caption uppercase text-black;
}

.modal--description {
	@apply text-center font-bold text-black;
}

.modal--wrapper {
	@apply fixed inset-0 z-50;
	@apply overflow-y-auto;
}

.modal--actions {
	@apply flex items-center justify-center;
	@apply space-x-4;
}

.modal-content {
	@apply w-full;
}
</style>
