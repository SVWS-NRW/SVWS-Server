<template>
	<slot :open-modal />
	<svws-ui-modal v-model:show="show" :close-in-title="false">
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

	const show = ref<boolean>(false);

	function leave() {
		emit('callback', true);
		closeModal();
	}

	function cancel() {
		emit('callback', false);
		closeModal();
	}

	function openModal() {
		show.value = true;
	}
	const closeModal = () => {
		show.value = false;
	};

</script>
