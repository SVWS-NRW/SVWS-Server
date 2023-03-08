<template>
	<!--	<svws-ui-button type="icon" class="notifications&#45;&#45;trigger" @click="toggleModal">
		<i-ri-notification-3-line/>
	</svws-ui-button>-->
	<Dialog class="notifications--wrapper" :initial-focus="null" :open="true">
		<slot />
	</Dialog>
</template>

<script setup lang='ts'>
	import {Dialog} from "@headlessui/vue";
	import {ref} from "vue";

	const isOpen = ref(false);

	function closeModal() {
		isOpen.value = false;
	}

	function openModal() {
		isOpen.value = true;
	}

	function toggleModal() {
		isOpen.value = !isOpen.value;
	}

	defineExpose({
		openModal,
		closeModal,
		toggleModal,
		isOpen
	});
</script>

<style lang="postcss">
.notifications--wrapper {
	@apply fixed top-0 right-0 z-50;
	@apply w-[32rem] max-w-[75vw] h-full;
	@apply p-6 gap-1;
	@apply flex flex-col justify-start;
	@apply pointer-events-none;
	@apply overflow-y-auto;
	-ms-overflow-style: none;
	scrollbar-width: none;

	&::-webkit-scrollbar {
		display: none;
	}

	.notifications__open & {
		@apply bg-white shadow-xl;
	}
}

.notifications--trigger.button {
	@apply pointer-events-auto z-[51];
	@apply fixed top-4 right-4;
	border-top-right-radius: theme('borderRadius.xl') !important;
}
</style>
