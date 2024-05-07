<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small">
		<template #modalTitle>Information zum Datenschutz</template>
		<template #modalDescription>
			<div class="space-y-2 text-left">
				<p>
					Die automatische Verarbeitung von personenbezogenen Daten von Schülerinnen und Schülern und Erziehungsberechtigten muss zur
					Erfüllung der den Schulen durch Rechtsvorschrift übertragenen Aufgaben erforderlich sein bzw. die
					Betroffene bzw. der Betroffene hat im Einzelfall schriftlich in die Datenverarbeitung eingewilligt. Dies ist bei der Wahl neuer Kategorien zu
					berücksichtigen.
				</p>
				<p>Die Art der gespeicherten Daten (Kategorien) ist dem zuständigen Behördlichen Datenschutzbeauftragten zur Aufnahme in das Verfahrensverzeichnis anzuzeigen.</p>
				<p>Personenbezogene Daten sind zu löschen, wenn ihre Kenntnis für die Aufgabenerfüllung nicht mehr erforderlich ist. § 9 Abs. 2 Satz 2 VO-DV I</p>
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button @click="closeModal">Ok</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { ref } from 'vue';

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function closeModal() {
		showModal().value = false;
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>