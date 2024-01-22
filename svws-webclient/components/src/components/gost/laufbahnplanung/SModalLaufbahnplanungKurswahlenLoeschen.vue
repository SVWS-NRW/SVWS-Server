<template>
	<div>
		<svws-ui-button @click="toggle_modal" size="small" type="transparent" class="hover--danger">
			<i-ri-delete-bin-line />
			<template v-if="schuelerAnsicht && hatFesteWahlen">Fachwahlen löschen</template>
			<template v-else-if="schuelerAnsicht">Fachwahlen zurücksetzen</template>
			<template v-else-if="gostJahrgangsdaten.abiturjahr === -1">Zurücksetzen aus Standardwerte</template>
			<template v-else>Zurücksetzen auf allg. Vorlage</template>
		</svws-ui-button>
		<svws-ui-modal :show="showModal" size="small" type="danger">
			<template #modalTitle>
				<template v-if="schuelerAnsicht && hatFesteWahlen">Nicht feste Fachwahlen löschen</template>
				<template v-else-if="schuelerAnsicht">Alle Kurswahlen zurücksetzen</template>
				<template v-else-if="gostJahrgangsdaten.abiturjahr === -1">Zurücksetzen aus Standardwerte</template>
				<template v-else>Zurücksetzen der Vorlage auf die allgemeine Vorlage</template>
			</template>
			<template #modalDescription>
				<div class="flex gap-1 mb-2">
					<template v-if="schuelerAnsicht && hatFesteWahlen">Sollen die nicht festen Fachwahlen gelöscht werden?</template>
					<template v-else-if="schuelerAnsicht">Soll die Laufbahnplanung auf die jahrgangs-spezifische Vorlage zurückgesetzt werden?</template>
					<div v-else-if="gostJahrgangsdaten.abiturjahr === -1">Soll die Vorlage auf die Standardwerte zurückgesetzt werden?</div>
					<div v-else>Soll diese jahrgangs-spezifisch Vorlage auf die allgemeine Vorlage zurückgesetzt werden?</div>
				</div>
			</template>
			<template #modalActions>
				<svws-ui-button @click="toggle_modal" type="secondary">Abbrechen</svws-ui-button>
				<svws-ui-button @click="reset_fachwahlen" type="danger">Ja</svws-ui-button>
			</template>
		</svws-ui-modal>
	</div>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import { type GostJahrgangsdaten } from '@core';

	const props = withDefaults(defineProps<{
		gostJahrgangsdaten: GostJahrgangsdaten;
		resetFachwahlen: () => Promise<void>;
		schuelerAnsicht?: boolean;
	}>(), {
		schuelerAnsicht: false,
	});

	const hatFesteWahlen = computed<boolean>(() => {
		const jg = props.gostJahrgangsdaten.jahrgang;
		return (jg === "Q1") || (jg === "Q2") || ((jg === "EF") && (props.gostJahrgangsdaten.istBlockungFestgelegt[0]));
	});

	const _showModal = ref<boolean>(false);
	const showModal = () => _showModal;

	function toggle_modal() {
		_showModal.value = !_showModal.value;
	}

	async function reset_fachwahlen() {
		_showModal.value = false;
		await props.resetFachwahlen();
	}

</script>
