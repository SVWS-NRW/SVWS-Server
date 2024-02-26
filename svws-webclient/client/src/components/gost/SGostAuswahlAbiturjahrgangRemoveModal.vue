<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" type="danger">
		<template #modalTitle>Abiturjahrgang {{ gostJahrgang.abiturjahr }} löschen?</template>
		<template #modalDescription>
			<div class="text-left">
				Soll der Abiturjahrgang {{ gostJahrgang.abiturjahr }} mit seinen Jahrgangs-bezogenen Defintionen entfernt werden?
				<ul>
					<li>Bereits erstellte Kursblockungen gehen verloren!</li>
					<li>Bereits erstellte Klausurplanungen gehen verloren!</li>
					<li>
						Bereits getätigten Fachwahlen der Schüler bleiben erhalten bis für diese Schüler erneut ein Abiturjahrgang angelegt wird. Da dieser
						ggf. andere erlaubte Fächer beinhaltet, können diese Fachwahlen dann Laufbahnfehler beinhalten.
					</li>
				</ul>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="danger" @click="removeAbiturjahrgang">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';
	import type { GostJahrgang } from '@core';

	const props = defineProps<{
		gostJahrgang: GostJahrgang;
		removeAbiturjahrgang: () => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	const openModal = () => {
		showModal().value = true;
	}

</script>