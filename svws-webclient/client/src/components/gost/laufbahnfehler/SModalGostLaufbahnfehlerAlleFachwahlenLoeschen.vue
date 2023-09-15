<template>
	<div>
		<svws-ui-button @click="toggle_modal" size="small" type="transparent" class="hover--danger"><i-ri-delete-bin-line />Alle Fachwahlen zurücksetzen</svws-ui-button>
		<svws-ui-modal :show="showModal" size="medium" type="danger">
			<template #modalTitle>Alle Fachwahlen aller Schüler im Abiturjahrgang zurücksetzen</template>
			<template #modalDescription>
				<div class="flex gap-1 mb-2">
					Sollen für alle Schüler des Abiturjahrgangs die Fachwahlen der noch in Planung befindlichen Halbjahre gelöscht werden?
				</div>
			</template>
			<template #modalActions>
				<svws-ui-button @click="toggle_modal" type="secondary">Abbrechen</svws-ui-button>
				<svws-ui-button @click="reset_fachwahlen" type="danger">Ja, löschen</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const emit = defineEmits<{
		(e: 'delete'): void;
	}>()

	function toggle_modal() {
		showModal().value = !showModal().value;
	}

	function reset_fachwahlen() {
		showModal().value = false;
		emit('delete');
	}

</script>
