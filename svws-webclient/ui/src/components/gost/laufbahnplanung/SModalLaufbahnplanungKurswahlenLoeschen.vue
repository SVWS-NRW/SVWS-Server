<template>
	<svws-ui-button v-if="!keineVorlage" @click="toggle_modal(false)" size="small" type="transparent" class="hover--danger">
		<span class="icon-sm i-ri-delete-bin-line" />{{ buttonText }}
	</svws-ui-button>
	<svws-ui-button v-if="keineVorlage || (schuelerAnsicht && !hatFesteWahlen)" @click="toggle_modal(true)" size="small" type="transparent" class="hover--danger">
		<span class="icon-sm i-ri-delete-bin-line" />Fachwahlen löschen
	</svws-ui-button>
	<svws-ui-modal v-model:show="show" size="small" type="danger">
		<template #modalTitle>
			{{ modalTitle }}
		</template>
		<template #modalDescription>
			{{ modalDescription }}
		</template>
		<template #modalActions>
			<svws-ui-button @click="toggle_modal" type="secondary">Abbrechen</svws-ui-button>
			<svws-ui-button @click="reset_fachwahlen" type="danger">Ja</svws-ui-button>
		</template>
	</svws-ui-modal>
</template>

<script setup lang="ts">

	import { computed, ref } from 'vue';
	import type { GostJahrgangsdaten } from '../../../../../core/src/core/data/gost/GostJahrgangsdaten';

	const props = withDefaults(defineProps<{
		gostJahrgangsdaten: GostJahrgangsdaten;
		resetFachwahlen: (forceDelete: boolean) => Promise<void>;
		schuelerAnsicht?: boolean;
		keineVorlage?: boolean;
	}>(), {
		schuelerAnsicht: false,
		keineVorlage: false,
	});

	const show = ref<boolean>(false);
	const doForceDelete = ref<boolean>(false);


	const buttonText = computed<string>(() => {
		if (props.schuelerAnsicht && hatFesteWahlen.value)
			return "Fachwahlen löschen";
		else if (props.schuelerAnsicht)
			return "Fachwahlen zurücksetzen";
		else if (props.gostJahrgangsdaten.abiturjahr === -1)
			return "Zurücksetzen aus Standardwerte";
		else
			return "Zurücksetzen auf allg. Vorlage";
	});

	const modalTitle = computed<string>(() => {
		if (props.schuelerAnsicht && hatFesteWahlen.value)
			return "Nicht feste Fachwahlen löschen";
		else if (props.schuelerAnsicht && doForceDelete.value)
			return "Alle Fachwahlen löschen";
		else if (props.schuelerAnsicht)
			return "Alle Fachwahlen zurücksetzen";
		else if (props.gostJahrgangsdaten.abiturjahr === -1)
			return "Zurücksetzen aus Standardwerte";
		else
			return "Zurücksetzen der Vorlage auf die allgemeine Vorlage";
	});

	const modalDescription = computed<string>(() => {
		if (props.schuelerAnsicht && hatFesteWahlen.value)
			return "Sollen die nicht festen Fachwahlen gelöscht werden?";
		else if (props.schuelerAnsicht && doForceDelete.value)
			return "Soll die Laufbahnplanung vollständig geleert werden?";
		else if (props.schuelerAnsicht)
			return "Soll die Laufbahnplanung auf die jahrgangs-spezifische Vorlage zurückgesetzt werden?";
		else if (props.gostJahrgangsdaten.abiturjahr === -1)
			return "Soll die Vorlage auf die Standardwerte zurückgesetzt werden?";
		else
			return "Soll diese jahrgangs-spezifisch Vorlage auf die allgemeine Vorlage zurückgesetzt werden?";
	});

	const hatFesteWahlen = computed<boolean>(() => {
		const jg = props.gostJahrgangsdaten.jahrgang;
		return (jg === "Q1") || (jg === "Q2") || ((jg === "EF") && (props.gostJahrgangsdaten.istBlockungFestgelegt[0]));
	});

	function toggle_modal(forceDelete: boolean) {
		show.value = !show.value;
		doForceDelete.value = forceDelete;
	}

	async function reset_fachwahlen() {
		const forceDelete = doForceDelete.value;
		doForceDelete.value = false;
		show.value = false;
		await props.resetFachwahlen(forceDelete);
	}

</script>
