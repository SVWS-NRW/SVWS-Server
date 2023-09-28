<template>
	<div class="page--content page--content--full min-w-fit gap-x-8 2xl:gap-x-16 relative">
		<Teleport to=".router-tab-bar--subnav-target" v-if="isMounted">
			<svws-ui-sub-nav>
				<svws-ui-button size="small" type="transparent" title="Planung exportieren" @click="export_laufbahnplanung"><i-ri-download-2-line />Exportieren</svws-ui-button>
				<svws-ui-button size="small" type="transparent" title="Planung importieren" @click="showModalImport().value = true"><i-ri-upload-2-line /> Importieren…</svws-ui-button>
				<s-laufbahnplanung-import-modal :show="showModalImport" :import-laufbahnplanung="importLaufbahnplanung" />
				<svws-ui-button size="small" :type="zwischenspeicher === undefined ? 'transparent' : 'error'" title="Planung merken" @click="saveLaufbahnplanung">Planung merken</svws-ui-button>
				<svws-ui-button size="small" type="danger" title="Planung merken" @click="restoreLaufbahnplanung" v-if="zwischenspeicher !== undefined">Planung wiederherstellen</svws-ui-button>
				<svws-ui-button size="small" :type="modus === 'manuell' ? 'error' : modus === 'hochschreiben' ? 'primary' : 'transparent'" @click="switchModus" :title="modus === 'manuell' ? 'Manueller Modus' : modus === 'normal' ? 'Normaler Modus':'Hochschreibemodus'">
					{{ modus === 'manuell' ? 'Manueller Modus' : modus === 'normal' ? 'Normaler Modus' : 'Hochschreibemodus' }}
					<template v-if="modus === 'manuell'">
						<svg width="1.2em" height="1.2em" fill="currentColor" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M3 16V5.75C3 5.06 3.56 4.5 4.25 4.5S5.5 5.06 5.5 5.75V12H6.5V2.75C6.5 2.06 7.06 1.5 7.75 1.5C8.44 1.5 9 2.06 9 2.75V12H10V1.25C10 .56 10.56 0 11.25 0S12.5 .56 12.5 1.25V12H13.5V3.25C13.5 2.56 14.06 2 14.75 2S16 2.56 16 3.25V15H16.75L18.16 11.47C18.38 10.92 18.84 10.5 19.4 10.31L20.19 10.05C21 9.79 21.74 10.58 21.43 11.37L18.4 19C17.19 22 14.26 24 11 24C6.58 24 3 20.42 3 16Z" /></svg>
					</template>
				</svws-ui-button>
				<s-modal-laufbahnplanung-kurswahlen-loeschen @delete="resetFachwahlen" />
			</svws-ui-sub-nav>
		</Teleport>
		<Teleport to=".svws-ui-header--actions" v-if="isMounted">
			<svws-ui-button type="secondary" @click.prevent="download_file" title="Wahlbogen herunterladen"><i-ri-printer-line />PDF herunterladen</svws-ui-button>
			<svws-ui-modal-hilfe> <hilfe-laufbahnplanung /> </svws-ui-modal-hilfe>
		</Teleport>
		<div class="flex-grow">
			<s-laufbahnplanung-card-planung v-if="visible" :abiturdaten-manager="abiturdatenManager" :modus="modus"
				:gost-jahrgangsdaten="gostJahrgangsdaten" :set-wahl="setWahl" />
		</div>
		<div class="w-2/5 3xl:w-1/2 min-w-[36rem]">
			<div class="flex flex-col gap-16">
				<s-laufbahnplanung-card-status v-if="visible" :abiturdaten-manager="abiturdatenManager"
					:fehlerliste="() => gostBelegpruefungErgebnis().fehlercodes" :gost-belegpruefungs-art="gostBelegpruefungsArt" @update:gost-belegpruefungs-art="setGostBelegpruefungsArt" />
				<s-laufbahnplanung-card-beratung v-if="visible" :gost-laufbahn-beratungsdaten="gostLaufbahnBeratungsdaten" :patch-beratungsdaten="patchBeratungsdaten" :map-lehrer="mapLehrer" :id="id" />
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">

	import type { SchuelerLaufbahnplanungProps } from "./SSchuelerLaufbahnplanungProps";
	import { computed, onMounted, ref } from "vue";

	const props = defineProps<SchuelerLaufbahnplanungProps>();

	const visible = computed<boolean>(() => props.schueler.abiturjahrgang !== undefined);

	const _showModalImport = ref<boolean>(false);
	const showModalImport = () => _showModalImport;

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

	async function download_file() {
		const { data, name } = await props.getPdfWahlbogen();
		const link = document.createElement("a");
		link.href = URL.createObjectURL(data);
		link.download = name;
		link.target = "_blank";
		link.click();
		URL.revokeObjectURL(link.href);
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

	// Check if component is mounted
	const isMounted = ref(false);
	onMounted(() => {
		isMounted.value = true;
	});

</script>
