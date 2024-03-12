<template>
	<slot :open-modal="openModal" />
	<svws-ui-modal :show="showModal" size="small" class="hidden">
		<template #modalTitle>Blockungsergebnis synchronisieren</template>
		<template #modalDescription>
			<div class="prose">
				Soll das Ergebnis in {{ blockungsname }} synchronisiert werden?
				<br><b class="text-black">Warnung:</b> Es werden die Kurse und die Leistungsdaten des aktuellen Schuljahresabschnitts mit den Daten dieses Blockungsergebnisses synchronisiert.
				<br>Dies bedeutet, dass:
				<ul>
					<li>gegebenenfalls neue Kurse angelegt werden</li>
					<li><b class="text-black">keine</b> leeren Kurse entfernt</li>
					<li>die Kurs-Schüler-Zuordnungen bei <b class="text-black">vorhanden</b> Leistungsdaten zu einem Fach auf die Zuordnungen der Blockungsdaten angepasst werden</li>
				</ul>
				Weitere Anpassungen, wie das Hinzufügen von Fächern in den Leistungsdaten der Schüler oder das Entfernen von Kursen aus der Kursliste müssen bei Bedarf später manuell erfolgen.
			</div>
		</template>
		<template #modalActions>
			<svws-ui-button type="secondary" @click="showModal().value = false">Abbrechen</svws-ui-button>
			<svws-ui-button type="primary" @click="activate_ergebnis">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import type { GostBlockungsdatenManager } from '@core';
	import { ref } from 'vue';

	const props = defineProps<{
		blockungsname: string;
		getDatenmanager: () => GostBlockungsdatenManager;
		ergebnisSynchronisieren: () => Promise<void>;
	}>();

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	async function activate_ergebnis() {
		showModal().value = false;
		await props.ergebnisSynchronisieren();
	}

	const openModal = () => {
		showModal().value = true;
	}

</script>
