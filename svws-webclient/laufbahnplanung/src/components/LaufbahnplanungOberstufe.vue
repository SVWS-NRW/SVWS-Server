<template>
	<div class="page--header">
		<div class="flex flex-row w-full">
			<div class="grow">
				<h2 class="text-headline"> {{ schueler.nachname }}, {{ schueler.vorname }} </h2>
				Jahrgang {{ gostJahrgangsdaten.jahrgang }} ({{ gostJahrgangsdaten.bezeichnung }})
			</div>
			<div class="flex flex-col gap-2">
				<div class="flex gap-3">
					<svws-ui-modal-hilfe> <hilfe-laufbahnplanung /> </svws-ui-modal-hilfe>
					<a href="https://www.svws.nrw.de/faq/impressum"> <svws-ui-button type="secondary"> Impressum </svws-ui-button> </a>
					<a href="#"> <svws-ui-button type="secondary"> Datenschutz </svws-ui-button> </a>
				</div>
				<span>{{ version }}</span>
			</div>
		</div>
	</div>
	<svws-ui-sub-nav>
		<svws-ui-button type="transparent" title="Planung exportieren" @click="export_laufbahnplanung"><i-ri-file-upload-line />Exportieren</svws-ui-button>
		<svws-ui-button type="transparent" title="Planung importieren" @click="showModalImport().value = true"><i-ri-file-download-line /> Importieren…</svws-ui-button>
		<s-laufbahnplanung-import-modal :show="showModalImport" :import-laufbahnplanung="import_laufbahnplanung" />
		<svws-ui-button :type="zwischenspeicher === undefined ? 'transparent' : 'error'" title="Planung merken" @click="saveLaufbahnplanung">Planung merken</svws-ui-button>
		<svws-ui-button type="danger" title="Planung wiederherstellen" @click="restoreLaufbahnplanung" v-if="zwischenspeicher !== undefined">Planung wiederherstellen</svws-ui-button>
		<svws-ui-button :type="modus === 'normal' ? 'transparent' : 'danger'" @click="switchModus" title="Modus wechseln">
			<i-ri-loop-right-line />
			Modus: <span>{{ modus }}</span>
		</svws-ui-button>
		<s-modal-laufbahnplanung-kurswahlen-loeschen schueler-ansicht :gost-jahrgangsdaten="gostJahrgangsdaten" :reset-fachwahlen="resetFachwahlen" />
	</svws-ui-sub-nav>
	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<div class="flex-grow">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturdaten-manager="abiturdatenManager" :modus="modus"
				:gost-jahrgangsdaten="gostJahrgangsdaten" :set-wahl="setWahl" />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-[36rem]">
			<div class="flex flex-col gap-16">
				<s-laufbahnplanung-card-status v-if="visible" :abiturdaten-manager="abiturdatenManager"
					:fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { LaufbahnplanungOberstufeProps } from "./LaufbahnplanungOberstufeProps";
	import { computed, onMounted, ref } from "vue";
	import { version } from '../../version';

	const props = defineProps<LaufbahnplanungOberstufeProps>();

	const _showModalImport = ref<boolean>(false);
	const showModalImport = () => _showModalImport;

	const visible = computed<boolean>(() => props.schueler.abiturjahrgang !== undefined);

	async function switchModus() {
		// wenn EF1 und EF2 bereits festgelegt sind, macht der Hochschreibemodus
		// keinen Sinn mehr und wird deaktiviert.
		const festgelegt = props.gostJahrgangsdaten.istBlockungFestgelegt
		if (festgelegt[0] && festgelegt[1]) {
			switch (props.modus) {
				case 'normal':
					await props.setModus('manuell');
					break;
				case 'manuell':
					await props.setModus('normal');
					break;
				case 'hochschreiben':
					await props.setModus('normal');
					break;
			}
		} else {
			switch (props.modus) {
				case 'normal':
					await props.setModus('hochschreiben');
					break;
				case 'hochschreiben':
					await props.setModus('manuell');
					break;
				case 'manuell':
					await props.setModus('normal');
					break;
			}
		}
	}

	async function export_laufbahnplanung() {
		const { data, name } = await props.exportLaufbahnplanung();
		const link = document.createElement("a");
		console.log(data, name)
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
	}

	async function import_laufbahnplanung(formData: FormData) {
		await props.importLaufbahnplanung(formData);
		return true;
	}

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>
