<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="() => ref(showModal)" :close-in-title="false">
		<template #modalTitle>Seite verlassen</template>
		<template #modalContent>
			Beim Verlassen dieser Seite gehen die Daten der neuen Klasse verloren.
			<span class="mt-2">Wollen Sie wirklich die Seite verlassen?</span>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="cancel">Nein</svws-ui-button>
			<svws-ui-button @click="leave">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from "vue";

	const emit = defineEmits(['callback']);

	const showModal = ref<boolean>(false);

	const leave = () => {
		emit('callback', true);
		closeModal();
	}

	const cancel = () => {
		emit('callback', false);
		closeModal();
	}

	const openModal = () => {
		showModal.value = true;
	}
	const closeModal = () => {
		showModal.value = false;
	}

</script>
