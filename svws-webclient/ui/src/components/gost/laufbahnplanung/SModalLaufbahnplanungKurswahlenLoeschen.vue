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

	import { useGostLaufbahnplanungState } from '@ui/states/GostLaufbahnplanungState';
	import { computed, ref } from 'vue';

	const props = withDefaults(defineProps<{
		schuelerAnsicht?: boolean;
		keineVorlage?: boolean;
	}>(), {
		schuelerAnsicht: false,
		keineVorlage: false,
	});
	const gostLaufbahnplanungState = useGostLaufbahnplanungState();

	const show = ref<boolean>(false);
	const doForceDelete = ref<boolean>(false);


	const buttonText = computed<string>(() => {
		if (props.schuelerAnsicht && hatFesteWahlen.value) {
			return "Fachwahlen löschen";
		} else if (props.schuelerAnsicht) {
			return "Fachwahlen zurücksetzen";
		} else if (gostLaufbahnplanungState.gostJahrgangsdaten.abiturjahr === -1) {
			return "Zurücksetzen aus Standardwerte";
		} else {
			return "Zurücksetzen auf allg. Vorlage";
		}
	});

	const modalTitle = computed<string>(() => {
		if (props.schuelerAnsicht && hatFesteWahlen.value) {
			return "Nicht feste Fachwahlen löschen";
		} else if (props.schuelerAnsicht && doForceDelete.value) {
			return "Alle Fachwahlen löschen";
		} else if (props.schuelerAnsicht) {
			return "Alle Fachwahlen zurücksetzen";
		} else if (gostLaufbahnplanungState.gostJahrgangsdaten.abiturjahr === -1) {
			return "Zurücksetzen aus Standardwerte";
		} else {
			return "Zurücksetzen der Vorlage auf die allgemeine Vorlage";
		}
	});

	const modalDescription = computed<string>(() => {
		if (props.schuelerAnsicht && hatFesteWahlen.value) {
			return "Sollen die nicht festen Fachwahlen gelöscht werden?";
		} else if (props.schuelerAnsicht && doForceDelete.value) {
			return "Soll die Laufbahnplanung vollständig geleert werden?";
		} else if (props.schuelerAnsicht) {
			return "Soll die Laufbahnplanung auf die jahrgangs-spezifische Vorlage zurückgesetzt werden?";
		} else if (gostLaufbahnplanungState.gostJahrgangsdaten.abiturjahr === -1) {
			return "Soll die Vorlage auf die Standardwerte zurückgesetzt werden?";
		} else {
			return "Soll diese jahrgangs-spezifisch Vorlage auf die allgemeine Vorlage zurückgesetzt werden?";
		}
	});

	const hatFesteWahlen = computed<boolean>(() => {
		const jg = gostLaufbahnplanungState.gostJahrgangsdaten.jahrgang;
		return (jg === "Q1") || (jg === "Q2") || ((jg === "EF") && (gostLaufbahnplanungState.gostJahrgangsdaten.istBlockungFestgelegt[0]));
	});

	function toggle_modal(forceDelete: boolean) {
		show.value = !show.value;
		doForceDelete.value = forceDelete;
	}

	async function reset_fachwahlen() {
		const forceDelete = doForceDelete.value;
		doForceDelete.value = false;
		show.value = false;
		await gostLaufbahnplanungState.resetFachwahlen(forceDelete);
	}

</script>
